package models

import play.api.libs.json.Json

case class ClientPreferenceDTO(name: String, templateId: String, repeat: String, clientId: Int)




object ClientPreferenceDTO{
  implicit val reads = Json.reads[ClientPreferenceDTO]
  implicit val wriate = Json.writes[ClientPreferenceDTO]

}


