#!/bin/sh
# ----------------------------------------------------------------------------------
# Script de facturation
# Maintenu par : Stephane Glauser - Globaz S.A.
# Domaine      : Web@AVS - Processus Musca
# Etat         : en TEST
# Deploye sur  : sccvsqua
# Sources      : X:\clients\GLOBAZ\System\Doctechnique\DB2_WAS\scripts\Shell-Scripts\
# ----------------------------------------------------------------------------------
# Historique   :
# 15.02.11 SGL : Creation
# ----------------------------------------------------------------------------------
# Note: -
#===================================================================================
CAT_SYNTAX()
{
cat <<-!
USAGE : $0 <arguments> ...

Required Arguments for script:
     -application  <appl>
     -classname <arg>
     -user <arg>
     -password <arg>
     -email <emailadresse>
     -start|-stop|-status

Example :
   #  $0  -application MUSCA -classname globaz.musca.process.FAInfoRom200GenerationComptabilisationPassageProcess -user user1 -password secret1 -email user@vs.ch  -start
   #  $0  -status
   #  $0  -stop

!
return
}

#===================================================================================
### Parsing Arguments ###
### =================================== ###
if  [[ "$#" = "0" ]];then  CAT_SYNTAX; exit 1 ; fi

while [ "$#" -gt "0" ]
do
   case $1 in
      -application)
           shift
           APPLICATION="application=$1"
           ;;
      -classname)
           shift
           CLASSNAME="classname=$1"
           ;;
      -user)
           shift
           USER="user=$1"
           ;;
      -password)
           shift
           PASSWORD="password=$1"
           ;;
      -email)
           shift
           EMAIL="eMailAddress=$1"
           ;;
      -start|-stop|-status)
            ACTION=$1
            ;;
      *|-h|--help)
           MSG "Syntax Error"
           CAT_SYNTAX
           exit 1
           ;;
   esac
   shift
done

### Start of script constant definition ###
### =================================== ###

# --- Information Produit ---
. ~/.profile

   PRODUCT="webavs"
   MODULE="muscat-factjour"

# --- Autres VARIABLES
INSTANCE="jobadm1"
VERSION="${PRODUCT}_ccju_prod"

# --- Autres VARIABLES
APPUSER="$INSTANCE"
APPSCRIPT="BATCHAVS.$INSTANCE"
APPHOME="/job/$INSTANCE"
APP="BATCH AVS pour instance : $INSTANCE"
BATCH_DIR="$APPHOME/batch"
NICELEVEL=10

LOGPATH="$BATCH_DIR/$VERSION/logs"

#   LOGPATH="$SCRIPTPATH/scriptlogs"
   LOGFILE="${VERSION}_${MODULE}.log"
   LOG="${LOGPATH}/${LOGFILE}"

# --- get db user profile
#   . /${APPHOME}/sqllib/db2profile

# --- Test or create script logfile
  if ! test -d $LOGPATH
  then mkdir -p $LOGPATH; fi

case $ACTION in
  -start)
     # --- Test presence JAVA_HOME ---
     if ! test -d $JAVA_HOME
     then
        echo "INFO  : Looking for $JAVA_HOME"
        echo "INFO  : but didn't found the directory so --> EXIT"
        exit 2
     fi
    echo "Starting ${PRODUCT}_${MODULE}"
    CALL_JAVA_PROCESS="${JAVA_HOME}/bin/java -DPRODUCT=${PRODUCT}_${MODULE} -Djava.awt.headless=true -Dclient.encoding.override=ISO8859-1 -Duser.country=CH -Duser.language=fr -Duser.timezone=Europe/Zurich -Djava.ext.dirs=${BATCH_DIR}/${VERSION}/lib:${JAVA_HOME}/lib/ext:/share/lib/db2:/share/lib/java -Xms512m -Xmx1024m   globaz.globall.tools.GlobazCommandLineJob $APPLICATION $CLASSNAME $USER $PASSWORD $EMAIL"
    TIMESTAMP=$(date +%Y%m%d%H%M%S)
    if [[ $(whoami) = $APPUSER ]] ;
    then
       find $LOGPATH -name ${LOGFILE}.* -ctime +15 -exec rm {} \;
       if [ -f $LOG ]; then cp $LOG ${LOG}.$TIMESTAMP; fi
       echo "INFO  : Script Log Location : $LOG"
       echo "=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=" 2>&1 | tee -a $LOG
       echo "JAVA_HOME = $JAVA_HOME"  2>&1 | tee $LOG
       echo "PRODUCT = $PRODUCT"  2>&1 | tee -a $LOG
       echo "VERSION = $VERSION"  2>&1 | tee -a $LOG
       echo "MODULE = $MODULE"  2>&1 | tee -a $LOG
       echo "APPLICATION = $APPLICATION" 2>&1 | tee -a $LOG
       echo "CLASSNAME = $CLASSNAME" 2>&1 | tee -a $LOG
       echo "USER = $USER" 2>&1 | tee -a $LOG
       echo "PASSWORD = $PASSWORD" 2>&1 | tee -a $LOG
       echo "EMAIL = $EMAIL" 2>&1 | tee -a $LOG
       echo "CALL_JAVA_PROCESS = $CALL_JAVA_PROCESS" 2>&1 | tee -a $LOG
       echo "=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=*=" 2>&1 | tee -a $LOG
       cd  $SCRIPTPATH
       $CALL_JAVA_PROCESS >> $LOG 2>&1 &
    else
       echo "ERROR  : you must log as $APPUSER user --> EXIT"   2>&1 | tee -a $LOG &
       exit
    fi
    ;;

  -stop)
    echo "Stopping ${PRODUCT}_${MODULE}"
    PID=$(ps -u $APPUSER -o pid,args | grep java |egrep "PRODUCT=${PRODUCT}_${MODULE}" | grep -v grep |  awk '{print $1}')
    echo "now killing PID $PID"
    kill -9 $PID
    ;;

  -status)
    echo "Status of ${PRODUCT}_${MODULE}"
       PID=$(ps -u $APPUSER -o pid,args | grep java | egrep "PRODUCT=${PRODUCT}_${MODULE}" | grep -v grep | awk '{print $1}')
    if [ -n "$PID" ]
    then
      echo "  -> ${PRODUCT}_${MODULE} process running (PID=$PID)"
    else
      echo "  -> ${PRODUCT}_${MODULE} process not running"
    fi
    ;;
esac
