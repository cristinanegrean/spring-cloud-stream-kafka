# Pull in the smallest Docker image with OracleJDK 8 (167MB)
FROM frolvlad/alpine-oraclejdk8:slim

# add bash and coreutils
RUN apk add --no-cache bash coreutils

MAINTAINER negrean.cristina@gmail.com

# We added a VOLUME pointing to "/tmp" because that is where a Spring Boot application creates working directories for
# Tomcat by default. The effect is to create a temporary file on your host under "/var/lib/docker" and link it to the
# container under "/tmp". This step is optional for the simple app that we wrote here, but can be necessary for other
# Spring Boot applications if they need to actually write in the filesystem.
VOLUME /tmp

# The project JAR file is ADDed to the container as "app.jar"
ADD spring-cloud-stream-kafka-1.0.0-SNAPSHOT.jar app.jar

#Expose Tomcat HTTP Port, by default 8080, overriden to server.port=9000
EXPOSE 9000
#Expose Management HTTP Port for non sensitive endpoints: health and flyway
EXPOSE 8081
# Treat the container like a remote server, expose JPDA Transport address
EXPOSE 5005

# You can use a RUN command to "touch" the jar file so that it has a file modification time
# (Docker creates all container files in an "unmodified" state by default)
# This actually isn’t important for the simple app that we wrote, but any static content (e.g. "index.html")
# would require the file to have a modification time.
RUN bash -c 'touch /app.jar'

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]