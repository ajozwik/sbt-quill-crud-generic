package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, WithFuture, WithJdbc }

object AsyncCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithFuture {
  private val asyncTransaction                          = "f"
  private val ec                                        = "ec"
  protected def genericPackage                          = "pl.jozwik.quillgeneric.quillmacro.async"
  protected def aliasName                               = "AsyncJdbcContextDateQuotes"
  protected def macroRepository: String                 = "AsyncJdbcRepository"
  protected def repositoryCompositeKey                  = "AsyncJdbcRepositoryCompositeKey"
  protected def macroRepositoryWithGenerated            = "AsyncJdbcRepositoryWithGeneratedId"
  override protected def contextTransactionStart        = s"     context.transaction { $asyncTransaction =>"
  override protected def implicitTransactionParameters  = s"(dSchema, $asyncTransaction)"
  override protected def implicitParameters: String     = s"(dSchema, $ec)"
  override protected def executionContext: String       = s"(implicit $ec: ExecutionContext)"
  override protected def executionContextImport: String = "import concurrent.ExecutionContext"
}
