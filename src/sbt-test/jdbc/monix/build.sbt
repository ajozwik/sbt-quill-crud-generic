import pl.jozwik.quillgeneric.sbt._

val `scalaVersion_2.12` = "2.12.12"

name := "quill-macro-example"

resolvers += Resolver.sonatypeRepo("releases")

resolvers += Resolver.sonatypeRepo("snapshots")

ThisBuild / scalaVersion := `scalaVersion_2.12`

ThisBuild / organization := "pl.jozwik.demo"

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "utf8",             // Option and arguments on same line
  "-Xfatal-warnings", // New lines for each options
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps"
)

lazy val readQuillMacroVersionSbt = sys.props.get("plugin.version") match {
  case Some(pluginVersion) =>
    pluginVersion
  case _ =>
    sys.error("""|The system property 'plugin.version' is not defined.
                 |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}

val `org.scalatest_scalatest` = "org.scalatest" %% "scalatest" % "3.0.8" % "test"

val `org.scalacheck_scalacheck` = "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"

val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

val `ch.qos.logback_logback-classic` = "ch.qos.logback" % "logback-classic" % "1.2.3"

val `com.h2database_h2` = "com.h2database" % "h2" % "1.4.200"

val basePackage        = "pl.jozwik.example"
val domainModelPackage = s"$basePackage.domain.model"

lazy val common = projectWithName("common", file("common"))
  .settings(libraryDependencies ++= Seq("com.github.ajozwik" %% "macro-quill" % readQuillMacroVersionSbt))

val monixPackage                   = s"$basePackage.monix"
val monixRepositoryPackage         = s"$monixPackage.impl"
val generateMonixRepositoryPackage = s"$monixPackage.repository"

lazy val monix = projectWithName("monix", file("monix"))
  .settings(
    generateMonixRepositories ++= Seq(
          RepositoryDescription(
            s"$domainModelPackage.Address",
            BeanIdClass(s"$domainModelPackage.AddressId"),
            s"$generateMonixRepositoryPackage.AddressRepositoryGen",
            true,
            Option(s"$monixRepositoryPackage.AddressRepositoryImpl[Dialect, Naming]"),
            None,
            Map("city" -> "city")
          ),
          RepositoryDescription(
            s"$domainModelPackage.Cell4d",
            BeanIdClass(s"$domainModelPackage.Cell4dId", KeyType.Composite),
            s"$generateMonixRepositoryPackage.Cell4dRepositoryGen",
            false,
            None,
            None,
            Map("id.fk1" -> "x", "id.fk2" -> "y", "id.fk3" -> "z", "id.fk4" -> "t")
          ),
          RepositoryDescription(
            s"$domainModelPackage.Configuration",
            BeanIdClass(s"$domainModelPackage.ConfigurationId"),
            s"$generateMonixRepositoryPackage.ConfigurationRepositoryGen",
            false,
            None,
            None,
            Map("id" -> "key")
          ),
          RepositoryDescription(
            s"$domainModelPackage.Person",
            BeanIdClass(s"$domainModelPackage.PersonId"),
            s"$generateMonixRepositoryPackage.PersonRepositoryGen",
            true,
            Option(s"$monixRepositoryPackage.PersonRepositoryImpl[Dialect, Naming]"),
            None,
            Map("birthDate" -> "dob")
          ),
          RepositoryDescription(
            s"$domainModelPackage.Product",
            BeanIdClass(s"$domainModelPackage.ProductId"),
            s"$generateMonixRepositoryPackage.ProductRepositoryGen",
            true
          ),
          RepositoryDescription(
            s"$domainModelPackage.Sale",
            BeanIdClass(s"$domainModelPackage.SaleId", KeyType.Composite),
            s"$generateMonixRepositoryPackage.SaleRepositoryGen",
            false,
            None,
            None,
            Map("id.fk1" -> "PRODUCT_ID", "id.fk2" -> "PERSON_ID")
          )
        )
  )
  .dependsOn(common, common % "test -> test")
  .enablePlugins(QuillRepositoryPlugin)

def projectWithName(name: String, file: File): Project =
  Project(name, file)
    .settings(
      libraryDependencies ++= Seq(
            `org.scalatest_scalatest`,
            `org.scalacheck_scalacheck`,
            `com.typesafe.scala-logging_scala-logging`,
            `ch.qos.logback_logback-classic`,
            `com.h2database_h2` % Test
          ),
      sources in (Compile, doc) := Seq.empty
    )
