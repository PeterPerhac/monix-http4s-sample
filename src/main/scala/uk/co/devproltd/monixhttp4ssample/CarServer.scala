package uk.co.devproltd.monixhttp4ssample

import cats.effect.Effect
import cats.syntax.flatMap._
import cats.syntax.functor._
import doobie.free.connection.ConnectionIO
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.util.transactor.Transactor.Aux
import fs2.StreamApp
import io.circe.Json
import io.circe.generic.JsonCodec
import io.circe.syntax._
import monix.eval.Task
import monix.eval.instances.CatsEffectForTask
import org.http4s.HttpService
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder

import scala.language.higherKinds
import implicits._

object implicits {
  implicit val scheduler = monix.execution.Scheduler.global
  implicit val taskEffect: Effect[Task] = new CatsEffectForTask()
}

object CarServer extends StreamApp[Task] {

  def stream(args: List[String], onShutdown: Task[Unit]): fs2.Stream[Task, StreamApp.ExitCode] = app[Task]

  def tx[F[_]: Effect]: Aux[F, Unit] =
    Transactor.fromDriverManager[F]("org.postgresql.Driver", "jdbc:postgresql:cars", "cars", "cars")

  def app[F[_]: Effect]: fs2.Stream[F, StreamApp.ExitCode] =
    BlazeBuilder[F]
      .bindHttp(8080, "0.0.0.0")
      .mountService(new CarService[F](CarRepository, tx).service, "/")
      .serve

}

@JsonCodec case class Car(id: Int, year: Int, make: String, model: String)

class CarService[F[_]: Effect](carRepository: CarRepository, transactor: Transactor[F]) extends ServiceBase[F] {

  val service: HttpService[F] = HttpService[F] {
    case GET -> Root / "cars" =>
      Ok(carRepository.findAll.transact(transactor).compile.toList.map(_.asJson))
    case GET -> Root / "cars" / IntVar(carId) =>
      for {
        lookupRes <- carRepository.find(carId.toInt).transact(transactor)
        res       <- lookupRes.fold(NotFound(s"Car ID=$carId was not found".asJsonError))(car => Ok(car.asJson))
      } yield res
    case DELETE -> Root / "cars" / IntVar(carId) =>
      Ok(carRepository.delete(carId).transact(transactor).map(n => s"$n car deleted".asJsonSuccess))
  }

}

abstract class ServiceBase[F[_]: Effect] extends Http4sDsl[F] {

  @JsonCodec case class GenericResponse(message: String, success: Boolean)

  implicit class StringOps(string: String) {
    def asJsonError: Json = GenericResponse(message = string, success = false).asJson

    def asJsonSuccess: Json = GenericResponse(message = string, success = true).asJson
  }

}

trait CarRepository {
  def find(id: Int): ConnectionIO[Option[Car]] =
    sql"select * from cars where id=$id".query[Car].option

  def findAll: fs2.Stream[ConnectionIO, Car] =
    sql"select * from cars".query[Car].stream

  def delete(id: Int): ConnectionIO[Int] =
    sql"delete from cars where id=$id".update.run

}

object CarRepository extends CarRepository
