FROM tomcat:10.1.34-jdk21-temurin

#ENV SPRING.DATASOURCE.URL=jdbc:mysql://mysql:3306/springboot
#ENV SPRING.DATASOURCE.USERNAME=root
#ENV SPRING.DATASOURCE.PASSWORD=root

#Exposer le port 9000
EXPOSE 8080

#Copie du ficher JAR recuperer de l'artefact de votre projet dans le conteneur
COPY ConversaAPI-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/conversaapi.war

#Commande pour exécuter le fichier JAR
CMD ["java", "-jar", "ConversaAPI-1.0-SNAPSHOT.war"]