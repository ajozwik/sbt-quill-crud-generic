val quillMacroVersion = sys.props.get("plugin.version") match {
  case Some(pluginVersion) =>
    pluginVersion
  case _ =>
    sys.error("""|The system property 'plugin.version' is not defined.
                 |Specify this property using the scriptedLaunchOpts -D...""".stripMargin)
}

ThisBuild / resolvers ++= Resolver.sonatypeOssRepos("releases")

ThisBuild / resolvers ++= Resolver.sonatypeOssRepos("snapshots")

addSbtPlugin("org.scalameta" % "sbt-scalafmt" % "2.5.0")

addSbtPlugin("com.github.ajozwik" % "sbt-quill-crud-generic" % quillMacroVersion)

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.2.0")

ThisBuild / libraryDependencySchemes ++= Seq(
  "org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always
)
