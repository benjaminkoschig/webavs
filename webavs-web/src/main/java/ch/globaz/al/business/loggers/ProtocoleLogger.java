package ch.globaz.al.business.loggers;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.log.business.JadeBusinessMessage;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.log.business.renderer.JadeBusinessMessageRenderer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;

import ch.globaz.al.business.exceptions.generation.prestations.ALGenerationException;
import ch.globaz.al.businessimpl.generation.prestations.context.ContextPrestation;

/**
 * Classe permettant de logguer les messages d'erreur/avertissement/information destinés à être afficher sur un document
 * 
 * @author jts
 * 
 */
public class ProtocoleLogger {

    public enum PROTOCOLE_MESSAGE_TYPE {
        ERR,
        INFO,
        WARN
    }

    /**
     * verrou pour le conteneur d'erreur
     */
    private static Object tokenError = new Object();
    /**
     * verrou pour le conteneur d'info
     */
    private static Object tokenInfo = new Object();

    /**
     * verrou pour le conteneur d'avertissement
     */
    private static Object tokenWarning = new Object();

    /**
     * Liste permettant de stocker des valeurs à afficher dans le protocole CSV
     */
    private List<String> csvFields = new ArrayList<>();
    /**
     * Conteneur de messages d'erreur
     */
    private LinkedHashMap<String, ProtocoleLogger> errorsContainer = new LinkedHashMap<String, ProtocoleLogger>();
    /**
     * Conteneur pour les erreurs système
     */
    private List<JadeBusinessMessage> fatalErrorsContainer = new ArrayList<>();
    /**
     * Identifiant de l'élément concerné par les erreurs. Exemple : id de dossier, numéro d'affilié, ...
     */
    private String idItem = null;
    /**
     * Conteneur de messages d'avertissement
     */
    private LinkedHashMap<String, ProtocoleLogger> infosContainer = new LinkedHashMap<String, ProtocoleLogger>();
    /**
     * Liste des messages
     */
    private Collection<JadeBusinessMessage> messages = null;

    /**
     * Nom de l'élément concerné par les erreurs. Exemples : nom de l'allocataire d'un dossier, nom d'un affilié, etc.
     */
    private String nomItem = null;

    /**
     * Conteneur de messages d'avertissement
     */
    private LinkedHashMap<String, ProtocoleLogger> warningsContainer = new LinkedHashMap<String, ProtocoleLogger>();

    /**
     * Constructeur
     */
    public ProtocoleLogger() {
        // DO NOTHING
    }

    /**
     * Initialise un nouveau logger
     * 
     * @param idItem
     *            identifiant
     * @param nomItem
     *            Nom lié à l'élément (allocataire, enfant, etc.)
     * @param csvFields
     *            Liste contenant des valeurs à afficher dans le protocole CSV. peut être null
     * 
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres n'est pas valide
     */
    private ProtocoleLogger(String idItem, String nomItem, List<String> csvFields) throws JadeApplicationException {

        super();

        if (JadeStringUtil.isEmpty(idItem)) {
            throw new ALGenerationException("ProtocoleLogger#GenerationLogger : id is null or empty");
        }

        if (JadeStringUtil.isEmpty(nomItem)) {
            throw new ALGenerationException("ProtocoleLogger#GenerationLogger : nom is null or empty");
        }

        messages = new ArrayList<JadeBusinessMessage>();
        this.idItem = idItem;
        this.nomItem = nomItem;
        this.csvFields = csvFields;
    }

