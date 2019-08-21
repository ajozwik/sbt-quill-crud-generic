package pl.jozwik.quillgeneric.sbt.generator

trait WithFuture {
  protected def monad: String       = "Future"
  protected def monadImport: String = s"import concurrent.$monad"
  protected def tryStart: String    = ""
  protected def tryEnd: String      = ""
}
