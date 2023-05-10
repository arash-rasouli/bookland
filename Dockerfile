FROM maven:3.8.4 AS build
COPY src /usr/bookland/src
COPY pom.xml /usr/bookland
RUN mvn -f /usr/bookland/pom.xml clean install -Pnative

FROM openjdk:17
COPY --from=build /usr/bookland/target/BooklandApp.jar BooklandApp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "BooklandApp.jar"]