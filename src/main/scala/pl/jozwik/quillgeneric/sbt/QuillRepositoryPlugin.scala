package pl.jozwik.quillgeneric.sbt

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin

object QuillRepositoryPlugin extends AutoPlugin {

  override def trigger: sbt.PluginTrigger = allRequirements

  override def requires: sbt.Plugins = JvmPlugin

  object autoImport extends PluginKeys

  import autoImport._

  override lazy val projectSettings: Seq[Def.Setting[_]] =
    defaultSettings ++ Seq(
      sourceGenerators in Compile += Def.task {
        val rootPath = (sourceManaged in Compile).value
        generateDescription.value.map {
          d =>
            val (file, content) = CodeGenerator.generate(rootPath)(d)
            IO.write(file, content)
            file
        }
      }.taskValue,
      libraryDependencies ++= Seq("com.github.ajozwik" %% "macro-quill" % quillMacroVersion.value))

}
