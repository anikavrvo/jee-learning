# JEE Learning

Documenting this as I go.

## 1 - firstcup-tutorial (not recommended)

Reference documentation: https://javaee.github.io/glassfish/documentation

> The above docs are quite outdated and I quickly learned there is no overwhelming reason to stick with Java EE for learning the basics. The below steps are thus unnecessary if one has a surface-level understanding of creating projects with Maven archetypes and building JEE projects into deployable WAR files.

1. Create JEE webapp project from the following Maven archetype: `maven-archetype-webapp`.
2. Follow the FirstCup tutorial for the `dukes-age` Java project and build it into a .war file.
3. Setup a local application server and deploy the .war file to this server.
    - I set up a simple Docker container to create a Weblogic application server from the corresponding Docker image provided on Oracle's image registry.
4. Access the `dukes-age/webapi/dukesAge` resource from the deployed application.


## 2 - My own jakarta-ee-application (much better)

> ChatGPT is a great teacher for step-by-step walkthroughs, but only once you know what you really want to do and you know how to validate its recommendations according to what you want. The roadmap below was decided before I used it as a development/learning guide.

1. Start with Docker environment setup - up until getting a Weblogic Server and Postgres database configured with respective volumes.
2. Spec a simple hospital authorization project and initialize C4 context diagrams (living docs) for reference.
3. Specify main process flows and/or ERD component diagrams.
4. Start webapp build with a simple approve/reject request flow as MVP.