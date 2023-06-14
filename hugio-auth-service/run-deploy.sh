docker_registry="tuanloc/project"
app_name="auth-service"
namespace="tdtu-project-1"
server_port=8000
grpc_port=9000
pvc="log-pvc"

echo "============================== DEPLOY ${docker_registry}:${app_name} =============================="

echo ">>>>>>>>>>>>>>>>>>BUILD MAVEN<<<<<<<<<<<<<<<<<<"
mvn clean install -DskipTests=true -Dfile.encoding=UTF8 -f pom.xml || echo 'No maven'

echo ">>>>>>>>>>>>>>>>>>BUILD IMAGE<<<<<<<<<<<<<<<<<<"
cat <<EOF | docker build -t ${docker_registry}:${app_name} . -f -
FROM tuanloc/project:prebuild

# WORKDIR /app
# COPY . /app
COPY ./target/app.jar /app.jar

ENTRYPOINT ["java", "-Dfile.encoding=UTF8", "-Dlogging.level.org.hibernate.SQL=OFF", "-Dlogging.level.org.hibernate.type.descriptor.sql.BasicBinder=OFF", "-jar", "app.jar"]
EOF

echo ">>>>>>>>>>>>>>>>>>PUSH TO REGISTRY<<<<<<<<<<<<<<<<<<"
docker push ${docker_registry}:${app_name}

echo ">>>>>>>>>>>>>>>>>>CLEAN MAVEN<<<<<<<<<<<<<<<<<<"
mvn clean -f pom.xml || echo 'No maven'

echo ">>>>>>>>>>>>>>>>>>CLEAN IMAGE<<<<<<<<<<<<<<<<<<"
docker rmi ${docker_registry}:${app_name}

echo ">>>>>>>>>>>>>>>>>>CLEAN K8S DEPLOY<<<<<<<<<<<<<<<<<<"
kubectl -n ${namespace} delete deployment ${app_name} || echo 'Not found'
kubectl -n ${namespace} delete svc ${app_name}-svc-1 || echo 'Not found'
kubectl -n ${namespace} delete svc ${app_name}-svc-2 || echo 'Not found'

echo ">>>>>>>>>>>>>>>>>>APPLY K8S DEPLOY<<<<<<<<<<<<<<<<<<"
cat <<EOF | kubectl apply -n ${namespace} -f -
apiVersion: apps/v1
kind: Deployment
metadata:
  name: ${app_name}
spec:
  selector:
    matchLabels:
      app: ${app_name}
  replicas: 1
  template:
    metadata:
      labels:
        app: ${app_name}
    spec:
      containers:
        - name: ${app_name}
          securityContext:
            runAsUser: 0
          volumeMounts:
            - mountPath: /data/log
              name: log
          image: ${docker_registry}:${app_name}
          imagePullPolicy: Always
          ports:
            - containerPort: ${server_port}
            - containerPort: ${grpc_port}
          env:
            - name: MYSQL_DB_URL
              value: "jdbc:mysql://172.19.0.1:3306/hugio_user_db?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false"
            - name: MYSQL_DB_USERNAME
              value: "root"
            - name: MYSQL_DB_PASSWORD
              value: "123456"
      tolerations:
        - key: node-role.kubernetes.io/control-plane
          operator: Exists
          effect: NoSchedule
      volumes:
        - name: log
          hostPath:
            path: /app/data/log
#          persistentVolumeClaim:
#            claimName: ${pvc}
---
apiVersion: v1
kind: Service
metadata:
  name: ${app_name}-svc-1
  labels:
    app: ${app_name}
spec:
  type: NodePort
  selector:
    app: ${app_name}
  ports:
    - protocol: "TCP"
      name: http
      port: ${server_port}
      targetPort: ${server_port}
---
apiVersion: v1
kind: Service
metadata:
  name: ${app_name}-svc-2
  labels:
    app: ${app_name}
spec:
  clusterIP: None
  selector:
    app: ${app_name}
  ports:
    - port: ${grpc_port}
      targetPort: ${grpc_port}
EOF

# sleep 5m