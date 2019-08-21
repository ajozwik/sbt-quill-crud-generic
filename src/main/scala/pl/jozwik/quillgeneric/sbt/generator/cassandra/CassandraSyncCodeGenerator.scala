package pl.jozwik.quillgeneric.sbt.generator.cassandra

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithTry }

object CassandraSyncCodeGenerator extends AbstractCodeGenerator with WithCassandra with WithTry {
  protected def genericPackage               = "pl.jozwik.quillgeneric.quillmacro.cassandra.sync"
  protected def aliasName                    = "CassandraContextDateQuotes"
  protected def macroRepository: String      = "CassandraRepository"
  protected def repositoryCompositeKey       = "CassandraRepositoryCompositeKey"
  protected def macroRepositoryWithGenerated = macroRepository
}
