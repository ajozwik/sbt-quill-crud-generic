import pl.jozwik.quillgeneric.sbt._

lazy val readQuillMacroVersionSbt = sys.props.get("plugin.version") match {
  case Some(pluginVersion) =>
    pluginVersion
  case _ =>
    sys.error("""|The system property 'plugin.version' is not defined.
                 |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}

lazy val common = projectWithName("common", file("common"))
  .settings(libraryDependencies ++= Seq("com.github.ajozwik" %% "macro-quill" % readQuillMacroVersionSbt))

ThisBuild / resolvers += Resolver.sonatypeRepo("releases")

ThisBuild / resolvers += Resolver.sonatypeRepo("snapshots")

val `scalaVersion_2.13` = "2.13.5"

val `scalaVersion_2.12` = "2.12.12"

name := "quill-macro-example"

ThisBuild / scalaVersion := `scalaVersion_2.12`

ThisBuild / crossScalaVersions := Seq(`scalaVersion_2.13`, `scalaVersion_2.12`)

ThisBuild / scapegoatVersion := "1.4.8"

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

val scalaTestVersion = "3.2.6"

val `org.scalatest_scalatest` = "org.scalatest" %% "scalatest" % scalaTestVersion % Test

val `org.scalacheck_scalacheck` = "org.scalacheck" %% "scalacheck" % "1.15.3" % Test

val `org.scalatestplus_scalacheck-1-15` = "org.scalatestplus" %% "scalacheck-1-15" % s"$scalaTestVersion.0" % Test

val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

val `ch.qos.logback_logback-classic` = "ch.qos.logback" % "logback-classic" % "1.2.3"

val `com.h2database_h2` = "com.h2database" % "h2" % "1.4.200"

val `org.cassandraunit_cassandra-unit` = "org.cassandraunit" % "cassandra-unit" % "3.11.2.0"

val `com.datastax.cassandra_cassandra-driver-extras` = "com.datastax.cassandra" % "cassandra-driver-extras" % "3.9.0"

val basePackage        = "pl.jozwik.example"
val domainModelPackage = s"$basePackage.domain.model"

val generateAsyncRepositoryPackage = s"$basePackage.repository.async"
val repositoryAsyncPackage         = s"$basePackage.async.impl"

lazy val async = projectWithSbtPlugin("async", file("async"))
  .settings(
    generateAsyncDescription := Seq(
      RepositoryDescription(
        s"$domainModelPackage.Address",
        BeanIdClass(s"$domainModelPackage.AddressId"),
        s"$generateAsyncRepositoryPackage.AddressRepositoryGen",
        true,
        Option(s"$repositoryAsyncPackage.AddressRepositoryImpl[Dialect, Naming, Connection]"),
        None,
        Map("city" -> "city")
      ),
      RepositoryDescription(
        s"$domainModelPackage.Person",
        BeanIdClass(s"$domainModelPackage.PersonId"),
        s"$generateAsyncRepositoryPackage.PersonRepositoryGen",
        true,
        Option(s"$repositoryAsyncPackage.PersonRepositoryImpl[Dialect, Naming, Connection]"),
        None
      )
    )
  )

val generateRepositoryPackage = s"$basePackage.repository"
val repositoryPackage         = s"$basePackage.sync.impl"

lazy val sync = projectWithSbtPlugin("sync", file("sync"))
  .settings(
    generateDescription := Seq(
      RepositoryDescription(
        s"$domainModelPackage.Address",
        BeanIdClass(s"$domainModelPackage.AddressId"),
        s"$generateRepositoryPackage.AddressRepositoryGen",
        true,
        Option(s"$repositoryPackage.AddressRepositoryImpl[Dialect, Naming]"),
        None,
        Map("city" -> "city")
      ),
      RepositoryDescription(
        s"$domainModelPackage.Cell4d",
        BeanIdClass(s"$domainModelPackage.Cell4dId", KeyType.Composite),
        s"$generateRepositoryPackage.Cell4dRepositoryGen",
        false,
        None,
        None,
        Map("id.fk1" -> "x", "id.fk2" -> "y", "id.fk3" -> "z", "id.fk4" -> "t")
      ),
      RepositoryDescription(
        s"$domainModelPackage.Configuration",
        BeanIdClass(s"$domainModelPackage.ConfigurationId"),
        s"$basePackage.ConfigurationRepositoryGen",
        false,
        None,
        None,
        Map("id" -> "key")
      ),
      RepositoryDescription(
        s"$domainModelPackage.Person",
        BeanIdClass(s"$domainModelPackage.PersonId"),
        s"$generateRepositoryPackage.PersonRepositoryGen",
        true,
        Option(s"$repositoryPackage.PersonRepositoryImpl[Dialect, Naming]"),
        None
      ),
      RepositoryDescription(
        s"$domainModelPackage.Product",
        BeanIdClass(s"$domainModelPackage.ProductId"),
        s"$generateRepositoryPackage.ProductRepositoryGen",
        true
      ),
      RepositoryDescription(
        s"$domainModelPackage.Sale",
        BeanIdClass(s"$domainModelPackage.SaleId", KeyType.Composite),
        s"$generateRepositoryPackage.SaleRepositoryGen",
        false,
        None,
        None,
        Map("id.fk1" -> "PRODUCT_ID", "id.fk2" -> "PERSON_ID")
      )
    )
  )

val monixPackage                   = s"$basePackage.monix"
val monixRepositoryPackage         = s"$monixPackage.impl"
val generateMonixRepositoryPackage = s"$monixPackage.repository"

lazy val monix = projectWithSbtPlugin("monix", file("monix"))
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

val cassandraPackage                        = s"$basePackage.cassandra"
val cassandraModelPackage                   = s"$cassandraPackage.model"
val generateCassandraRepositoryPackage      = s"$cassandraPackage.sync.repository"
val generateCassandraAsyncRepositoryPackage = s"$cassandraPackage.async.repository"

lazy val cassandra = projectWithCassandra("cassandra", file("cassandra"))
  .settings(
    generateCassandraSyncRepositories ++= Seq(
      RepositoryDescription(
        s"$cassandraModelPackage.Address",
        BeanIdClass(s"$cassandraModelPackage.AddressId"),
        s"$generateCassandraRepositoryPackage.AddressRepositoryGen"
      )
    ),
    generateCassandraAsyncRepositories ++= Seq(
      RepositoryDescription(
        s"$cassandraModelPackage.Address",
        BeanIdClass(s"$cassandraModelPackage.AddressId"),
        s"$generateCassandraAsyncRepositoryPackage.AddressRepositoryGen"
      )
    ),
    parallelExecution in Test := false
  )

val generateCassandraMonixRepositoryPackage = s"$cassandraPackage.monix.repository"

lazy val cassandraMonix = projectWithCassandra("cassandra-monix", file("cassandra-monix"))
  .settings(
    generateCassandraMonixRepositories ++= Seq(
      RepositoryDescription(
        s"$cassandraModelPackage.Address",
        BeanIdClass(s"$cassandraModelPackage.AddressId"),
        s"$generateCassandraMonixRepositoryPackage.AddressRepositoryGen"
      )
    )
  )
  .dependsOn(cassandra, cassandra % "test -> test")

def projectWithCassandra(name: String, file: File): Project =
  projectWithSbtPlugin(name, file)
    .settings(
      Test / fork := true,
      libraryDependencies ++= Seq(`org.cassandraunit_cassandra-unit` % Test, `com.datastax.cassandra_cassandra-driver-extras` % Test)
    )

def projectWithSbtPlugin(name: String, file: File): Project =
  projectWithName(name, file)
    .dependsOn(common, common % "test -> test")
    .enablePlugins(QuillRepositoryPlugin)

def projectWithName(name: String, file: File): Project =
  Project(name, file)
    .settings(
      libraryDependencies ++= Seq(
        `org.scalatest_scalatest`,
        `org.scalacheck_scalacheck`,
        `org.scalatestplus_scalacheck-1-15`,
        `com.typesafe.scala-logging_scala-logging`,
        `ch.qos.logback_logback-classic`,
        `com.h2database_h2` % Test
      ),
      sources in (Compile, doc) := Seq.empty
    )
