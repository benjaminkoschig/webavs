package ch.globaz.al.business.services.decision;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import ch.globaz.al.business.models.dossier.DossierComplexModel;
import ch.globaz.libra.business.exceptions.LibraException;
import ch.globaz.topaz.datajuicer.DocumentData;

/**
 * Service d'impression des décisions
 * 
 * @author jts
 */
public interface DecisionBuilderService extends JadeApplicationService {

    /**
     * Ajoute le document <code>docData</code> dans le conteneur <code>containerDecision</code> en tant que copie
     * 
     * @param containerDecision
     *            le conteneur de documents
     * @param docData
     *            le document à ajouter comme copie
     * @return le conteneur mis à jour
     */
    public JadePrintDocumentContainer addCopies(JadePrintDocumentContainer containerDecisions, DocumentData docData);

    /**
     * Ajoute une décision dans un conteneur
     * 
     * @param dossier
     *            Dossier auquel appartient la décision
     * @param docData
     *            Le document à ajouter au conteneur
     * @param isGed
     *            Indique si le document doit être mis en GED
     * @param containerDecision
     *            Le conteneur de documents
     * @return Le conteneur mis à jour
     * 
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si un service utilisé par la méthode n'est pas disponible
     */
    public JadePrintDocumentContainer addDecision(DossierComplexModel dossier, DocumentData docData, boolean isGed,
            JadePrintDocumentContainer containerDecision, String email, String dateImpression)
            throws JadeApplicationServiceNotAvailableException, Exception;

    /**
     * Récuprère la décision pour un dossier
     * 
     * @param idDossier
     *            Identifiant du dossier
     * @param email
     *            E-mail du destinataire de la décision
     * @param userName
     *            Nom du destinataire de la décision
     * @param phone
     *            Téléphone du destinataire de la décision
     * @param visa
     *            Visa du destinataire de la décision
     * @param dateImpression
     *            Date d'impression à afficher sur la décision
     * @return La décision générée
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public HashMap<String, ArrayList<DocumentData>> getDecision(String idDossier, String email, String userName,
            String phone, String visa, String dateImpression) throws JadeApplicationException, JadePersistenceException;

    public HashMap<String, ArrayList<DocumentData>> getDecision(String idDossier, String email, String userName,
            String phone, String visa, String dateImpression, String texteLibre, boolean gestionTexteLibre)
            throws JadeApplicationException, JadePersistenceException;

    // split decision et copie suite au mandat D0113
    public HashMap<String, ArrayList<DocumentData>> getDecision(String idDossier, String email, String userName,
            String phone, String visa, String dateImpression, boolean gestionCopie) throws JadeApplicationException,
            JadePersistenceException;

    public HashMap<String, ArrayList<DocumentData>> getDecision(String idDossier, String email, String userName,
            String phone, String visa, String dateImpression, boolean gestionCopie, String texteLibre,
            boolean gestionTexteLibre) throws JadeApplicationException, JadePersistenceException;

    /**
     * Prépare la décision.
     * 
     * Fait appel à :
     * <ul>
     * <li>getDecision</li>
     * <li>addDecision</li>
     * </ul>
     * 
     * @param containerDecision
     *            Le conteneur de documents
     * @param dossier
     *            Le dossier pour lequel la décision doit être éditée
     * @param email
     *            E-mail du destinataire de la décision
     * @param userName
     *            Nom du destinataire de la décision
     * @param phone
     *            Téléphone du destinataire de la décision
     * @param visa
     *            Visa du destinataire de la décision
     * @param dateImpression
     *            Date d'impression à afficher sur la décision
     * @param isGED
     *            Indique si le document doit être mis en GED
     * @return Le conteneur mis à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getDecision(JadePrintDocumentContainer containerDecision,
            DossierComplexModel dossier, String email, String userName, String phone, String visa,
            String dateImpression, boolean isGED, boolean gestionCopie) throws JadeApplicationException,
            JadePersistenceException;

    public JadePrintDocumentContainer getDecision(JadePrintDocumentContainer containerDecision,
            DossierComplexModel dossier, String email, String userName, String phone, String visa,
            String dateImpression, boolean isGED, boolean gestionCopie, String texteLibre, boolean gestionTexteLibre)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Prépare la décision et les copies.
     * 
     * Fait appel à :
     * <ul>
     * <li>getDecision</li>
     * <li>addDecision</li>
     * <li>addCopies</li>
     * </ul>
     * 
     * @param containerDecision
     *            Le conteneur de documents
     * @param dossier
     *            Le dossier pour lequel la décision doit être éditée
     * @param email
     *            E-mail du destinataire de la décision
     * @param userName
     *            Nom du destinataire de la décision
     * @param phone
     *            Téléphone du destinataire de la décision
     * @param visa
     *            Visa du destinataire de la décision
     * @param dateImpression
     *            Date d'impression à afficher sur la décision
     * @param isGED
     *            Indique si le document doit être mis en GED
     * @return Le conteneur mis à jour
     * 
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getDecisionEtCopies(JadePrintDocumentContainer containerDecision,
            DossierComplexModel dossier, String email, String userName, String phone, String visa,
            String dateImpression, boolean isGED) throws JadeApplicationException, JadePersistenceException;

    public JadePrintDocumentContainer getDecisionEtCopies(JadePrintDocumentContainer containerDecision,
            DossierComplexModel dossier, String email, String userName, String phone, String visa,
            String dateImpression, boolean isGED, String texteLibre, boolean gestionTexteLibre)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Prépare les informations de publication du document fusionné.
     * 
     * @param email
     *            E-mail du destinataire
     * @param dateImpression
     *            Date d'impression
     * @return Les informations de publication
     */
    public JadePublishDocumentInfo getMergedDocumentInfos(String email, String dateImpression);

    /**
     * Journalise le dossier avec le motif "DECISION EDITEE"
     * 
     * @param dossier
     *            le dossier à journaliser
     * @throws LibraException
     *             Exception levée si un problème se produit pendant la journalisation
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception levée si le service de journalisation n'est pas disponible
     */
    public void journaliserDecision(DossierComplexModel dossier) throws LibraException,
            JadeApplicationServiceNotAvailableException;

    /**
     * envoie la liste de dossier par mail au format CSV
     * 
     */
    public String getListeDossiersCSV(Collection<DossierComplexModel> dossiers, HashMap<String, String> params)
            throws JadeApplicationException;

}
