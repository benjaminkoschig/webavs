# MySQLCreateImportCommand - Utilitaire permettant de cr�er la ligne de commande pour l'importation de donn�es dans des tables MySQL.
# 
# Les arguments sont:
# 
# - dbName          nom de la base de donn�es
# - user            nom de l'utilisateur de MySQL
# - password        mot de passe de l'utilisateur de MySQL
# - path            chemin du r�pertoire contenant les fichiers � importer
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolMySQLCreateImportCommand %1 %2 %3 %4

