package pl.jozwik.quillgeneric.sbt

import sbt._

object DependencyHelper {

  def addImport(add: Boolean, module: String, version: String): Seq[ModuleID] =
    if (add) {
      Seq("com.github.ajozwik" %% module % version)
    } else {
      Seq.empty[ModuleID]
    }
}
