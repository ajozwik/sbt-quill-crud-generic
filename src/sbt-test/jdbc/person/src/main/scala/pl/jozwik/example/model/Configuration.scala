package pl.jozwik.example.model

import pl.jozwik.quillgeneric.quillmacro.WithId

final case class ConfigurationId(value: String) extends AnyVal

final case class Configuration(id: ConfigurationId, value: String) extends WithId[ConfigurationId]
