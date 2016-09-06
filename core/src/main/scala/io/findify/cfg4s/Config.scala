package io.findify.cfg4s

import io.findify.cfg4s.provider.Provider
import io.findify.cfg4s.value.{ObjectValue, StringValue}

import scala.concurrent.Future
import scala.reflect.ClassTag
import scala.reflect.runtime.universe.{TypeTag, runtimeMirror, termNames, MethodMirror}
/**
  * Created by shutty on 9/6/16.
  */
class Config[T:TypeTag](provider:Provider, clazz:Class[T]) {


  def get:Future[T] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val constr = constructorFor(clazz)
    Future.sequence(symbols(constr).map(symbol => symbol.typeSignature.resultType.toString match {
      case "String" => provider.load(List(symbol.name.toString))
      case other => loadObject(List(symbol.name.toString), other)
    }))
      .map(values => values.map {
        case StringValue(name, value) => value
        case ObjectValue(name, value) => value
      })
      .map(args => constr(args: _*).asInstanceOf[T])
  }

  private def constructorFor[N:TypeTag](cl:Class[N]) = {
    val ctag = ClassTag(cl)
    val rm = runtimeMirror(ctag.runtimeClass.getClassLoader)
    val tpe = rm.typeOf[N].resultType
    val classTest = tpe.typeSymbol.asClass
    val classMirror = rm.reflectClass(classTest)
    val constructorDecl = tpe.decl(termNames.CONSTRUCTOR)
    val constructor = constructorDecl.asMethod
    classMirror.reflectConstructor(constructor)
  }

  private def nestedFor(cl:String) = {
    val c = Class.forName(cl)
    val clMirror = runtimeMirror(c.getClassLoader)
    val sym = clMirror.staticClass(cl)
    val tpe = sym.selfType
    val constructorDecl = tpe.decl(termNames.CONSTRUCTOR)
    val constructor = constructorDecl.asMethod
    clMirror.reflectClass(sym).reflectConstructor(constructor)
  }

  private def symbols(c:MethodMirror) = c.symbol.paramLists.flatten

  private def loadObject(path:List[String], name:String):Future[ObjectValue] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    val constr = nestedFor(name)
    Future.sequence(symbols(constr).map(symbol => symbol.typeSignature.resultType.toString match {
      case "String" => provider.load(path ++ List(symbol.name.toString))
      case other => loadObject(path ++ List(symbol.name.toString), other)
    }))
      .map(values => values.map {
        case StringValue(_, value) => value
        case ObjectValue(_, value) => value
      })
      .map(args => ObjectValue("", constr.apply(args: _*)))
  }
}
