package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithJdbc, WithMonix }

object MonixJdbcCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithMonix {
  protected def genericPackage               = "pl.jozwik.quillgeneric.quillmacro.monix.jdbc"
  protected def aliasName                    = "MonixJdbcContextDateQuotes"
  protected def macroRepository: String      = "MonixJdbcRepository"
  protected def repositoryCompositeKey       = "MonixJdbcRepositoryCompositeKey"
  protected def macroRepositoryWithGenerated = "MonixJdbcRepositoryWithGeneratedId"
}
