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
  protected def updateResult            = "Long"
  protected def contextTransactionStart = "inTransaction {"
  protected def contextTransactionEnd   = "}"
  protected def sqlIdiomImport          = "import io.getquill.context.sql.idiom.SqlIdiom"

  protected def aliasGenericDeclaration =
    s"${CodeGenerationTemplates.DialectTemplate} <: SqlIdiom, ${CodeGenerationTemplates.NamingTemplate} <: NamingStrategy"

  protected def genericDeclaration = s"${CodeGenerationTemplates.DialectTemplate}, ${CodeGenerationTemplates.NamingTemplate}"

  protected def createOrUpdate: String =
    s"""  override def createOrUpdate(entity: $BeanTemplate): $Monad[$BeanIdTemplate] =
       |    $ContextTransactionStart $ImplicitContext $ToTask
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
       |   $ToTaskEnd $ContextTransactionEnd""".stripMargin
  protected def createOrUpdateAndRead = "createOrUpdateAndRead"

  protected val update: String =
    """  override def update(entity: __BEAN__): __MONAD__[__UP__] =
      |    __TRY_START__run(find(entity.id).updateValue(entity))__TRY_END__""".stripMargin
}
