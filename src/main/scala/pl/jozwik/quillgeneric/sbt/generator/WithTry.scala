package pl.jozwik.quillgeneric.sbt.generator

trait WithTry {
  protected def monad: String       = "Try"
  protected def monadImport: String = s"import util.$monad"
  protected def tryStart: String    = "Try {"
  protected def tryEnd: String      = "}"
}
