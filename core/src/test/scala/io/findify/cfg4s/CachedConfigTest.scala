package io.findify.cfg4s

import org.scalatest.{FlatSpec, Matchers}
import scala.concurrent.duration._
/**
  * Created by shutty on 9/7/16.
  */

case class Conf(foo:String, bar:Int)
class CachedConfigTest extends FlatSpec with Matchers {
  import scala.concurrent.ExecutionContext.Implicits.global
  val provider = new MapProvider(Map("foo" -> "x", "bar" -> "1"))
  val conf = new CachedConfig[Conf](provider, classOf[Conf])
  "cached config loader" should "return config for the first time" in {
    val c = conf.get()
    c shouldBe Conf("x", 1)
  }
  it should "cache the result" in {
    val c = conf.get()
    c shouldBe Conf("x", 1)
  }
  it should "refresh config on timeout" in {
    provider.map = Map("foo" -> "y", "bar" -> "1")
    val c = conf.get(100.millis)
    c shouldBe Conf("x", 1)
    Thread.sleep(200)
    val c2 = conf.get(100.millis)
    c2 shouldBe Conf("y", 1)
  }
}
