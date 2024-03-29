package pl.jozwik.quillgeneric.sbt.generator

import java.io.File
import java.nio.file.Paths
import pl.jozwik.quillgeneric.sbt.RepositoryDescription
import sbt._

import scala.io.{ Codec, Source }

abstract class AbstractCodeGenerator extends Generator with CodeGenerationTemplates {
  private val Connection                   = "Connection"
  private val Dialect                      = "Dialect"
  private val Naming                       = "Naming"
  private val template                     = "$template$.txt"
  private val templateWithGeneratedId      = "$template_generate_id$.txt"
  protected def importMacroTraitRepository = s"import $genericPackage.$domainRepository.$ContextAlias"

  private def repositoryWithGeneratedWithGeneric =
    s"$domainRepositoryWithGenerated[$BeanIdTemplate, $BeanTemplate, $DialectTemplate, $NamingTemplate]"

  private def repositoryWithGeneratedImport = s"import $genericPackage.$domainRepositoryWithGenerated"

  private val headerFile = "$header$.txt"

  private val header: String = readTemplate(headerFile)

  private def repositoryWithGeneric =
    (s"$domainRepository[$BeanIdTemplate, $BeanTemplate, $genericDeclaration]()", s"import $genericPackage.$domainRepository")

  private def chooseTemplate(generateId: Boolean): String =
    if (generateId) {
      templateWithGeneratedId
    } else {
      template
    }

  def generate(rootPath: File)(description: RepositoryDescription): (File, String) = {
    import description._
    val templateFile = chooseTemplate(generateId)
    val content      = readTemplate(templateFile)
    val path         = Paths.get(rootPath.getAbsolutePath, packageName*)
    val dir          = path.toFile
    mkdirs(dir)
    val pName         = toPackageName(packageName)
    val file          = dir / s"$repositorySimpleClassName.scala"
    val columnMapping = toColumnMapping(mapping)
    val importCtx     = toImportContext
    val (repositoryTraitSimpleClassName, repositoryImport, defaultRepositoryImport) =
      toRepositoryTraitImport(repositoryTrait, packageName, repositoryPackageName, repositoryTraitSimpleClassNameOpt, generateId)
    val genericContent = toGenericContent(content)
    val findByKey      = toFindByKey(description.beanIdClass.keyLength)
    val result = genericContent
      .replace(RepositoryTraitSimpleClassName, repositoryTraitSimpleClassName)
      .replace(RepositoryTraitImport, repositoryImport)
      .replace(PackageTemplate, pName)
      .replace(RepositoryClassTemplate, repositorySimpleClassName)
      .replace(BeanTemplate, beanSimpleClassName)
      .replace(BeanClassImport, createImport(packageName, beanPackageName, beanClass))
      .replace(BeanIdTemplate, beanIdSimpleClassName)
      .replace(BeanIdClassImport, createImport(packageName, beanIdPackageName, beanIdClass.name))
      .replace(ColumnMapping, columnMapping)
      .replace(ImportContext, importCtx)
      .replace(TableNamePattern, toTableName)
      .replace(RepositoryImport, defaultRepositoryImport)
      .replace(DialectTemplate, Dialect)
      .replace(ConnectionTemplate, Connection)
      .replace(NamingTemplate, Naming)
      .replace(ContextAlias, aliasName)
      .replace(UpdateResult, updateResult)
      .replace(Monad, monad)
      .replace(ContextTransactionStart, contextTransactionStart)
      .replace(ContextTransactionEnd, contextTransactionEnd)
      .replace(MonadImport, monadImport)
      .replace(FindByKey, findByKey)
      .replace(TryStart, tryStart)
      .replace(TryEnd, tryEnd)
      .replace(SqlIdiomImport, sqlIdiomImport)
      .replace(CreateOrUpdate, createOrUpdate)
      .replace(CreateOrUpdateAndRead, createOrUpdateAndRead)
      .replace(ExecutionContext, executionContext)
      .replace(ExecutionContextImport, executionContextImport)
      .replace(ImplicitContext, implicitContext)
      .replace(ImplicitParameters, implicitParameters)
      .replace(ImplicitTransactionParameters, implicitTransactionParameters)
      .replace(ConnectionImport, connectionImport)
    (file, s"$header\n$result")
  }

  private def toGenericContent(content: String) =
    content
      .replace(Update,update)
      .replace(ImplicitBaseVariable, implicitBaseVariable)
      .replace(CreateOrUpdate, createOrUpdate)
      .replace(ToTask, toTask)
      .replace(ToTaskEnd, toTaskEnd)
      .replace(AliasGenericDeclaration, aliasGenericDeclaration)
      .replace(GenericDeclaration, genericDeclaration)
      .replace(RepositoryMacroTraitImport, importMacroTraitRepository)

  private def toPackageName(packageName: Seq[String]): String =
    packageName match {
      case Seq() =>
        ""
      case s =>
        s"""package ${s.mkString(".")}"""
    }

  private def toRepositoryTraitImport(
      repositoryTrait: Option[String],
      packageName: Seq[String],
      repositoryPackageName: Seq[String],
      repositoryTraitSimpleClassNameOpt: String,
      generateId: Boolean
  ) =
    if (repositoryTraitSimpleClassNameOpt.isEmpty) {
      val (repository, repositoryImport) = if (generateId) {
        (repositoryWithGeneratedWithGeneric, repositoryWithGeneratedImport)
      } else {
        repositoryWithGeneric
      }
      (s"$repository", "", repositoryImport)

    } else {
      val clazzName = repositoryTrait.getOrElse("")
      val withoutGeneric = clazzName.indexOf('[') match {
        case -1 =>
          clazzName
        case index =>
          clazzName.substring(0, index)
      }
      val imp = createImport(packageName, repositoryPackageName, withoutGeneric)
      (s"$repositoryTraitSimpleClassNameOpt", imp, "")
    }

  private def createImport(packageNameSeq: Seq[String], packageNameBean: Seq[String], className: String) =
    if (packageNameSeq.sameElements(packageNameBean)) { "" }
    else { s"import $className" }

  private def toColumnMapping(mapping: Map[String, String]) = {
    val columnMapping = {
      val map = mapping.map { case (k, v) =>
        s"""alias(_.$k, "$v")"""
      }
      if (map.isEmpty) {
        ""
      } else {
        s""", ${map.mkString(", ")}"""
      }
    }
    columnMapping
  }

  private val toImportContext =
    "import context.*"

  private def readTemplate(templateResource: String): String = {
    val input = Option(getClass.getClassLoader.getResourceAsStream(templateResource))
      .getOrElse(getClass.getClassLoader.getResourceAsStream(s"/$templateResource"))
    try {
      Source.fromInputStream(input)(Codec.UTF8).mkString
    } finally {
      input.close()
    }
  }

  private def mkdirs(dir: File): Unit = if (dir.isDirectory) { () }
  else { if (!dir.mkdirs()) { sys.error(s"${dir.getAbsolutePath} can not be created") } }

  private def toFindByKey(keyLength: Option[Byte]) = {
    keyLength match {
      case Some(l) if l > 1 =>
        createFindByKey(l)
      case _ =>
        "filter(_.id == lift(id))"
    }
  }

  private def createFindByKey(l: Byte) =
    (1 to l)
      .map { i =>
        findBy(i)
      }
      .mkString(".")

  private def findBy(key: Int) = s"filter(_.id.fk$key == lift(id.fk$key))"
}
