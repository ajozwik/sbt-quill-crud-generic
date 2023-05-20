package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{AbstractCodeGenerator, WithDoobie, WithJdbc}

object DoobieCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithDoobie {
  protected def genericPackage                = "pl.jozwik.quillgeneric.doobie"
  protected def aliasName                     = "DoobieJdbcContextWithDateQuotes"
  protected def domainRepository: String      = "DoobieRepository"
  protected def domainRepositoryWithGenerated = "DoobieRepositoryWithTransactionWithGeneratedId"

}
