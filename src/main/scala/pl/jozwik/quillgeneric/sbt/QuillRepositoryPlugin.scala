package pl.jozwik.quillgeneric.sbt

import sbt.Keys._
import sbt._
import sbt.plugins.JvmPlugin
import DependencyHelper._
import pl.jozwik.quillgeneric.sbt.generator.cassandra.{ CassandraAsyncCodeGenerator, CassandraMonixCodeGenerator, CassandraSyncCodeGenerator }
import pl.jozwik.quillgeneric.sbt.generator.jdbc.{ AsyncCodeGenerator, MonixJdbcCodeGenerator, SyncCodeGenerator }
import pl.jozwik.quillgeneric.sbt.generator.Generator

object QuillRepositoryPlugin extends AutoPlugin {

  override def trigger: sbt.PluginTrigger = allRequirements

  override def requires: sbt.Plugins = JvmPlugin

  object autoImport extends PluginKeys

  import autoImport._

  private def generate(descriptions: Seq[RepositoryDescription], rootPath: File, generator: Generator): Seq[File] =
    descriptions.map { d =>
      val (file, content) = generator.generate(rootPath)(d)
      IO.write(file, content)
      file
    }

  override lazy val projectSettings: Seq[Def.Setting[_]] =
    defaultSettings ++ Seq(
      Compile / sourceGenerators += Def.task {
        val rootPath = (Compile / sourceManaged).value
        generate(generateDescription.value, rootPath, SyncCodeGenerator) ++
          generate(generateAsyncDescription.value, rootPath, AsyncCodeGenerator) ++
          generate(generateMonixRepositories.value, rootPath, MonixJdbcCodeGenerator) ++
          generate(generateCassandraSyncRepositories.value, rootPath, CassandraSyncCodeGenerator) ++
          generate(generateCassandraAsyncRepositories.value, rootPath, CassandraAsyncCodeGenerator) ++
          generate(generateCassandraMonixRepositories.value, rootPath, CassandraMonixCodeGenerator)
      }.taskValue,
      libraryDependencies ++=
        addImport(true, "macro-quill", quillMacroVersion.value).toSeq ++
          addImport(generateDescription.value.nonEmpty, "quill-jdbc-macro", quillMacroVersion.value) ++
          addImport(generateAsyncDescription.value.nonEmpty, "quill-async-jdbc-macro", quillMacroVersion.value) ++
          addImport(generateMonixRepositories.value.nonEmpty, "quill-jdbc-monix-macro", quillMacroVersion.value) ++
          addImport(
            generateCassandraSyncRepositories.value.nonEmpty || generateCassandraAsyncRepositories.value.nonEmpty,
            "quill-cassandra-macro",
            quillMacroVersion.value
          ) ++
          addImport(
            generateCassandraSyncRepositories.value.nonEmpty || generateCassandraAsyncRepositories.value.nonEmpty,
            "quill-cassandra-macro",
            quillMacroVersion.value
          ) ++
          addImport(generateCassandraMonixRepositories.value.nonEmpty, "quill-cassandra-monix-macro", quillMacroVersion.value)
    )

}
