FROM tuanloc/ubuntu:java17.0.8

#WORKDIR /app
#COPY . /app
#COPY ./target/inventory-service.jar /app/app.jar
#COPY . .
COPY ./target/app.jar app.jar
COPY ./logback.xml logback.xml

# ENTRYPOINT ["java", "-Dfile.encoding=UTF8", "-Dlogging.config=/logback.xml", "-Dlogging.level.org.hibernate.SQL=OFF", "-Dlogging.level.org.hibernate.type.descriptor.sql.BasicBinder=OFF", "-jar", "app.jar"]
ENTRYPOINT ["java", "-Dfile.encoding=UTF8", "-Dlogging.config=/logback.xml", "-jar", "app.jar"]
