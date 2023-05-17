package pl.jozwik.example.sync

import pl.jozwik.example.domain.model._
import pl.jozwik.example.repository.{ PersonRepositoryGen, ProductRepositoryGen, SaleRepositoryGen }
import org.scalatest.TryValues._
import scala.util.Success

trait SaleRepositorySuite extends AbstractSyncSpec {

  private val saleRepository    = new SaleRepositoryGen(ctx, "Sale")
  private val personRepository  = new PersonRepositoryGen(ctx, "Person3")
  private val productRepository = new ProductRepositoryGen(ctx, "Product")
  "Sale Repository " should {
    "Call all operations on Sale" in {
      saleRepository.all shouldBe Success(Seq())
      val personWithoutId  = Person(PersonId.empty, "firstName", "lastName", today)
      val person           = personRepository.createAndRead(personWithoutId).success.value
      val productWithoutId = Product(ProductId.empty, "productName")
      val product          = productRepository.createAndRead(productWithoutId).success.value
      val saleId           = SaleId(product.id, person.id)
      val sale             = Sale(saleId, now)
      saleRepository.createAndRead(sale).success.value shouldBe sale
      saleRepository.update(sale).success.value shouldBe 1
      val modSale = sale.copy(saleDate = sale.saleDate.plusDays(1))
      saleRepository.createOrUpdateAndRead(modSale).success.value shouldBe modSale

      saleRepository.read(saleId).success.value shouldBe Option(modSale)
      saleRepository.delete(saleId).success.value shouldBe 1
      productRepository.delete(product.id)
      personRepository.delete(person.id)
    }
  }

}
