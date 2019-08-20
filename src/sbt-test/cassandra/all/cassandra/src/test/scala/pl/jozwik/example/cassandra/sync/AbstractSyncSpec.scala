package pl.jozwik.example.cassandra.sync

import io.getquill.{ CassandraSyncContext, SnakeCase }
import pl.jozwik.example.cassandra.AbstractCassandraSpec
import pl.jozwik.quillgeneric.quillmacro.sync.CrudWithContext.CrudWithContextDateQuotesUnit

trait AbstractSyncSpec extends AbstractCassandraSpec {
  protected lazy val ctx = new CassandraSyncContext(SnakeCase, "cassandra") with CrudWithContextDateQuotesUnit
}
