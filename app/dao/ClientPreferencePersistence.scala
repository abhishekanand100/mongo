package dao

import java.util.Date

import com.google.inject.{Inject, Singleton}
import com.mongodb.client.{MongoCollection, MongoCursor}
import com.mongodb.client.result.DeleteResult
import com.mongodb.{BasicDBObject, MongoClient, MongoClientURI}
import models._
import org.bson.Document
import org.bson.types.ObjectId
import org.joda.time.DateTime


@Singleton
class ClientPreferencePersistence @Inject()(persistenceLayer: PersistenceLayer) {
  private val collectionName = ClientPreference.collectionName

  def findForFilter(clientPreferenceSearchQuery: ClientPreferenceSearchQuery): MongoCursor[Document] = {
    val query: BasicDBObject = createFilter(clientPreferenceSearchQuery)
    getCollection.find(query).iterator()
  }


  private def createFilter(clientPreferenceSearchQuery: ClientPreferenceSearchQuery) = {
    val query = new BasicDBObject
    if (clientPreferenceSearchQuery.id.isDefined) {
      query.put("_id", new ObjectId(clientPreferenceSearchQuery.id.get))
    }
    if (clientPreferenceSearchQuery.clientId.isDefined) {
      query.put("clientId", new Integer(clientPreferenceSearchQuery.clientId.get))
    }
    if (clientPreferenceSearchQuery.name.isDefined) {
      query.put("name", clientPreferenceSearchQuery.name.get)
    }
    if (clientPreferenceSearchQuery.templateId.isDefined) {
      query.put("templateId", clientPreferenceSearchQuery.templateId.get)
    }
    if (clientPreferenceSearchQuery.repeat.isDefined) {
      query.put("repeat", clientPreferenceSearchQuery.repeat.get)
    }
    query.put("isActive", Boolean.box(true))
    query
  }

  def save(clientPreference: ClientPreference): ClientPreference = {
    val collection = persistenceLayer.getCollection(collectionName)
    val document = new Document()
    document.put("clientId", new Integer(clientPreference.clientId))
    document.put("name", clientPreference.name)
    document.put("templateId", clientPreference.templateId.label)
    document.put("startDate", clientPreference.startDate.toDate)
    document.put("repeat", clientPreference.repeat.label)
    document.put("isActive", clientPreference.isActive)
    collection.insertOne(document)
    clientPreference.copy(id = document.get("_id").toString)
  }

  def delete(clientPreferenceSearchQuery: ClientPreferenceSearchQuery): DeleteResult = {
    val query: BasicDBObject = createFilter(clientPreferenceSearchQuery)
    getCollection.deleteOne(query)
  }

  private def getCollection: MongoCollection[Document] = {
    persistenceLayer.getCollection(collectionName)
  }

}


