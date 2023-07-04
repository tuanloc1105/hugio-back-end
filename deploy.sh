#!/bin/bash

mvn clean install -DskipTests=true -Dfile.encoding=UTF8 -f pom.xml

echo "================================== tuanloc/hugio:auth-service =================================="
cd hugio-auth-service
echo "BUILD IMAGE"
docker build . -t tuanloc/hugio:auth-service
echo "PUSH IMAGE"
docker push tuanloc/hugio:auth-service
echo "UPGRADE HELM"
helm upgrade -i -n hugio auth-service ./helm_chart
echo "REMOVE IMAGE"
docker rmi tuanloc/hugio:auth-service
cd ..

echo "================================== tuanloc/hugio:product-service =================================="
cd hugio-product-service
echo "BUILD IMAGE"
docker build . -t tuanloc/hugio:product-service
echo "PUSH IMAGE"
docker push tuanloc/hugio:product-service
echo "UPGRADE HELM"
helm upgrade -i -n hugio product-service ./helm_chart
echo "REMOVE IMAGE"
docker rmi tuanloc/hugio:product-service
cd ..

echo "================================== tuanloc/hugio:user-service =================================="
cd hugio-user-service
echo "BUILD IMAGE"
docker build . -t tuanloc/hugio:user-service
echo "PUSH IMAGE"
docker push tuanloc/hugio:user-service
echo "UPGRADE HELM"
helm upgrade -i -n hugio user-service ./helm_chart
echo "REMOVE IMAGE"
docker rmi tuanloc/hugio:user-service
cd ..

mvn clean -f pom.xml