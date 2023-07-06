c=$(date -d "$b 0 min" "+%Y-%m-%d %H:%M:%S")

git add -A
git commit -m "Update code ${c}"
git push origin master
echo "=============================== DONE ==============================="

#sleep 5m