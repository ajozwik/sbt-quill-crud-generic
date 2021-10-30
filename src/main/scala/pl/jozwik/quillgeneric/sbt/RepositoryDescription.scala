package pl.jozwik.quillgeneric.sbt

object KeyType extends Enumeration {
  type KeyType = Value
  val Single, Composite: KeyType = Value
}

final case class BeanIdClass(name: String, keyType: KeyType.Value = KeyType.Single)

final case class RepositoryDescription(
    beanClass: String,
    beanIdClass: BeanIdClass,
    repositoryClassName: String,
    generateId: Boolean = false,
    repositoryTrait: Option[String] = None,
    tableName: Option[String] = None,
    mapping: Map[String, String] = Map.empty[String, String]
) {

  val (packageName: Seq[String], repositorySimpleClassName: String)   = toPackageNameSimpleClass(repositoryClassName)
  val (beanPackageName: Seq[String], beanSimpleClassName: String)     = toPackageNameSimpleClass(beanClass)
  val (beanIdPackageName: Seq[String], beanIdSimpleClassName: String) = toPackageNameSimpleClass(beanIdClass.name)

  val (repositoryPackageName: Seq[String], repositoryTraitSimpleClassNameOpt: String) =
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
