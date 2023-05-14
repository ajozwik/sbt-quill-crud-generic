package pl.jozwik.quillgeneric.sbt.generator
import pl.jozwik.quillgeneric.sbt.generator.CodeGenerationTemplates.*
import pl.jozwik.quillgeneric.sbt.generator.jdbc.SyncCodeGenerator.{
  BeanIdTemplate,
  BeanTemplate,
  ContextTransactionEnd,
  ContextTransactionStart,
  TryEnd,
  TryStart
}

trait WithJdbc {
  protected def update                  = "Long"
  protected def contextTransactionStart = "context.transaction {"
  protected def contextTransactionEnd   = "}"
  protected def sqlIdiomImport          = "import io.getquill.context.sql.idiom.SqlIdiom"

  protected def aliasGenericDeclaration =
    s"${CodeGenerationTemplates.DialectTemplate} <: SqlIdiom, ${CodeGenerationTemplates.NamingTemplate} <: NamingStrategy"

  protected def genericDeclaration = s"${CodeGenerationTemplates.DialectTemplate}, ${CodeGenerationTemplates.NamingTemplate}"

  protected def createOrUpdate: String =
    s"""  override def createOrUpdate(entity: $BeanTemplate): $Monad[$BeanIdTemplate] =
       |    $ContextTransactionStart $ImplicitContext
       |      for {
       |        el <- ${TryStart}run(find(entity.id).updateValue(entity))$TryEnd
       |        id <- el match {
       |          case 0 =>
       |            create(entity)
       |          case _ =>
       |            pure(entity.id)
       |          }
       |      } yield {
       |        id
       |      }
       |   $ContextTransactionEnd""".stripMargin
  protected def createOrUpdateAndRead = "createOrUpdateAndRead"
}
