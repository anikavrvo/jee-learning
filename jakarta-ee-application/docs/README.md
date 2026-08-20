# Running the project locally

1. The following Docker images are required to be installed:
    - container-registry.oracle.com/middleware/weblogic:14.1.1.0-11
    - postgres:14.24-alpine3.23
2. You also need a local Docker engine to be running (starting up Docker Desktop is simplest and provides a nice interface for monitoring, logs etc.)
3. Build application into initial WAR file:
   - `cd ../hospital-auths-webapp`
   - `mvn clean package`
4. Start infrastructure up:
   - `cd docker`
   - `docker compose up -d`
5. Deploy WAR to Weblogic server:
   - This currently happens automatically upon starting the docker environment up with docker compose.
   - But this would have to happen manually within the Weblogic Admin console under 'Deployments' for each rebuild once the docker environment is already running.
   - Eventually the latter scenario will also be automated with the use of a Maven build profile: `mvn clean package -Pdeploy`


# Jakarta EE concepts covered

| Concept              | Tool                     |
| -------------------- | ------------------------------------ |
| HTTP API             | JAX-RS                               |
| REST endpoints       | `@Path`, `@GET`, `@POST`             |
| JSON                 | JSON-B / JSON-P                      |
| Dependency injection | CDI                                  |
| Connection pooling   | WebLogic DataSource                  |
| Resource lookup      | JNDI                                 |
| Transactions         | JTA                                  |
| Persistence          | JDBC → potentially JPA               |
| Configuration        | WebLogic + application configuration |
| Packaging            | WAR                                  |
| Deployment           | WebLogic                             |
| Build                | Maven                                |
| Infrastructure       | Docker Compose                       |