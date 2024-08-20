FROM openjdk:17-slim
WORKDIR /app
COPY target/transaction-service-0.0.1-SNAPSHOT.jar /app/transaction-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "transaction-service-0.0.1-SNAPSHOT.jar"]