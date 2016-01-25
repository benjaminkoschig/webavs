#! /bin/sh

# add the libraries to the HERMES_CLASSPATH.
# EXEDIR is the directory where this executable is.
EXEDIR=${0%/*}
DIRLIBS=${EXEDIR}/../../WEB-INF/lib/*.jar
for i in ${DIRLIBS}
do
  if [ -z "$HERMES_CLASSPATH" ] ; then
    HERMES_CLASSPATH=$i
  else
    HERMES_CLASSPATH="$i":$HERMES_CLASSPATH
  fi
done

HERMES_CLASSPATH="${EXEDIR}/../../WEB-INF/classes":$HERMES_CLASSPATH

java -classpath "$HERMES_CLASSPATH:$CLASSPATH" globaz.hermes.print.itext.HEDocumentZas "$@"
