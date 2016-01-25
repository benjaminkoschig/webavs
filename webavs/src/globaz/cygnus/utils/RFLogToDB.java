package globaz.cygnus.utils;

import globaz.cygnus.exceptions.RFBusinessException;
import globaz.framework.util.FWLog;
import globaz.framework.util.FWMessage;
import globaz.globall.db.BSession;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.log.JadeLogger;

/**
 * Classe utilitaire permettant aux RFM de logger des infos de debug en DB.
 * Les infos sont néanmoins logguées avec un niveau INFO. Chaque instanciation de RFLogToDB créé un nouveau
 * {@link globaz.framework.util.FWLog}. Puis chaque utilisation de cet objet créé un nouveau
 * {@link globaz.framework.util.FWMessage} qui sera lié au FWLog.
 * L'idéal est de conserver le même RFLogToDB tout au long d'un processus ou d'une opération.
 * 
 * @author vch
 */
public final class RFLogToDB {
    private final BSession aSession;
    private FWLog log;

    /**
     * Constructeur avec session pour ajouter les logs. Ce constructeur ne créé pas de {@link FWLog} en DB.
     * 
     * @param aSessionToLog La session doit être non <code>null</code> et connectée sinon on jette une exception
     * @throws IllegalArgumentException si la session est <code>null</code> ou non connectée
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
     * Ajoute en DB un message lié. Lors de la première utilisation de cette méthode, un {@link FWLog} est créé en DB.
     * 
     * @param message un message à logger en DB. Ne doit jamais être <code>null</code> ou vide (<code>length==0</code>).
     * @param logSource l'endroit du code d'où on appelle cette méthode. Cette information est utilisée pour indiquer à
     *            quel moment/classe/méthode
     *            on a loggé l'info. Ne doit jamais être <code>null</code> ou vide (<code>length==0</code>).
     * @return cet objet. Permet de 'chaîner' des appels aux méthodes de {@link RFLogToDB}. Jamais <code>null</code>.
     * @throws RFBusinessException s'il y a un problème de création en DB du FWLot ou du FWMessage
     * @throws IllegalStateException si la session est en erreur (BSession.hasErrors)
     * @throws IllegalArgumentException si le message est null ou vide, ou si le logSource est null ou vide.
     */
    public RFLogToDB logInfoToDB(String message, String logSource) throws RFBusinessException {
        // TODO: message maximum 255
        // check des paramètres: on va pas bosser avec des trucs pourraves...
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
        // FWLog.logMessage() met à jour FWLog, mais ne fait pas l'update en DB, donc on le force ici.
        try {
            log.update();
        } catch (Exception e) {
            throw new RFBusinessException("couldn't update log: " + e.toString(), e);
        }
        return this;
    }
}
