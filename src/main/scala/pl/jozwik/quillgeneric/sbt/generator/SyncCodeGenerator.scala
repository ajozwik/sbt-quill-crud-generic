package pl.jozwik.quillgeneric.sbt.generator

object SyncCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithTry {
  protected def genericPackage               = "pl.jozwik.quillgeneric.quillmacro.sync"
  protected def aliasName                    = "JdbcContextDateQuotes"
  protected def macroRepository: String      = "JdbcRepository"
  protected def repositoryCompositeKey       = "JdbcRepositoryCompositeKey"
  protected def macroRepositoryWithGenerated = "JdbcRepositoryWithGeneratedId"
}
