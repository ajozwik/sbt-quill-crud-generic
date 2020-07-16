package pl.jozwik.example.sync

import pl.jozwik.example.domain.model._
import pl.jozwik.example.repository.{ PersonRepositoryGen, ProductRepositoryGen, SaleRepositoryGen }
import org.scalatest.TryValues._
import scala.util.Success

trait SaleRepositorySuite extends AbstractSyncSpec {

  private val repository        = new SaleRepositoryGen(ctx, "Sale")
  private val personRepository  = new PersonRepositoryGen(ctx, "Person2")
  private val productRepository = new ProductRepositoryGen(ctx, "Product")
  "Sale Repository " should {
    "Call all operations on Sale" in {
      repository.all shouldBe Success(Seq())
      val personWithoutId  = Person(PersonId.empty, "firstName", "lastName", today)
      val person           = personRepository.createAndRead(personWithoutId).success.value
      val productWithoutId = Product(ProductId.empty, "productName")
      val product          = productRepository.createAndRead(productWithoutId).success.value
      val saleId           = SaleId(product.id, person.id)
      val sale             = Sale(saleId, now)
      repository.createAndRead(sale).success.value shouldBe sale

      repository.createOrUpdateAndRead(sale) shouldBe Symbol("success")

      repository.read(saleId).success.value shouldBe Option(sale)
      repository.delete(saleId) shouldBe Symbol("success")
      productRepository.delete(product.id)
      personRepository.delete(person.id)
    }
  }

}
