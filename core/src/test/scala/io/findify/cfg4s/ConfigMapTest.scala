package io.findify.cfg4s

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by shutty on 9/6/16.
  */
case class TestConfPlain(foo:String)
case class TestConfNested(bar:String, qux:TestConfPlain)
class ConfigMapTest extends FlatSpec with Matchers {
  val map = new MapProvider(Map("foo" -> "bar", "bar" -> "foo", "qux.foo" -> "zzz"))
  "config map" should "load plain data" in {
    val conf = new Config[TestConfPlain](map, classOf[TestConfPlain])
    val result = Await.result(conf.get, 10.seconds)
    result shouldBe TestConfPlain("bar")
  }
  it should "load nested configs" in {
    val conf = new Config[TestConfNested](map, classOf[TestConfNested])
    val result = Await.result(conf.get, 10.seconds)
    result shouldBe TestConfNested("foo", TestConfPlain("bar"))
  }
}
