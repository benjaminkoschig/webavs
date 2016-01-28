package ch.globaz.al.business.services.declarationVersement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service liées au chargement des données pour la création de déclaration de versement
 * 
 * @author PTA
 * 
 */
public interface DeclarationsVersementService extends JadeApplicationService {

    /**
     * Méthode générant les documents concernant les frontaliers à paiement indirect
     * 
     * @param periodeDe
     *            début de la période
     * @param periodeA
     *            fin de la période
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            type de document
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getDeclarationsFrontaliers(String periodeDe, String periodeA, String dateImpr,
            String typeDocument, String numDossier, String numAffilie, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode générant les documents concernant les dossiers à paiements directs, imposés à la source
     * 
     * @param periodeDe
     *            début de la période
     * @param periodeA
     *            fin de la période
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            type de document (global, détaillé)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public JadePrintDocumentContainer getDeclarationsImposeSource(String periodeDe, String periodeA, String dateImpr,
            String typeDocument, String numDossier, String numAffilie, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode générant les documents concernant paiements directs dont l'allocataire est non actif
     * 
     * @param periodeDe
     *            début de la période
     * @param periodeA
     *            fin de la période
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            type de documnet (détaillé, global)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getDeclarationsNonActifPaiementsIndirects(String periodeDe, String periodeA,
            String dateImpr, String typeDocument, String numDossier, String numAffilie, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode générant les documents par le numéro d'affilié
     * 
     * @param numAffilie
     *            numéro de l'affilie
     * 
     * @param periodeDe
     *            début de la période
     * @param periodeA
     *            fin de la période
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            type de déclaration de versement (détaillée ou global)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public JadePrintDocumentContainer getDeclarationsVersementAffilie(String num, String periodeDe, String periodeA,
            String dateImpr, String typeDocument, Boolean textImpot) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * Méthode générant les documents d'un dossier par son id
     * 
     * @param idDossier
     *            identifiant du dossier
     * @param periodeDe
     *            début de la période
     * @param periodeA
     *            fin de la période
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            type de déclaration de versement de versement (détaillée ou glboal)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getDeclarationsVersementDemande(String idDossier, String numAffilie,
            String periodeDe, String periodeA, String dateImpr, String typeDocument, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * Méthode générant les documents concernant les dossiers à paiements directs et non imposés à la source
     * 
     * @param periodeDe
     *            début de la période
     * @param periodeA
     *            fin de la période
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            (type de document détaillé, global)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getDeclarationsVersementDirect(String periodeDe, String periodeA,
            String dateImpr, String typeDocument, String numDossier, String numAffilie, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException;

}
