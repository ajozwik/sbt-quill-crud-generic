package pl.jozwik.quillgeneric.sbt

import java.io.File
import java.nio.file.Paths

import sbt._

import scala.io.Source

object CodeGenerator {

  private val packageTemplate = "__PACKAGE__"
  private val repositoryClassTemplate = "__REPOSITORY_NAME__"

  private val beanTemplate = "__BEAN__"
  private val beanPackageTemplate = "__BEAN_PACKAGE__"

  private val beanIdTemplate = "__ID__"
  private val beanIdPackageTemplate = "__ID_PACKAGE__"

  private val columnMapping = "__COLUMN_MAPPING__"

  private val importContext = "__IMPORT_CONTEXT__"

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

    val toColumnMapping = {
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
    val importCtx = if (toColumnMapping.isEmpty) {
      ""
    } else {
      "import context._"
    }

    val result = content
      .replace(packageTemplate, p)
      .replace(repositoryClassTemplate, repositorySimpleClassName)
      .replace(beanTemplate, beanSimpleClassName)
      .replace(beanPackageTemplate, beanClass)
      .replace(beanIdTemplate, beanIdSimpleClassName)
      .replace(beanIdPackageTemplate, beanIdClass)
      .replace(columnMapping, toColumnMapping)
      .replace(importContext, importCtx)

    (file, result)
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
