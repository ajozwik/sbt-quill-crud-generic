package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithJdbc, WithZio }

object ZioJdbcCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithZio {
  protected def genericPackage                = "pl.jozwik.quillgeneric.zio"
  protected def aliasName                     = "ZioJdbcContextWithDateQuotes"
  protected def domainRepository: String      = "ZioJdbcRepository"
  protected def domainRepositoryWithGenerated = "ZioJdbcRepositoryWithGeneratedId"

}
