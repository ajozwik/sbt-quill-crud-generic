package pl.jozwik.quillgeneric.sbt

import sbt.{ Def, File, settingKey, taskKey }

trait PluginKeys {
  val generateRepositories      = taskKey[Seq[File]]("Generate scala repositories from descriptions")
  val generateMonixRepositories = settingKey[Seq[RepositoryDescription]]("Monix repositories descriptions")
  val generateDescription       = settingKey[Seq[RepositoryDescription]]("The repositories descriptions")
  val quillMacroVersion         = settingKey[String]("Quill macro version")

  val defaultSettings: Seq[Def.Setting[_]] =
    Seq(generateDescription := Seq.empty, generateMonixRepositories := Seq.empty, quillMacroVersion := "0.7.1.5")
}
