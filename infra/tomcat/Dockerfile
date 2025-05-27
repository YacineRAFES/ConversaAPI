FROM tomcat:10.1.34-jdk21-temurin

# Copy the WAR file into the container
# à ne pas faire car target est supprimé dans le cadre d'un clean maven
# COPY target/myapp.war /usr/local/tomcat/webapps/
RUN mkdir /usr/local/tomcat/webapps/manager

RUN cp -r /usr/local/tomcat/webapps.dist/manager /usr/local/tomcat/webapps/manager

# Expose Tomcat Port
EXPOSE 8080

# Set Tomcat as Default Command
CMD ["catalina.sh", "run"]