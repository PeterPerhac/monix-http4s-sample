package uk.co.devproltd.monixhttp4ssample

import java.time.LocalDateTime

import io.circe.generic.JsonCodec
import io.circe.java8.time._

@JsonCodec case class Car(
  id: Int,
  year: Int,
  make: String,
  model: String
)
