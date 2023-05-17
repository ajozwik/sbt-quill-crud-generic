package pl.jozwik.quillgeneric.sbt

import sbt.{ Def, settingKey }

trait PluginKeys {
  val generateAsyncDescription           = settingKey[Seq[RepositoryDescription]]("The async repositories descriptions")
  val generateCassandraAsyncRepositories = settingKey[Seq[RepositoryDescription]]("Cassandra async repositories descriptions")
  val generateCassandraMonixRepositories = settingKey[Seq[RepositoryDescription]]("Cassandra monix repositories descriptions")
  val generateCassandraSyncRepositories  = settingKey[Seq[RepositoryDescription]]("Cassandra sync repositories descriptions")
  val generateDescription                = settingKey[Seq[RepositoryDescription]]("The repositories descriptions")
  val generateMonixRepositories          = settingKey[Seq[RepositoryDescription]]("Monix repositories descriptions")
  val generateZioRepositories          = settingKey[Seq[RepositoryDescription]]("Zio repositories descriptions")
  val quillMacroVersion                  = settingKey[String]("Quill macro version")

  val defaultSettings: Seq[Def.Setting[_]] =
    Seq(
      generateAsyncDescription := Seq.empty[RepositoryDescription],
      generateCassandraAsyncRepositories := Seq.empty[RepositoryDescription],
      generateCassandraMonixRepositories := Seq.empty[RepositoryDescription],
      generateCassandraSyncRepositories := Seq.empty[RepositoryDescription],
      generateDescription := Seq.empty[RepositoryDescription],
      generateMonixRepositories := Seq.empty[RepositoryDescription],
      generateZioRepositories := Seq.empty[RepositoryDescription],
      quillMacroVersion := "1.2.1"
    )
}
