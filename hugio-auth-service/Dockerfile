FROM tuanloc/ubuntu:java17

#WORKDIR /app
#COPY . /app
#COPY ./target/auth-service.jar /app/app.jar
COPY . .
COPY ./target/app.jar app.jar

ENTRYPOINT ["java", "-Dfile.encoding=UTF8", "-Dspring.output.ansi.enabled=ALWAYS", "-Dlogging.level.org.hibernate.SQL=OFF", "-Dlogging.level.org.hibernate.type.descriptor.sql.BasicBinder=OFF", "-jar", "app.jar"]
