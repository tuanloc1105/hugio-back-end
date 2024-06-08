#!/bin/bash

CURRENT_DIRECTORY=$(pwd)

mvn dependency:resolve

protoc -I=./tdtu-proto-lib/src/main/proto --java_out=./tdtu-proto-lib/src/main/java ./tdtu-proto-lib/src/main/proto/*.proto

mvn clean install -DskipTests=true -Dfile.encoding=UTF8 -f pom.xml

cd tdtu-proto-lib/

git reset .

git restore .

cd ${CURRENT_DIRECTORY}
