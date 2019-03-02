name := "playmongo"
 
version := "1.0" 
      
lazy val `playmongo` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.12.6"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice ,  "org.mongodb" % "mongo-java-driver" % "3.10.1","com.typesafe.play" %% "play-json-joda" % "2.6.11"
)

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

      