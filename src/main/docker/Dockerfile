FROM openjdk:8-jdk-alpine
VOLUME /tmp
EXPOSE 8080
ADD winter-hello-documentdb-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT java -Dspring.profiles.active=$PROFILES -Dazure.documentdb.key=$DOCUMENTDB_KEY -Dazure.documentdb.uri=$DOCUMENTDB_URI -Djava.security.egd=file:/dev/./urandom -jar /app.jar
