#!/bin/bash

echo "================================== tuanloc/hugio:auth-service =================================="
cd hugio-auth-service
echo "UPGRADE HELM"
helm upgrade -i -n hugio auth-service ./helm_chart
cd ..

echo "================================== tuanloc/hugio:product-service =================================="
cd hugio-product-service
echo "UPGRADE HELM"
helm upgrade -i -n hugio product-service ./helm_chart
cd ..

echo "================================== tuanloc/hugio:user-service =================================="
cd hugio-user-service
echo "UPGRADE HELM"
helm upgrade -i -n hugio user-service ./helm_chart
cd ..

mvn clean -f pom.xml