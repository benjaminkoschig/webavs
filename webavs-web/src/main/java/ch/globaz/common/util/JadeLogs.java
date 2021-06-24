package ch.globaz.common.util;

import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import org.slf4j.Logger;

import java.util.Arrays;

/**
 * Classe permettant  de loguer les erreurs Jade Thread dans les Logs applicatifs et de les supprimer afin de continuer les
 * accès et traitement des sessions Jade (qui sont bloqués en cas d'erreur dans la Jade Thread).
 */
public class JadeLogs {

    private JadeLogs(){

    }

    /**
     * Logue les messages de la Jade Thread et supprime les log en JadeThread.
     * @param methodeSource : méthode appelante.
     * @param logger : Logger applicatif.
     */
    public static void logAndClear(String methodeSource, Logger logger){
        if(JadeThread.logHasMessages()){
            logger.info(methodeSource);
            logJadeThreadMessages(logger);
            JadeThread.logClear();
        }
    }

    /**
     * Logue les messages de la Jade Thread dans le Log applicatif.
     */
    private static void logJadeThreadMessages(Logger logger) {

        JadeBusinessMessage[] messages = JadeThread.logMessages();
        if(messages != null){
            Arrays.stream(messages).forEach(message ->  logBusinessMessage(logger, message));
        }
    }

    /**
     * Logue les messages de la Jade Thread dans le log applicatif suivant le level du message
     * @param logger : Logger applicatif.
     * @param message : message à logger
     */
    private static void logBusinessMessage(Logger logger, JadeBusinessMessage message) {
        if(JadeBusinessMessageLevels.ERROR == message.getLevel()){
            logger.error(message.getContents(null));
        }else if(JadeBusinessMessageLevels.WARN == message.getLevel()) {
            logger.warn(message.getContents(null));
        }else if(JadeBusinessMessageLevels.INFO == message.getLevel()){
            logger.info(message.getContents(null));
        }else{
            logger.info("Unknow level : {}", message.getContents(null));
        }
    }
}
