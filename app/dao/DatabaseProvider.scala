package dao

import com.google.inject.Singleton
import com.mongodb.client.{MongoCollection, MongoDatabase}
import javax.inject.Inject
import org.bson.Document

@Singleton
class DatabaseProvider @Inject()(mongoDatabase: MongoDatabase) {

  def getCollection(collectionName: String): MongoCollection[Document] = {
    mongoDatabase.getCollection(collectionName)
  }

}
