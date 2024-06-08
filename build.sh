#!/bin/bash

clear

ROOT_PROJECT_DIRECTORY=$(pwd)

cd common/

echo -e '\n\n >> Generate Java code \n\n'

./protoc/bin/protoc -I=./proto-lib/src/main/proto --java_out=./proto-lib/src/main/java --grpc-java_out=./proto-lib/src/main/java ./proto-lib/src/main/proto/*.proto

echo -e '\n\n >> Convert javax package to jakarta \n\n'

python ./replace-code.py

cd ${ROOT_PROJECT_DIRECTORY}

echo -e '\n\n >> Build maven \n\n'

mvn clean install -DskipTests=true -Dfile.encoding=UTF8 -f pom.xml
