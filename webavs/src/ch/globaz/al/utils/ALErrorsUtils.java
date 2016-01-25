package ch.globaz.al.utils;

import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;

public class ALErrorsUtils {

    /**
     * Ajoute les messages passés en paramètre dans la session
     * 
     * @param logMessages
     *            les message à ajouter
     * @throws JadeNoBusinessLogSessionError
     */
    public static void addMessages(JadeBusinessMessage[] logMessages) throws JadeNoBusinessLogSessionError {
        if (logMessages != null) {
            for (int i = 0; i < logMessages.length; i++) {
                switch (logMessages[i].getLevel()) {
                    case JadeBusinessMessageLevels.ERROR:
                        JadeThread.logError(logMessages[i].getSource(), logMessages[i].getMessageId(),
                                logMessages[i].getParameters());
                        break;

                    case JadeBusinessMessageLevels.INFO:
                        JadeThread.logInfo(logMessages[i].getSource(), logMessages[i].getMessageId(),
                                logMessages[i].getParameters());
                        break;

                    case JadeBusinessMessageLevels.WARN:
                        JadeThread.logWarn(logMessages[i].getSource(), logMessages[i].getMessageId(),
                                logMessages[i].getParameters());
                        break;

                    default:
                        break;
                }
            }
        }
    }

    /**
     * Récupère les messages contenu dans le log de la session puis vide le log
     * 
     * @return les messages de la session, null si aucun message n'est présent
     */
    public static JadeBusinessMessage[] getMessageFromJadeThreadLog() {
        JadeBusinessMessage[] logMessages = null;

        if (JadeThread.logMessagesFromLevel(JadeBusinessMessageLevels.WARN) != null) {
            logMessages = JadeThread.logMessages();
            JadeThread.logClear();
        }

        return logMessages;
    }
}
