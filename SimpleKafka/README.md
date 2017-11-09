This application just reads query from request and converts to ProtoBuf message. Then it pushes the message to Kafka Server.

/**This does not have validation on Method or Exception handling as of now.**/

------------------------------------------------------------------------------
Steps to bring up the server.

1 . Set followings VM_Args
	-DPORT = "8080"
	-DKAFKA_HOST = "localhost:9092"
2. Run Server from src/main/java/com/praba/server/HttpNettyServer

----------------------------------------------------------------------------

Sample Request Format: 
----------------------
	Method: POST
	uri: http://localhost:8080/{topicName}
	Body: {id:10, "firstname":"Prabakaran", "lastName":"loganathan"}
	
	------------------------------------------------------------