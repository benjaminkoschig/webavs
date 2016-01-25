#!/bin/sh
# ----------------------------------------------------------------------------------
# Script de gestion des batch java (JADE) pour Web@AVS
# ----------------------------------------------------------------------------------
# Note: - En fonction de la caisse , adapter les variables
#       - VERSION
#===================================================================================
### Start of script constant definition ###
### =================================== ###
# --- get user profile
. ~/.profile

# --- Information caisse AVS ---
VERSION="$1"

# --- Information Produit ---
PRODUCT="webavs"
APP="downloadbvrftp"

# --- Autres VARIABLES
APPUSER=`whoami`
APPHOME=${HOME}
BATCH_DIR="$APPHOME/batch"
MODULE="OSIRIS"
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
CALL_JAVA_PROCESS="$JAVA_HOME/bin/java -DPRODUCT=${PRODUCT}_${MODULE} -Djava.awt.headless=true -Djava.ext.dirs=${JAVA_EXTDIR} -Xms512m -Xmx1024m globaz.batch.osiris.bvrftp.CABvrFtpAutomaticDownload -user $2 -password $3 -email $4 -idorgane $5"

echo "Running batch : $APP"
TIMESTAMP=$(date +%Y%m%d%H%M%S)
if [[ $(whoami) = root ]] ; then
        echo "Script must not bi run as root -> EXIT"
        exit
else
    find $BATCH_DIR/$VERSION/logs -name ${MODULE}.log.* -ctime +15 -exec rm {} \;
    if [ -f $LOGFILE ]; then cp $LOGFILE ${LOGFILE}.$TIMESTAMP; fi
    nice -n $NICELEVEL $CALL_JAVA_PROCESS > $LOGFILE 2>&1
fi

$JAVA_HOME/bin/java -Xms512m -Xmx512m -cp $CLASSPATH globaz.batch.osiris.bvrftp.CABvrFtpAutomaticDownload -user $2 -password $3 -email $4 -idorgane $5