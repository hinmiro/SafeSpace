FROM maven:latest

RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml /app/

COPY . /app/

RUN mvn package

RUN apt-get update && apt-get install -y openjfx


# Run the main class from the built JAR
CMD ["java", "--module-path", "/usr/share/openjfx/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "target/SafeSpace.jar"]