# studyshuffle

## Description
The following project is a maven spring-boot rest api with an embedded thymeleaf client. This project makes use of a mariadb database (v10.0.5) and runs in an embedded tomcat environment.
In order to run this project locally the following requirements need to be met.

## Requirements
Linux environment
Java 17.0.9
Maven 3.8.7
Mariadb 10.0.5
Springboot

## Compilation and Local Deployment
This project follows the maven lifecycle policy up until the package step, deployment is handled with a more manual approach and can only be accomplished by someone with the correct access rights.

In order to compile, test and package the project run ``` mvn package ``` 
In order to start the project locally run ``` mvn spring-boot:run ```

You will need to define logging directories for the project at /var/log/studyshuffle and create a application.properties file at /etc/studyshuffle

Here is an example application.properties file:

```
spring.application.name=studyshuffle
spring.datasource.url=jdbc:mariadb://localhost:3306/studyshuffle?useSSL=false
spring.datasource.username=studyshuffle
spring.datasource.password=<db-password>
spring.jpa.properties.javax.persistence.sharedCache.mode=NONE
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect

# App Properties
studyshuffle.app.jwtCookieName=studyshuffle
studyshuffle.app.jwtSecret=<64-character-token>
studyshuffle.app.jwtExpirationMs=30000
```

note you'll need a local instance of mariadb running with the database created and privileges granted (with a password) to the study shuffle user. This can be achieved by running the following query in mariadb ``` GRANT ALL ON studyshuffle.* TO 'studyshuffle'@'localhost' IDENTIFIED BY '<db-password>'; ```

Once you've run the application for the first time you will need to run the db init script to create the user roles. 

## Deployment to cloud 
The production version of this application is running on a centos VM in a cloud environment in a software defined network. Both the spring app and db are running on this machine. The machine's name is sandbox-dylan-db03. Ideally the spring app and database would be running on two separate machines but due to resource and time constraints i decided to setup both services on the same machine, this is a security risk.

sandbox-dylan-db03 is only accessible via a VPN. Only authorized users can connect to this machine via ssh through a vpn. The website is exposed to the public through the use of public proxy, please see the gateway diagram for more context. This proxy's name is sandbox-gateway-0x. sandbox-gateway-0x is running haproxy, which proxies tcp requests to the desired endpoints. For my project I have a public domain which points to sandbox-gateway-0x, any tcp request from this domain is proxied through to sandbox-dylan-db. apache is running on sandbox-dylan-db. apache serves the ssl certificate and proxies request through to a instance of the spring app running locally on non-specific port.

sandbox-dylan-db03 is responsible for building and deploying the project. There is a cron job that periodically checks for changes on the remote git repository on github. If it detects a change, it will trigger a mvn package, copy that project to the deployment directory and start it by using systemd. A systemd service is defined to manage to the starting and stopping of the java application. The production environment (application.properties) only exists on sandbox-dylan-db03. This environment is loaded in when the java app starts. 