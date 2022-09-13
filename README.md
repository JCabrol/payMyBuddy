# payMyBuddy

A transfer money application permitting to send easily money to other person connected.

A SpringBoot application with security and responsive design...

![image](https://user-images.githubusercontent.com/74394605/189933401-a00ff3e2-46b3-44d4-a651-c8fcabb54067.png)
![image](https://user-images.githubusercontent.com/74394605/189933702-232274b0-d105-40b6-bd87-d1a9cf3ee849.png)

A project done as part of my Java application developper training (project n°6).

The main skills acquired with this project were:

* design the technical architecture of an application using UML diagram
* implement a data schema in a relational database
* make a design diagram of the application database
* build a java web application with the repository pattern

 Here are the [presentation slideshows](https://github.com/JCabrol/payMyBuddy/blob/master/Cabrol_Justine_5_Presentation_022022.ppsx) I made for this project.

## Technical specifications

**Code:** 
* Java 13
* Maven 4.0.0
* Spring Boot 2.6.3

**Data:**
* MySql 8.0
* Spring Data JPA

**Security:**
* Spring Security

**Tests:**
* JUnit
* Mockito

**Templates:**
* Thymeleaf
* Bootstrap 5

**Measures, documentation,monitoring and code quality:**
* JaCoCo
* Surefire
* Swagger2
* Spring Boot Actuators
* Log4j
* Sonarcloud ([Here is my sonarcloud repository](https://sonarcloud.io/summary/new_code?id=JCabrol_payMyBuddy))

## UML class diagram

The UML class diagram explaining the class structure of the model.

![modeleConceptuelDeDonnees](https://user-images.githubusercontent.com/74394605/162656034-e9cd5d28-7095-4db0-b4dd-4d27f16d2685.png)

## Physical data model

The physical data model explaining the structure of the database tables.

![modeleLogiqueDeDonnees](https://user-images.githubusercontent.com/74394605/162634102-6da59ea8-864c-4e29-a086-e18b1e8d6880.png)

## Database SQL scripts

The database SQL scripts are present in the file ***data.sql*** located at [***src/main/resources/data.sql***](https://github.com/JCabrol/payMyBuddy/blob/master/src/main/resources/schema.sql).
