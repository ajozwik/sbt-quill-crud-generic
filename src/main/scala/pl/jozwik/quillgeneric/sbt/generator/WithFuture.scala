package pl.jozwik.quillgeneric.sbt.generator

trait WithFuture {
  protected def monad: String = "Future"
  protected def monadImport: String =
    s"""import concurrent.$monad
       |import cats.Monad""".stripMargin
  protected def tryStart: String = ""
  protected def tryEnd: String   = ""

  protected def implicitContext = "implicit f =>"

  protected def implicitBaseVariable = "(implicit protected val ec: ExecutionContext, protected val monad:Monad[Future])"
}
