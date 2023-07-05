#!/bin/bash

tag=$(date -d "$b 0 min" "+%Y_%m_%d_%H_%M_%S")
auth="tuanloc/auth-service"
product="tuanloc/product-service"
user="tuanloc/user-service"

mvn clean install -DskipTests=true -Dfile.encoding=UTF8 -f pom.xml

echo "================================== $auth:$tag =================================="
cd hugio-auth-service
echo "BUILD IMAGE"
docker build . -t $auth:$tag
echo "PUSH IMAGE"
docker push $auth:$tag
echo "UPGRADE HELM"
helm upgrade -i --set image.name=$auth,image.tag=$tag -n hugio auth-service ./helm_chart
echo "REMOVE IMAGE"
docker rmi $auth:$tag
cd ..

echo "================================== $product:$tag =================================="
cd hugio-product-service
echo "BUILD IMAGE"
docker build . -t $product:$tag
echo "PUSH IMAGE"
docker push $product:$tag
echo "UPGRADE HELM"
helm upgrade -i --set image.name=$product,image.tag=$tag -n hugio product-service ./helm_chart
echo "REMOVE IMAGE"
docker rmi $product:$tag
cd ..

echo "================================== $user:$tag =================================="
cd hugio-user-service
echo "BUILD IMAGE"
docker build . -t $user:$tag
echo "PUSH IMAGE"
docker push $user:$tag
echo "UPGRADE HELM"
helm upgrade -i --set image.name=$user,image.tag=$tag -n hugio user-service ./helm_chart
echo "REMOVE IMAGE"
docker rmi $user:$tag
cd ..

mvn clean -f pom.xml