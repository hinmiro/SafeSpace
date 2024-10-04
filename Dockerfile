FROM openjdk:17-slim

RUN apt-get update && apt-get install -y openjfx

ENV JAVAFX_HOME=/usr/share/javafx-sdk-17.0.2

WORKDIR /app

COPY pom.xml /app/

COPY . /app/

RUN mvn package

RUN apt-get update && apt-get install -y openjfx


# Run the main class from the built JAR
ENTRYPOINT ["java", "--module-path", "/usr/share/javafx-sdk-17.0.2/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "target/SafeSpace.jar"]