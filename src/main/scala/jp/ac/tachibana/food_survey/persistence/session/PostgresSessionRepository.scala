package jp.ac.tachibana.food_survey.persistence.session

import cats.Applicative
import cats.syntax.applicative.*

import jp.ac.tachibana.food_survey.domain.session.Session
import jp.ac.tachibana.food_survey.domain.user.User

class PostgresSessionRepository[F[_]: Applicative] extends SessionRepository[F]:

  override def getActiveSession: F[Option[Session]] =
    None.pure[F]