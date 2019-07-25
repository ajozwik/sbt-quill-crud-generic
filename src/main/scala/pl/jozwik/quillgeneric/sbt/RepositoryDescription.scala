package pl.jozwik.quillgeneric.sbt

object RepositoryDescription {
  def firstToLower(txt: String): String =
    if (txt.length == 0) {
      ""
    } else if (txt.charAt(0).isLower) {
      txt
    } else {
      val chars = txt.toCharArray
      chars(0) = chars(0).toLower
      new String(chars)
    }
}

final case class RepositoryDescription(
  beanClass: String,
  beanIdClass: String,
  repositoryClassName: String,
  repositoryTrait: Option[String] = None,
  tableName: Option[String] = None,
  mapping: Map[String, String] = Map.empty) {

  val (packageName, repositorySimpleClassName) = toPackageNameSimpleClass(repositoryClassName)
  val (beanPackageName, beanSimpleClassName) = toPackageNameSimpleClass(beanClass)
  val (beanIdPackageName, beanIdSimpleClassName) = toPackageNameSimpleClass(beanIdClass)
  val (repositoryPackageName, repositoryTraitSimpleClassNameOpt) =
    toPackageNameSimpleClass(repositoryTrait.getOrElse(""))

  lazy val toTableName: String = tableName
    .filter(_.trim.nonEmpty)
    .getOrElse {
      val (_, simpleClassName) = toPackageNameSimpleClass(beanClass)
      RepositoryDescription.firstToLower(simpleClassName)
    }

  private[sbt] def toPackageNameSimpleClass(className: String): (Seq[String], String) = {
    val array = className.split("\\.")
    val packageName = array.slice(0, array.length - 1)
    val repositorySimpleClassName = array(array.length - 1)
    (packageName, repositorySimpleClassName)
  }
}