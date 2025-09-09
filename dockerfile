# Base image with Java and Maven
FROM eclipse-temurin:24-jdk

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src
COPY testng.xml .

# Install Allure
RUN apt-get update && apt-get install -y unzip curl \
    && curl -Lo /tmp/allure.zip https://github.com/allure-framework/allure2/releases/download/2.21.0/allure-2.21.0.zip \
    && unzip /tmp/allure.zip -d /opt/ \
    && ln -s /opt/allure-2.21.0/bin/allure /usr/bin/allure \
    && rm /tmp/allure.zip


RUN mvn clean install -DskipTests


CMD mvn test -Dsurefire.suiteXmlFiles=testng.xml \
    && allure generate target/allure-results --clean -o target/allure-report
