package pl.jozwik.quillgeneric.sbt

import sbt._

object DependencyHelper {

  def addImport(add: Boolean, module: String, version: String): Option[ModuleID] =
    if (add) {
      Option("com.github.ajozwik" %% module % version)
    } else {
      None
    }
}
