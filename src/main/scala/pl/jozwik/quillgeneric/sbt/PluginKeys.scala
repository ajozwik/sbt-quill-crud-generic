package pl.jozwik.quillgeneric.sbt

import sbt.{ File, settingKey, taskKey }

object PluginKeys extends PluginKeys {
  val defaultSettings = Seq(
    generateDescription := Seq.empty,
    quillMacroVersion := "0.1.3")
}

trait PluginKeys {
  val generateRepositories = taskKey[Seq[File]]("Generate scala repositories from descriptions")
  val generateDescription = settingKey[Seq[RepositoryDescription]]("The repository descriptions")
  val quillMacroVersion = settingKey[String]("Quill macro version")
}
