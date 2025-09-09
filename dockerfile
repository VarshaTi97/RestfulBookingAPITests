# Base image with Java and Maven
FROM maven:3.9.9-eclipse-temurin-21

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src


# Install Allure
RUN apt-get update && apt-get install -y unzip curl \
    && curl -Lo /tmp/allure.zip https://github.com/allure-framework/allure2/releases/download/2.21.0/allure-2.21.0.zip \
    && unzip /tmp/allure.zip -d /opt/ \
    && ln -s /opt/allure-2.21.0/bin/allure /usr/bin/allure \
    && rm /tmp/allure.zip

RUN rm -rf target/allure-results target/allure-report

RUN mvn clean install -DskipTests

CMD ["sh", "-c", "mvn test -Dsurefire.suiteXmlFiles=src/test/resources/testExecutor/testng.xml && allure generate target/allure-results --clean -o target/allure-report"]
