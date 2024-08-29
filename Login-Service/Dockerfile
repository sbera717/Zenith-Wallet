FROM openjdk:17-slim
WORKDIR /app
COPY target/login-service-0.0.1-SNAPSHOT.jar /app/login-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "login-service-0.0.1-SNAPSHOT.jar"]