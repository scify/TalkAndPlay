#! /bin/bash
cd "$(dirname "$0")"
NEW_JAR=`find . -name '*talkandplay-*dependencies.jar'`
if [ ! -z $NEW_JAR ]
then
  cp $NEW_JAR "./talkandplay.jar"
  rm $NEW_JAR
fi
java -jar talkandplay.jar &