FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]

#FROM maven:3.8-jdk-11
#COPY src /home/app/src
#COPY pom.xml /home/app
#RUN mvn -f /home/app/pom.xml clean package
#
#FROM openjdk:11-jre-slim
#COPY --from=build /home/app/target/hua-api-0.0.1-SNAPSHOT.jar /usr/local/lib/hua-api.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/usr/local/lib/demo.jar"]