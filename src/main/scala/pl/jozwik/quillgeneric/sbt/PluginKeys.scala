package pl.jozwik.quillgeneric.sbt

import sbt.{ Def, File, settingKey, taskKey }

trait PluginKeys {
  val generateRepositories = taskKey[Seq[File]]("Generate scala repositories from descriptions")
  val generateDescription = settingKey[Seq[RepositoryDescription]]("The repository descriptions")
  val quillMacroVersion = settingKey[String]("Quill macro version")

  val defaultSettings: Seq[Def.Setting[_]] = Seq(
    generateDescription := Seq.empty,
    quillMacroVersion := "0.4.3.1")
}
