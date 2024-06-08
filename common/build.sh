#!/bin/bash

clear

CURRENT_DIRECTORY=$(pwd)

mvn dependency:resolve

protoc -I=./tdtu-proto-lib/src/main/proto --java_out=./tdtu-proto-lib/src/main/java --grpc-java_out=./tdtu-proto-lib/src/main/java ./tdtu-proto-lib/src/main/proto/*.proto

mvn clean install -DskipTests=true -Dfile.encoding=UTF8 -f pom.xml

cd tdtu-proto-lib/

rm -rf ./tdtu-proto-lib/src/main/java/vn/com/hugio/grpc/

cd ${CURRENT_DIRECTORY}
