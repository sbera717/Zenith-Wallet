FROM openjdk:17-slim
WORKDIR /app
COPY target/user-service-0.0.1-SNAPSHOT.jar /app/user-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "user-service-0.0.1-SNAPSHOT.jar"]