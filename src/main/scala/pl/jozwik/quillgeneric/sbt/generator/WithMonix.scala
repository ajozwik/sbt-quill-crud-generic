package pl.jozwik.quillgeneric.sbt.generator

trait WithMonix {
  protected def monad: String       = "Task"
  protected def monadImport: String = s"import monix.eval.$monad"
  protected def tryStart: String    = ""
  protected def tryEnd: String      = ""
}
