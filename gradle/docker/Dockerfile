FROM cnegrean/springcloudstream-kafka-consumer-base-image:ibm-semeru-runtimes_jdk-17.0.2.8_openj9-ubuntu-focal

ARG service_name

COPY dependencies/ ./
COPY spring-boot-loader/ ./
COPY libs-dependencies/ ./
COPY application/ ./

#Expose Tomcat HTTP Port, by default 8080, overriden to server.port=9000
EXPOSE 9000
# 8081 is for the K8S probes and is served by a dedicated Tomcat Connector
EXPOSE 8081
# Treat the container like a remote server, expose JPDA Transport address
# EXPOSE 5005
