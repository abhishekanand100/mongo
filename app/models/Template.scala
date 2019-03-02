package models

import play.api.libs.json._

sealed trait Template {
  val label: String
}

case object SingleImageAd extends Template{
  override val label: String = "Single Image Ad"
}


case object CarouselAd extends Template{
  override val label: String = "Carousel Ad"
}


object Template{

  val all = Seq(
    SingleImageAd,
    CarouselAd
  )

  private val lookupTable: Map[String, Template] =
    all.map(p => p.label.toLowerCase -> p).toMap

  def find(s: String): Option[Template] = lookupTable.get(s.toLowerCase)

  implicit val reads: Reads[Template] = new Reads[Template] {
    val failureString = "Not a Valid Template Type"
    override def reads(js: JsValue): JsResult[Template] = js match {
      case JsString(value) =>
        Template
          .find(value)
          .map(JsSuccess(_))
          .getOrElse(JsError(failureString))
      case _ => JsError(failureString)
    }
  }

  implicit val writes: Writes[Template] = new Writes[Template] {
    override def writes(o: Template): JsValue = JsString(o.label)
  }


}