FROM maven:latest

WORKDIR /app

COPY pom.xml /app/
COPY . /app/

RUN mvn package

# Install necessary tools and dependencies
RUN apt-get update && apt-get install -y wget unzip libgtk-3-0 libcanberra-gtk-module xvfb

# Download and extract JavaFX SDK from GluonHQ
RUN wget https://download2.gluonhq.com/openjfx/23/openjfx-23_linux-x64_bin-sdk.zip -O javafx-sdk.zip
RUN unzip javafx-sdk.zip -d /opt/
RUN rm javafx-sdk.zip

# Set JavaFX environment variables
ENV PATH="/opt/javafx-sdk-23/bin:${PATH}"
ENV JAVA_HOME="/opt/javafx-sdk-23"
ENV LD_LIBRARY_PATH="/opt/javafx-sdk-23/lib"
ENV DISPLAY=:99

# Start Xvfb and run the Java application
CMD ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & java -Djava.awt.headless=true -Dglass.platform=gtk -Dprism.order=sw -Dprism.verbose=true --module-path /opt/javafx-sdk-23/lib --add-modules javafx.controls,javafx.fxml -jar target/SafeSpaceClient.jar"]