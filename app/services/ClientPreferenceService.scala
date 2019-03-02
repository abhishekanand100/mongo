package services

import com.google.inject.Inject
import dao.ClientPreferencePersistence
import exceptions.{ExecutionException, NoDocumentFoundException}
import javax.inject.Singleton
import models.{ClientPreference, ClientPreferenceSearchQuery, Frequency, Template}
import org.bson.Document
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

@Singleton
class ClientPreferenceService @Inject()(clientPreferenceDAO: ClientPreferencePersistence) {

  def findById(objectId: String): ClientPreference = {
    val cursor = clientPreferenceDAO.findForFilter(ClientPreferenceSearchQuery(id = Some(objectId)))
    if(cursor.hasNext){
      createPreferenceObject(cursor.next())
    } else {
      throw NoDocumentFoundException(s"No Document found for $objectId")
    }
  }

  def find(clientPreferenceSearchQuery: ClientPreferenceSearchQuery): Seq[ClientPreference] = {
    val cursor = clientPreferenceDAO.findForFilter(clientPreferenceSearchQuery)
    if(cursor.hasNext){
      val list = new ListBuffer[ClientPreference]()
      while(cursor.hasNext){
        val savedPreference = createPreferenceObject(cursor.next())
        list += savedPreference
      }
      list
    } else {
      throw NoDocumentFoundException(s"No Document found for search query")
    }
  }

  def delete(objectId: String): String = {
    val deleteResult = clientPreferenceDAO.delete(ClientPreferenceSearchQuery(id = Some(objectId)))
    if(deleteResult.wasAcknowledged()){
      "Deleted"
    } else {
      throw ExecutionException(s"Could not delete document for $objectId")
    }
  }

  def deleteByQuery(clientPreferenceSearchQuery: ClientPreferenceSearchQuery): String = {
    val deleteResult = clientPreferenceDAO.delete(clientPreferenceSearchQuery)
    if(deleteResult.wasAcknowledged()){
      "Deleted"
    } else {
      throw ExecutionException(s"Could not delete document for ")
    }
  }


  def save(clientPreference: ClientPreference): ClientPreference = {
    //todo add logic for duplicate
    clientPreferenceDAO.save(clientPreference)
  }

  def findByClientId(clientId: Int): Seq[ClientPreference] = {
    val cursor = clientPreferenceDAO.findForFilter(ClientPreferenceSearchQuery(clientId = Some(clientId)))
        if(cursor.hasNext){
          val list = Seq[ClientPreference]()
          while(cursor.hasNext){
            list :+ createPreferenceObject(cursor.next())
          }
          list
        } else {
          throw NoDocumentFoundException(s"No Document found for Client id $clientId")
        }
  }



  private def createPreferenceObject(document: Document): ClientPreference = {
    val id: String = document.get("_id").toString
    val clientId: Int = Integer.valueOf(document.get("clientId").toString)
    val name: String = document.get("name").toString
    val templateId: String = document.get("templateId").toString
    val startDate: String = document.get("startDate").toString
    val repeat: String = document.get("repeat").toString
    val isActive: Boolean = document.get("isActive").asInstanceOf[Boolean]
    ClientPreference(clientId, name, Template.find(templateId).get,
      DateTime.parse(startDate, DateTimeFormat.forPattern("EEE MMM dd HH:mm:ss zzz yyyy")),
      Frequency.find(repeat).get, isActive, id)
  }
}
