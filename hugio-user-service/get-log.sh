pod_name="auth-service-8684bbfd49-m8zzc"
namespace="tdtu-project-1"
file_path="data/log"

kubectl -n ${namespace} cp ${pod_name}:${file_path} .

sleep 5m