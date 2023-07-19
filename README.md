# AssetManager-backend
AssetManager is asset management software which enables the tracking of equipment. The main purpose is to improve communication between company employees and IT departments during asset exchange or new asset assignment process.


This is a server-side program responsible for responding to requests coming from the client application (frontend). It is responsible for performing operations on the database, as well as analytical and administrative tasks (such as user authentication).
The "db.migrations" folder contains all migrations used to build the database, ordered from the oldest to the newest version. The "mail.template" folder contains HTML files that serve as email template structures. These templates are used by the server-side application to send emails to users based on the status of various components, such as order status changes. The "config" folder holds configuration files related to application security, utilizing libraries like Spring Security. All controllers are located in the "controller" folder, where incoming requests from the client application are handled in respective methods. These methods are placed in the "service" directory. The "dto" folder stores classes with the appropriate structure used for receiving/sending responses to client application requests. Certain functionalities of the Asset Manager system have imposed restrictions, and classes handling specific exceptions are created to handle such inconsistencies. These exception classes are placed in the "exception" directory. To monitor system actions, a "logger" directory is added, which contains .java files displaying logs in the console with information about the current operations performed by the system. The "mapper" folder stores abstract classes and interfaces used for data structure conversion. The "repository" folder contains files that include database commands, with each method responsible for a specific data operation. For example, one operation could be finding users with the role of IT Employee.
The springframework.Scheduler library was used, with its functions invoked in the "scheduler" folder to fulfill requirement of generating email messages to DHL managers after fourteen days from sending a new device to a recipient. The last folder is "util" where additional tools used by the system are stored, such as a class for creating tokens or a class for retrieving the current date.

## Versions
Make sure you are using below versions:
* Open JDK 17.0.1
* Maven 3.8.5
* Docker Desktop 4.x.x
## How to Run

This application is packaged as a war which has Tomcat 8 embedded. No Tomcat installation is necessary. You run it using the ```mvn spring-boot:run``` command.

* Clone this repository
* Make sure you are using Open JDK 17.0.1 and Maven 3.x
* Set Active Directory domain ```ad.domain``` and url ```ad.url``` in ```application-DEV.yml``` file.
* Start up the application stack using the ```docker compose up``` command.
* Create database named: ```asset-manager```
* You can build the project and run the tests by running ```mvn clean package```
* Once successfully built, you can run the service by method ```mvn spring-boot:run```. Please make sure, that you execute a command in the project's root directory (the folder that contains the pom.xml file)
## Security

To secure the Asset Manager application against unwanted attacks, the Spring Security library is utilized as a filter, enabling highly configurable authentication and access control. To obtain secured information from the server, it is necessary to send all user information using JSON Web Tokens (JWT) during each request to the server. Authorization is achieved through the LDAP protocol. User credentials provided by the user are sent to the Active Directory. After successful authentication, the Active Directory returns the user's login. Based on the login, the server-side application assigns the appropriate role to the logged-in user.
##About the Service
AssetManager-backend uses relational database MSSQL to store data about users (eg. user roles). Database connection properties are set in ```docker-compose.yml``` and ```application-DEV.yml``` files.
After application is connected to MSSQL, please create database named: ```asset-manager```.
Migrations of database are performed programmatically by using a schema migration tool (Flyway).
If your database connection properties work, you can call some REST endpoints defined in ```com.dhl.assetmanager.controller``` on port **8080**. (see below example)

* Update order: PUT/```http://localhost:8080/api/orders/1```
```
Accept: application/json
Content-Type: application/json
Authorization: Bearer Token

{
    "deliveryType": "PICKUP",
    "deliveryAddress": null,
    "pickupDate": "2023-06-01T22:53:30",
    "serialNumber": "123-456-test3",
    "remark": "test",    
    "status": "HANDED_FOR_COMPLETION",
    "receiverId": 1, 
    "requestedModel": "LARGE_LAPTOP",
    "assignedTo": 1,
    "assignedBy": 1
}

RESPONSE: HTTP 200 (Updated order data)
```
###Email service
The service automatically sends appropriate email messages, according to HTML templates located in ```resources.mail.templates```, based on changes in the order status.
You can view the sent email messages by the server application using MailDev, which is running at the URL: ```http://localhost:8082/```.
Configuration properties are set in ```docker-compose.yml``` file.
###Scheduler service
The service automatically checks every hour if a specific event has been completed. If not, the server application automatically sends a reminder email to the appropriate user. Service is located in ```com.dhl.assetmanager.service.RemindersScheduler```.
