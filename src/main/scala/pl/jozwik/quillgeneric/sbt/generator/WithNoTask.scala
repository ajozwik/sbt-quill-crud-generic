package pl.jozwik.quillgeneric.sbt.generator

trait WithNoTask {
  protected def toTask: String = ""

  protected def toTaskEnd: String = ""
}
