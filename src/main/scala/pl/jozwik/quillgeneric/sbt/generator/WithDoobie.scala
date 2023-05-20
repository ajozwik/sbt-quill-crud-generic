package pl.jozwik.quillgeneric.sbt.generator

import pl.jozwik.quillgeneric.sbt.generator.CodeGenerationTemplates.*

trait WithDoobie extends WithNoTask {
  protected def monad: String = "ConnectionIO"
  protected def monadImport: String =
    s"""import doobie.$monad
       |import cats.Monad
       |""".stripMargin
  protected def tryStart: String = ""
  protected def tryEnd: String   = ""

  protected def implicitContext: String = ""

  protected def implicitBaseVariable = s"(implicit protected val monad: Monad[$Monad])"

}
