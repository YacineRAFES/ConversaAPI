FROM mysql:8.0

# Copie le fichier de configuration personnalisé
COPY my.cnf /etc/mysql/conf.d/my.cnf
# Change les permissions pour les configurations
RUN chmod 644 /etc/mysql/conf.d/my.cnf

# Copie les fichiers de sauvegarde dans le conteneur
COPY dump/* /docker-entrypoint-initdb.d/