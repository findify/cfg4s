package io.findify.cfg4s.consul

import io.findify.cfg4s.provider.Provider
import io.findify.cfg4s.value.ConfigValue

import scala.concurrent.Future

/**
  * Created by shutty on 9/6/16.
  */
class ConsulProvider extends Provider {
  override def load(path: List[String]): Future[ConfigValue] = ???
}
