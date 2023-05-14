package pl.jozwik.example.domain.model

import java.time.LocalDateTime

import pl.jozwik.quillgeneric.repository.{ CompositeKey2, WithId }

final case class SaleId(fk1: ProductId, fk2: PersonId) extends CompositeKey2[ProductId, PersonId] {
  def productId: ProductId = fk1

  def personId: PersonId = fk2
}

final case class Sale(id: SaleId, saleDate: LocalDateTime) extends WithId[SaleId]
