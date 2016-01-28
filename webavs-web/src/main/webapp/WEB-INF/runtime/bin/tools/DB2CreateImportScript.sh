# DB2CreateImportScript - Utilitaire permettant de cr�er un script d'importation de donn�es dans des tables DB2.
# 
# Les arguments sont:
# 
# - db2instance     nom de l'instance DB2
# - schema          nom du sch�ma � acc�der
# - path            chemin du r�pertoire contenant les fichiers � importer
# - [commitCount]   le nombre d'insertions avant un commit (optionnel, d�faut=1000)
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.tools.JadeToolDB2CreateImportScript $1 $2 $3 $4

