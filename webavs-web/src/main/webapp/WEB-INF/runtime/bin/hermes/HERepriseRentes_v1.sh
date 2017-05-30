#!/bin/sh
# ----------------------------------------------------------------------------------
# Script de gestion des batch java (JADE) pour Web@AVS
# ----------------------------------------------------------------------------------
# Note: - En fonction de la caisse , adapter les variables
#       - VERSION
# !!!!!!!!!!!!!! SCRIPT PARACHUTE PERMETTANT UN RETOUR AU TRAITEMENT INITIAL MODIFIE POUR LA v.1.15.01 !!!!!!!!!!!!!!!!!!!!!!
# !!!!!!!!!!!!!! A SUPPRIMER POUR LA VERSION 1.15.02 SI PAS DE PROBLEMES !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
#!!!!!!!!!!!!!!! VOIR EN INTERNE: http://dev-confluence.ju.globaz.ch/pages/editpage.action?pageId=21235949 !!!!!!!!!!!!!!!!!!
#===================================================================================
### Start of script constant definition ###
### =================================== ###
# --- get user profile
. ~/.profile

# --- Information caisse AVS ---
VERSION="$1"

# --- Information Produit ---
PRODUCT="webavs"
APP="HERepriseV2"

# --- Autres VARIABLES
APPUSER=`whoami`
APPHOME=${HOME}
BATCH_DIR="$APPHOME/batch"
MODULE="HERMES"
NICELEVEL=10

LOGFILE="$BATCH_DIR/$VERSION/logs/${MODULE}.log"

# --- Test presence JAVA_HOME ---
if ! test -d $JAVA_HOME
then
   echo "Looking for JAVA_HOME : $JAVAHOME"
   echo "but didn't found the directory so --> EXIT"
   exit 2
fi
# --- Test if DIR exists
if ! test -d ${BATCH_DIR}/${VERSION}
then
   echo "Looking for ${BATCH_DIR}/${VERSION}"
   echo "but didn't found the script so --> EXIT"
   echo "If you are in a cluster environnement, the DB2 instance is maybe on the other node."
   exit 2
fi

# --- Process definition
JAVA_EXTDIR="${BATCH_DIR}/${VERSION}/lib:$JAVA_HOME/lib/ext:/share/lib/db2"
CALL_JAVA_PROCESS_1="$JAVA_HOME/bin/java -DPRODUCT=${PRODUCT}_${MODULE} -Djava.awt.headless=true -Djava.ext.dirs=${JAVA_EXTDIR} -Xms512m -Xmx2048m globaz.hermes.zas.HEReprise $2 $3 $4 $5"

CALL_JAVA_PROCESS_2="$JAVA_HOME/bin/java -DPRODUCT=${PRODUCT}_${MODULE} -Djava.awt.headless=true -Djava.ext.dirs=${JAVA_EXTDIR} -Xms512m -Xmx2048m globaz.corvus.process.REImporterCIProcessV1 $4 $5"

echo "Running batch : $APP"
TIMESTAMP=$(date +%Y%m%d%H%M%S)
if [[ $(whoami) = root ]] ; then
        echo "Script must not bi run as root -> EXIT"
        exit
else
    find $BATCH_DIR/$VERSION/logs -name ${MODULE}.log.* -ctime +15 -exec rm {} \;
    if [ -f $LOGFILE ]; then cp $LOGFILE ${LOGFILE}.$TIMESTAMP; fi
    nice -n $NICELEVEL $CALL_JAVA_PROCESS_1 > $LOGFILE 2>&1
	nice -n $NICELEVEL $CALL_JAVA_PROCESS_2 >> $LOGFILE 2>&1
fi