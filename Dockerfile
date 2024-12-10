# SINGLE STAGE
# # Referenceing for first stage image
# FROM maven:3.9.9-eclipse-temurin-23

# # These is just a name
# ARG APP_DIR=/app
# WORKDIR ${APP_DIR}

# COPY pom.xml .
# COPY mvnw .
# COPY mvnw.cmd .
# COPY src src
# COPY .mvn .mvn

# # RUN chmod a+x ./mvnw
# # RUN ./mvnw clean package -Dmaven.skip.tests=true
# RUN mvn clean package -Dmaven.skip.tests=true

# # SERVER_PORT must match the server.port in application properties, meaning springboot starts and listen on port 3000
# ENV SERVER_PORT=3000
# # EXPOSE tells Docker that the container exposes a specific port to the host, meaning DOcker knows that your app inside container listens on port 3000
# EXPOSE ${SERVER_PORT}

# # ENTRYPOINT ./mvnw spring-boot:RUN
# # jar file in target folder, artifactid+version+.jar
# ENTRYPOINT ["java", "-jar", "target/vttp5_day19l_pracws-0.0.1-SNAPSHOT.jar"]





# MULTISTAGE
# Referenceing for first stage image (Build Stage)
# compiler & runtime names can be named anything, it is just for stage 2 to reference in the "--from=compiler".
# if compiler is not named, COPY --from=0 /code_folder/target/app.jar /app/app.jar
# --from=0 refers to the first unnamed stage (indexed as 0).
FROM maven:3.9.9-eclipse-temurin-23 AS compiler

# These is just a name
ARG COMPILE_DIR=/code_folder
WORKDIR ${COMPILE_DIR}

# Copy project files and build
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY src src
COPY .mvn .mvn

#Compile and package the application
RUN mvn clean package -Dmaven.skip.tests=true


# stage 2 (Runtime Stage)
# Consider using a lighter base image like  switch to a runtime-only image like eclipse-temurin:23-jre or openjdk:23-jre to reduce the image size.
# The maven image in the runtime stage includes unnecessary tools for running the application.
# FROM eclipse-temurin:23-jre AS runtime
FROM maven:3.9.9-eclipse-temurin-23 AS runtime

ARG DEPLOY_DIR=/app
WORKDIR ${DEPLOY_DIR}

# Just copying the jar file and renaming it to day17l
COPY --from=compiler /code_folder/target/vttp5_day19l_pracws-0.0.1-SNAPSHOT.jar pracws.jar

ENV SERVER_PORT=3000
EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java", "-jar", "pracws.jar"]