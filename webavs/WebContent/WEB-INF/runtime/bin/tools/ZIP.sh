# ZIP - Utilitaire permettant de compresser un fichier
#
# Les arguments sont:
#
# - inputName le nom du fichier à compresser
# - zipName   le nom du fichier ZIP (optionnel)
# 
java -Djava.ext.dirs=../../lib globaz.jade.client.zip.JadeZip $1 $2

