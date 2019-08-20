import pl.jozwik.quillgeneric.sbt._

val `scalaVersion_2.12` = "2.12.9"

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

val `org.scalatest_scalatest` = "org.scalatest" %% "scalatest" % "3.0.8" % "test"

val `org.scalacheck_scalacheck` = "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"

val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

val `ch.qos.logback_logback-classic` = "ch.qos.logback" % "logback-classic" % "1.2.3"

val `com.h2database_h2` = "com.h2database" % "h2" % "1.4.199"

val `org.cassandraunit_cassandra-unit` = "org.cassandraunit" % "cassandra-unit" % "3.11.2.0"

val `com.datastax.cassandra_cassandra-driver-extras` = "com.datastax.cassandra" % "cassandra-driver-extras" % "3.7.2"

val basePackage        = "pl.jozwik.example"
val domainModelPackage = s"$basePackage.domain.model"

lazy val readQuillMacroVersionSbt = sys.props.get("plugin.version") match {
  case Some(pluginVersion) =>
    pluginVersion
  case _ =>
    sys.error("""|The system property 'plugin.version' is not defined.
                 |Specify this property using the scriptedLaunchOpts -D.""".stripMargin)
}

lazy val common = projectWithName("common", file("common"))
  .settings(libraryDependencies ++= Seq("com.github.ajozwik" %% "macro-quill" % readQuillMacroVersionSbt))

val cassandraPackage                   = s"$basePackage.cassandra"
val cassandraModelPackage              = s"$cassandraPackage.model"
val generateCassandraRepositoryPackage = s"$cassandraPackage.sync.repository"

lazy val cassandra = projectWithCassandra("cassandra", file("cassandra"))
  .settings(
    generateCassandraSyncRepositories ++= Seq(
          RepositoryDescription(
            s"$cassandraModelPackage.Address",
            BeanIdClass(s"$cassandraModelPackage.AddressId"),
            s"$generateCassandraRepositoryPackage.AddressRepositoryGen"
          )
        )
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
            `com.typesafe.scala-logging_scala-logging`,
            `ch.qos.logback_logback-classic`,
            `com.h2database_h2` % Test
          )
    )
