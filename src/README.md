# **README** 


#### Planner is a web api, that helps  menage  meetings. Base functions:
* Add meetings - during time between 15 and 120 minuets.
* Browsing existing meetings.
* Cancel meeting.
* Delete meetings only when is canceled.

#### Technologies:
* Java 13.0.2
* Spring-Boot 2.3.0
* Apache maven: 3.6.3
* Project Lombok
* Spock 
* Thymeleaf

#### Running application from IDE:
* Load the project to your IDE(import steps vary depending on your IDE).
* Run application via AppStarter.class

#### Running as packaged application:
Install:
	
*	download repository and extract,
*	run console and go to the planner folder,
*	create .jar file: `$ mvn package`.
	
Run:
	
*   go to the planner folder in console,
*	run application `$ java -jar target/Planner-v1.0.jar`.

By default application is avalilable at the following addres: `http://localhost:8080`.
*	IDE: run application.properties file and set: `server.port = 8850`

* 	console: `$ java -jar -Dserver.port=8850 target/Planner-v1.0.jar`




