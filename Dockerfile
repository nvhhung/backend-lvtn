#https://www.youtube.com/watch?v=nhqcecpi47s
# syntax=docker/dockerfile:1
# Which offical Java image
FROM openjdk:15-jdk
# working directory
WORKDIR /app
# copu from your Host(PC, laptop) to container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Run this inside the image => download dependency
RUN ./mvnw dependency:go-offline
# copy src for src of container
COPY src ./src
# expose port interact with docker
EXPOSE 2701
# run inside container
CMD ["./mvnw", "spring-boot:run"]