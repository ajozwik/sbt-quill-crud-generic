package pl.jozwik.quillgeneric.sbt

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
}
