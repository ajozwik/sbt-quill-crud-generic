package pl.jozwik.quillgeneric.sbt

import java.io.File

class CodeGeneratorSpec extends AbstractSpec {

  private val baseTempPath = System.getProperty("java.io.tmpdir")

  "Generator " should {
    "Generate code " in {
      val description = RepositoryDescription("Person", "PersonId", "PersonRepository")
      val (file: File, content: String) = generateAndLog(description)
      file.exists() shouldBe false
      content should include(description.beanClass)
      content should include(description.beanIdClass)
      content should include(description.repositoryClassName)
      content should include(description.toTableName)

      generateAndLog(description.copy(mapping = Map("birthDate" -> "dob")))
    }
  }

  private def generateAndLog(description: RepositoryDescription) = {
    val (file, content) = CodeGenerator.generate(new File(baseTempPath))(description)
    logger.debug(s"$file")
    logger.debug(s"\n$content")
    (file, content)
  }
}
