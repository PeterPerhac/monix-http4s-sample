package uk.co.devproltd.monixhttp4ssample

import java.time.{Instant, LocalDateTime}
import java.time.ZoneOffset.UTC

import doobie.util.meta.Meta

package object repository {

  implicit val ldtMeta: Meta[LocalDateTime] = Meta[Instant].xmap(LocalDateTime.ofInstant(_, UTC), _.toInstant(UTC))

}
