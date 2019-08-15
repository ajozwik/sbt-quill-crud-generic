package pl.jozwik.quillgeneric.sbt

import java.io.File

class SyncGeneratorCodeSpec extends AbstractCodeGeneratorSpec(SyncCodeGenerator)

class MonixGeneratorCodeSpec extends AbstractCodeGeneratorSpec(MonixCodeGenerator)

abstract class AbstractCodeGeneratorSpec(generator: Generator) extends AbstractSpec {

  private val baseTempPath = System.getProperty("java.io.tmpdir")

  "Generator " should {
      "Generate code for Person" in {
        val description                   = RepositoryDescription("Person", BeanIdClass("PersonId"), "PersonRepository", true)
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
          true,
          Option("pl.jozwik.quillgeneric.sbt.MyPersonRepository[Dialect, Naming]"),
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
        val description                   = RepositoryDescription("Sale", BeanIdClass("SaleId", KeyType.Composite), "SaleRepository")
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
