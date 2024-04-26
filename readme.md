<br>
<h1 align="center">Email Chatter</h1>
<h3 align="center">(the-game)</h3>
<br>

<p align="center">
Email Chatter is a responsive single page application facilitating real-time message and event exchange between users based on their email addresses, leveraging WebSocket technology. This application features a scalable microservice architecture built on Spring Cloud API, with
 Api Gateway incorporating the Eureka server-client pattern along with Feign clients for efficient inter-service communication. It utilizes two databases, PostgreSQL and MongoDB, for data storage and management. This project demonstrates proficiency in modern backend development, cloud architecture, and database integration within a distributed system.
<br>

Application target is to build responsive online browser-based game, but currently it is being expanded. Initially, it emerged as an idea for a responsive browser game. For now, a solid foundation has been laid with user authentication infrastructure, contact creation, and real-time message transmission via WebSocket for real-time processing.

</p>
<br>


|    Service    |                       LOCAL                        |       Login        |      Password       |
|:-------------:|:--------------------------------------------------:|:------------------:|:-------------------:|
|  LandingPage  |  [http://localhost:8083/](http://localhost:8083/)  |       tester       |       secret        |
|               |                                                    |       michal       |       secret        |
|  Prometheus   |  [http://localhost:9090/](http://localhost:9090/)  |         x          |          x          |
|    Grafana    |  [http://localhost:3000/](http://localhost:3000/)  |       admin        |       secret        |
|    Zipkin     |  [http://localhost:9411/](http://localhost:9411/)  |         x          |          x          |
| Mongo-Express | [http://localhost:27016/](http://localhost:27016/) |       mongo        |     dontgotosql     |




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

<h1 align="center">Real time invitation event</h1>

<br>

![](readme/accept-invite-second-user-event.webp)
