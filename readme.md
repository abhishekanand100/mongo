#  CRUD

### Installing

Git clone

To run locally `sbt -jvm-debug 5005`

Environment Variable `$CONNNECTION`



## Create
####Via API

POST `/create`

with request body
`{"name":"preference", "clientId":123, "templateId":"Carousel Ad", "repeat":"daily"}`

####Via Form
Open url `/formAdd`

### Delete
###Via API
GET `/deleteById/5c79f8337db80e0e72102908`




####Via Form
Open url `/formDelete`


### Search
####Via API
POST Request `http://localhost:9000/find`

with request body

`{"id":"jkqbd22331"}`


####Via Form
Open url `/formSearch`




