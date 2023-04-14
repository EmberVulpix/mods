#!/bin/bash

mods="autoadvance camera core gearex lootfilter noloading nonews pandora pocketshop showping stayonline teleport tweaks"

mkdir -p ./target/code-mods/
chmod -R 777 ./target/code-mods/
rm -rf ./target/code-mods/*.jar

for i in $mods;
do
cp ./$i/target/*.jar ./target/code-mods/
cp ./$i/*.yml ./target/code-mods/
done

