#Dockerfile for your app
FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean -DskipTests install
RUN echo "build completed"

FROM anapsix/alpine-java:8u144b01_jdk
VOLUME /tmp
ADD target/search-backend-0.1.0.jar app.jar
ENV JAVA_OPTS=""
RUN echo "added search-backend-0.1.0.jar"
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar
