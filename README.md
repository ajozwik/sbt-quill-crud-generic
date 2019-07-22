# sbt-quill-crud-generic

[![Build Status](https://travis-ci.org/ajozwik/sbt-quill-crud-generic.svg?branch=master)](https://travis-ci.org/ajozwik/sbt-quill-crud-generic)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ajozwik/sbt-quill-crud-generic/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ajozwik/sbt-quill-crud-generic)

Plugin to create generic repository based on [quill-generic](https://github.com/ajozwik/quill-generic) and [quill](https://github.com/getquill/quill)

See [quill-macro-example](https://github.com/ajozwik/quill-macro-example) for example of usage

- create model classes (must be visible during compilation)

```
package pl.jozwik.example.model

import java.time.LocalDate

import pl.jozwik.quillgeneric.quillmacro.WithId

final case class PersonId(value: Int) extends AnyVal

final case class Person(id: PersonId, firstName: String, lastName: String, birthDate: LocalDate) extends WithId[PersonId]
```

- add imports:
```
import pl.jozwik.quillgeneric.sbt.RepositoryDescription
import pl.jozwik.quillgeneric.sbt.QuillRepositoryPlugin._
```
- add settings
```
  generateDescription := Seq(
    RepositoryDescription("pl.jozwik.example.model.Person",
    "pl.jozwik.example.model.PersonId",
    "pl.jozwik.example.repository.PersonRepository")
    ),
  quillMacroVersion := "0.1.5"
```

- enable auto plugin

```
  enablePlugins(QuillRepositoryPlugin)
```
