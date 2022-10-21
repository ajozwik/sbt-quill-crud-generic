name := "sbt-quill-crud-generic"

organization := "com.github.ajozwik"

ThisBuild / resolvers ++= Resolver.sonatypeOssRepos("releases")

ThisBuild / resolvers ++= Resolver.sonatypeOssRepos("snapshots")

val targetJdk = "1.8"

ThisBuild / scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-deprecation",         // warning and location for usages of deprecated APIs
  "-feature",             // warning and location for usages of features that should be imported explicitly
  "-unchecked",           // additional warnings where generated code depends on assumptions
  "-Xlint",               // recommended additional warnings
  "-Ywarn-adapted-args",  // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code",
  "-language:reflectiveCalls",
  "-Ydelambdafy:method",
  s"-target:jvm-$targetJdk"
)

ThisBuild / javacOptions ++= Seq("-Xlint:deprecation", "-Xdiags:verbose", "-source", targetJdk, "-target", targetJdk)

ThisBuild / scalaVersion := "2.12.17"

val quillMacroVersion = "1.0.1"

val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"

val `ch.qos.logback_logback-classic` = "ch.qos.logback" % "logback-classic" % "1.4.4"

val `org.scalatest_scalatest` = "org.scalatest" %% "scalatest" % "3.2.14" % Test

val `org.scalacheck_scalacheck` = "org.scalacheck" %% "scalacheck" % "1.17.0" % Test

val `com.github.ajozwik_macro-quill` = "com.github.ajozwik" %% "quill-jdbc-macro" % quillMacroVersion

ThisBuild / libraryDependencies ++= Seq(
  `ch.qos.logback_logback-classic`           % Test,
  `com.typesafe.scala-logging_scala-logging` % Test,
  `org.scalatest_scalatest`,
  `org.scalacheck_scalacheck`,
  `com.github.ajozwik_macro-quill` % Test
)

ThisScope / sbtPlugin := true

lazy val root = (project in file("."))
  .settings(
    scriptedLaunchOpts += ("-Dplugin.version=" + version.value),
    scriptedBufferLog := false,
    Compile / compile / wartremoverWarnings ++= Warts.allBut(Wart.ImplicitParameter, Wart.DefaultArguments, Wart.Enumeration)
  )
  .enablePlugins(SbtPlugin)
