package io.findify.cfg4s.consul

import com.typesafe.scalalogging.LazyLogging
import consul.Consul
import io.findify.cfg4s.provider.Provider

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by shutty on 9/6/16.
  */
class ConsulProvider(host:String, prefix:List[String])(implicit ec:ExecutionContext) extends Provider with LazyLogging {
  lazy val client = new Consul(host, 8500)
  lazy val kv = client.keyStore()
  override def loadString(path: List[String]): Future[String] = {
    kv.get((prefix ++ path).mkString("/")) match {
      case "" =>
        logger.warn(s"empty response for $path")
        Future.successful("")
      case str =>
        logger.debug(s"response: $path = $str")
        Future.successful(str)
    }
  }

  override def loadInt(path: List[String]): Future[Int] = loadString(path).map(_.toInt)
}
