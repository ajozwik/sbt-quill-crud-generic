package pl.jozwik.quillgeneric.sbt.generator.jdbc

import pl.jozwik.quillgeneric.sbt.generator.{ AbstractCodeGenerator, CodeGenerationTemplates, WithFuture, WithJdbc }

object AsyncCodeGenerator extends AbstractCodeGenerator with WithJdbc with WithFuture {
  private val asyncTransaction               = "f"
  private val ec                             = "ec"
  protected def genericPackage               = "pl.jozwik.quillgeneric.quillmacro.async"
  protected def aliasName                    = "AsyncJdbcContextDateQuotes"
  protected def macroRepository: String      = "AsyncJdbcRepository"
  protected def repositoryCompositeKey       = "AsyncJdbcRepositoryCompositeKey"
  protected def macroRepositoryWithGenerated = "AsyncJdbcRepositoryWithGeneratedId"

  override protected def aliasGenericDeclaration =
    s"${super.aliasGenericDeclaration}, ${CodeGenerationTemplates.ConnectionTemplate} <: ConcreteConnection"
  override protected def genericDeclaration = s"${super.genericDeclaration}, ${CodeGenerationTemplates.ConnectionTemplate}"

  override protected def contextTransactionStart =
    s"""{
       |     import context.toFuture
       |     context.transaction { $asyncTransaction =>""".stripMargin

  override protected def contextTransactionEnd =
    """ }
      | }""".stripMargin
  override protected def implicitTransactionParameters  = s"(dSchema, $asyncTransaction)"
  override protected def implicitParameters: String     = s"(dSchema, $ec)"
  override protected def executionContext: String       = s"(implicit $ec: ExecutionContext)"
  override protected def executionContextImport: String = "import concurrent.ExecutionContext"
  override protected def connectionImport: String       = "import com.github.jasync.sql.db.ConcreteConnection"
}
