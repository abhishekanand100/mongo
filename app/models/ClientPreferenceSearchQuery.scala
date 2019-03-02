package models

import play.api.libs.json.Json

case class ClientPreferenceSearchQuery(clientId: Option[Int] = None, name: Option[String] = None, id: Option[String] = None, templateId: Option[String] = None, repeat: Option[String] = None)

object ClientPreferenceSearchQuery{

  implicit val reads = Json.reads[ClientPreferenceSearchQuery]
  implicit val writes = Json.writes[ClientPreferenceSearchQuery]

}
