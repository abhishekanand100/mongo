package models

import play.api.libs.json._

sealed trait Frequency {
  val label: String
}


case object Daily extends Frequency{
  override val label: String = "daily"
}

case object Weekly extends Frequency{
  override val label: String = "weekly"
}

case object Monthly extends Frequency{
  override val label: String = "monthly"
}


object Frequency{

  val all = Seq(
    Daily,
    Weekly,
    Monthly
  )

  private val lookupTable: Map[String, Frequency] =
    all.map(p => p.label.toLowerCase -> p).toMap

  def find(s: String): Option[Frequency] = lookupTable.get(s.toLowerCase)


  implicit val reads: Reads[Frequency] = new Reads[Frequency] {
    val failureString = "Not a Valid Frequency Type"
    override def reads(js: JsValue): JsResult[Frequency] = js match {
      case JsString(value) =>
        Frequency
          .find(value)
          .map(JsSuccess(_))
          .getOrElse(JsError(failureString))
      case _ => JsError(failureString)
    }
  }

  implicit val writes: Writes[Frequency] = new Writes[Frequency] {
    override def writes(o: Frequency): JsValue = JsString(o.label)
  }
}