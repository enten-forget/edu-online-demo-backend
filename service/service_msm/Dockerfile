FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY ./target/*.jar edu_online.jar
ENTRYPOINT ["java","-jar","/edu_online.jar", "&"]