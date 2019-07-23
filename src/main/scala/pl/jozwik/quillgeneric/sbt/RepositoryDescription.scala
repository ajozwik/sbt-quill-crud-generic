package pl.jozwik.quillgeneric.sbt

final case class RepositoryDescription(
  beanClass: String,
  beanIdClass: String,
  repositoryClassName: String,
  tableName: Option[String] = None,
  mapping: Map[String, String] = Map.empty) {
  lazy val toTableName: String = tableName.getOrElse {
    val (_, simpleClassName) = CodeGenerator.toPackageNameSimpleClass(beanClass)
    simpleClassName
  }
}