FROM openjdk:8-alpine
LABEL "name"="com.ecash.cms-api" "version"="0.0.1-SNAPSHOT"

RUN mkdir -p /project/target
WORKDIR /project

# Add the build artifacts to the container
ADD ./target/cms-api.jar /project/target/

EXPOSE 8080
CMD ["java", "-Xmx716m", "-jar", "/project/target/cms-api.jar"]