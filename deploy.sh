#!/bin/bash

mvn clean install -DskipTests=true -Dfile.encoding=UTF8 -f pom.xml

cd hugio-auth-service
docker build . -t tuanloc/project:auth-service
helm upgrade -i -n default auth-service ./helm_chart
cd..

cd hugio-product-service
docker build . -t tuanloc/project:product-service
helm upgrade -i -n default product-service ./helm_chart
cd..

cd hugio-user-service
docker build . -t tuanloc/project:user-service
helm upgrade -i -n default user-service ./helm_chart
cd..
