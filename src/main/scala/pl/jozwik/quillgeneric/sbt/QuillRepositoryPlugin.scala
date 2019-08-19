package pl.jozwik.quillgeneric.sbt

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin
import DependencyHelper._

object QuillRepositoryPlugin extends AutoPlugin {

  override def trigger: sbt.PluginTrigger = allRequirements

  override def requires: sbt.Plugins = JvmPlugin

  object autoImport extends PluginKeys

  import autoImport._

  private def generate(descriptions: Seq[RepositoryDescription], rootPath: File, generator: Generator) =
    descriptions.map { d =>
      val (file, content) = generator.generate(rootPath)(d)
      IO.write(file, content)
      file
    }

  override lazy val projectSettings: Seq[Def.Setting[_]] =
    defaultSettings ++ Seq(
          sourceGenerators in Compile += Def.task {
                val rootPath = (sourceManaged in Compile).value
                generate(generateDescription.value, rootPath, SyncCodeGenerator) ++
                  generate(generateMonixRepositories.value, rootPath, MonixJdbcCodeGenerator)
              }.taskValue,
          libraryDependencies ++=
              addImport(true, "macro-quill", quillMacroVersion.value) ++
                  addImport(generateDescription.value.nonEmpty, "quill-jdbc-macro", quillMacroVersion.value) ++
                  addImport(generateMonixRepositories.value.nonEmpty, "quill-jdbc-monix-macro", quillMacroVersion.value)
        )

}
