resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.4.6")

addSbtPlugin("com.github.sbt" % "sbt-release" % "1.1.0")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.18")

addSbtPlugin("com.github.sbt" % "sbt-pgp" % "2.2.1")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "2.0.6")

addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.3.7")

addSbtPlugin("org.wartremover" % "sbt-wartremover" % "3.1.0")
