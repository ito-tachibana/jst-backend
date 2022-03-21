package jp.ac.tachibana.food_survey.persistence.session

import cats.effect.Async
import cats.syntax.applicative.*

import jp.ac.tachibana.food_survey.domain.question.Question
import jp.ac.tachibana.food_survey.domain.session.{SessionElement, SessionTemplate}

class PostgresSessionTemplateRepository[F[_]: Async] extends SessionTemplateRepository[F]:

  override def getActiveTemplate: F[SessionTemplate] =
    SessionTemplate(
      Vector(
        SessionElement.Question(
          SessionElement.Number.zero,
          Question.Basic(
            Question.Id("test1"),
            text = "あなたは、「涙の出ないタマネギ」を\n食べてみたいと思いますか？",
            Question.ScaleText(
              minBoundCaption = "まったく\n食べたくない",
              maxBoundCaption = "とても\n食べてみつぃ"
            )
          )
        )
      )
    ).pure[F]