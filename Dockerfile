FROM tomcat:10.1.34-jdk21-temurin

#ENV SPRING.DATASOURCE.URL=jdbc:mysql://mysql:3306/springboot
#ENV SPRING.DATASOURCE.USERNAME=root
#ENV SPRING.DATASOURCE.PASSWORD=root

RUN rm -rf /usr/local/tomcat/webapps/ROOT

COPY ConversaAPI-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/api.war

#Exposer le port 9000
EXPOSE 9000