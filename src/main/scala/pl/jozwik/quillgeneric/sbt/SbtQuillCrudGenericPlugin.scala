package pl.jozwik.quillgeneric.sbt

import sbt._
import sbt.plugins.JvmPlugin
import Keys._

object SbtQuillCrudGenericPlugin extends AutoPlugin {

  final case class RepositoryDescription(beanClass: String, beanIdClass: String, repositoryClassName: String)

  override def trigger = allRequirements

  override def requires = JvmPlugin

  object autoImport {
    val generateRepositories = taskKey[Seq[File]]("Generate scala repositories from descriptions")
    val repositories = settingKey[Seq[RepositoryDescription]]("The repository descriptions")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    repositories := Seq.empty,
    sourceGenerators in Compile += Def.task {
      val file = (sourceManaged in Compile).value / "demo" / "Test.scala"
      IO.write(file, """object Test extends App { println("Hi") }""")
      Seq(file)
    }.taskValue)

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq()
}
