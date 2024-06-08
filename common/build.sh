#!/bin/bash

# mvn dependency:resolve

echo -e '\n\n >> Set java protoc plugin into PATH \n\n'

export PATH="$PATH:./protoc-plugin/"

echo -e '\n\n >> Generate Java code \n\n'

./protoc/bin/protoc -I=./proto-lib/src/main/proto --java_out=./proto-lib/src/main/java --grpc-java_out=./proto-lib/src/main/java ./proto-lib/src/main/proto/*.proto

echo -e '\n\n >> Convert javax package to jakarta \n\n'

python ./replace-code.py
