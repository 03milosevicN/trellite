<!--About the project-->
<details>
<summary><strong>Table of Contents</strong></summary>

- [Backend](#backend)
  - [About the Project](#about-the-project)
  - [Built With](#built-with)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
      - [Maven](#maven)
      - [Docker](#docker)
  - [Project Architecture](#project-architecture)
    - [Class Diagram](#class-diagram)
    - [Mongo Collections](#mongo-collections)
    - [API Reference](#api-reference)
  - [Roadmap](#roadmap)

- [Frontend](#frontend)
  - [Built With](#built-with-1)
  - [Getting Started](#getting-started-1)
    - [Prerequisites](#prerequisites-1)
    - [Installation](#installation-1)
      - [NPM](#npm)
  - [Roadmap](#roadmap-1)

- [License](#license)

</details>


# Backend

<!--About the project-->
## About the project

Trellite is a Kanban-based project management system built for organizations.

Users register and form organizations, collaborative bodies that own the projects.
A project represents the main workspace which is structured into a hierarchy of boards and cards, the main unit of work.

A card is titled, contains a description and further nested components such as:
- Checklists: to-do lists.
- Comments: means of inter-project communication.
- Acitivites: automatic logs of events as work progress.

Organizations expose an admin dashboard for project owners, offering a periodic overview of activities for member evaluation and relevant information.
<!--About the project-->

<!--Built with-->
### Built with
[![Spring Boot][SpringBoot.io]][SpringBoot-url]
[![Java][Java.io]][Java-url]
[![PostgreSQL][PostgreSQL.io]][PostgreSQL-url]
[![MongoDB][MongoDB.io]][MongoDB-url]
[![JWT][JWT.io]][JWT-url]

[Java.io]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white
[Java-url]: https://www.java.com/en/
[SpringBoot.io]: https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[SpringBoot-url]: https://spring.io/projects/spring-boot
[PostgreSQL.io]: https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white
[PostgreSQL-url]: https://www.postgresql.org/
[MongoDB.io]: https://img.shields.io/badge/MongoDB-47A248?style=for-the-badge&logo=mongodb&logoColor=white
[MongoDB-url]: https://www.mongodb.com/
[JWT.io]: https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white
[JWT-url]: https://jwt.io/
<!--Built with-->

<!--Getting started!-->
## Getting started

### Prerequisites
- Java 25
- Maven
- PostgreSQL: database named `trellitedb` and the credentials within the `application.properties`.
- MongoDB: database named `trellite-mong-db` will be created on first run.

### Installation
#### Maven
1. Clone the repo
```
git clone https://github.com/03milosevicN/trellite-be.git
cd trellite-be/
```
2. Configure `application.properties`
```
spring.datasource.username=your_username
spring.datasource.password=your_password
jwt.secret=your_jwt_secret
```
3. Build and run
```
mvn clean install
mvnw spring-boot:run
```
By default, the app will start on `http://localhost:8080`.
#### Docker
- TBD (To Be Dockerized :])
<!--Getting started-->

## Project archtecture

### Class diagram
<img width="920" height="1332" alt="org_members (DDL)  trellite-postgresql" src="https://github.com/user-attachments/assets/95bb7461-04d8-4a6b-8779-a3ead0c5067a" />

### Mongo collections
<img width="420" height="1924" alt="trellite-monbo-diagram" src="https://github.com/user-attachments/assets/3d52ffb4-9011-49f1-bdc7-ba0ad4d7786c" />


### API Reference
- GET `/swagger-ui/index.html` for OpenAPI documentation

<!--Roadmap-->
## Roadmap
- Dockerization
- Detailed RBAC
<!--Roadmap-->


## Frontend

<!--Built with-->

### Built with

[![Angular][Angular.io]][Angular-url]
[![TypeScript][TypeScript.io]][TypeScript-url]
[![Bootstrap][Bootstrap.io]][Bootstrap-url]
[![Lucide][Lucide.io]][Lucide-url]

<!--MARKDOWN LINKS & IMAGES-->

[Angular.io]: https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white
[Angular-url]: https://angular.dev/
[TypeScript.io]: https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white
[TypeScript-url]: https://www.typescriptlang.org/
[Bootstrap.io]: https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com/
[Lucide.io]: https://img.shields.io/badge/Lucide-F56565?style=for-the-badge&logo=lucide&logoColor=white
[Lucide-url]: https://lucide.dev/

<!--Built with-->

<!--Getting Started-->

## Getting Started

### Prerequisites

* Node.js v25.3.0
* Angular CLI 21.0.5
* The [trellite-be](https://github.com/03milosevicN/trellite-be) backend running locally

### Installation

#### NPM

1. Clone the repo

```bash
git clone https://github.com/03milosevicN/trellite-fe.git
cd trellite-fe/
```

2. Install dependencies

```bash
npm install
```

3. Start the development server

```bash
ng serve
```

The application will be available at `http://localhost:4200` by default.

<!--Getting Started-->

## Roadmap

* Dockerization
* Unit tests
* E2E tests


<!--License-->
## License
Distributed under the MIT License. See `LICENSE.txt` for more information.
<!--License-->
