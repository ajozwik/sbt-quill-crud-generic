package pl.jozwik.quillgeneric.sbt.model

import java.time.LocalDateTime
import pl.jozwik.quillgeneric.repository.WithId

final case class SaleId(fk1: ProductId, fk2: PersonId)

final case class Sale(id: SaleId, saleDate: LocalDateTime) extends WithId[SaleId]
