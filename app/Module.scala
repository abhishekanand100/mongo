import com.google.inject.AbstractModule
import com.mongodb.{MongoClient, MongoClientURI}
import dao.{ClientPreferenceDAO, DatabaseProvider}
import services.ClientPreferenceService

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {

  override def configure(): Unit = {

    val uri = "mongodb://admin1:admin1@abhishek-shard-00-00-wwlre.mongodb.net:27017,abhishek-shard-00-01-wwlre.mongodb.net:27017,abhishek-shard-00-02-wwlre.mongodb.net:27017/test?ssl=true&replicaSet=abhishek-shard-0&authSource=admin&retryWrites=true"
    val db = "abhishek"
    val mongoDatabase = new MongoClient(new MongoClientURI(uri)).getDatabase(db)

    val databaseProvider = new DatabaseProvider(mongoDatabase)
//    CodecRegistries.fromRegistries()
    bind(classOf[DatabaseProvider]) toInstance databaseProvider
    bind(classOf[ClientPreferenceService]).asEagerSingleton()
    bind(classOf[ClientPreferenceDAO]).asEagerSingleton()

  }

}
