package jp.ac.tachibana.food_survey.domain.question

import jp.ac.tachibana.food_survey.domain.question.Question
import jp.ac.tachibana.food_survey.domain.session.Session
import jp.ac.tachibana.food_survey.domain.user.User

sealed trait QuestionAnswer:
  def sessionNumber: Session.Number
  def questionId: Question.Id
  def respondentId: User.Id
  def value: Option[QuestionAnswer.ScaleValue]
  def comment: Option[QuestionAnswer.Comment]

object QuestionAnswer:

  opaque type ScaleValue = Int

  object ScaleValue:

    val minValue: QuestionAnswer.ScaleValue = 0
    val maxValue: QuestionAnswer.ScaleValue = 5
    val range: Vector[QuestionAnswer.ScaleValue] =
      (minValue.value to maxValue.value).toVector

    extension (_value: QuestionAnswer.ScaleValue) def value: Int = _value

    def apply(value: Int): QuestionAnswer.ScaleValue = value

  opaque type Comment = String

  object Comment:

    def apply(comment: String): QuestionAnswer.Comment = comment

    extension (comment: QuestionAnswer.Comment) def value: String = comment

  case class Basic(
    sessionNumber: Session.Number,
    questionId: Question.Id,
    respondentId: User.Id,
    value: Option[QuestionAnswer.ScaleValue],
    comment: Option[QuestionAnswer.Comment])
      extends QuestionAnswer

  case class Repeated(
    sessionNumber: Session.Number,
    questionId: Question.Id,
    respondentId: User.Id,
    value: Option[QuestionAnswer.ScaleValue],
    comment: Option[QuestionAnswer.Comment],
    previousQuestionId: Question.Id)
      extends QuestionAnswer
