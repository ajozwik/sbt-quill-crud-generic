package pl.jozwik.quillgeneric.sbt

final case class RepositoryDescription(
  beanClass: String,
  beanIdClass: String,
  repositoryClassName: String,
  tableName: Option[String] = None) {
  lazy val toTableName: String = tableName.getOrElse {
    val (_, simpleClassName) = CodeGenerator.toPackageNameSimpleClass(beanClass)
    simpleClassName
  }
}