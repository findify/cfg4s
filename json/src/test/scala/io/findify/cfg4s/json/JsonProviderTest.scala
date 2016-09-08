package io.findify.cfg4s.json

import java.io.{ByteArrayOutputStream, File, FileOutputStream}

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import scala.concurrent.duration._
import scala.concurrent.Await

/**
  * Created by shutty on 9/8/16.
  */
class JsonProviderTest extends FlatSpec with Matchers with BeforeAndAfterAll {
  import scala.concurrent.ExecutionContext.Implicits.global
  override def beforeAll = {
    val json = new File("/tmp/some.json")
    val stream = new FileOutputStream(json)
    stream.write("""{"foo":"bar", "baz":1, "a": {"b": "c"}}""".getBytes())
    stream.close()
  }
  "json provider" should "load strings" in {
    val prov = new JsonProvider("/tmp/some.json")
    Await.result(prov.open, 1.second)
    Await.result(prov.loadString(List("foo")), 1.second) shouldBe "bar"
  }
  it should "load ints" in {
    val prov = new JsonProvider("/tmp/some.json")
    Await.result(prov.open, 1.second)
    Await.result(prov.loadInt(List("baz")), 1.second) shouldBe 1
  }
  it should "load nested strings" in {
    val prov = new JsonProvider("/tmp/some.json")
    Await.result(prov.open, 1.second)
    Await.result(prov.loadString(List("a", "b")), 1.second) shouldBe "c"
  }
}
