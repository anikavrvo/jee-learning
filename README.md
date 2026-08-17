# JEE Learning

Documenting this as I go.

## firstcup-tutorial

Reference documentation: https://javaee.github.io/glassfish/documentation

> The above docs are quite outdated and I quickly learned there is no overwhelming reason to stick with Java EE for learning the basics. The below steps are thus unnecessary if one has a surface-level understanding of creating projects with Maven archetypes and building Java projects into deployable files.

1. Create JEE webapp project from the following Maven archetype: `maven-archetype-webapp`.
2. Follow the FirstCup tutorial for the `dukes-age` Java project and build it into a .war file.
3. Setup a local application server and deploy the .war file to this server.
    - I set up a simple Docker container to create a Weblogic application server from the corresponding Docker image provided on Oracle's image registry.
4. Access the `dukes-age/webapi/dukesAge` resource from the deployed application.


## jakarta-ee-application
*In progress.*