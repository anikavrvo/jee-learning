# Project structure

```
jakarta-ee-application/
│
├── docker/
│   └── ... -> infrastructure setup
│
└── hospital-auths-webapp/
    └── ... -> application development
```

# Running the project locally

1. The following Docker images are required to be installed:
    - container-registry.oracle.com/middleware/weblogic:14.1.1.0-11
    - postgres:14.24-alpine3.23
2. Start infrastructure up:
   - `cd docker`
   - `docker compose up -d`
3. Build application into WAR file
   - `cd ../hospital-auths-webapp`
   - `mvn clean package`
4. Deploy WAR
   ...