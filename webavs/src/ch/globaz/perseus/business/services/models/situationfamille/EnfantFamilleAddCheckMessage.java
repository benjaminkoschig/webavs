package ch.globaz.perseus.business.services.models.situationfamille;

import globaz.globall.db.BSessionUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;

/**
 * Conteneur destiné à contenir les informations de potentiesl messages générés par le JadeThread.
 * Destiné à être sérialisé et retourné via un appel Ajax
 * 
 * @author sce
 * 
 */
public class EnfantFamilleAddCheckMessage {

    public enum MessageType {
        WARN,
        ERROR,
        EXCEPTION,
        OK
    };

    private String message;
    private MessageType msgType;

    // constructeur par défaut requis pour sérialisation GSON
    public EnfantFamilleAddCheckMessage() {
    };

    public EnfantFamilleAddCheckMessage(String message, MessageType msgType) {
        this.message = message;
        this.msgType = msgType;
    }

    public static EnfantFamilleAddCheckMessage ok() {
        return new EnfantFamilleAddCheckMessage("ok", MessageType.OK);
    }

    public static EnfantFamilleAddCheckMessage warn() {

        String msg = computeMessagesFromJadeThread(JadeBusinessMessageLevels.WARN);

        msg += BSessionUtil.getSessionFromThreadContext().getLabel("AJOUT_ENFANT_QUESTION_CONTINUER");

        return new EnfantFamilleAddCheckMessage(msg, MessageType.WARN);
    }

    public static EnfantFamilleAddCheckMessage warnForCopie() {

        String msg = computeMessagesFromJadeThread(JadeBusinessMessageLevels.WARN);

        return new EnfantFamilleAddCheckMessage(msg, MessageType.WARN);
    }

    public static EnfantFamilleAddCheckMessage warnForCopieDemande() {

        String msg = computeMessagesFromJadeThread(JadeBusinessMessageLevels.WARN);

        msg += BSessionUtil.getSessionFromThreadContext().getLabel("AJOUT_ENFANT_COPIE_DEMANDE_INFORMATION");

        return new EnfantFamilleAddCheckMessage(msg, MessageType.WARN);
    }

    public static EnfantFamilleAddCheckMessage error() {

        String msg = computeMessagesFromJadeThread(JadeBusinessMessageLevels.ERROR);

        return new EnfantFamilleAddCheckMessage(msg, MessageType.ERROR);
    }

    public static EnfantFamilleAddCheckMessage exception(String message) {
        return new EnfantFamilleAddCheckMessage(message, MessageType.EXCEPTION);
    }

    public String getMessage() {
        return message;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    private static String computeMessagesFromJadeThread(int level) {

        StringBuilder msgs = new StringBuilder("");

        for (JadeBusinessMessage msg : JadeThread.logMessagesFromLevel(level)) {
            msgs.append(msg.getContents(BSessionUtil.getSessionFromThreadContext().getIdLangue()));
            msgs.append("<br />");
        }

        return msgs.toString();

    }

}
