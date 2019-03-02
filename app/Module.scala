import com.google.inject.AbstractModule
import com.mongodb.{MongoClient, MongoClientURI}
import dao.{ClientPreferenceDAO, DatabaseProvider}
import services.ClientPreferenceService


class Module extends AbstractModule {

  override def configure(): Unit = {

    val uri = ""
    val db = "abhishek"
    val mongoDatabase = new MongoClient(new MongoClientURI(uri)).getDatabase(db)

    val databaseProvider = new DatabaseProvider(mongoDatabase)
//    CodecRegistries.fromRegistries()
    bind(classOf[DatabaseProvider]) toInstance databaseProvider
    bind(classOf[ClientPreferenceService]).asEagerSingleton()
    bind(classOf[ClientPreferenceDAO]).asEagerSingleton()

  }

}
