package controllers

import com.google.inject.Inject
import models._
import org.joda.time.DateTime
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc._
import services.ClientPreferenceService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class APIController @Inject()(val controllerComponents: ControllerComponents, clientPreferenceService: ClientPreferenceService) extends BaseController {

  def find: Action[JsValue] = Action(parse.json) {
    request =>
      request.body.validate[ClientPreferenceSearchQuery].fold(
        errors => BadRequest(JsError.toJson(errors)),
        request => Ok(Json.toJson(clientPreferenceService.find(request))))
      }


  def delete(id: String): Action[AnyContent] = Action {
    clientPreferenceService
      .delete(id)
      Ok(s"Successfully deleted id $id")

  }

  def home = Action {
    Ok("App Running successfully")
  }

  def save: Action[JsValue] = Action.async(parse.json) {
    request =>
      request.body.validate[ClientPreferenceDTO].fold(
        errors => Future.successful(BadRequest(JsError.toJson(errors))),
        request => {
          val preference = ClientPreference(request.clientId, request.name,
            Template.find(request.templateId).get, new DateTime(),
            Frequency.find(request.repeat).get, isActive = true)
          Future{clientPreferenceService.save(preference)}.map(v => Ok(Json.toJson(v)))
        }
      )
  }

}
