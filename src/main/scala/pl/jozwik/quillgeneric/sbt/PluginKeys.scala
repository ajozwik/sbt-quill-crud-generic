package pl.jozwik.quillgeneric.sbt

import sbt.{ Def, settingKey }

trait PluginKeys {
  val generateMonixRepositories = settingKey[Seq[RepositoryDescription]]("Monix repositories descriptions")
  val generateDescription       = settingKey[Seq[RepositoryDescription]]("The repositories descriptions")
  val quillMacroVersion         = settingKey[String]("Quill macro version")

  val defaultSettings: Seq[Def.Setting[_]] =
    Seq(generateDescription := Seq.empty, generateMonixRepositories := Seq.empty, quillMacroVersion := "0.8.0-SNAPSHOT")
}
