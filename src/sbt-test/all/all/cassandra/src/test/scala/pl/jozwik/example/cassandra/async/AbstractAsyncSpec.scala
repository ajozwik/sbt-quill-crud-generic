package pl.jozwik.example.cassandra.async

import io.getquill.{ CassandraAsyncContext, SnakeCase }
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{ Millis, Seconds, Span }
import pl.jozwik.example.cassandra.AbstractCassandraSpec
import pl.jozwik.quillgeneric.quillmacro.async.AsyncCrudWithContext.AsyncCrudWithContextUnit

import scala.concurrent.ExecutionContext

trait AbstractAsyncSpec extends AbstractCassandraSpec with ScalaFutures {
  protected lazy val ctx = new CassandraAsyncContext(SnakeCase, "ctx") with AsyncCrudWithContextUnit
  private val sleep      = 15

  protected implicit val defaultPatience: PatienceConfig =
    PatienceConfig(timeout = Span(2, Seconds), interval = Span(sleep, Millis))
  protected implicit val ec: ExecutionContext = ExecutionContext.global
}
