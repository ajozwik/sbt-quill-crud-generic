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

  private val template = "$template$.txt"

  private lazy val defaultContent = readTemplate

  def generate(rootPath: File, content: String = defaultContent)(description: RepositoryDescription): (File, String) = {
    val (packageName, repositorySimpleClassName) = toPackageNameSimpleClass(description.repositoryClassName)
    val (_, beanSimpleClassName) = toPackageNameSimpleClass(description.beanClass)
    val (_, beanIdSimpleClassName) = toPackageNameSimpleClass(description.beanIdClass)
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
      val map = description.mapping.map {
        case (k, v) =>
          s"""alias(_.$k, "$v")"""
      }
      if (map.isEmpty) {
        ""
      } else {
        s""", ${map.mkString(", ")}"""
      }
    }

    val result = content
      .replace(packageTemplate, p)
      .replace(repositoryClassTemplate, repositorySimpleClassName)
      .replace(beanTemplate, beanSimpleClassName)
      .replace(beanPackageTemplate, description.beanClass)
      .replace(beanIdTemplate, beanIdSimpleClassName)
      .replace(beanIdPackageTemplate, description.beanIdClass)
      .replace(columnMapping, toColumnMapping)

    (file, result)
  }

  private[sbt] def toPackageNameSimpleClass(className: String): (Seq[String], String) = {
    val array = className.split("\\.")
    val packageName = array.slice(0, array.length - 1)
    val repositorySimpleClassName = array(array.length - 1)
    (packageName, repositorySimpleClassName)
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
