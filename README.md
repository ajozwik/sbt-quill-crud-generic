# sbt-quill-crud-generic

[![Build Status](https://travis-ci.org/ajozwik/sbt-quill-crud-generic.svg?branch=master)](https://travis-ci.org/ajozwik/sbt-quill-crud-generic)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ajozwik/sbt-quill-crud-generic/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ajozwik/sbt-quill-crud-generic)
[![Coverage Status](https://coveralls.io/repos/github/ajozwik/sbt-quill-crud-generic/badge.svg?branch=master)](https://coveralls.io/github/ajozwik/sbt-quill-crud-generic?branch=master)

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
  quillMacroVersion := "0.2.1.2"
```
For simpler inject support (guice/spring) you can use own trait

```
package pl.jozwik.example

import pl.jozwik.example.model.{Person, PersonId}
import pl.jozwik.quillgeneric.quillmacro.sync.Repository

trait MyPersonRepository extends Repository[PersonId,Person]
```
and point to them
```
 RepositoryDescription("pl.jozwik.example.model.Person",
      "pl.jozwik.example.model.PersonId",
      "pl.jozwik.example.PersonRepository",
      Option("pl.jozwik.example.MyPersonRepository")
  )
```


- enable auto plugin

```
  enablePlugins(QuillRepositoryPlugin)
```

- run compile task

```
sbt compile
```

The generated repositories are in:
```
target/scala-2.12/src_managed/main/
```

