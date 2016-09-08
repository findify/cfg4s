package io.findify.cfg4s

import io.findify.cfg4s.error.KeyNotFoundException
import io.findify.cfg4s.provider.Provider

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shutty on 9/6/16.
  */
class MapProvider(var map:Map[String,String])(implicit ec:ExecutionContext) extends Provider {
  override def loadString(path: List[String]): Future[String] = map.get(path.mkString(".")) match {
    case Some(value) => Future.successful(value)
    case None => Future.failed(KeyNotFoundException(path))
  }

  override def loadInt(path: List[String]): Future[Int] = loadString(path).map(_.toInt)
}
