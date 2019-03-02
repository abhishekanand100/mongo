package controllers

import com.google.inject.Inject
import models._
import org.joda.time.DateTime
import play.api.data.Forms._
import play.api.data._
import play.api.libs.json.Json
import play.api.mvc._
import services.ClientPreferenceService

class ClientPreferenceController @Inject()(cc: MessagesControllerComponents, clientPreferenceService: ClientPreferenceService)
  extends MessagesAbstractController(cc) {


  private val logger = play.api.Logger(this.getClass)

  val clientDTOForm: Form[ClientPreferenceDTO] = Form (
    mapping(
      "name" -> nonEmptyText,
      "templateId" -> text,
      "repeat" -> text,
      "clientId" -> number,
    )((name, templateId, repeat, clientId) => ClientPreferenceDTO(name, templateId, repeat, clientId))
    ((clientPreferenceDTO: ClientPreferenceDTO) => Some(clientPreferenceDTO.name, clientPreferenceDTO.templateId,
      clientPreferenceDTO.repeat, clientPreferenceDTO.clientId))
  )

  val clientPreferenceSearchForm: Form[ClientPreferenceSearchQuery] = Form (
    mapping(
      "id" -> text,
      "clientId" -> number,
      "name" -> text,
      "repeat" -> text,
      "templateId" -> text,
    )((id, clientId, name, repeat, templateId) => ClientPreferenceSearchQuery(Some(clientId), Some(name), Some(id), Some(templateId), Some(repeat)))
    ((clientPreferenceSearchQuery: ClientPreferenceSearchQuery) => Some(clientPreferenceSearchQuery.id.getOrElse(""), clientPreferenceSearchQuery.clientId.getOrElse(0),
      clientPreferenceSearchQuery.name.getOrElse(""), clientPreferenceSearchQuery.repeat.getOrElse(""), clientPreferenceSearchQuery.templateId.getOrElse("")))
  )

  def formAdd = Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.clientPreference(clientDTOForm, routes.ClientPreferenceController.save()))
  }

  def formSearch =  Action { implicit request: MessagesRequest[AnyContent] =>
    Ok(views.html.clientPreferenceSearch(clientPreferenceSearchForm, routes.ClientPreferenceController.search()))
  }

  def search =  Action {
    implicit request: MessagesRequest[AnyContent] =>
      val errorFunction = { formWithErrors: Form[ClientPreferenceSearchQuery] =>
        logger.debug("CAME INTO errorFunction")
        // this is the bad case, where the form had validation errors.
        // show the user the form again, with the errors highlighted.
        BadRequest(views.html.clientPreferenceSearch(formWithErrors, routes.ClientPreferenceController.formSearch()))
      }

      val successFunction = { data: ClientPreferenceSearchQuery =>
        logger.debug("CAME INTO successFunction")
        // this is the SUCCESS case, where the form was successfully parsed as a BlogPost
        val searchQuery = ClientPreferenceSearchQuery(
          data.clientId,
          data.name,
          data.id,
          data.templateId,
          data.repeat
        )
        val searchResults = clientPreferenceService.find(searchQuery)
        val result = Json.toJson(searchResults).toString()
        Redirect(routes.ClientPreferenceController.formSearch()).flashing("info" -> s"Saved successfully with id $result")
      }

      val formValidationResult: Form[ClientPreferenceSearchQuery] = clientPreferenceSearchForm.bindFromRequest
      formValidationResult.fold(
        errorFunction,
        successFunction
      )
  }


  def save = Action {
    implicit request: MessagesRequest[AnyContent] =>
      val errorFunction = { formWithErrors: Form[ClientPreferenceDTO] =>
        logger.debug("CAME INTO errorFunction")
        // this is the bad case, where the form had validation errors.
        // show the user the form again, with the errors highlighted.
        BadRequest(views.html.clientPreference(formWithErrors, routes.ClientPreferenceController.formAdd()))
      }

      val successFunction = { data: ClientPreferenceDTO =>
        logger.debug("CAME INTO successFunction")
        // this is the SUCCESS case, where the form was successfully parsed as a BlogPost
        val preferenceDTO = ClientPreferenceDTO(
          data.name,
          data.templateId,
          data.repeat,
          data.clientId
        )
        val preference = ClientPreference(preferenceDTO.clientId, preferenceDTO.name,
          Template.find(preferenceDTO.templateId).get, new DateTime(),
          Frequency.find(preferenceDTO.repeat).get, isActive = true)
        val savedPreference = clientPreferenceService.save(preference)
        Redirect(routes.ClientPreferenceController.formAdd()).flashing("info" -> s"Saved successfully with id ${savedPreference.id}")
      }

      val formValidationResult: Form[ClientPreferenceDTO] = clientDTOForm.bindFromRequest
      formValidationResult.fold(
        errorFunction,
        successFunction
      )
  }
}