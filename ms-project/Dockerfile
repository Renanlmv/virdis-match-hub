FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /opt/app

COPY pom.xml .

COPY src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:25-jre

WORKDIR /opt/app

COPY --from=build /opt/app/target/*.jar app.jar

ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "app.jar"]