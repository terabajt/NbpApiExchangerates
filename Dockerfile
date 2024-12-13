FROM maven:3.9.6-eclipse-temurin
COPY . /app
WORKDIR /app
RUN mvn clean package
RUN mv ./target/*.jar ./target/spring-docker-app.jar
ENTRYPOINT ["java", "-jar", "./target/spring-docker-app.jar" ]