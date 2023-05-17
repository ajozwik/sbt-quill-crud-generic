package pl.jozwik.example.zio

import pl.jozwik.example.domain.model.*
import pl.jozwik.example.zio.repository.{ PersonRepositoryGen, ProductRepositoryGen, SaleRepositoryGen }
import zio.interop.catz.*
trait SaleRepositorySuite extends AbstractZioSpec {

  private val saleRepository    = new SaleRepositoryGen(ctx, "Sale")
  private val personRepository  = new PersonRepositoryGen(ctx, "Person3")
  private val productRepository = new ProductRepositoryGen(ctx, "Product")
  "Sale Repository " should {
    "Call all operations on Sale" in {
      saleRepository.all.runSyncUnsafe() shouldBe Seq()
      val personWithoutId  = Person(PersonId.empty, "firstName", "lastName", today)
      val person           = personRepository.createAndRead(personWithoutId).runSyncUnsafe()
      val productWithoutId = Product(ProductId.empty, "productName")
      val product          = productRepository.createAndRead(productWithoutId).runSyncUnsafe()
      val saleId           = SaleId(product.id, person.id)
      val sale             = Sale(saleId, now)
      saleRepository.createAndRead(sale).runSyncUnsafe() shouldBe sale

      saleRepository.createOrUpdateAndRead(sale).runSyncUnsafe() shouldBe sale

      saleRepository.read(saleId).runSyncUnsafe() shouldBe Option(sale)
      val modSale = sale.copy(saleDate = sale.saleDate.plusDays(1))
      saleRepository.createOrUpdateAndRead(modSale).runSyncUnsafe() shouldBe modSale
      saleRepository.delete(saleId).runSyncUnsafe() shouldBe 1
      productRepository.delete(product.id).runSyncUnsafe() shouldBe 1
      personRepository.delete(person.id).runSyncUnsafe() shouldBe 1
    }
  }

}
