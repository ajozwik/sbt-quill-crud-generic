# sbt-quill-crud-generic

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/76974955a39d4373b990b656fb1c9de5)](https://app.codacy.com/manual/ajozwik/sbt-quill-crud-generic?utm_source=github.com&utm_medium=referral&utm_content=ajozwik/sbt-quill-crud-generic&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.org/ajozwik/sbt-quill-crud-generic.svg?branch=master)](https://travis-ci.org/ajozwik/sbt-quill-crud-generic)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.ajozwik/sbt-quill-crud-generic/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.ajozwik/sbt-quill-crud-generic)
[![Coverage Status](https://coveralls.io/repos/github/ajozwik/sbt-quill-crud-generic/badge.svg?branch=master)](https://coveralls.io/github/ajozwik/sbt-quill-crud-generic?branch=master)

Plugin to create generic repository based on [quill-generic](https://github.com/ajozwik/quill-generic) and [quill](https://github.com/getquill/quill)

See [quill-macro-example](https://github.com/ajozwik/quill-macro-example) for example of usage

- create model classes (must be visible during compilation)

- add plugin (project/plugins.sbt)
```
addSbtPlugin("com.github.ajozwik" % "sbt-quill-crud-generic" % "<version>")
```

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
    "pl.jozwik.example.repository.PersonRepository",
    true,
    Option("pl.jozwik.example.repository.MyPersonRepository[Dialect, Naming]"))
    )
```
For simpler inject support (guice/spring) you can use own trait

```
package pl.jozwik.example.repository

import java.time.LocalDate

import io.getquill.NamingStrategy
import io.getquill.context.sql.idiom.SqlIdiom
import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository
import pl.jozwik.quillgeneric.sbt.model.{ Person, PersonId }

trait MyPersonRepository[Dialect <: SqlIdiom, Naming <: NamingStrategy]
  extends JdbcRepository[PersonId, Person, Dialect, Naming] {
  def max: Option[LocalDate] = {
    import context._
    val r = dynamicSchema.map(p => p.birthDate)
    context.run(r.max)
  }
}
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

