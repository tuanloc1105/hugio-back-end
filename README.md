# HUGIO BACK END SERVICE

## Configuration before running project

Requirement:
1. JDK 21
2. Maven > 3.9.0
3. Mysql version >= 8

Read [this](./common/tdtu-proto-lib/NOTE.md) before build the project to make sure there is no error.

After the maven build finished, choose a service to run, there are 5 services:

- [hugio-auth-service](hugio-auth-service)
- [hugio-inventory-service](hugio-inventory-service)
- [hugio-order-service](hugio-order-service)
- [hugio-product-service](hugio-product-service)
- [hugio-user-service](hugio-user-service)

Go to the folder of service you want to run. For instance,

```bash
cd hugio-auth-service
mvn spring-boot:run
```

VoTuanLoc - LeMinhDuc
