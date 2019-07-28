import pl.jozwik.quillgeneric.sbt.RepositoryDescription

val `scala_2.12` = "2.12.8"

resolvers += Resolver.sonatypeRepo("releases")

ThisBuild / organization := "pl.jozwik.demo"

ThisBuild / scalacOptions ++= Seq(
  "-encoding", "utf8", // Option and arguments on same line
  "-Xfatal-warnings", // New lines for each options
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)


val `org.scalatest_scalatest` = "org.scalatest" %% "scalatest" % "3.0.8" % "test"

val `org.scalacheck_scalacheck` = "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"

val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

val `ch.qos.logback_logback-classic` = "ch.qos.logback" % "logback-classic" % "1.2.3"

val `com.h2database_h2` = "com.h2database" % "h2" % "1.4.199"

lazy val root = Project("quill-macro-example", file(".")).settings(
  libraryDependencies ++= Seq(
    `org.scalatest_scalatest`,
    `org.scalacheck_scalacheck`,
    `com.typesafe.scala-logging_scala-logging`,
    `ch.qos.logback_logback-classic`,
    `com.h2database_h2` % Test
  ),
  generateDescription := Seq(
    RepositoryDescription("pl.jozwik.example.model.Person",
      "pl.jozwik.example.model.PersonId",
      "pl.jozwik.example.repository.PersonRepository",
      true,
      Option("pl.jozwik.example.repository.MyPersonRepository[Dialect, Naming]"),
      None),
    RepositoryDescription("pl.jozwik.example.model.Address",
      "pl.jozwik.example.model.AddressId",
      "pl.jozwik.example.repository.AddressRepository",
      true,
      None,
      None,
      Map("city"-> "city")),
    RepositoryDescription("pl.jozwik.example.model.Person",
      "pl.jozwik.example.model.PersonId",
      "pl.jozwik.example.PersonRepository",
      true,
      Option("pl.jozwik.example.repository.MyPersonRepository[Dialect, Naming]"),
      None),
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


