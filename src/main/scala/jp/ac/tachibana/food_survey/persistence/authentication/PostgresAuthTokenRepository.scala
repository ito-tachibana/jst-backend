package jp.ac.tachibana.food_survey.persistence.authentication

import cats.effect.Async
import cats.syntax.functor.*
import doobie.*
import doobie.implicits.*

import jp.ac.tachibana.food_survey.domain.user.User
import jp.ac.tachibana.food_survey.persistence.util.ParameterInstances.*
import jp.ac.tachibana.food_survey.util.crypto.Hash

class PostgresAuthTokenRepository[F[_]: Async](implicit tr: Transactor[F]) extends AuthTokenRepository[F]:

  override def save(
    userId: User.Id,
    tokenHash: Hash): F[Unit] =
    sql"INSERT INTO user_session (user_id, token_hash) VALUES ($userId, $tokenHash)".update.run
      .transact(tr)
      .void

  override def load(tokenHash: Hash): F[Option[User.Id]] =
    sql"SELECT user_id FROM user_session WHERE token_hash = $tokenHash"
      .query[String]
      .option
      .transact(tr)
      .map(_.map(User.Id(_)))

  override def remove(tokenHash: Hash): F[Unit] =
    sql"DELETE FROM user_session WHERE token_hash = $tokenHash".update.run
      .transact(tr)
      .void
