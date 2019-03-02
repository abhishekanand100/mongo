package controllers

import com.google.inject.Inject
import models._
import org.joda.time.DateTime
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.{Action, BaseController, ControllerComponents}
import services.ClientPreferenceService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ApplicationController @Inject()(val controllerComponents: ControllerComponents, clientPreferenceService: ClientPreferenceService) extends BaseController {

  def find: Action[JsValue] = Action.async(parse.json) {
    request =>
      request.body.validate[ClientPreferenceSearchQuery].fold(
        errors => Future.successful(BadRequest(JsError.toJson(errors))),
        request => {
          Future{clientPreferenceService.find(request)}.map(v => Ok(Json.toJson(v)))
        }
      )
  }

//  def home = Action {
//    Ok(views.html.index())
//  }

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
