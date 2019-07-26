package pl.jozwik.quillgeneric.sbt

import java.io.File
import java.nio.file.Paths

import sbt._

import scala.io.Source

object CodeGenerator {

  private val PackageTemplate = "__PACKAGE__"

  private val RepositoryClassTemplate = "__REPOSITORY_NAME__"

  private val BeanTemplate = "__BEAN__"

  private val BeanClassImport = "__BEAN_CLASS_IMPORT__"

  private val BeanIdTemplate = "__ID__"

  private val BeanIdClassImport = "__ID_CLASS_IMPORT__"

  private val ColumnMapping = "__COLUMN_MAPPING__"

  private val ImportContext = "__IMPORT_CONTEXT__"

  private val TableNamePattern = "__TABLE_NAME__"

  private val RepositoryTraitImport = "__REPOSITORY_TRAIT_IMPORT__"

  private val RepositoryTraitSimpleClassName = "__REPOSITORY_TRAIT_SIMPLE_NAME__"

  private val RepositoryImport = "__REPOSITORY_IMPORT__"

  private val macroRepositoryImport = "import pl.jozwik.quillgeneric.quillmacro.sync.Repository"

  private val template = "$template$.txt"

  private lazy val defaultContent = readTemplate

  def generate(rootPath: File, content: String = defaultContent)(description: RepositoryDescription): (File, String) = {
    import description._
    val path = Paths.get(rootPath.getAbsolutePath, packageName: _*)
    val dir = path.toFile
    dir.mkdirs()
    val p = packageName match {
      case Seq() =>
        ""
      case s =>
        s"""package ${s.mkString(".")}"""
    }
    val file = dir / s"$repositorySimpleClassName.scala"

    val columnMapping = toColumnMapping(mapping)
    val importCtx = toImportContext(columnMapping)

    val (repositoryTraitSimpleClassName, repositoryImport, defaultRepositoryImport) =
      toRepositoryTraitImport(repositoryTrait, packageName, repositoryPackageName, repositoryTraitSimpleClassNameOpt)

    val result = content
      .replace(RepositoryTraitSimpleClassName, repositoryTraitSimpleClassName)
      .replace(RepositoryTraitImport, repositoryImport)
      .replace(PackageTemplate, p)
      .replace(RepositoryClassTemplate, repositorySimpleClassName)
      .replace(BeanTemplate, beanSimpleClassName)
      .replace(BeanClassImport, createImport(packageName, beanPackageName, beanClass))
      .replace(BeanIdTemplate, beanIdSimpleClassName)
      .replace(BeanIdClassImport, createImport(packageName, beanIdPackageName, beanIdClass))
      .replace(ColumnMapping, columnMapping)
      .replace(ImportContext, importCtx)
      .replace(TableNamePattern, toTableName)
      .replace(RepositoryImport, defaultRepositoryImport)

    (file, result)
  }

  private def toRepositoryTraitImport(
    repositoryTrait: Option[String],
    packageName: Seq[String],
    repositoryPackageName: Seq[String],
    repositoryTraitSimpleClassNameOpt: String) =
    if (repositoryTraitSimpleClassNameOpt.isEmpty) {
      (s"Repository[$BeanIdTemplate, $BeanTemplate]", "", macroRepositoryImport)
    } else {
      val clazzName = repositoryTrait.getOrElse("")
      val withoutGeneric = clazzName.indexOf('[') match {
        case -1 =>
          clazzName
        case index =>
          clazzName.substring(0, index)
      }
      val imp = createImport(packageName, repositoryPackageName, withoutGeneric)
      (s"$repositoryTraitSimpleClassNameOpt", imp, "")
    }

  private def createImport(packageNameSeq: Seq[String], packageNameBean: Seq[String], className: String) =
    if (packageNameSeq == packageNameBean) "" else s"import $className"

  private def toColumnMapping(mapping: Map[String, String]) = {
    val columnMapping = {
      val map = mapping.map {
        case (k, v) =>
          s"""alias(_.$k, "$v")"""
      }
      if (map.isEmpty) {
        ""
      } else {
        s""", ${map.mkString(", ")}"""
      }
    }
    columnMapping
  }

  private def toImportContext(toColumnMapping: String) = {
    val importCtx = if (toColumnMapping.isEmpty) {
      ""
    } else {
      "import context._"
    }
    importCtx
  }

  private lazy val readTemplate: String = {
    val input = Option(getClass.getClassLoader.getResourceAsStream(template))
      .getOrElse(getClass.getClassLoader.getResourceAsStream(s"/$template"))
    try {
      Source.fromInputStream(input).mkString
    } finally {
      input.close()
    }
  }
}
