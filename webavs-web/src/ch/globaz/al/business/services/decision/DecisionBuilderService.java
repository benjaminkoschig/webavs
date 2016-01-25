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
 * Service d'impression des d�cisions
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
     *            le document � ajouter comme copie
     * @return le conteneur mis � jour
     */
    public JadePrintDocumentContainer addCopies(JadePrintDocumentContainer containerDecisions, DocumentData docData);

    /**
     * Ajoute une d�cision dans un conteneur
     * 
     * @param dossier
     *            Dossier auquel appartient la d�cision
     * @param docData
     *            Le document � ajouter au conteneur
     * @param isGed
     *            Indique si le document doit �tre mis en GED
     * @param containerDecision
     *            Le conteneur de documents
     * @return Le conteneur mis � jour
     * 
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception lev�e si un service utilis� par la m�thode n'est pas disponible
     */
    public JadePrintDocumentContainer addDecision(DossierComplexModel dossier, DocumentData docData, boolean isGed,
            JadePrintDocumentContainer containerDecision, String email, String dateImpression)
            throws JadeApplicationServiceNotAvailableException, Exception;

    /**
     * R�cupr�re la d�cision pour un dossier
     * 
     * @param idDossier
     *            Identifiant du dossier
     * @param email
     *            E-mail du destinataire de la d�cision
     * @param userName
     *            Nom du destinataire de la d�cision
     * @param phone
     *            T�l�phone du destinataire de la d�cision
     * @param visa
     *            Visa du destinataire de la d�cision
     * @param dateImpression
     *            Date d'impression � afficher sur la d�cision
     * @return La d�cision g�n�r�e
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * Pr�pare la d�cision.
     * 
     * Fait appel � :
     * <ul>
     * <li>getDecision</li>
     * <li>addDecision</li>
     * </ul>
     * 
     * @param containerDecision
     *            Le conteneur de documents
     * @param dossier
     *            Le dossier pour lequel la d�cision doit �tre �dit�e
     * @param email
     *            E-mail du destinataire de la d�cision
     * @param userName
     *            Nom du destinataire de la d�cision
     * @param phone
     *            T�l�phone du destinataire de la d�cision
     * @param visa
     *            Visa du destinataire de la d�cision
     * @param dateImpression
     *            Date d'impression � afficher sur la d�cision
     * @param isGED
     *            Indique si le document doit �tre mis en GED
     * @return Le conteneur mis � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * Pr�pare la d�cision et les copies.
     * 
     * Fait appel � :
     * <ul>
     * <li>getDecision</li>
     * <li>addDecision</li>
     * <li>addCopies</li>
     * </ul>
     * 
     * @param containerDecision
     *            Le conteneur de documents
     * @param dossier
     *            Le dossier pour lequel la d�cision doit �tre �dit�e
     * @param email
     *            E-mail du destinataire de la d�cision
     * @param userName
     *            Nom du destinataire de la d�cision
     * @param phone
     *            T�l�phone du destinataire de la d�cision
     * @param visa
     *            Visa du destinataire de la d�cision
     * @param dateImpression
     *            Date d'impression � afficher sur la d�cision
     * @param isGED
     *            Indique si le document doit �tre mis en GED
     * @return Le conteneur mis � jour
     * 
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
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
     * Pr�pare les informations de publication du document fusionn�.
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
     *            le dossier � journaliser
     * @throws LibraException
     *             Exception lev�e si un probl�me se produit pendant la journalisation
     * @throws JadeApplicationServiceNotAvailableException
     *             Exception lev�e si le service de journalisation n'est pas disponible
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
