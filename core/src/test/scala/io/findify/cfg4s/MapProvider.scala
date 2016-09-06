package io.findify.cfg4s

import io.findify.cfg4s.error.KeyNotFoundException
import io.findify.cfg4s.provider.Provider
import io.findify.cfg4s.value.{ConfigValue, StringValue}

import scala.concurrent.Future

/**
  * Created by shutty on 9/6/16.
  */
class MapProvider(map:Map[String,String]) extends Provider {
  override def load(path: List[String]): Future[ConfigValue] = map.get(path.mkString(".")) match {
    case Some(value) => Future.successful(StringValue(path.head, value))
    case None => Future.failed(KeyNotFoundException(path))
  }
}
