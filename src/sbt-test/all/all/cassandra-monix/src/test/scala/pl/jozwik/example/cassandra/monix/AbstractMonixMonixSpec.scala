package pl.jozwik.example.cassandra.monix

import io.getquill.{ CassandraMonixContext, SnakeCase }
import monix.execution.Scheduler
import pl.jozwik.quillgeneric.quillmacro.monix.MonixWithContextDateQuotes.MonixWithContextUnit

trait AbstractMonixMonixSpec extends AbstractCassandraMonixSpec {
  protected lazy val ctx                      = new CassandraMonixContext(SnakeCase, "ctx") with MonixWithContextUnit
  protected implicit val scheduler: Scheduler = Scheduler.Implicits.global
}
