import sbtrelease.ReleaseStateTransformations._

ThisScope / sbtrelease.ReleasePlugin.autoImport.releasePublishArtifactsAction := PgpKeys.publishSigned.value
ThisScope / sbtrelease.ReleasePlugin.autoImport.releaseCrossBuild := true

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  releaseStepCommandAndRemaining("^test"),
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("^publish"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

ThisBuild / developers := List(
  Developer(
    id = "ajozwik",
    name = "Andrzej Jozwik",
    email = "andrzej.jozwik@gmail.com",
    url = url("https://github.com/ajozwik")
  )
)

ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) {
    Option("snapshots" at nexus + "content/repositories/snapshots")
  } else {
    Option("releases" at nexus + "service/local/staging/deploy/maven2")
  }
}

Test / publishArtifact := false

ThisBuild / licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

val organizationUrl = "https://github.com/ajozwik"

val projectUrl = s"$organizationUrl/quill-generic"

ThisBuild / scmInfo := Option(
  ScmInfo(
    url(projectUrl),
    "scm:git@github.com:ajozwik/quill-generic.git"
  )
)

ThisBuild / versionScheme := Option("strict")

ThisBuild / organizationHomepage := Option(url(organizationUrl))

ThisBuild / homepage := Option(url(projectUrl))
