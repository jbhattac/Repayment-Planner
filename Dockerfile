FROM openjdk:18
MAINTAINER Joydeep Bhattacharjee

VOLUME /tmp
ADD target/*.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]