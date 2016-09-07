package io.findify.cfg4s.provider

import scala.concurrent.Future

/**
  * Created by shutty on 9/6/16.
  */
trait Provider {
  def loadString(path:List[String]):Future[String]
  def loadInt(path:List[String]):Future[Int]
}
