package io.findify.cfg4s.consul

import io.findify.cfg4s.ConfigLoader
import io.findify.cfg4s.consul.tool.EmbeddedConsulSupport
import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Created by shutty on 9/7/16.
  */
case class FooConfig1(foo:String, bar:Int)

class ConsulPlainTest extends EmbeddedConsulSupport {
  import scala.concurrent.ExecutionContext.Implicits.global
  "consul provider" should "load plain confs" in {
    assert(client.set("foo", "bar"))
    assert(client.set("bar", "1"))
    val conf = new ConfigLoader[FooConfig1](new ConsulProvider("http://localhost", List()), classOf[FooConfig1])
    val result = Await.result(conf.get, 10.seconds)
    result shouldBe FooConfig1("bar", 1)

  }
}
