# **“Système de Gestion des Emprunts de Bibliothèque ** Projet POO-Avancé M1 2025-2026 :


## **Présentation générale**

Ce projet a été réalisé par **Nouannapha PHICHITH** et **Aissata GUINDO** dans le cadre de l’UE **Programmation Objet Avancée**.  
Il a été développé durant le semestre 1 (octobre–novembre 2025) du **Master 1 I2D**.

L’objectif est de concevoir une application logicielle en **Java** permettant
d’informatiser et de gérer les opérations d’emprunt et de retour de documents au sein
d’une bibliothèque.

Le système permet à l’administrateur de :
- gérer les adhérents
- gérer les documents
- gérer les emprunts et les retours
- gérer les **retards et pénalités**
 

## **1. Pré-requis**

- Java (JRE / JDK) installé
- SQLite installé (commande `sqlite3`)
- Système Linux ou Windows (avec terminal)

## **2. Contenu de l'archive**

- `GestionBiblio.jar` : JAR exécutable de l’application
- `lib/` : bibliothèques nécessaires (JavaFX, SQLite JDBC)
- `run.sh` : script permettant de lancer automatiquement l’application
- `README.md` : documentation du projet
- `Database/` : dossier devant contenir la base de données
- `src/` : code source Java
- `user.pdf` : guide d’utilisation de l’application
- sql : contient le shema sql à éxécuter pour créer ses tables 

## **3. Base de données (IMPORTANT)**

### Emplacement obligatoire
La base de données **doit impérativement** se trouver dans le dossier : Database/bibliothque.db


Le nom `bibliotheque.db` est **obligatoire**, car il est utilisé directement
dans les classes de connexion de l’application.


### Création de la base de données

Depuis la racine du projet :

```bash
mkdir -p Database
sqlite3 Database/bibliotheque.db

Ensuite créer les tables nécessaires : 
.read sql/shema.sql
```

### Identifiant et Mot de passee (OBLIGATOIRE)


Avant de pouvoir utiliser l’application, **un identifiant et un mot de passe** doivent
être insérés dans la table prévue pour l’authentification.

INSERT INTO Gestionnaire (Login,MotDePasse)
VALUES ('admin', 'mdp');


Sans cet enregistrement, l’application ne pourra pas être utilisée.
**Remarque** : les mots de passe sont stockés en clair (choix volontaire dans le cadre
d’un projet scolaire).


## 4. Lancement de l'application


Tout d'abord, merci d'avoir téléchargé mon programme ! Pour pouvoir l'exécuter, c'est très simple. Vous avez trois options. Ouvrez un invite de commande (CMD) ou un terminal. 

### Option 1 - Script de lancement(recommandé)

Double cliquer  sur le fihcier run.bat qui lance automatiquement l'application

ou faire sur le terminal

cd "C:\Projet Java M1\Gestion des Emprunts_Biblio"
cmd /c run.bat


### Option 2/ Lancer directement le jar

java --module-path ".\javafx-sdk-21.0.2\lib" --add-modules javafx.controls,javafx.fxml -cp ".;GestionBiblio.jar;lib\sqlite-jdbc-3.50.3.0.jar" App


### Option 3/ Recompiler après modification du code(avancé) :

Si vous modifiez le code source:
  Supprimer les ancinnes compilations
  Recompiler les fichier .java
  Recréer le JAR

Commandes detaillées : 

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




### ** Option 4 / Placez-vous à la racine du projet**
Ouvrez un invite de commande (CMD) ou un terminal. 

Par exemple :

/home/user/PROJET_SGEB

où le dernier répertoire de cette adresse est celui dans lequel sont notamment contenus le fichier projet.jar, run.sh et le dossier src

Ensuite, vous avez lancer l'application. Entrez les 2 instructions suivantes (la 1re pour rendre le fichier run.sh exécutable, et la deuxième lancer l'application
avec toutes les dépendances):
      chmod +x run.sh
      ./run.sh

 ## ** 4 Utilisation**
Une fois l'application lancée:

- Connectez-vous avec l'identifiant créé dans la base
- Gérez les documents, les adhérents et les emprunts
- Consultez automatiquement les retards et pénalités

Un guide détaillé est disponible dans le fichier user.pdf

## **5 Licence **
Projet scolaire - libre d'uilisation à des fins pédagogiques


























