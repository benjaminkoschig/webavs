package globaz.cygnus.utils;

import globaz.cygnus.exceptions.RFBusinessException;
import globaz.framework.util.FWLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * Classe utilitaire permettant aux RFM de logger des infos de debug en DB.
 * Les infos sont n�anmoins loggu�es avec un niveau INFO. Chaque instanciation de RFLogToDB cr�� un nouveau
 * {@link globaz.framework.util.FWLog}. Puis chaque utilisation de cet objet cr�� un nouveau
 * {@link globaz.framework.util.FWMessage} qui sera li� au FWLog.
 * L'id�al est de conserver le m�me RFLogToDB tout au long d'un processus ou d'une op�ration.
 * 
 * @author vch
 */
public final class RFLogToDB {
    private final BSession aSession;
    private FWLog log;

    /**
     * Constructeur avec session pour ajouter les logs. Ce constructeur ne cr�� pas de {@link FWLog} en DB.
     * 
     * @param aSessionToLog La session doit �tre non <code>null</code> et connect�e sinon on jette une exception
     * @throws IllegalArgumentException si la session est <code>null</code> ou non connect�e
     */
    public RFLogToDB(BSession aSessionToLog) {
        aSession = aSessionToLog;
        if (aSession == null) {
            throw new IllegalArgumentException("Problem in RFLogToDB constructor: session is null.");
        }
        if (!aSession.isConnected()) {
            throw new IllegalArgumentException("Problem in RFLogToDB constructor: session is not connected.");
        }
    }

    private void createFWLogIfNotExist() throws RFBusinessException {
        if (log != null) {
            return;
        }
        log = new FWLog();
        log.setSession(aSession);
        try {
            log.add();
        } catch (Exception e) {
            JadeLogger.error(this, "RFLogToDB couldn't create FWLog: " + e.toString());
            throw new RFBusinessException("RFLogToDB couldn't create FWLog: " + e.toString(), e);
        }
    }

    /**
     * 
     */

    private static String compactMessageToMaxLength(String message, int length) {
        if (message.length() <= length) { // it's ok :)
            return message;
        }
        int halfResultLength = (int) Math.floor((length - 2.0f) / 2.0f);
        String result = message.substring(0, halfResultLength) + ".."
                + message.substring(message.length() - halfResultLength);
        return result;
    }

    /**
     * Ajoute en DB un message li�. Lors de la premi�re utilisation de cette m�thode, un {@link FWLog} est cr�� en DB.
     * 
     * @param message un message � logger en DB. Ne doit jamais �tre <code>null</code> ou vide (<code>length==0</code>).
     * @param logSource l'endroit du code d'o� on appelle cette m�thode. Cette information est utilis�e pour indiquer �
     *            quel moment/classe/m�thode
     *            on a logg� l'info. Ne doit jamais �tre <code>null</code> ou vide (<code>length==0</code>).
     * @return cet objet. Permet de 'cha�ner' des appels aux m�thodes de {@link RFLogToDB}. Jamais <code>null</code>.
     * @throws RFBusinessException s'il y a un probl�me de cr�ation en DB du FWLot ou du FWMessage
     * @throws IllegalStateException si la session est en erreur (BSession.hasErrors)
     * @throws IllegalArgumentException si le message est null ou vide, ou si le logSource est null ou vide.
     */
    public RFLogToDB logInfoToDB(String message, String logSource) throws RFBusinessException {
        // TODO: message maximum 255
        // check des param�tres: on va pas bosser avec des trucs pourraves...
        if (JadeStringUtil.isBlank(message)) {
            throw new java.lang.IllegalArgumentException("[message] parameter passed to " + RFLogToDB.class.getName()
                    + ".logInfoToDB is " + message);
        }
        if (JadeStringUtil.isBlank(logSource)) {
            throw new java.lang.IllegalArgumentException("[logSource] parameter passed to " + RFLogToDB.class.getName()
                    + ".logInfoToDB is " + message);
        }
        if (aSession.hasErrors()) {
            throw new IllegalStateException("RFLogToDB.logInfoToDB: cannot log message [" + message + "] from source ["
                    + logSource + "] since BSession has errors: " + aSession.getErrors().toString());
        }
        logSource = compactMessageToMaxLength("RFM - " + logSource, 39); // logsource est max 40 chars en DB
        message = compactMessageToMaxLength(message, 254); // message est max 255 chars en DB
        createFWLogIfNotExist();
        log.logMessage(message, FWMessage.INFORMATION, logSource);
        // FWLog.logMessage() met � jour FWLog, mais ne fait pas l'update en DB, donc on le force ici.
        try {
            log.update();
        } catch (Exception e) {
            throw new RFBusinessException("couldn't update log: " + e.toString(), e);
        }
        return this;
    }
}
