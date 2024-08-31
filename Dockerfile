FROM openjdk:21

WORKDIR /app

COPY . .

RUN chmod +x ./mvnw

ENV JAVA_HOME="/usr/java/openjdk-21"

CMD ["java", "-jar", "./target/server-0.0.1-SNAPSHOT.jar"]
