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
class ClientPreferenceDAO @Inject()(databaseProvider: DatabaseProvider) {
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
    val collection = databaseProvider.getCollection(collectionName)
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
    databaseProvider.getCollection(collectionName)
  }

}


object test {

  def main(args: Array[String]): Unit = {
    val uri: String = "mongodb+srv://admin:admin1@mongodbabhishek-wwlre.mongodb.net/admin?retryWrites=true"

    val newUri = "mongodb://admin1:admin1@abhishek-shard-00-00-wwlre.mongodb.net:27017,abhishek-shard-00-01-wwlre.mongodb.net:27017,abhishek-shard-00-02-wwlre.mongodb.net:27017/test?ssl=true&replicaSet=abhishek-shard-0&authSource=admin&retryWrites=true"
    val mongoClient = new MongoClientURI(newUri)
    val mongo = new MongoClient(mongoClient)


    val abc = mongo.getDatabase("abhishek")
    val deff = abc.listCollections()

    val collection = abc.getCollection("clientPreference")


    val clientPreference = ClientPreference(33, "firstone", CarouselAd, new DateTime(), Weekly, isActive = true)


    val doc = new Document()
    doc.put("clientId", clientPreference.clientId)
    doc.put("name", clientPreference.name)
    doc.put("templateId", clientPreference.templateId.label)
    doc.put("startDate", new Date())
    doc.put("repeat", clientPreference.repeat.label)
    doc.put("isActive", clientPreference.isActive)
    collection.insertOne(doc)
  }


}