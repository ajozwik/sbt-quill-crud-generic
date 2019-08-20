package pl.jozwik.quillgeneric.sbt.generator

object CassandraMonixCodeGenerator extends AbstractCodeGenerator with WithCassandra with WithMonix {
  protected def genericPackage               = "pl.jozwik.quillgeneric.quillmacro.cassandra.monix"
  protected def aliasName                    = "CassandraMonixContextDateQuotes"
  protected def macroRepository: String      = "CassandraMonixRepository"
  protected def repositoryCompositeKey       = "CassandraMonixRepositoryCompositeKey"
  protected def macroRepositoryWithGenerated = macroRepository
}
