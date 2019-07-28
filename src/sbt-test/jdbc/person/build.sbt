import pl.jozwik.quillgeneric.sbt.RepositoryDescription
import pl.jozwik.quillgeneric.sbt.QuillRepositoryPlugin._

val `scala_2.12` = "2.12.8"

scalaVersion := `scala_2.12`

enablePlugins(QuillRepositoryPlugin)

lazy val root = Project("person", file(".")).settings(
  libraryDependencies ++= Seq(
  ),
  generateDescription := Seq(
    RepositoryDescription("pl.jozwik.example.model.Person",
      "pl.jozwik.example.model.PersonId",
      "pl.jozwik.example.repository.PersonRepository",
      true,
      Option("pl.jozwik.example.repository.MyPersonRepository[Dialect, Naming]")),
    RepositoryDescription("pl.jozwik.example.model.Address",
      "pl.jozwik.example.model.AddressId",
      "pl.jozwik.example.repository.AddressRepository",
      true
    ),
    RepositoryDescription("pl.jozwik.example.model.Person",
      "pl.jozwik.example.model.PersonId",
      "pl.jozwik.example.generated.repository.PersonRepository",
      true,
      Option("pl.jozwik.example.repository.MyPersonRepository[Dialect, Naming]")
    ),
    RepositoryDescription("pl.jozwik.example.model.Configuration",
      "pl.jozwik.example.model.ConfigurationId",
      "pl.jozwik.example.ConfigurationRepository",
      false,
      None,
      None,
      Map("id"->"key")
    )
  )
)
  .enablePlugins(QuillRepositoryPlugin)