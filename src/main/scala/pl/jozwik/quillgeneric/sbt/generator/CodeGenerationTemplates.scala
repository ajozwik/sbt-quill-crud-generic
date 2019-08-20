package pl.jozwik.quillgeneric.sbt.generator

object CodeGenerationTemplates extends CodeGenerationTemplates

trait CodeGenerationTemplates {
  val DialectTemplate                = "__DIALECT__"
  val NamingTemplate                 = "__NAMING__"
  val PackageTemplate                = "__PACKAGE__"
  val RepositoryClassTemplate        = "__REPOSITORY_NAME__"
  val BeanTemplate                   = "__BEAN__"
  val BeanClassImport                = "__BEAN_CLASS_IMPORT__"
  val BeanIdTemplate                 = "__ID__"
  val BeanIdClassImport              = "__ID_CLASS_IMPORT__"
  val ColumnMapping                  = "__COLUMN_MAPPING__"
  val ImportContext                  = "__IMPORT_CONTEXT__"
  val TableNamePattern               = "__TABLE_NAME__"
  val RepositoryTraitImport          = "__REPOSITORY_TRAIT_IMPORT__"
  val RepositoryTraitSimpleClassName = "__REPOSITORY_TRAIT_SIMPLE_NAME__"
  val RepositoryImport               = "__REPOSITORY_IMPORT__"
  val ContextAlias                   = "__CONTEXT_ALIAS__"
  val Update                         = "__UPDATE__"
  val Monad                          = "__MONAD__"
  val ContextTransactionStart        = "__CONTEXT_TRANSACTION_START__"
  val ContextTransactionEnd          = "__CONTEXT_TRANSACTION_END__"
  val MonadImport                    = "__MONAD_IMPORT__"
  val TryStart                       = "__TRY_START__"
  val TryEnd                         = "__TRY_END__"
  val RepositoryMacroTraitImport     = "__REPOSITORY_MACRO_TRAIT_IMPORT__"
  val SqlIdiomImport                 = "__SQL_IDIOM_IMPORT__"
  val AliasGenericDeclaration        = "__ALIAS_GENERIC_DECLARATION__"
  val GenericDeclaration             = "__GENERIC_DECLARATION__"
  val CreateOrUpdate                 = "__CREATE_OR_UPDATE__"
  val CreateOrUpdateAndRead          = "__CREATE_OR_UPDATE_AND_READ__"
}
