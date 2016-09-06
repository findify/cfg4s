package io.findify.cfg4s.provider

import io.findify.cfg4s.value.ConfigValue

import scala.concurrent.Future

/**
  * Created by shutty on 9/6/16.
  */
trait Provider {
  def load(path:List[String]):Future[ConfigValue]
}
