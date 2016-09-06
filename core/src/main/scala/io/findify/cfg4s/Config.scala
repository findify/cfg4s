package io.findify.cfg4s

import io.findify.cfg4s.provider.Provider
import io.findify.cfg4s.value.StringValue

import scala.concurrent.Future
import scala.reflect.ClassTag
import scala.reflect.runtime.universe.{runtimeMirror, termNames, TypeTag}
/**
  * Created by shutty on 9/6/16.
  */
class Config[T:TypeTag](provider:Provider, clazz:Class[T]) {
  val ctag = ClassTag(clazz)
  val rm = runtimeMirror(ctag.runtimeClass.getClassLoader)
  val tpe = rm.typeOf[T].resultType
  val classTest = tpe.typeSymbol.asClass
  val classMirror = rm.reflectClass(classTest)
  val constructorDecl = tpe.decl(termNames.CONSTRUCTOR)
  val constructor = constructorDecl.asMethod
  val constructorMirror = classMirror.reflectConstructor(constructor)

  def get:Future[T] = {
    import scala.concurrent.ExecutionContext.Implicits.global
    Future.sequence(constructor.paramLists.flatten.map(symbol => symbol.typeSignature.resultType.toString match {
      case "String" => provider.load(List(symbol.name.toString))
    }))
      .map(values => values.map {
        case StringValue(name, value) => value
      })
      .map(args => constructorMirror(args: _*).asInstanceOf[T])
  }
}
