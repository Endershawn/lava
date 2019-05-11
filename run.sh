#!/bin/sh

./gradlew build
sh install.sh
./gradlew runClient
