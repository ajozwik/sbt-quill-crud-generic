package pl.jozwik.quillgeneric.sbt

object KeyType extends Enumeration {
  val Single, Composite = Value
}

final case class BeanIdClass(name: String, keyType: KeyType.Value = KeyType.Single)

final case class RepositoryDescription(
    beanClass: String,
    beanIdClass: BeanIdClass,
    repositoryClassName: String,
    generateId: Boolean = false,
    repositoryTrait: Option[String] = None,
    tableName: Option[String] = None,
    mapping: Map[String, String] = Map.empty
) {

  val (packageName, repositorySimpleClassName)   = toPackageNameSimpleClass(repositoryClassName)
  val (beanPackageName, beanSimpleClassName)     = toPackageNameSimpleClass(beanClass)
  val (beanIdPackageName, beanIdSimpleClassName) = toPackageNameSimpleClass(beanIdClass.name)

  val (repositoryPackageName, repositoryTraitSimpleClassNameOpt) =
    toPackageNameSimpleClass(repositoryTrait.getOrElse(""))

  lazy val toTableName: String = tableName
    .filter(_.trim.nonEmpty)
    .getOrElse {
      val (_, simpleClassName) = toPackageNameSimpleClass(beanClass)
      simpleClassName
    }

  private[sbt] def toPackageNameSimpleClass(className: String): (Seq[String], String) = {
    val array                     = className.split("\\.")
    val packageName               = array.slice(0, array.length - 1)
    val repositorySimpleClassName = array(array.length - 1)
    (packageName, repositorySimpleClassName)
  }
}
