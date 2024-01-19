import pl.jozwik.quillgeneric.sbt._

lazy val readQuillMacroVersionSbt = sys.props.get("plugin.version") match {
  case Some(pluginVersion) =>
    pluginVersion
  case _ =>
    "1.2.5"
}

def init(): Unit = {
  sys.props.put("quill.macro.log", false.toString)
  sys.props.put("quill.binds.log", true.toString)
}

val fake: Unit = init()

lazy val common = projectWithName("common", file("common"))
  .settings(libraryDependencies ++= Seq("com.github.ajozwik" %% "repository" % readQuillMacroVersionSbt))

ThisBuild / resolvers ++= Resolver.sonatypeOssRepos("releases")

ThisBuild / resolvers ++= Resolver.sonatypeOssRepos("snapshots")

val `scalaVersion_2.13` = "2.13.12"

val `scalaVersion_2.12` = "2.12.18"

name := "quill-generic-example"

ThisBuild / scalaVersion := `scalaVersion_2.13`

ThisBuild / crossScalaVersions := Seq(`scalaVersion_2.13`, `scalaVersion_2.12`)

ThisBuild / scapegoatVersion := "2.1.3"

ThisBuild / organization := "pl.jozwik.demo"

ThisBuild / scalacOptions ++= Seq("-Dquill.macro.log=false")

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "utf8",             // Option and arguments on same line
  "-Xfatal-warnings", // New lines for each options
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:higherKinds",
  "-language:existentials",
  "-language:postfixOps",
  "-Xsource:3"
)

val scalaTestVersion = "3.2.17"

val `ch.qos.logback_logback-classic`                 = "ch.qos.logback"              % "logback-classic"         % "1.2.11"
val `com.datastax.cassandra_cassandra-driver-extras` = "com.datastax.cassandra"      % "cassandra-driver-extras" % "3.11.5"
val `com.h2database_h2`                              = "com.h2database"              % "h2"                      % "2.2.224"
val `com.typesafe.scala-logging_scala-logging`       = "com.typesafe.scala-logging" %% "scala-logging"           % "3.9.5"
val `org.cassandraunit_cassandra-unit`               = "org.cassandraunit"           % "cassandra-unit"          % "4.3.1.0"
val `org.scalacheck_scalacheck`                      = "org.scalacheck"             %% "scalacheck"              % "1.17.0"               % Test
val `org.scalatest_scalatest`                        = "org.scalatest"              %% "scalatest"               % scalaTestVersion       % Test
val `org.scalatestplus_scalacheck-1-15`              = "org.scalatestplus"          %% "scalacheck-1-17"         % s"$scalaTestVersion.0" % Test
val `org.tpolecat_doobie-h2`                         = "org.tpolecat"               %% "doobie-h2"               % "1.0.0-RC4"

val basePackage        = "pl.jozwik.example"
val domainModelPackage = s"$basePackage.domain.model"
def repositories(generatePackage: String, repositoryImplPackage: String) = {
  Seq(
    RepositoryDescription(
      s"$domainModelPackage.Address",
      BeanIdClass(s"$domainModelPackage.AddressId"),
      s"$generatePackage.AddressRepositoryGen",
      true,
      Option(s"$repositoryImplPackage.AddressRepositoryImpl[Dialect, Naming]"),
      None,
      Map("city" -> "city")
    ),
    RepositoryDescription(
      s"$domainModelPackage.Cell4d",
      BeanIdClass(s"$domainModelPackage.Cell4dId", Option(4)),
      s"$generatePackage.Cell4dRepositoryGen",
      false,
      None,
      None,
      Map("id.fk1" -> "x", "id.fk2" -> "y", "id.fk3" -> "z", "id.fk4" -> "t")
    ),
    RepositoryDescription(
      s"$domainModelPackage.Configuration",
      BeanIdClass(s"$domainModelPackage.ConfigurationId"),
      s"$generatePackage.ConfigurationRepositoryGen",
      false,
      None,
      None,
      Map("id" -> "`KEY`", "value" -> "`VALUE`")
    ),
    RepositoryDescription(
      s"$domainModelPackage.Person",
      BeanIdClass(s"$domainModelPackage.PersonId"),
      s"$generatePackage.Person3RepositoryGen",
      true,
      Option(s"$repositoryImplPackage.PersonRepositoryImpl[Dialect, Naming]"),
      None,
      Map("birthDate" -> "dob")
    ),
    RepositoryDescription(
      s"$domainModelPackage.Person",
      BeanIdClass(s"$domainModelPackage.PersonId"),
      s"$generatePackage.PersonRepositoryGen",
      true,
      Option(s"$repositoryImplPackage.PersonRepositoryImpl[Dialect, Naming]"),
      None,
      Map("birthDate" -> "dob")
    ),
    RepositoryDescription(
      s"$domainModelPackage.Product",
      BeanIdClass(s"$domainModelPackage.ProductId"),
      s"$generatePackage.ProductRepositoryGen",
      true
    ),
    RepositoryDescription(
      s"$domainModelPackage.Sale",
      BeanIdClass(s"$domainModelPackage.SaleId", Option(2)),
      s"$generatePackage.SaleRepositoryGen",
      false,
      None,
      None,
      Map("id.fk1" -> "PRODUCT_ID", "id.fk2" -> "PERSON_ID")
    )
  )
}

val doobiePackage                   = s"$basePackage.doobie"
val doobieImplPackage               = s"$doobiePackage.impl"
val doobieGenerateRepositoryPackage = s"$doobiePackage.repository"

lazy val doobie = projectWithSbtPlugin("doobie", file("doobie"))
  .settings(
    generateDoobieRepositories ++= repositories(doobieGenerateRepositoryPackage, doobieImplPackage),
    libraryDependencies ++= Seq(`org.tpolecat_doobie-h2` % Test)
  )
  .dependsOn(sync % "test->test")

val generateRepositoryPackage = s"$basePackage.repository"
val repositoryPackage         = s"$basePackage.sync.impl"

lazy val sync = projectWithSbtPlugin("sync", file("sync"))
  .settings(
    generateDescription := repositories(generateRepositoryPackage, repositoryPackage)
  )

val monixPackage                   = s"$basePackage.monix"
val monixImplPackage               = s"$monixPackage.impl"
val monixGenerateRepositoryPackage = s"$monixPackage.repository"

lazy val monix = projectWithSbtPlugin("monix", file("monix"))
  .settings(
    generateMonixRepositories ++= repositories(monixGenerateRepositoryPackage, monixImplPackage)
  )

val zioPackage                   = s"$basePackage.zio"
val zioImplPackage               = s"$zioPackage.impl"
val zioGenerateRepositoryPackage = s"$zioPackage.repository"

lazy val zio = projectWithSbtPlugin("zio", file("zio"))
  .settings(
    generateZioRepositories ++= repositories(zioGenerateRepositoryPackage, zioImplPackage)
  )
  .dependsOn(sync % "test->test")

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
    Test / parallelExecution := false
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
      libraryDependencies ++= Seq(`org.cassandraunit_cassandra-unit` % Test, `com.datastax.cassandra_cassandra-driver-extras` % Test)
    )

def projectWithSbtPlugin(name: String, file: File): Project =
  projectWithName(name, file)
    .dependsOn(common, common % "test -> test")
    .settings(Test / fork := true)
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
      Compile / doc / sources := Seq.empty
    )
