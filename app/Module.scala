import com.google.inject.AbstractModule
import com.mongodb.{MongoClient, MongoClientURI}
import com.typesafe.config.ConfigFactory
import dao.{ClientPreferencePersistence, PersistenceLayer}
import services.ClientPreferenceService


class Module extends AbstractModule {

  override def configure(): Unit = {

    val uri = ConfigFactory.load.getString("connection")
    val db = "abhishek"
    val mongoDatabase = new MongoClient(new MongoClientURI(uri)).getDatabase(db)

    val databaseProvider = new PersistenceLayer(mongoDatabase)
//    CodecRegistries.fromRegistries()
    bind(classOf[PersistenceLayer]) toInstance databaseProvider
    bind(classOf[ClientPreferenceService]).asEagerSingleton()
    bind(classOf[ClientPreferencePersistence]).asEagerSingleton()

  }

}
