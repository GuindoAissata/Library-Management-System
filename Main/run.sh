#!/bin/bash

PATH_TO_FX=lib/javafx/lib

mkdir -p bin

# 1️ : Compiler tous les fichiers
javac -Xlint:unchecked -d bin \
    --module-path $PATH_TO_FX \
    --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web,javafx.swing \
    -cp "lib/sqlite-jdbc-3.50.3.0.jar" \
    $(find Main/src -name "*.java")

# 2️ : Créer / recréer le JAR
rm -rf temp
mkdir -p temp
cp -r bin/* temp/
mkdir -p temp/lib
cp lib/sqlite-jdbc-3.50.3.0.jar temp/lib/
jar cfm nono.jar manifest.txt -C temp .
rm -rf temp

# 3️ : Lancer l’application
java --module-path $PATH_TO_FX \
     --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web,javafx.swing \
     -cp "nono.jar:lib/sqlite-jdbc-3.50.3.0.jar" \
     Interface
