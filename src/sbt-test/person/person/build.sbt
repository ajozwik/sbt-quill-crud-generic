import pl.jozwik.quillgeneric.sbt.RepositoryDescription
import pl.jozwik.quillgeneric.sbt.QuillRepositoryPlugin._

val `scala_2.12` = "2.12.8"

enablePlugins(QuillRepositoryPlugin)

lazy val root = Project("person", file(".")).settings(
  libraryDependencies ++= Seq(
  ),
  generateDescription := Seq(
    RepositoryDescription("pl.jozwik.example.model.Person",
      "pl.jozwik.example.model.PersonId",
      "pl.jozwik.example.repository.PersonRepository",
      None),

    RepositoryDescription("pl.jozwik.example.model.Address",
      "pl.jozwik.example.model.AddressId",
      "pl.jozwik.example.repository.AddressRepository",None, Map("city"-> "city")))
)
  .enablePlugins(QuillRepositoryPlugin)