package pl.jozwik.quillgeneric.sbt.generator
import CodeGenerationTemplates.*
trait WithMonix {
  protected def monad: String = "Task"
  protected def monadImport: String =
    s"""import monix.eval.$monad
       |import cats.Monad""".stripMargin
  protected def tryStart: String = ""
  protected def tryEnd: String   = ""

  protected def implicitContext: String = ""

  protected def implicitBaseVariable = s"(implicit protected val monad: Monad[$Monad])"
}
