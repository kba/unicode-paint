#!/bin/zsh
out=$1
while read line;do
  code=$(echo $line|grep -a -o '0x[0-9A-F]\{4\}'|sed 's/0x//')
  if [[ ! -z $code ]];then
    echo "\u$code$line" 
  else
    echo $line
  fi
done
