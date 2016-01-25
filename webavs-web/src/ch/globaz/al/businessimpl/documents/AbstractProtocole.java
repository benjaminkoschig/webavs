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
 * Classe de base pour la g�n�ration d'un protocole. Elle d�finit les m�thodes communes � tous les protocoles
 * 
 * @author jts
 * 
 */
public abstract class AbstractProtocole extends AbstractDocument {

    /**
     * Initialise le document en appelant les m�thodes suivantes :
     * 
     * <ul>
     * <li>setIdDocument</li>
     * <li>setIdEntete</li>
     * <li>setProtocoleTitle</li>
     * <li>setInfos</li>
     * </ul>
     * 
     * @param params
     *            Param�tres tels que le num�ro de passage, p�riodes, etc.
     * @return Le document initialis�
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * D�finit l'id de l'en-t�te � utiliser en fonction des param�tres de la caisse
     * 
     * @param document
     *            document � g�n�rer
     * @throws JadePersistenceException
     *             Exception lev�e si le nom de la caisse n'a pas pu �tre r�cup�r�
     * @throws JadeApplicationException
     *             Exception lev�e si le nom de la caisse n'a pas pu �tre r�cup�r�
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
     * l'interface {@link ALConstProtocoles}. Les ID texte sont ceux d�finit dans les fichiers al*.properties
     * 
     * La m�thode appellera les m�thodes correspondante. les cl� invalides seront ignor�es. Les m�thodes ne n�cessitant
     * pas de param�tre (utilisateur, date, ...) sont appel�es dans tous les cas.
     * 
     * @param document
     *            document auquel ajouter les infos
     * @param infos
     *            HashMap contenant les id des textes devant �tre pass�e au document
     * @throws JadeApplicationException
     *             Exception lev�e si l'un des param�tres est null ou vide
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

        // traitement des param�tres contenus dans 'infos'
        for (Iterator<String> it = infos.keySet().iterator(); it.hasNext();) {
            String key = it.next();

            document.addData(key + "_label", this.getText("al.protocoles.commun." + key.replace('_', '.') + ".label"));

            document.addData(key + "_val", this.getText(infos.get(key)));
        }
    }

    /**
     * D�finit le titre du document
     * 
     * @param document
     *            Document auquel ajouter le titre
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     */
    protected void setProtocoleTitle(DocumentData document) throws JadeApplicationException {
        document.addData("protocole_titre", this.getText("al.protocoles.commun.titre"));
    }
}
