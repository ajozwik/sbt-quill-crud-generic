package pl.jozwik.quillgeneric.sbt

import java.nio.file.Paths

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

import scala.io.Source

object SbtQuillCrudGenericPlugin extends AutoPlugin {

  final case class RepositoryDescription(beanClass: String, beanIdClass: String, repositoryClassName: String)

  override def trigger = allRequirements

  override def requires = JvmPlugin

  private val template = "$template$.txt"

  private val packageTemplate = "__PACKAGE__"
  private val repositoryClassTemplate = "__REPOSITORY_NAME__"

  private val beanTemplate = "__BEAN__"
  private val beanPackageTemplate = "__BEAN_PACKAGE__"

  private val beanIdTemplate = "__ID__"
  private val beanIdPackageTemplate = "__ID_PACKAGE__"

  object autoImport {
    val generateRepositories = taskKey[Seq[File]]("Generate scala repositories from descriptions")
    val repositories = settingKey[Seq[RepositoryDescription]]("The repository descriptions")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    repositories := Seq.empty,
    sourceGenerators in Compile += Def.task {
      val content = readTemplate
      val rootPath = (sourceManaged in Compile).value
      repositories.value.map {
        case RepositoryDescription(beanClass, beanIdClass, repositoryClassName) =>
          val (packageName, repositorySimpleClassName) = toPackageNameSimpleClass(repositoryClassName)
          val (_, beanSimpleClassName) = toPackageNameSimpleClass(beanClass)
          val (_, beanIdSimpleClassName) = toPackageNameSimpleClass(beanIdClass)
          val path = Paths.get(rootPath.getAbsolutePath, packageName: _*)
          val dir = path.toFile
          dir.mkdirs()
          val file = dir / s"$repositorySimpleClassName.scala"
          val result = content
            .replace(packageTemplate, packageName.mkString("."))
            .replace(repositoryClassTemplate, repositorySimpleClassName)
            .replace(beanTemplate, beanSimpleClassName)
            .replace(beanPackageTemplate, beanClass)
            .replace(beanIdTemplate, beanIdSimpleClassName)
            .replace(beanIdPackageTemplate, beanIdClass)

          IO.write(file, result)
          file
      }
    }.taskValue,
    libraryDependencies ++= Seq("com.github.ajozwik" %% "macro-quill" % "0.1.3"))

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq()

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
