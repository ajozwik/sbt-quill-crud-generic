package pl.jozwik.quillgeneric.sbt.generator

trait WithJdbc {
  protected def update                  = "Long"
  protected def contextTransactionStart = "     context.transaction {"
  protected def contextTransactionEnd   = "         }"
  protected def sqlIdiomImport          = "import io.getquill.context.sql.idiom.SqlIdiom"
  protected def aliasGenericDeclaration =
    s"[${CodeGenerationTemplates.DialectTemplate} <: SqlIdiom, ${CodeGenerationTemplates.NamingTemplate} <: NamingStrategy]"
  protected def genericDeclaration    = s"${CodeGenerationTemplates.DialectTemplate}, ${CodeGenerationTemplates.NamingTemplate}"
  protected def createOrUpdate        = "createOrUpdate"
  protected def createOrUpdateAndRead = "createOrUpdateAndRead"
}
