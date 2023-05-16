package pl.jozwik.quillgeneric.sbt.generator.cassandra

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithTry }

object CassandraSyncCodeGenerator extends AbstractCodeGenerator with WithCassandra with WithTry {
  protected def genericPackage               = "pl.jozwik.quillgeneric.cassandra.sync"
  protected def aliasName                    = "CassandraContextDateQuotes"
  protected def domainRepository: String      = "CassandraRepository"
  protected def domainRepositoryWithGenerated = domainRepository
}
