package pl.jozwik.quillgeneric.sbt.generator

object CassandraSyncCodeGenerator extends AbstractCodeGenerator with WithCassandra with WithTry {
  protected def genericPackage               = "pl.jozwik.quillgeneric.quillmacro.cassandra.sync"
  protected def aliasName                    = "CassandraContextDateQuotes"
  protected def macroRepository: String      = "CassandraRepository"
  protected def repositoryCompositeKey       = "CassandraRepositoryCompositeKey"
  protected def macroRepositoryWithGenerated = macroRepository
}
