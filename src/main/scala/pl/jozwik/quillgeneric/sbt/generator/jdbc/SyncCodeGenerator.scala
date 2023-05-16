package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithJdbc, WithTry }

object SyncCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithTry {
  protected def genericPackage               = "pl.jozwik.quillgeneric.monad"
  protected def aliasName                    = "JdbcContextDateQuotes"
  protected def domainRepository: String      = "TryJdbcRepository"
  protected def domainRepositoryWithGenerated = "TryJdbcRepositoryWithGeneratedId"

}
