package pl.jozwik.quillgeneric.sbt

import sbt.Keys._
import sbt.plugins.JvmPlugin
import sbt.{ Def, _ }

object QuillRepositoryPlugin extends AutoPlugin {

  override def trigger = allRequirements

  override def requires = JvmPlugin

  import PluginKeys._

  override lazy val projectSettings: Seq[Def.Setting[_]] =
    defaultSettings ++ Seq(
      sourceGenerators in Compile += Def.task {
        val rootPath = (sourceManaged in Compile).value
        generateDescription.value.map {
          CodeGenerator.generate(rootPath)
        }
      }.taskValue,
      libraryDependencies ++= Seq("com.github.ajozwik" %% "macro-quill" % quillMacroVersion.value))

}
