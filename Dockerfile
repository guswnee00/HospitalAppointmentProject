FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY build/libs/hospital-appointment-service.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]