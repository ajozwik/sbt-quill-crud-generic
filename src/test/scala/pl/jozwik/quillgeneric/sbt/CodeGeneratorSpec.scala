package pl.jozwik.quillgeneric.sbt

import java.io.File

class CodeGeneratorSpec extends AbstractSpec {

  private val baseTempPath = System.getProperty("java.io.tmpdir")

  "Generator " should {
    "Generate code " in {
      val description = RepositoryDescription("Person", "PersonId", "PersonRepository")
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
        "pl.jozwik.model.PersonId",
        "pl.jozwik.repository.PersonRepository",
        Option("pl.jozwik.quillgeneric.sbt.MyPersonRepository"),
        None,
        Map("birthDate" -> dob))
      val (file: File, content: String) = generateAndLog(description)
      file.exists() shouldBe false
      content should include(description.beanSimpleClassName)
      content should include(description.beanIdSimpleClassName)
      content should include(description.repositorySimpleClassName)
      content should include(description.toTableName)
      content should include(dob)
    }
  }

  private def generateAndLog(description: RepositoryDescription) = {
    val (file, content) = CodeGenerator.generate(new File(baseTempPath))(description)
    logger.debug(s"$file")
    logger.debug(s"\n$content")
    (file, content)
  }
}
