#!/bin/bash

message=$(cat git-commit-message.txt)

c=$(date -d "$b 0 min" "+%Y-%m-%d %H:%M:%S")

git add -A
# git commit -m "Update code ${c}"
git commit -m "$message"
git push origin master
echo "=============================== DONE ==============================="

sleep 5s
