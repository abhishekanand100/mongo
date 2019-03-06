#  CRUD

Using Scala, Play Web Framwork, Google Guice DI and MongoDB
### Installing

Git clone

Dependencies `sbt`

https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Mac.html



To run locally `sbt -jvm-debug 5005`

Environment Variable `$CONNNECTION`

Runs on `localhost:9000`



## Create via API

POST `/create`

with request body
`{"name":"preference", "clientId":123, "templateId":"Carousel Ad", "repeat":"daily"}`


### Delete via API

GET `/deleteById/5c79f8337db80e0e72102908`





### Search via API
POST Request `http://localhost:9000/find`

with request body

`{"id":"jkqbd22331"}`


## Create via Form

Open url `/formAdd`
## Delete via Form

GET `/deleteById/5c79f8337db80e0e72102908`


## Search via Form
Open url `/formSearch`

