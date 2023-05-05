package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithJdbc, WithTry }

object SyncCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithTry {
  protected def genericPackage               = "pl.jozwik.quillgeneric.quillmacro.sync"
  protected def aliasName                    = "JdbcContextDateQuotes"
  protected def macroRepository: String      = "JdbcRepository"
  protected def repositoryCompositeKey       = "JdbcRepositoryCompositeKey"
  protected def macroRepositoryWithGenerated = "JdbcRepositoryWithGeneratedId"

  override val aliasGenericDeclarationPlus: Boolean = true
}
