package pl.jozwik.example.domain.model

import pl.jozwik.quillgeneric.repository.WithId

final case class ConfigurationId(value: String) extends AnyVal

final case class Configuration(id: ConfigurationId, value: String) extends WithId[ConfigurationId]
