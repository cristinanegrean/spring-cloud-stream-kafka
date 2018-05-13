FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
#Expose Tomcat HTTP Port, by default 8080, overriden to server.port=9000
EXPOSE 9000
#Expose Management metrics endpoint
EXPOSE 8081
# Treat the container like a remote server, expose JPDA Transport address
EXPOSE 5005
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]