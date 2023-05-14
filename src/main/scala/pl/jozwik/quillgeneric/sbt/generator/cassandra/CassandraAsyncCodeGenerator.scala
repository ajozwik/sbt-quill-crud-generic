package pl.jozwik.quillgeneric.sbt.generator.cassandra

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithFuture }

object CassandraAsyncCodeGenerator extends AbstractCodeGenerator with WithCassandra with WithFuture {
  private val ec                                        = "ec"
  protected def genericPackage                          = "pl.jozwik.quillgeneric.cassandra.async"
  protected def aliasName                               = "CassandraAsyncContextDateQuotes"
  protected def domainRepository: String                 = "CassandraAsyncRepository"
  protected def repositoryCompositeKey                  = "CassandraAsyncRepositoryCompositeKey"
  protected def domainRepositoryWithGenerated            = domainRepository
  override protected def implicitParameters: String     = s"(dSchema, $ec)"
  override protected def executionContext: String       = s"(implicit $ec: ExecutionContext)"
  override protected def executionContextImport: String = "import concurrent.ExecutionContext"
}
