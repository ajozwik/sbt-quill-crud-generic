package pl.jozwik.quillgeneric.sbt.generator.cassandra

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithMonix }

object CassandraMonixCodeGenerator extends AbstractCodeGenerator with WithCassandra with WithMonix {
  protected def genericPackage                = "pl.jozwik.quillgeneric.cassandra.monix"
  protected def aliasName                     = "CassandraMonixContextDateQuotes"
  protected def domainRepository: String      = domainRepositoryWithGenerated
  protected def domainRepositoryWithGenerated = "CassandraMonixRepository"
}
