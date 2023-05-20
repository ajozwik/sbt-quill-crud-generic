package pl.jozwik.quillgeneric.sbt

import java.io.File
import pl.jozwik.quillgeneric.sbt.generator.Generator
import pl.jozwik.quillgeneric.sbt.generator.cassandra.{ CassandraAsyncCodeGenerator, CassandraMonixCodeGenerator, CassandraSyncCodeGenerator }
import pl.jozwik.quillgeneric.sbt.generator.jdbc.{ AsyncCodeGenerator, DoobieCodeGenerator, MonixJdbcCodeGenerator, SyncCodeGenerator, ZioJdbcCodeGenerator }

class SyncGeneratorCodeSpec extends AbstractCodeGeneratorSpec(SyncCodeGenerator)

class AsyncGeneratorCodeSpec extends AbstractCodeGeneratorSpec(AsyncCodeGenerator)

class DoobieGeneratorCodeSpec extends AbstractCodeGeneratorSpec(DoobieCodeGenerator)

class MonixGeneratorCodeSpec extends AbstractCodeGeneratorSpec(MonixJdbcCodeGenerator)

class ZioGeneratorCodeSpec extends AbstractCodeGeneratorSpec(ZioJdbcCodeGenerator)

class CassandraMonixGeneratorCodeSpec extends AbstractCodeGeneratorSpec(CassandraMonixCodeGenerator, "[Naming]", false)

class CassandraSyncGeneratorCodeSpec extends AbstractCodeGeneratorSpec(CassandraSyncCodeGenerator, "[Naming]", false)

class CassandraAsyncGeneratorCodeSpec extends AbstractCodeGeneratorSpec(CassandraAsyncCodeGenerator, "[Naming]", false)

abstract class AbstractCodeGeneratorSpec(generator: Generator, generic: String = "[Dialect, Naming]", generatedId: Boolean = true) extends AbstractSpec {

  private val baseTempPath = System.getProperty("java.io.tmpdir")

  "Generator " should {
    "Generate code for Person" in {
      val description                   = RepositoryDescription("Person", BeanIdClass("PersonId"), "PersonRepository", generatedId)
      val (file: File, content: String) = generateAndLog(description)
      file.exists() shouldBe false
      content should include(description.beanSimpleClassName)
      content should include(description.beanIdSimpleClassName)
      content should include(description.repositorySimpleClassName)
      content should include(description.toTableName)
    }
    "Custom mapping " in {
      val dob = "dob"
      val description = RepositoryDescription(
        "pl.jozwik.model.Person",
        BeanIdClass("pl.jozwik.model.PersonId"),
        "pl.jozwik.repository.PersonRepository",
        generatedId,
        Option(s"pl.jozwik.quillgeneric.sbt.MyPersonRepository$generic"),
        None,
        Map("birthDate" -> dob)
      )
      val (file: File, content: String) = generateAndLog(description)
      file.exists() shouldBe false
      content should include(description.beanSimpleClassName)
      content should include(description.beanIdSimpleClassName)
      content should include(description.repositorySimpleClassName)
      content should include(description.toTableName)
      content should include(dob)
    }

    "Generate code for Configuration" in {
      val description                   = RepositoryDescription("Configuration", BeanIdClass("ConfigurationId"), "PersonRepository")
      val (file: File, content: String) = generateAndLog(description)
      file.exists() shouldBe false
      content should include(description.beanSimpleClassName)
      content should include(description.beanIdSimpleClassName)
      content should include(description.repositorySimpleClassName)
      content should include(description.toTableName)
    }

    "Generate code for Sale" in {
      val description                   = RepositoryDescription("Sale", BeanIdClass("SaleId", Option(2)), "SaleRepository")
      val (file: File, content: String) = generateAndLog(description)
      file.exists() shouldBe false
      content should include(description.beanSimpleClassName)
      content should include(description.beanIdSimpleClassName)
      content should include(description.repositorySimpleClassName)
      content should include(description.toTableName)
    }
  }

  private def generateAndLog(description: RepositoryDescription) = {
    val (file, content) = generator.generate(new File(baseTempPath))(description)
    logger.debug(s"$file")
    logger.debug(s"\n$content")
    (file, content)
  }
}
