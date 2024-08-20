FROM openjdk:17-slim
WORKDIR /app
COPY target/wallet-service-0.0.1-SNAPSHOT.jar /app/wallet-service-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD ["java", "-jar", "wallet-service-0.0.1-SNAPSHOT.jar"]