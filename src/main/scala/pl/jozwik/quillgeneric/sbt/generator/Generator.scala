package pl.jozwik.quillgeneric.sbt.generator

import pl.jozwik.quillgeneric.sbt.RepositoryDescription

import java.io.File

trait Generator {
  protected def aliasName: String
  protected def monad: String
  protected def monadImport: String
  protected def update: String
  protected def domainRepository: String
  protected def domainRepositoryWithGenerated: String
  protected def genericPackage: String
  protected def contextTransactionStart: String
  protected def contextTransactionEnd: String
  protected def tryStart: String
  protected def tryEnd: String
  protected def importMacroTraitRepository: String
  protected def sqlIdiomImport: String
  protected def genericDeclaration: String
  protected def aliasGenericDeclaration: String
  protected def createOrUpdate: String
  protected def createOrUpdateAndRead: String
  protected def executionContext: String              = ""
  protected def executionContextImport: String        = ""
  protected def implicitParameters: String            = "(dSchema)"
  protected def implicitTransactionParameters: String = ""
  protected def connectionImport: String              = ""
  protected def implicitContext: String

  protected def implicitBaseVariable: String

  def generate(rootPath: File)(description: RepositoryDescription): (File, String)
}
