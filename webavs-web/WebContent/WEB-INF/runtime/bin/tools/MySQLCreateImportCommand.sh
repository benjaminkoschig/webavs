# MySQLCreateImportCommand - Utilitaire permettant de créer la ligne de commande pour l'importation de données dans des tables MySQL.
# 
# Les arguments sont:
# 
# - dbName          nom de la base de données
# - user            nom de l'utilisateur de MySQL
# - password        mot de passe de l'utilisateur de MySQL
# - path            chemin du répertoire contenant les fichiers à importer
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolMySQLCreateImportCommand %1 %2 %3 %4

