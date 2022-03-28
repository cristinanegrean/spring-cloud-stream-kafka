FROM ibm-semeru-runtimes:open-17.0.2_8-jre-focal

RUN chown -R root:root /opt/java/openjdk/ \
    && chmod -R u+rwx /opt/java \
    && chmod -R g+rx /opt/java \
    && chmod -R o+rx /opt/java

VOLUME /tmp

RUN addgroup --gid 2001 springcloudstream && \
    adduser -u 2001 --disabled-login --home /home/springcloudstream --shell /sbin/nologin --ingroup springcloudstream --gecos "" springcloudstream
USER gateway
ENV WORKDIR=/home/springcloudstream
WORKDIR ${WORKDIR}

ENV JAVA_OPTS="-Xmx384m"

ENTRYPOINT java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom org.springframework.boot.loader.JarLauncher