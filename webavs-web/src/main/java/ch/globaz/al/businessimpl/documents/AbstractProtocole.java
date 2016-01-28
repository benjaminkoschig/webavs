package ch.globaz.al.businessimpl.documents;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import ch.globaz.al.business.constantes.ALConstDocument;
import ch.globaz.al.business.constantes.ALConstProtocoles;
import ch.globaz.al.business.exceptions.protocoles.ALProtocoleException;
import ch.globaz.al.businessimpl.services.ALImplServiceLocator;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Classe de base pour la génération d'un protocole. Elle définit les méthodes communes à tous les protocoles
 * 
 * @author jts
 * 
 */
public abstract class AbstractProtocole extends AbstractDocument {

    /**
     * Initialise le document en appelant les méthodes suivantes :
     * 
     * <ul>
     * <li>setIdDocument</li>
     * <li>setIdEntete</li>
     * <li>setProtocoleTitle</li>
     * <li>setInfos</li>
     * </ul>
     * 
     * @param params
     *            Paramètres tels que le numéro de passage, périodes, etc.
     * @return Le document initialisé
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    protected DocumentData init(HashMap<String, String> params) throws JadePersistenceException,
            JadeApplicationException {

        if (params == null) {
            throw new ALProtocoleException("AbstractProtocoleErreurs#getDocumentData : params is null");
        }

        DocumentData document = new DocumentData();
        setIdDocument(document);
        this.setIdEntete(document);
        setProtocoleTitle(document);
        setInfos(document, params);

        return document;
    }

    /**
     * Définit l'id de l'en-tête à utiliser en fonction des paramètres de la caisse
     * 
     * @param document
     *            document à générer
     * @throws JadePersistenceException
     *             Exception levée si le nom de la caisse n'a pas pu être récupéré
     * @throws JadeApplicationException
     *             Exception levée si le nom de la caisse n'a pas pu être récupéré
     */
    @Override
    protected void setIdEntete(DocumentData document) throws JadeApplicationException, JadePersistenceException {
        document.addData(
                "idEntete",
                ALImplServiceLocator.getEnteteService().getEnteteDocument(null, ALConstDocument.DOCUMENT_PROTOCOLE,
                        JadeThread.currentLanguage()));
    }

    /**
     * Initialise toutes les infos communes
     * 
     * <code>infos</code> contient des paires 'constante' => ID texte. les constantes possible sont disponible dans
     * l'interface {@link ALConstProtocoles}. Les ID texte sont ceux définit dans les fichiers al*.properties
     * 
     * La méthode appellera les méthodes correspondante. les clé invalides seront ignorées. Les méthodes ne nécessitant
     * pas de paramètre (utilisateur, date, ...) sont appelées dans tous les cas.
     * 
     * @param document
     *            document auquel ajouter les infos
     * @param infos
     *            HashMap contenant les id des textes devant être passée au document
     * @throws JadeApplicationException
     *             Exception levée si l'un des paramètres est null ou vide
     */
    protected void setInfos(DocumentData document, HashMap<String, String> infos) throws JadeApplicationException {

        if (document == null) {
            throw new ALProtocoleException("AbstractProtocoleServiceImpl#initInfos : document is null");
        }

        if (infos == null) {
            throw new ALProtocoleException("AbstractProtocoleServiceImpl#initInfos : infos is null or empty");
        }

        // date & heure
        if (JadeStringUtil.isEmpty(infos.get(ALConstProtocoles.INFO_DATEHEURE))) {
            String format = "dd/MM/yyyy HH:mm";
            SimpleDateFormat formater = new java.text.SimpleDateFormat(format);
            infos.put(ALConstProtocoles.INFO_DATEHEURE, formater.format(new Date()));
        }

        // informations de l'utilisateur
        if (JadeStringUtil.isEmpty(infos.get(ALConstProtocoles.INFO_UTILISATEUR))) {

            infos.put(ALConstProtocoles.INFO_UTILISATEUR, JadeThread.currentUserName());
        }

        // traitement des paramètres contenus dans 'infos'
        for (Iterator<String> it = infos.keySet().iterator(); it.hasNext();) {
            String key = it.next();

            document.addData(key + "_label", this.getText("al.protocoles.commun." + key.replace('_', '.') + ".label"));

            document.addData(key + "_val", this.getText(infos.get(key)));
        }
    }

    /**
     * Définit le titre du document
     * 
     * @param document
     *            Document auquel ajouter le titre
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     */
    protected void setProtocoleTitle(DocumentData document) throws JadeApplicationException {
        document.addData("protocole_titre", this.getText("al.protocoles.commun.titre"));
    }
}
