package pl.jozwik.quillgeneric.sbt

import sbt.Keys.*
import sbt.*
import sbt.plugins.JvmPlugin
import DependencyHelper.*
import pl.jozwik.quillgeneric.sbt.generator.cassandra.{ CassandraAsyncCodeGenerator, CassandraMonixCodeGenerator, CassandraSyncCodeGenerator }
import pl.jozwik.quillgeneric.sbt.generator.jdbc.{ AsyncCodeGenerator, MonixJdbcCodeGenerator, SyncCodeGenerator, ZioJdbcCodeGenerator }
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

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  override lazy val projectSettings: Seq[Def.Setting[_]] = {
    defaultSettings ++ Seq[Def.Setting[_]](
      Compile / sourceGenerators += Def.task {
        val rootPath = (Compile / sourceManaged).value
        generate(generateDescription.value, rootPath, SyncCodeGenerator) ++
          generate(generateAsyncDescription.value, rootPath, AsyncCodeGenerator) ++
          generate(generateMonixRepositories.value, rootPath, MonixJdbcCodeGenerator) ++
          generate(generateZioRepositories.value, rootPath, ZioJdbcCodeGenerator) ++
          generate(generateCassandraSyncRepositories.value, rootPath, CassandraSyncCodeGenerator) ++
          generate(generateCassandraAsyncRepositories.value, rootPath, CassandraAsyncCodeGenerator) ++
          generate(generateCassandraMonixRepositories.value, rootPath, CassandraMonixCodeGenerator)
      }.taskValue,
      libraryDependencies ++=

        addImport(true, "repository", quillMacroVersion.value) ++
          addImport(generateDescription.value.nonEmpty, "repository-jdbc-monad", quillMacroVersion.value) ++
          addImport(generateAsyncDescription.value.nonEmpty, "quill-async-jdbc", quillMacroVersion.value) ++
          addImport(generateMonixRepositories.value.nonEmpty, "quill-jdbc-monix", quillMacroVersion.value) ++
          addImport(generateZioRepositories.value.nonEmpty, "quill-jdbc-zio", quillMacroVersion.value) ++
          addImport(
            generateCassandraSyncRepositories.value.nonEmpty || generateCassandraAsyncRepositories.value.nonEmpty,
            "repository-cassandra",
            quillMacroVersion.value
          ) ++
          addImport(generateCassandraMonixRepositories.value.nonEmpty, "quill-cassandra-monix", quillMacroVersion.value)
    )
  }

}
