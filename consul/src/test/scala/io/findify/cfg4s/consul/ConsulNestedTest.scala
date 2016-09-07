package io.findify.cfg4s.consul

import io.findify.cfg4s.ConfigLoader
import io.findify.cfg4s.consul.tool.EmbeddedConsulSupport
import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Created by shutty on 9/7/16.
  */

case class FooConfigNested(foo:String, bar:Int)
case class FooConfigRoot(qux:String, baz:FooConfigNested)

class ConsulNestedTest extends EmbeddedConsulSupport {
  import scala.concurrent.ExecutionContext.Implicits.global
  "consul provider" should "load nested config" in {
    assert(client.set("qux", "zzz"))
    assert(client.set("baz/foo", "bar"))
    assert(client.set("baz/bar", "1"))
    val conf = new ConfigLoader[FooConfigRoot](new ConsulProvider("http://localhost", List()), classOf[FooConfigRoot])
    val result = Await.result(conf.get, 10.seconds)
    result shouldBe FooConfigRoot("zzz", FooConfigNested("bar", 1))
  }

}
