package jp.ac.tachibana.food_survey

import cats.effect.{IO, IOApp}
import doobie.Transactor

import jp.ac.tachibana.food_survey.configuration.domain.ApplicationConfig
import jp.ac.tachibana.food_survey.http.HttpService
import jp.ac.tachibana.food_survey.http.middleware.AuthenticationMiddleware
import jp.ac.tachibana.food_survey.http.routes.AuthenticationRoutes
import jp.ac.tachibana.food_survey.persistence.DatabaseTransactor
import jp.ac.tachibana.food_survey.persistence.authentication.PostgresAuthTokenRepository
import jp.ac.tachibana.food_survey.services.authentication.DefaultAuthenticationService

object Main extends IOApp.Simple:

  override def run: IO[Unit] =
    for {
      appConfig <- ApplicationConfig.load[IO]
      _ <- IO.delay(println(appConfig.http))
      result <- DatabaseTransactor.start[IO](appConfig.persistence).use { (tr: Transactor[IO]) =>
        implicit val transactor: Transactor[IO] = tr
        val authTokenRepository = new PostgresAuthTokenRepository[IO]()
        for {
          authenticationService <- DefaultAuthenticationService.create[IO](authTokenRepository)
          authenticationMiddleware =
            new AuthenticationMiddleware[IO](
              appConfig.http.authentication,
              authenticationService
            )
          httpService = new HttpService[IO](
            config = appConfig.http,
            authenticationRoutes = new AuthenticationRoutes[IO](authenticationMiddleware, authenticationService)
          )
          start <- httpService.start(runtime.compute)
        } yield start
      }
    } yield result
