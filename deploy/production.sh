#!/bin/bash

cd ../hugio-product-service

tag=$(date -d "$b 0 min" "+%Y_%m_%d_%H_%M_%S")
image="tuanloc/product-service"
k8s_replica=1

mvn clean install -DskipTests=true -Dfile.encoding=UTF8 -f pom.xml

echo "================================== $image:$tag =================================="
cd hugio-product-service
echo "BUILD IMAGE"
docker build . -t $image:$tag
echo "PUSH IMAGE"
#docker push $image:$tag
kind load docker-image $image:$tag
echo "UPGRADE HELM"
helm upgrade -i --set image.name=$image,image.tag=$tag,replica=$k8s_replica -n hugio product-service ./helm_chart
echo "REMOVE IMAGE"
docker rmi $image:$tag
cd ..