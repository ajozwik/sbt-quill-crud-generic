package pl.jozwik.quillgeneric.sbt

final case class RepositoryDescription(
  beanClass: String,
  beanIdClass: String,
  repositoryClassName: String,
  tableName: Option[String] = None,
  mapping: Map[String, String] = Map.empty) {

  val (packageName, repositorySimpleClassName) = toPackageNameSimpleClass(repositoryClassName)
  val (_, beanSimpleClassName) = toPackageNameSimpleClass(beanClass)
  val (_, beanIdSimpleClassName) = toPackageNameSimpleClass(beanIdClass)

  lazy val toTableName: String = tableName.getOrElse {
    val (_, simpleClassName) = toPackageNameSimpleClass(beanClass)
    simpleClassName
  }

  private[sbt] def toPackageNameSimpleClass(className: String): (Seq[String], String) = {
    val array = className.split("\\.")
    val packageName = array.slice(0, array.length - 1)
    val repositorySimpleClassName = array(array.length - 1)
    (packageName, repositorySimpleClassName)
  }
}