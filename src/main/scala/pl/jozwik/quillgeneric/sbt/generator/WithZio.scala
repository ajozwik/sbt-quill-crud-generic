package pl.jozwik.quillgeneric.sbt.generator

import pl.jozwik.quillgeneric.sbt.generator.CodeGenerationTemplates.*

trait WithZio {
  protected def monad: String = "Task"
  protected def monadImport: String =
    s"""import zio.$monad
       |import cats.Monad
       |""".stripMargin
  protected def tryStart: String = ""
  protected def tryEnd: String   = ""

  protected def implicitContext: String = ""

  protected def implicitBaseVariable = s"(implicit protected val monad: Monad[$Monad])"

  protected val toTask: String =
    """
      |      toTask {""".stripMargin

  protected val toTaskEnd: String =
    """ }
      |    """.stripMargin
}
