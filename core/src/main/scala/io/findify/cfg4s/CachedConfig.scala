package io.findify.cfg4s

import com.typesafe.scalalogging.LazyLogging
import io.findify.cfg4s.provider.Provider

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.reflect.runtime.universe.TypeTag

/**
  * Created by shutty on 9/7/16.
  */
class CachedConfig[T: TypeTag](provider: Provider, clazz: Class[T])(implicit ec:ExecutionContext) extends LazyLogging {
  val loader = new ConfigLoader[T](provider, clazz)
  private var currentConf: T = Await.result(loader.get, Duration.Inf)
  private var updatingConf: Future[T] = _
  private var lastUpdate = System.currentTimeMillis()

  def get(timeout:Duration = 10.seconds) = {
    val now = System.currentTimeMillis()
    if ((now - lastUpdate) > timeout.toMillis) {
      logger.debug(s"scheduling config update for $clazz")
      updatingConf = loader.get
      updatingConf.onSuccess {
        case conf =>
          logger.debug(s"config updated for $clazz")
          lastUpdate = System.currentTimeMillis()
          currentConf = conf
      }
      currentConf
    } else {
      logger.debug(s"passed ${now - lastUpdate} of ${timeout.toMillis}, returning cached config")
      currentConf
    }
  }
}
