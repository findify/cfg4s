package io.findify.cfg4s.consul.tool
import scala.sys.process._
import com.pszymczyk.consul.{ConsulProcess, ConsulStarterBuilder}
import consul.Consul
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers, Suite}

/**
  * Created by shutty on 9/7/16.
  */
class EmbeddedConsulSupport extends FlatSpec with Matchers with BeforeAndAfterAll {
  lazy val client = new Consul("http://localhost", 8500).keyStore()
  private lazy val server = ConsulStarterBuilder.consulStarter().withHttpPort(8500).build()
  private var process:ConsulProcess = _
  override def beforeAll() = {
    super.beforeAll()
    Seq("killall", "consul").!
    process = server.start()
  }
  override def afterAll() = {
    super.afterAll()
    process.close()
  }
}
