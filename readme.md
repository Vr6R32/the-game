<br>
<h1 align="center">Email Chatter</h1>
<h3 align="center">(the-game)</h3>
<br>

<p align="center">
Email Chatter is a responsive single page application facilitating real-time message and event exchange between users based on their email addresses, leveraging WebSocket technology. This application features a scalable microservice architecture built on Spring Cloud API, with
 Api Gateway incorporating the Eureka server-client pattern along with Feign clients for efficient inter-service communication. It utilizes two databases, PostgreSQL and MongoDB, for data storage and management. This project demonstrates proficiency in modern backend development, cloud architecture, and database integration within a distributed system.
</p>
<br>


<p align="center">
Application target is to build responsive online browser-based game,
project is undergoing expansion and is not yet fully ready for use. Initially, it emerged as an idea for a responsive browser game. For now, a solid foundation has been laid with user authentication infrastructure, contact creation, and real-time message transmission via WebSocket for real-time processing.
</p>
<br>


<h1 align="center">Fully scaled & tracked infrastructure</h1>

<p align="center">
Application is build as a fully scaled infrastructure with possibility to load up more instances if needed. 
</p>


<h2 align="left">
Eureka: 
</h2>

Used for service discovery and registration, allowing services to locate and communicate with each other without hardcoding service locations.

<h2 align="left">
Spring Cloud Feign: 
</h2>

Simplifies the development of RESTful clients by providing a higher-level abstraction over HTTP clients. This enables declarative REST calls between microservices.

<h2 align="left">
Prometheus: 
</h2>

An open-source monitoring and alerting toolkit that collects metrics from your services and infrastructure, helping you detect and diagnose issues in real time.

<h2 align="left">
Kafka: 
</h2>

A distributed event streaming platform used to publish, subscribe to, store, and process streams of records in real time. It serves as an asynchronous messaging backbone for communication between WebSocket service instances.

<h2 align="left">
WebSocket Service: 
</h2>

Utilizes SockJS and STOMP protocol for handling real-time, session-based communication. The infrastructure is designed to seamlessly scale WebSocket services by leveraging Apache Kafka for asynchronous message exchange between service instances.

<h2 align="left">
Grafana with Loki and Promtail:
</h2>
Grafana is used for metric visualization and monitoring dashboards, with Loki log aggregation system and Promtail that ships the logs to Loki are dispatched for logging and log aggregation.


<br>
<br>

<h1 align="center">
Monitoring stack pictures
</h1>


![](readme/png/prometheus-targets.png)

<br>
<br>


![](readme/png/websocket-monitoring.png)

<br>
<br>


![](readme/png/tracing-logging.png)


<br>
<br>


![](readme/png/conversation-monitoring.png)
<br>
<br>


![](readme/png/mongo-sessions.png)
<br>
<br>






|       Service        |                       LOCAL                        |                                          OpenAPI                                           | Login  |  Password   |
|:--------------------:|:--------------------------------------------------:|:------------------------------------------------------------------------------------------:|:------:|:-----------:|
|     LandingPage      |  [http://localhost:8083/](http://localhost:8083/)  |                                             x                                              | tester |   secret    |
|                      |                                                    |                                                                                            | michal |   secret    |
|      Prometheus      |  [http://localhost:9090/](http://localhost:9090/)  |                                             x                                              |   x    |      x      |
|       Grafana        |  [http://localhost:3000/](http://localhost:3000/)  |                                             x                                              | admin  |   secret    |
|        Zipkin        |  [http://localhost:9411/](http://localhost:9411/)  |                                             x                                              |   x    |      x      |
|    Mongo-Express     | [http://localhost:27016/](http://localhost:27016/) |                                             x                                              | mongo  | dontgotosql |
|                      |                                                    |                                                                                            |        |             |
|     User-Service     |  [http://localhost:8082/](http://localhost:8082/)  | [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html) |   x    |      x      |
|       Frontend       |  [http://localhost:8080/](http://localhost:8080/)  | [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) |   x    |      x      |
|     Auth-Service     |  [http://localhost:8084/](http://localhost:8084/)  | [http://localhost:8084/swagger-ui/index.html](http://localhost:8084/swagger-ui/index.html) |   x    |      x      |
| Conversation-Service |  [http://localhost:8087/](http://localhost:8087/)  | [http://localhost:8087/swagger-ui/index.html](http://localhost:8087/swagger-ui/index.html) |   x    |      x      |
|  WebSocket-Service   |  [http://localhost:8085/](http://localhost:8085/)  | [http://localhost:8085/swagger-ui/index.html](http://localhost:8085/swagger-ui/index.html) |   x    |      x      |
|     Api-Gateway      |  [http://localhost:8083/](http://localhost:8083/)  |                                             x                                              |   x    |      x      |
|    Eureka-Server     |  [http://localhost:8761/](http://localhost:8761/)  |                                             x                                              |   x    |      x      |




<h1 align="center">Project compilation & local docker mount:</h1>

```bash
 mvn clean install jib:dockerBuild -Plocal
```

<h1 align="center">Docker deployment:</h1>

```bash
docker compose up
```
<br>


<h1 align="center">Project GUI :</h1>

<h1 align="center">Login & Logout animation</h1>
<br>

![](readme/login-logout.webp)
<br>

<h1 align="center">Responsive buttons</h1>

<br>

![](readme/responsive-chat-hover-buttons.webp)
<br>

<h1 align="center">Contact search filter</h1>

<br>

![](readme/search-filter-input.webp)
<br>

<h1 align="center">Real time logout events</h1>

<br>

![](readme/second-user-logout.webp)
<br>

<h1 align="center">Real time login & message events</h1>

<br>

![](readme/chat-message-delivery.webp)
<br>

<h1 align="center">Accepting conversation</h1>

<br>

![](readme/accept-invite.webp)
<br>

<h1 align="center">Real time invitation accept event</h1>

<br>

![](readme/accept-invite-second-user-event.webp)
<br>

<h1 align="center">Invitation Section</h1>

<br>

![](readme/new-contact-invitation.webp)
<br>

<h1 align="center">Real time offline status update</h1>

<br>

![](readme/real-time-offline-status-update.webp)
<br>

<h1 align="center">Conversation messages pagination pulldown effect</h1>

<br>

![](readme/messages-pagination-pulldown.webp)


