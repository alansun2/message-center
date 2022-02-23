FROM openjdk:8-jdk-alpine
VOLUME /tmp
ENV TZ=Asia/Shanghai
COPY message-server-1.0.0-SNAPSHOT.jar /app.jar
ENTRYPOINT java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar