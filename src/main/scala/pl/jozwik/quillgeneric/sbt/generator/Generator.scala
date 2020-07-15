package pl.jozwik.quillgeneric.sbt.generator

import java.io.File

import pl.jozwik.quillgeneric.sbt.RepositoryDescription

trait Generator {
  protected def aliasName: String
  protected def monad: String
  protected def monadImport: String
  protected def update: String
  protected def macroRepository: String
  protected def repositoryCompositeKey: String
  protected def macroRepositoryWithGenerated: String
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
  protected def executionContext: String       = ""
  protected def executionContextImport: String = ""
  protected def implicitParameters: String     = "(dSchema)"
  protected def implicitTransactionParameters  = ""
  protected def connectionImport               = ""

  def generate(rootPath: File)(description: RepositoryDescription): (File, String)
}
