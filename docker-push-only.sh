#!/bin/bash

mvn clean install -DskipTests=true -Dfile.encoding=UTF8 -f pom.xml

echo "================================== tuanloc/project:auth-service =================================="
cd hugio-auth-service
echo "BUILD IMAGE"
docker build . -t tuanloc/project:auth-service
echo "PUSH IMAGE"
docker push tuanloc/project:auth-service
echo "REMOVE IMAGE"
docker rmi tuanloc/project:auth-service
cd ..

echo "================================== tuanloc/project:product-service =================================="
cd hugio-product-service
echo "BUILD IMAGE"
docker build . -t tuanloc/project:product-service
echo "PUSH IMAGE"
docker push tuanloc/project:product-service
echo "REMOVE IMAGE"
docker rmi tuanloc/project:product-service
cd ..

echo "================================== tuanloc/project:user-service =================================="
cd hugio-user-service
echo "BUILD IMAGE"
docker build . -t tuanloc/project:user-service
echo "PUSH IMAGE"
docker push tuanloc/project:user-service
echo "REMOVE IMAGE"
docker rmi tuanloc/project:user-service
cd ..

mvn clean -f pom.xml