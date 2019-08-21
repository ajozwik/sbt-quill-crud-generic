package pl.jozwik.quillgeneric.sbt.generator.cassandra

import pl.jozwik.quillgeneric.sbt.generator.CodeGenerationTemplates

trait WithCassandra {
  protected def update                     = "Unit"
  protected def contextTransactionStart    = ""
  protected def contextTransactionEnd      = ""
  protected def sqlIdiomImport             = ""
  protected def genericDeclaration: String = CodeGenerationTemplates.NamingTemplate
  protected def aliasGenericDeclaration    = s"[$genericDeclaration <: NamingStrategy]"
  protected def createOrUpdate             = "create"
  protected def createOrUpdateAndRead      = "createAndRead"
}
