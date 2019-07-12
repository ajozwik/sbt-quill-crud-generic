package pl.jozwik.quillgeneric.sbt

import sbt.Keys._
import sbt.{Def, _}
import sbt.plugins.JvmPlugin

import scala.io.Source

object QuillRepositoryPlugin extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = JvmPlugin

  private val template = "$template$.txt"

  import PluginKeys._

  override lazy val projectSettings: Seq[Def.Setting[_]] =
    defaultSettings ++ Seq(
    sourceGenerators in Compile += Def.task {
      val content = readTemplate
      val rootPath = (sourceManaged in Compile).value
      generateDescription.value.map {
        CodeGenerator.generate(rootPath, content)
      }
    }.taskValue,
    libraryDependencies ++= Seq("com.github.ajozwik" %% "macro-quill" % quillMacroVersion.value))


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
