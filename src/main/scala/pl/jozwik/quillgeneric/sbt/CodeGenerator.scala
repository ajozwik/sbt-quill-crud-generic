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

  private val template = "$template$.txt"

  private val defaultContent = readTemplate

  def generate(rootPath: File, content: String = defaultContent)(description: RepositoryDescription) = {
    val (packageName, repositorySimpleClassName) = toPackageNameSimpleClass(description.repositoryClassName)
    val (_, beanSimpleClassName) = toPackageNameSimpleClass(description.beanClass)
    val (_, beanIdSimpleClassName) = toPackageNameSimpleClass(description.beanIdClass)
    val path = Paths.get(rootPath.getAbsolutePath, packageName: _*)
    val dir = path.toFile
    dir.mkdirs()
    val file = dir / s"$repositorySimpleClassName.scala"
    val result = content
      .replace(packageTemplate, packageName.mkString("."))
      .replace(repositoryClassTemplate, repositorySimpleClassName)
      .replace(beanTemplate, beanSimpleClassName)
      .replace(beanPackageTemplate, description.beanClass)
      .replace(beanIdTemplate, beanIdSimpleClassName)
      .replace(beanIdPackageTemplate, description.beanIdClass)

    IO.write(file, result)
    file
  }

  private def toPackageNameSimpleClass(className: String): (Seq[String], String) = {
    val array = className.split("\\.")
    val packageName = array.slice(0, array.length - 1)
    val repositorySimpleClassName = array(array.length - 1)
    (packageName, repositorySimpleClassName)
  }

  private val readTemplate: String = {
    val input = Option(getClass.getClassLoader.getResourceAsStream(template))
      .getOrElse(getClass.getClassLoader.getResourceAsStream(s"/$template"))
    try {
      Source.fromInputStream(input).mkString
    } finally {
      input.close()
    }
  }
}