    /**
     * Ajoute le message et le stacktrace d'une exception dans le log des erreurs fatales
     * 
     * @param message
     *            Message à ajouter dans la liste
     */
    public void addFatalError(Exception e) {
        if (e != null) {
            StringBuffer message = new StringBuffer("Message : ").append(e.getMessage()).append("\n");
            message.append("Stacktrace : \n");

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);

            message.append(sw.toString());

            fatalErrorsContainer.add(new JadeBusinessMessage(JadeBusinessMessageLevels.ERROR, ContextPrestation.class
                    .getName(), message.toString()));
        }
    }

    /**
     * Ajoute un message dans le log des erreurs fatales
     * 
     * @param message
     *            Message à ajouter dans la liste
     */
    public void addFatalError(JadeBusinessMessage message) {
        if (message != null) {
            fatalErrorsContainer.add(message);
        }
    }

    /**
     * Ajoute un message dans le log
     * 
     * @param message
     *            Message à ajouter dans la liste
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres n'est pas valide
     */
    public void addMessage(JadeBusinessMessage message) throws JadeApplicationException {

        if (message == null) {
            throw new ALGenerationException("ProtocoleLogger#GenerationLogger : message is null");
        }

        messages.add(message);
    }

    /**
     * @return le conteneur de messages d'erreur
     */
    public Map<String, ProtocoleLogger> getErrorsContainer() {
        return errorsContainer;
    }

    /**
     * Retourne l'instance du logger de messages d'erreurs lié à l'élément <code>idItem</code>
     * 
     * @param idItem
     *            identifiant
     * @param nomItem
     *            Nom lié à l'élément (allocataire, enfant, etc.)
     * @return instance du logger
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres n'est pas valide
     */
    public ProtocoleLogger getErrorsLogger(String idItem, String nomItem) throws JadeApplicationException {
        return this.getErrorsLogger(idItem, nomItem, null);
    }

    /**
     * Retourne l'instance du logger de messages d'erreurs lié à l'élément <code>idItem</code>
     * 
     * @param idItem
     *            identifiant
     * @param nomItem
     *            Nom lié à l'élément (allocataire, enfant, etc.)
     * @param csvFields
     *            Liste contenant des valeurs à afficher dans le protocole CSV. peut être null
     * @return instance du logger
     * @throws JadeApplicationException
     *             Exception levée si idItem est vide ou null
     */
    public ProtocoleLogger getErrorsLogger(String idItem, String nomItem, List<String> csvFields)
            throws JadeApplicationException {

        if (JadeStringUtil.isEmpty(idItem)) {
            throw new ALGenerationException("ProtocoleLogger#getErrorsLogger : idItem is null or empty");
        }

        if (JadeStringUtil.isEmpty(nomItem)) {
            nomItem = "?";
        }

        ProtocoleLogger logger = null;

        synchronized (ProtocoleLogger.tokenError) {
            logger = errorsContainer.get(idItem);

            if (logger == null) {
                logger = new ProtocoleLogger(idItem, nomItem, csvFields);
                errorsContainer.put(idItem, logger);
            }
        }

        return logger;
    }

    /**
     * @return the fatalErrorsContainer
     */
    public List<JadeBusinessMessage> getFatalErrorsContainer() {
        return fatalErrorsContainer;
    }

    /**
     * @return Identifiant de l'élément concerné par les erreurs. Exemple : id de dossier, numéro d'affilié, ...
     */
    public String getIdItem() {
        return idItem;
    }

    /**
     * @return le conteneur contenant les messages d'information
     */
    public Map<String, ProtocoleLogger> getInfosContainer() {
        return infosContainer;
    }

    /**
     * Retourne l'instance du logger de messages d'informations lié à l'élément <code>idItem</code>
     * 
     * @param idItem
     *            identifiant
     * @param nomItem
     *            Nom lié à l'élément (allocataire, enfant, etc.)
     * @return instance du logger
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres n'est pas valide
     */
    public ProtocoleLogger getInfosLogger(String idItem, String nomItem) throws JadeApplicationException {
        return this.getInfosLogger(idItem, nomItem, null);
    }

    /**
     * Retourne l'instance du logger de messages d'informations lié à l'élément <code>idItem</code>
     * 
     * @param idItem
     *            identifiant
     * @param nomItem
     *            Nom lié à l'élément (allocataire, enfant, etc.)
     * @param csvFields
     *            Liste contenant des valeurs à afficher dans le protocole CSV. peut être null
     * @return instance du logger
     * @throws JadeApplicationException
     *             Exception levée si idItem est vide ou null
     */
    public ProtocoleLogger getInfosLogger(String idItem, String nomItem, List<String> csvFields)
            throws JadeApplicationException {

        if (JadeStringUtil.isEmpty(idItem)) {
            throw new ALGenerationException("ProtocoleLogger#getInfosLogger : idItem is null or empty");
        }

        if (JadeStringUtil.isEmpty(nomItem)) {
            nomItem = "?";
        }

        ProtocoleLogger logger = null;

        synchronized (ProtocoleLogger.tokenInfo) {
            logger = infosContainer.get(idItem);

            if (logger == null) {
                logger = new ProtocoleLogger(idItem, nomItem, csvFields);
                infosContainer.put(idItem, logger);
            }
        }

        return logger;
    }

    /**
     * @return liste des messages
     */
    public Collection<JadeBusinessMessage> getMessages() {
        return messages;
    }

    /**
     * Retourne le nom lié au dossier
     * 
     * @return the nomItem
     */
    public String getNomItem() {
        return nomItem;
    }

    /**
     * @return the warningsContainer
     */
    public Map<String, ProtocoleLogger> getWarningsContainer() {

        return warningsContainer;
    }

    /**
     * Retourne l'instance du logger de messages d'avertissement lié à l'élément <code>idItem</code>
     * 
     * @param idItem
     *            identifiant
     * @param nomItem
     *            Nom lié à l'élément (allocataire, enfant, etc.)
     * @return instance du logger
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres n'est pas valide
     */
    public ProtocoleLogger getWarningsLogger(String idItem, String nomItem) throws JadeApplicationException {
        return this.getWarningsLogger(idItem, nomItem, null);
    }

    /**
     * Retourne l'instance du logger de messages d'avertissement lié à l'élément <code>idItem</code>
     * 
     * @param idItem
     *            identifiant
     * @param nomItem
     *            Nom lié à l'élément (allocataire, enfant, etc.)
     * @param csvFields
     *            Liste contenant des valeurs à afficher dans le protocole CSV. peut être null
     * @return instance du logger
     * @throws JadeApplicationException
     *             Exception levée si idItem est vide ou null
     */
    public ProtocoleLogger getWarningsLogger(String idItem, String nomItem, List<String> csvFields)
            throws JadeApplicationException {

        if (JadeStringUtil.isEmpty(idItem)) {
            throw new ALGenerationException("ProtocoleLogger#getWarningsLogger : idItem is null or empty");
        }

        if (JadeStringUtil.isEmpty(nomItem)) {
            nomItem = "?";
        }

        ProtocoleLogger logger = null;

        synchronized (ProtocoleLogger.tokenWarning) {
            logger = warningsContainer.get(idItem);

            if (logger == null) {
                logger = new ProtocoleLogger(idItem, nomItem, csvFields);
                warningsContainer.put(idItem, logger);
            }
        }

        return logger;
    }

    /**
     * Vérifie si des erreurs sont logguées
     * 
     * @return <code>true</code> si le logguer contient des erreurs, <code>false</code> sinon
     */
    public boolean hasError() {
        return errorsContainer.size() > 0;
    }

    /**
     * Vérifie si des erreurs fatales sont logguées
     * 
     * @return <code>true</code> si le logguer contient des erreurs fatales, <code>false</code> sinon
     */
    public boolean hasFatalError() {
        return fatalErrorsContainer.size() > 0;
    }

    /**
     * Vérifie si des infos sont logguées
     * 
     * @return <code>true</code> si le logguer contient des infos, <code>false</code> sinon
     */
    public boolean hasInfo() {
        return infosContainer.size() > 0;
    }

    /**
     * Vérifie si des avertissements sont logguées
     * 
     * @return <code>true</code> si le logguer contient des avertissements, <code>false</code> sinon
     */
    public boolean hasWarning() {
        return warningsContainer.size() > 0;
    }

    private StringBuffer processLog(Map<String, ProtocoleLogger> errorsLogs, String type) {

        JadeBusinessMessageRenderer renderer = JadeBusinessMessageRenderer.getInstance();
        StringBuffer sb = new StringBuffer();

        for (ProtocoleLogger errorsLog : errorsLogs.values()) {

            StringBuffer csvFieldsStr = new StringBuffer();

            if ((errorsLog.csvFields != null) && (errorsLog.csvFields.size() > 0)) {
                for (String item : errorsLog.csvFields) {
                    csvFieldsStr.append(item).append(";");
                }
            }

            for (JadeBusinessMessage message : errorsLog.getMessages()) {
                sb.append(type).append(";");
                sb.append(csvFieldsStr);
                sb.append(errorsLog.getIdItem()).append(";");
                sb.append(errorsLog.getNomItem()).append(";");
                sb.append(renderer.getDefaultAdapter()
                        .renderMessage(message, JadeThread.currentContext().getLanguage()));

                sb.append("\n");
            }
        }

        return sb;
    }

    /**
     * Retire un warningLogger de la liste
     * 
     * @param idItem
     *            identifiant de l'élément à retirer de la liste
     */
    public void removeErrorLogger(String idItem) {
        errorsContainer.remove(idItem);

    }

    /**
     * Retire un infoLogger de la liste
     * 
     * @param idItem
     *            identifiant de l'élément à retirer de la liste
     */
    public void removeInfoLogger(String idItem) {
        infosContainer.remove(idItem);

    }

    /**
     * Retire un warningLogger de la liste
     * 
     * @param idItem
     *            identifiant de l'élément à retirer de la liste
     */
    public void removeWarningLogger(String idItem) {
        warningsContainer.remove(idItem);
    }

    /**
     * Retourne le contenu du logger (WARN et ERR) sous forme d'une chaine CSV
     * 
     * @return chaine CSV
     */
    public String toCSV() {
        StringBuffer sb = new StringBuffer();
        sb.append(processLog(warningsContainer, "WARN"));
        sb.append(processLog(errorsContainer, "ERR"));
        return sb.toString();
    }
}
