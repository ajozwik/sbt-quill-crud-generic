package pl.jozwik.quillgeneric.sbt.generator

import pl.jozwik.quillgeneric.sbt.generator.CodeGenerationTemplates.Monad

trait WithTry {
  protected def monad: String = "Try"
  protected def monadImport: String =
    s"""import util.$monad
       |import cats.Monad""".stripMargin
  protected def tryStart: String = "Try {"
  protected def tryEnd: String   = "}"

  protected def implicitContext: String = ""

  protected def implicitBaseVariable = s"(implicit protected val monad: Monad[$Monad])"
}
