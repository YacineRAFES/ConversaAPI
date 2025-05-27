FROM maven:3.8.3-jdk-11

# Copie du fichier de configuration settings.xml pour définir les paramètres Maven
#COPY ./settings.xml /root/.m2/settings.xml

# Copie du fichier pom.xml de l'application dans le conteneur
#COPY ../../app/pom.xml ./app
# Configuration du répertoire de travail dans le conteneur
# WORKDIR /app

# Installation des dépendances Maven
# RUN mvn dependency:go-offline
# sources de l'application dans le conteneur
#COPY ../app/src ./src

# Compilation de l'application
RUN mvn clean package

# Copie du fichier WAR généré dans le dossier deployerFile du serveur Tomcat
RUN cp ./target/*.war /deployerFile