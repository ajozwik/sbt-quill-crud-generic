package pl.jozwik.quillgeneric.sbt.generator

object CodeGenerationTemplates extends CodeGenerationTemplates

trait CodeGenerationTemplates {
  val AliasGenericDeclaration        = "__ALIAS_GENERIC_DECLARATION__"
  val BeanClassImport                = "__BEAN_CLASS_IMPORT__"
  val BeanIdClassImport              = "__ID_CLASS_IMPORT__"
  val BeanIdTemplate                 = "__ID__"
  val BeanTemplate                   = "__BEAN__"
  val ColumnMapping                  = "__COLUMN_MAPPING__"
  val ConnectionImport               = "__CONNECTION_IMPORT__"
  val ConnectionTemplate             = "__CONNECTION__"
  val ContextAlias                   = "__CONTEXT_ALIAS__"
  val ContextTransactionEnd          = "__CONTEXT_TRANSACTION_END__"
  val ContextTransactionStart        = "__CONTEXT_TRANSACTION_START__"
  val CreateOrUpdate                 = "__CREATE_OR_UPDATE__"
  val CreateOrUpdateAndRead          = "__CREATE_OR_UPDATE_AND_READ__"
  val DialectTemplate                = "__DIALECT__"
  val ExecutionContext               = "__EXECUTION_CONTEXT__"
  val ExecutionContextImport         = "__EXECUTION_CONTEXT_IMPORT__"
  val FindByKey                      = "__FIND_BY_KEY__"
  val GenericDeclaration             = "__GENERIC_DECLARATION__"
  val ImplicitBaseVariable           = "__IMPLICIT_BASE_VARIABLE__"
  val ImplicitContext                = "__IMPLICIT_CONTEXT__"
  val ImplicitParameters             = "__IMPLICIT_PARAMETERS__"
  val ImplicitTransactionParameters  = "__IMPLICIT_TRANSACTION_PARAMETERS__"
  val ImportContext                  = "__IMPORT_CONTEXT__"
  val Monad                          = "__MONAD__"
  val MonadImport                    = "__MONAD_IMPORT__"
  val NamingTemplate                 = "__NAMING__"
  val PackageTemplate                = "__PACKAGE__"
  val RepositoryClassTemplate        = "__REPOSITORY_NAME__"
  val RepositoryImport               = "__REPOSITORY_IMPORT__"
  val RepositoryMacroTraitImport     = "__REPOSITORY_MACRO_TRAIT_IMPORT__"
  val RepositoryTraitImport          = "__REPOSITORY_TRAIT_IMPORT__"
  val RepositoryTraitSimpleClassName = "__REPOSITORY_TRAIT_SIMPLE_NAME__"
  val SqlIdiomImport                 = "__SQL_IDIOM_IMPORT__"
  val TableNamePattern               = "__TABLE_NAME__"
  val ToTask                         = "__TO_TASK__"
  val ToTaskEnd                      = "__TO_TASK_END__"
  val TryEnd                         = "__TRY_END__"
  val TryStart                       = "__TRY_START__"
  val UpdateResult                   = "__UP__"
  val Update                         = "__UPDATE__"
}
