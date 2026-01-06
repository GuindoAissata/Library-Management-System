@echo off
cd /d "%~dp0"

java --module-path ".\javafx-sdk-21.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp ".;GestionBiblio.jar;lib\sqlite-jdbc-3.50.3.0.jar" App

pause
