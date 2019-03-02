package models


import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json._

case class ClientPreference(clientId: Int, name: String, templateId: Template, startDate: DateTime, repeat: Frequency, isActive: Boolean, id: String = "")

object ClientPreference extends Metadata{
  val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
  implicit val dateFormat =
    Format[DateTime](JodaReads.jodaDateReads(pattern), JodaWrites.jodaDateWrites(pattern))

  implicit val writes: Writes[ClientPreference] = new Writes[ClientPreference] {

    override def writes(o: ClientPreference) = Json.obj(
      "clientId" -> o.clientId,
      "name" -> o.name,
      "startDate" -> o.startDate.withZone(DateTimeZone.UTC).toString,
      "isActive" -> o.isActive,
      "repeat" -> o.repeat.label,
      "templateId" -> o.templateId.label,
      "id" -> o.id
    )
  }

  implicit val reads: Reads[ClientPreference] = {

    for {
      id <- (JsPath \ "id").read[String]
      clientId <- (JsPath \ "clientId").read[Int]
      startDate <- (JsPath \ "startDate").read[DateTime]
      name <- (JsPath \ "name").read[String]
      isActive <- (JsPath \ "isActive").read[Boolean]
      repeat <- (JsPath \ "repeat").read[Frequency]
      templateId <- (JsPath \ "templateId").read[Template]
    } yield
      ClientPreference(
        clientId,
        name,
        templateId,
        startDate,
        repeat,
        isActive,
        id
      )
  }

  override def collectionName: String = "clientPreference"
}