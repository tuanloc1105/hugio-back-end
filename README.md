# HUGIO BACK END SERVICE

## Configuration before running project

Requirement:
1. JDK 17
2. Maven > 3.8.5
3. Mysql instance
4. Docker
5. Helm

Build project in one times

Go to root folder (hugio-back-end) and run this command
```
mvn clean install -DskipTests=true -Dfile.encoding=UTF8 -f pom.xml
```

After the maven build finished, choose a service to run, there are 5 services:

- [hugio-auth-service](hugio-auth-service)
- [hugio-inventory-service](hugio-inventory-service)
- [hugio-order-service](hugio-order-service)
- [hugio-product-service](hugio-product-service)
- [hugio-user-service](hugio-user-service)

Go to the folder of service you want to run. For instance,

```
cd hugio-auth-service

mvn spring-boot:run
```

### Running with Kubernetes (K8S)

This project also can be run on K8S

For example, you want to run [hugio-auth-service](hugio-auth-service) on K8S

```
cd hugio-auth-service

docker build . -t $dockerImage:$dockerTag

helm upgrade -i --set image.name=$dockerImage,image.tag=$dockerTag,replica=$k8sReplica -n $k8sNamespace auth-service ./helm_chart
```