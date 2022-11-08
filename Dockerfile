#https://www.youtube.com/watch?v=nhqcecpi47s
# syntax=docker/dockerfile:1
# Which offical Java image
FROM openjdk:20-jdk
# working directory
WORKDIR /app
# copu from your Host(PC, laptop) to container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Run this inside the image => download dependency
RUN ./mvnw dependency:go-offline
COPY src ./src
# run inside container
CMD ["./mvnw", "spring-boot:run"]
