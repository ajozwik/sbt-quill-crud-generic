package pl.jozwik.quillgeneric.sbt

import java.io.File
import java.nio.file.Paths

import sbt._

import scala.io.Source

trait WithJdbc {
  protected def update                  = "Long"
  protected def contextTransactionStart = "     context.transaction {"
  protected def contextTransactionEnd   = "         }"
}

object SyncCodeGenerator extends AbstractCodeGenerator with WithJdbc {
  protected def genericPackage               = "pl.jozwik.quillgeneric.quillmacro.sync"
  protected def aliasName                    = "JdbcContextDateQuotes"
  protected def macroRepository: String      = "JdbcRepository"
  protected def repositoryCompositeKey       = "JdbcRepositoryCompositeKey"
  protected def macroRepositoryWithGenerated = "JdbcRepositoryWithGeneratedId"
  protected def template                     = "$template$.txt"
  protected def templateWithGeneratedId      = "$template_generate_id$.txt"
  protected def monad: String                = "Try"
  protected def monadImport: String          = s"import util.$monad"
  protected def tryStart: String             = "      Try {"
  protected def tryEnd: String               = "      }"
  protected def importMacroTraitRepository   = s"import pl.jozwik.quillgeneric.quillmacro.sync.JdbcRepository.$ContextAlias"
}

object MonixJdbcCodeGenerator extends AbstractCodeGenerator with WithJdbc {
  protected def genericPackage               = "pl.jozwik.quillgeneric.quillmacro.monix.jdbc"
  protected def aliasName                    = "MonixJdbcContextDateQuotes"
  protected def macroRepository: String      = "MonixJdbcRepository"
  protected def repositoryCompositeKey       = "MonixJdbcRepositoryCompositeKey"
  protected def macroRepositoryWithGenerated = "MonixJdbcRepositoryWithGeneratedId"
  protected def template                     = "$monix_template$.txt"
  protected def templateWithGeneratedId      = "$monix_template_generated_id$.txt"
  protected def monad: String                = "Task"
  protected def monadImport: String          = s"import monix.eval.$monad"
  protected def tryStart: String             = ""
  protected def tryEnd: String               = ""
  protected def importMacroTraitRepository   = s"import pl.jozwik.quillgeneric.quillmacro.monix.jdbc.MonixJdbcRepository.$ContextAlias"
}

trait Generator {
  protected def aliasName: String
  protected def monad: String
  protected def monadImport: String
  protected def update: String
  protected def macroRepository: String
  protected def repositoryCompositeKey: String
  protected def macroRepositoryWithGenerated: String
  protected def genericPackage: String
  protected def template: String
  protected def templateWithGeneratedId: String
  protected def contextTransactionStart: String
  protected def contextTransactionEnd: String
  protected def tryStart: String
  protected def tryEnd: String
  protected def importMacroTraitRepository: String

  def generate(rootPath: File)(description: RepositoryDescription): (File, String)
}

abstract class AbstractCodeGenerator extends Generator with CodeGenerationTemplates {
  private val Dialect = "Dialect"
  private val Naming  = "Naming"

  private val macroRepositoryWithGeneratedWithGeneric = s"$macroRepositoryWithGenerated[$BeanIdTemplate, $BeanTemplate, $DialectTemplate, $NamingTemplate]"

  private val macroRepositoryWithGeneratedImport = s"import $genericPackage.$macroRepositoryWithGenerated"

  private val headerFile = "$header$.txt"

  private def macroRepositoryWithGeneric(key: KeyType.Value) = {
    val repo = key match {
      case KeyType.Composite =>
        repositoryCompositeKey
      case _ =>
        macroRepository
    }
    (s"$repo[$BeanIdTemplate, $BeanTemplate, $DialectTemplate, $NamingTemplate]", s"import $genericPackage.$repo")
  }

  def generate(rootPath: File)(description: RepositoryDescription): (File, String) = {
    import description._
    val templateFile = if (generateId) {
      templateWithGeneratedId
    } else {
      template
    }
    val header  = readTemplate(headerFile)
    val content = readTemplate(templateFile)
    val path    = Paths.get(rootPath.getAbsolutePath, packageName: _*)
    val dir     = path.toFile
    dir.mkdirs()
    val pName = packageName match {
      case Seq() =>
        ""
      case s =>
        s"""package ${s.mkString(".")}"""
    }
    val file          = dir / s"$repositorySimpleClassName.scala"
    val columnMapping = toColumnMapping(mapping)
    val importCtx     = toImportContext(columnMapping)
    val (repositoryTraitSimpleClassName, repositoryImport, defaultRepositoryImport) =
      toRepositoryTraitImport(repositoryTrait, packageName, repositoryPackageName, repositoryTraitSimpleClassNameOpt, generateId, beanIdClass.keyType)

    val result = content
      .replace(RepositoryMacroTraitImport, importMacroTraitRepository)
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
      .replace(NamingTemplate, Naming)
      .replace(ContextAlias, aliasName)
      .replace(Update, update)
      .replace(Monad, monad)
      .replace(ContextTransactionStart, contextTransactionStart)
      .replace(ContextTransactionEnd, contextTransactionEnd)
      .replace(MonadImport, monadImport)
      .replace(TryStart, tryStart)
      .replace(TryEnd, tryEnd)

    (file, s"$header\n$result")
  }

  private def toRepositoryTraitImport(
      repositoryTrait: Option[String],
      packageName: Seq[String],
      repositoryPackageName: Seq[String],
      repositoryTraitSimpleClassNameOpt: String,
      generateId: Boolean,
      keyType: KeyType.Value
  ) =
    if (repositoryTraitSimpleClassNameOpt.isEmpty) {
      val (repository, repositoryImport) = if (generateId) {
        (macroRepositoryWithGeneratedWithGeneric, macroRepositoryWithGeneratedImport)
      } else {
        macroRepositoryWithGeneric(keyType)
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
    if (packageNameSeq == packageNameBean) "" else s"import $className"

  private def toColumnMapping(mapping: Map[String, String]) = {
    val columnMapping = {
      val map = mapping.map {
        case (k, v) =>
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

  private def toImportContext(toColumnMapping: String) = {
    val importCtx = if (toColumnMapping.isEmpty) {
      ""
    } else {
      "import context._"
    }
    importCtx
  }

  private def readTemplate(templateResource: String): String = {
    val input = Option(getClass.getClassLoader.getResourceAsStream(templateResource))
      .getOrElse(getClass.getClassLoader.getResourceAsStream(s"/$templateResource"))
    try {
      Source.fromInputStream(input).mkString
    } finally {
      input.close()
    }
  }
}
