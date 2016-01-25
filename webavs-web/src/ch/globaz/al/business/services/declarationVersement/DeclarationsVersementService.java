package ch.globaz.al.business.services.declarationVersement;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.service.provider.application.JadeApplicationService;

/**
 * Service li�es au chargement des donn�es pour la cr�ation de d�claration de versement
 * 
 * @author PTA
 * 
 */
public interface DeclarationsVersementService extends JadeApplicationService {

    /**
     * M�thode g�n�rant les documents concernant les frontaliers � paiement indirect
     * 
     * @param periodeDe
     *            d�but de la p�riode
     * @param periodeA
     *            fin de la p�riode
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            type de document
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getDeclarationsFrontaliers(String periodeDe, String periodeA, String dateImpr,
            String typeDocument, String numDossier, String numAffilie, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode g�n�rant les documents concernant les dossiers � paiements directs, impos�s � la source
     * 
     * @param periodeDe
     *            d�but de la p�riode
     * @param periodeA
     *            fin de la p�riode
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            type de document (global, d�taill�)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public JadePrintDocumentContainer getDeclarationsImposeSource(String periodeDe, String periodeA, String dateImpr,
            String typeDocument, String numDossier, String numAffilie, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode g�n�rant les documents concernant paiements directs dont l'allocataire est non actif
     * 
     * @param periodeDe
     *            d�but de la p�riode
     * @param periodeA
     *            fin de la p�riode
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            type de documnet (d�taill�, global)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getDeclarationsNonActifPaiementsIndirects(String periodeDe, String periodeA,
            String dateImpr, String typeDocument, String numDossier, String numAffilie, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode g�n�rant les documents par le num�ro d'affili�
     * 
     * @param numAffilie
     *            num�ro de l'affilie
     * 
     * @param periodeDe
     *            d�but de la p�riode
     * @param periodeA
     *            fin de la p�riode
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            type de d�claration de versement (d�taill�e ou global)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */

    public JadePrintDocumentContainer getDeclarationsVersementAffilie(String num, String periodeDe, String periodeA,
            String dateImpr, String typeDocument, Boolean textImpot) throws JadeApplicationException,
            JadePersistenceException;

    /**
     * M�thode g�n�rant les documents d'un dossier par son id
     * 
     * @param idDossier
     *            identifiant du dossier
     * @param periodeDe
     *            d�but de la p�riode
     * @param periodeA
     *            fin de la p�riode
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            type de d�claration de versement de versement (d�taill�e ou glboal)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getDeclarationsVersementDemande(String idDossier, String numAffilie,
            String periodeDe, String periodeA, String dateImpr, String typeDocument, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException;

    /**
     * M�thode g�n�rant les documents concernant les dossiers � paiements directs et non impos�s � la source
     * 
     * @param periodeDe
     *            d�but de la p�riode
     * @param periodeA
     *            fin de la p�riode
     * @param dateImpr
     *            date du document
     * @param typeDocument
     *            (type de document d�taill�, global)
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception lev�e par la couche m�tier lorsqu'elle n'a pu effectuer l'op�ration souhait�e
     * @throws JadePersistenceException
     *             Exception lev�e lorsque le chargement ou la mise � jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public JadePrintDocumentContainer getDeclarationsVersementDirect(String periodeDe, String periodeA,
            String dateImpr, String typeDocument, String numDossier, String numAffilie, Boolean textImpot)
            throws JadeApplicationException, JadePersistenceException;

}
