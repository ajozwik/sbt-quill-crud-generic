name := "sbt-quill-crud-generic"

organization := "com.github.ajozwik"

resolvers += Resolver.sonatypeRepo("releases")

ThisBuild / scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-encoding", "UTF-8",
  "-deprecation", // warning and location for usages of deprecated APIs
  "-feature", // warning and location for usages of features that should be imported explicitly
  "-unchecked", // additional warnings where generated code depends on assumptions
  "-Xlint", // recommended additional warnings
  "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver
  "-Ywarn-value-discard", // Warn when non-Unit expression results are unused
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code",
  "-language:reflectiveCalls",
  "-Ydelambdafy:method"
)

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalaVersion := "2.12.8"

val quillMacroVersion = "0.4.0"

val `com.typesafe.scala-logging_scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"

val `ch.qos.logback_logback-classic` = "ch.qos.logback" % "logback-classic" % "1.2.3"

val `org.scalatest_scalatest` = "org.scalatest" %% "scalatest" % "3.0.8" % Test

val `org.scalacheck_scalacheck` = "org.scalacheck" %% "scalacheck" % "1.14.0" % Test

val `com.github.ajozwik_macro-quill` = "com.github.ajozwik" %% "macro-quill" % quillMacroVersion

ThisBuild / libraryDependencies ++= Seq(
  `ch.qos.logback_logback-classic` % Test,
  `com.typesafe.scala-logging_scala-logging` % Test,
  `org.scalatest_scalatest`,
  `org.scalacheck_scalacheck`,
  `com.github.ajozwik_macro-quill` % Test
)

ThisScope / sbtPlugin := true

lazy val root = (project in file("."))
  .settings(
    scriptedLaunchOpts += ("-Dplugin.version=" + version.value),
    scriptedBufferLog := false
  ).enablePlugins(SbtPlugin)
