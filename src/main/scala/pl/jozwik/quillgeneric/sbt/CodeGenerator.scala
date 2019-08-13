package pl.jozwik.quillgeneric.sbt

import java.io.File
import java.nio.file.Paths

import sbt._

import scala.io.Source

trait CodeGenerationTemplates {
  val DialectTemplate                = "__DIALECT__"
  val NamingTemplate                 = "__NAMING__"
  val PackageTemplate                = "__PACKAGE__"
  val RepositoryClassTemplate        = "__REPOSITORY_NAME__"
  val BeanTemplate                   = "__BEAN__"
  val BeanClassImport                = "__BEAN_CLASS_IMPORT__"
  val BeanIdTemplate                 = "__ID__"
  val BeanIdClassImport              = "__ID_CLASS_IMPORT__"
  val ColumnMapping                  = "__COLUMN_MAPPING__"
  val ImportContext                  = "__IMPORT_CONTEXT__"
  val TableNamePattern               = "__TABLE_NAME__"
  val RepositoryTraitImport          = "__REPOSITORY_TRAIT_IMPORT__"
  val RepositoryTraitSimpleClassName = "__REPOSITORY_TRAIT_SIMPLE_NAME__"
  val RepositoryImport               = "__REPOSITORY_IMPORT__"
  val ContextAlias                   = "__CONTEXT_ALIAS__"
}

object CodeGenerator extends CodeGenerationTemplates {
  private val Dialect = "Dialect"
  private val Naming  = "Naming"

  private val aliasName = "JdbcContextDateQuotes"

  private val macroRepository = "Repository"

  private val repositoryCompositeKey = "RepositoryCompositeKey"

  private val macroRepositoryWithGenerated = "JdbcRepositoryWithGeneratedId"

  private val macroRepositoryWithGeneratedWithGeneric = s"$macroRepositoryWithGenerated[$BeanIdTemplate, $BeanTemplate, $DialectTemplate, $NamingTemplate]"

  private val macroRepositoryWithGeneratedImport = s"import pl.jozwik.quillgeneric.quillmacro.sync.$macroRepositoryWithGenerated"

  private val template = "$template$.txt"

  private val templateWithGeneratedId = "$template_generate_id$.txt"

  private val headerFile = "$header$.txt"

  private def macroRepositoryWithGeneric(key: KeyType.Value) = {
    val repo = key match {
      case KeyType.Composite =>
        repositoryCompositeKey
      case _ =>
        macroRepository
    }
    (s"$repo[$BeanIdTemplate, $BeanTemplate]", s"import pl.jozwik.quillgeneric.quillmacro.sync.$repo")
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
    val p = packageName match {
      case Seq() =>
        ""
      case s =>
        s"""package ${s.mkString(".")}"""
    }
    val file = dir / s"$repositorySimpleClassName.scala"

    val columnMapping = toColumnMapping(mapping)
    val importCtx     = toImportContext(columnMapping)

    val (repositoryTraitSimpleClassName, repositoryImport, defaultRepositoryImport) =
      toRepositoryTraitImport(repositoryTrait, packageName, repositoryPackageName, repositoryTraitSimpleClassNameOpt, generateId, beanIdClass.keyType)

    val result = content
      .replace(RepositoryTraitSimpleClassName, repositoryTraitSimpleClassName)
      .replace(RepositoryTraitImport, repositoryImport)
      .replace(PackageTemplate, p)
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
