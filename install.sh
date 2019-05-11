#!/bin/sh

MODSDIR="~/.minecraft/mods/"
VERSION=$(grep -E '^version' ./build.gradle | cut -d "'" -f 2)
BASENAME=$(grep -E '^archive' ./build.gradle | cut -d "'" -f 2)
echo "Building '$BASENAME-$VERSION'"
./gradlew build

echo "Installing '$BASENAME' version $VERSION to $MODSDIR"

cp -v ./build/libs/$BASENAME-$VERSION.jar ~/.minecraft/mods/
