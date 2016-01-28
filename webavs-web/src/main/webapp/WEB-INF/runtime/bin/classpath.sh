#!/bin/sh

echo "************************************************************"
echo "******************** calling classpath *********************"
echo "************************************************************"

# --- Test if Batch DIR argument specified
if [ "$1" ]; then
   if [ -d $1 ] ; then
       # --- Batch DIR argument specified so using absolute path
       BATCH_LIBRARY_PATH="$1/lib"
   else
       echo "ERROR : WRONG BATCH DIR argument specified"
       exit
   fi
else
   # --- Batch DIR argument no specified so using relative path
   BATCH_LIBRARY_PATH="../lib"
fi


# --- init variables

SEP=:
CLASSPATH=.

# --- test Plateform and set specific variables

case $(uname) in
   AIX)
      echo "Info   : platform AIX detected ..."; echo
      if test -e /opt/WebSphere/AppServer ; then
         SERVER_PATH=/opt/WebSphere/AppServer
                 JAVA_HOME=${SERVER_PATH}/java
      else
         JAVA_HOME=/usr/java14
      fi
      SQLLIB=/usr/opt/db2_08_01/java
      export LIBPATH=/usr/lib:/lib:/usr/opt/db2_08_01/lib/
      ;;
   Linux)
      echo "Info   : platform Linux detected ..."; echo
      if test -e /opt/WebSphere/AppServer ; then
      SERVER_PATH=/opt/WebSphere/AppServer
          JAVA_HOME=${SERVER_PATH}/java
      else
         JAVA_HOME=/usr/lib/java/jre
      fi

      SQLLIB=/opt/IBM/db2/V8.1/java
      export LD_LIBRARY_PATH=/opt/IBM/db2/V8.1/lib
      export LC_CTYPE=ISO08859_1

      ;;
   *)
      echo "Error : Illegal platform (`uname`)  --> EXIT"
      exit 2
      ;;
esac

# set common variable


# --- load classpath for sqllib jars

for file in `ls $SQLLIB/*`
   do CLASSPATH=${CLASSPATH}${SEP}${file}
done


# --- load classpath for application jars

#for file in `ls $BATCH_DIR/$VERSION/lib/*.*`
for file in `ls ${BATCH_LIBRARY_PATH}/*.*`
   do CLASSPATH=${CLASSPATH}${SEP}${file}
done

if test -e ${SERVER_PATH}/lib/j2ee.jar ; then
  CLASSPATH=${CLASSPATH}${SEP}${SERVER_PATH}/lib/j2ee.jar
fi


# --- print results

echo "JAVA_HOME = ${JAVA_HOME}"
echo
echo "SQLLIB = ${SQLLIB}"
echo
echo "CLASSPATH = ${CLASSPATH}"
echo
if [[ $(uname) = "AIX" ]] ; then
   echo "LIBPATH = ${LIBPATH}"
else
   echo "LD_LIBRARY_PATH = ${LD_LIBRARY_PATH}"
fi
echo
echo "DB2INSTANCE = ${DB2INSTANCE}"
echo
echo "*****************************************************************"