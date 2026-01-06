# **Projet de POA M1 2025-2026 :  “Système de Gestion des Emprunts de Bibliothèque (SGEB)**


## **Présentation générale**

Ce projet a été réalisé par Nouannapha PHICHITH et Aissata GUINDO dans le cadre de l'UE Programmation Objet avancée.
Il a été réalisé au cours du semestre 1 (octobre-novembre 2025) du M1 I2D.

L'objectif de ce projet est de développer une application logicielle en Java pour 
informatiser et gérer les opérations d'emprunt et de retour de documents au sein d'une 
bibliothèque. Le système permet aux administrateurs de la bibliothèque (i.e. l'utilisateur) de gérer 
les adhérents, les documents et les transactions d'emprunt de manière efficace. 

## **1. Pré-requis**

Votre machine doit posséder un environnement JRE (Java) pour pouvoir compiler et exécuter le programme.

## **2. Contenu de l'archive**

- projet.jar : JAR exécutable contenant toutes les classes du projet
- lib/ : bibliothèque nécessaires (JavaFX et SQLite JDBC)
- run.sh : script pour compiler et lancer automatiquement le projet
- README.md : ce fichier que vous êtes en train de lire
- Database/ : contient la base de données du projet
- src/ : contenant le code de notre projet
- user.pdf : guide d'utilisation de l'application


## **3. Installation**


Tout d'abord, merci d'avoir téléchargé mon programme ! Pour pouvoir l'exécuter, c'est très simple. Vous avez trois options. Ouvrez un invite de commande (CMD) ou un terminal. 

### **1/ Appuyez sur le fihcier .bat qui lance automatiquement l'application**

Double cliquer sur le dossier 

ou faire sur le terminal

cd "C:\Projet Java M1\Gestion des Emprunts_Biblio"
cmd /c Aissata_Nouannapha.bat


### **2/ Lancer l'application depuis le jar**

java --module-path ".\javafx-sdk-21.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp ".;GestionBiblio.jar;lib\sqlite-jdbc-3.50.3.0.jar" App


### **3/ Lorsqu'on modifie le code faire :** 

cd "C:\Projet Java M1\Gestion des Emprunts_Biblio"

##### Supprimer l'ancien dossier de classes compilées
if (Test-Path .\out) { Remove-Item -Recurse -Force .\out }

##### Supprimer l'ancien jar
if (Test-Path .\GestionBiblio.jar) { Remove-Item .\GestionBiblio.jar }

##### Récompiler 
mkdir out

javac -encoding UTF-8 `
  --module-path ".\javafx-sdk-21.0.2\lib" `
  --add-modules javafx.controls,javafx.fxml `
  -d out `
  (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName })

##### Vérifier que des .class sont crées 
  dir .\out

##### Recréer le JAR
& "$env:USERPROFILE\AppData\Roaming\Code\User\globalStorage\pleiades.java-extension-pack-jdk\java\latest\bin\jar.exe" cfm GestionBiblio.jar MANIFEST.MF -C out .

##### puis 
dir GestionBiblio.jar













cd "C:\Projet Java M1\Gestion des Emprunts_Biblio"

javac -encoding UTF-8 `
  --module-path ".\javafx-sdk-21.0.2\lib" `
  --add-modules javafx.controls,javafx.fxml `
  -d out `
  (Get-ChildItem -Recurse src -Filter *.java | ForEach-Object { $_.FullName })




### **3/ Placez-vous à la racine du projet**
Ouvrez un invite de commande (CMD) ou un terminal. 

Par exemple :

/home/user/PROJET_SGEB

où le dernier répertoire de cette adresse est celui dans lequel sont notamment contenus le fichier projet.jar, run.sh et le dossier src

Ensuite, vous avez lancer l'application. Entrez les 2 instructions suivantes (la 1re pour rendre le fichier run.sh exécutable, et la deuxième lancer l'application
avec toutes les dépendances):
      chmod +x run.sh
      ./run.sh


Normalement, l'application est lancée ! Vous pouvez suivre le guide d'utilisateur pour comprendre comment utiliser notre application (fichier user.pdf).



## requête 

UPDATE Emprunt SET Date_Emprunt = '2025-11-30', Date_RetourPrevue = '2025-12-14' WHERE ID_Emprunt = 20251130025514;









