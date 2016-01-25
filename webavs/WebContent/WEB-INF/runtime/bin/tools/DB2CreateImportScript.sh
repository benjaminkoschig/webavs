# DB2CreateImportScript - Utilitaire permettant de créer un script d'importation de données dans des tables DB2.
# 
# Les arguments sont:
# 
# - db2instance     nom de l'instance DB2
# - schema          nom du schéma à accéder
# - path            chemin du répertoire contenant les fichiers à importer
# - [commitCount]   le nombre d'insertions avant un commit (optionnel, défaut=1000)
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolDB2CreateImportScript $1 $2 $3 $4

