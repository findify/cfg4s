package io.findify.cfg4s.json

import com.typesafe.scalalogging.LazyLogging
import org.json4s.native.JsonMethods.parse
import io.findify.cfg4s.provider.Provider
import org.json4s.{DefaultFormats, JString, JValue}
import org.json4s.native.JsonMethods._
import org.json4s.JsonDSL._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source


/**
  * Created by shutty on 9/8/16.
  */
class JsonProvider(path:String)(implicit ec:ExecutionContext) extends Provider with LazyLogging {
  var json:JValue = _
  override def open: Future[Unit] = Future {
    json = parse(Source.fromFile(path).reader())
  }
  override def loadString(path: List[String]): Future[String] = {
    implicit val formats = DefaultFormats
    def field(value:JValue, path:List[String]):String = path match {
      case Nil => value.extract[String]
      case name :: tail => field(value \\ name, tail)
    }
    Future {
      field(json, path)
    }
  }

  override def loadInt(path: List[String]): Future[Int] = loadString(path).map(_.toInt)

}
