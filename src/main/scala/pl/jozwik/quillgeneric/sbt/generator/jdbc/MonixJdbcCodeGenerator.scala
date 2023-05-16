package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithJdbc, WithMonix }

object MonixJdbcCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithMonix {
  protected def genericPackage               = "pl.jozwik.quillgeneric.monix.jdbc"
  protected def aliasName                    = "MonixJdbcContextDateQuotes"
  protected def domainRepository: String      = "MonixJdbcRepository"
  protected def domainRepositoryWithGenerated = "MonixJdbcRepositoryWithGeneratedId"

}
