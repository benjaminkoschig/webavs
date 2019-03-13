package ch.globaz.al.business.services.recapitulatifs;

import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Service pour les récapitulatifs d'entreprise (destinés aux employeurs) à imprimer
 * 
 * @author PTA
 * 
 */
public interface RecapitulatifEntrepriseImpressionService extends JadeApplicationService {
    /**
     * Exécute le calcul pour chaque dossier de la liste pour la période annoncée
     * 
     * @param dossiersToGenerate
     * @param periode
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public RecapitulatifEntrepriseImpressionComplexSearchModel calculPrestationsStoreInRecapsDocs(
            ArrayList<String> dossiersToGenerate, String periode, String bonification) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Méthode qui retourne la liste des recap à sortir en pdf
     * 
     * @param listRecap
     *            liste totale des récapitulatifs
     * @return ArrayLsit liste des récap à sortir en pdf
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList loadArrayListCsv(ArrayList listRecap) throws JadePersistenceException, JadeApplicationException;

    /**
     * Méthode qui retourne la liste des recap à sortir en csv
     * 
     * @param listRecap
     *            liste totale des récapitulatifs
     * @return ArrayLsit liste des récap à sortir en pdf
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList loadArrayListDocData(ArrayList listRecap) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * Méthode qui charge les données d'un récap dont la destination est un fichier CSV
     * 
     * @param recapList
     *            liste des récap
     * @return une HashMap
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    
    public HashMap loadCSVDocument(ArrayList recapList, Boolean isCharNssRecap) throws JadePersistenceException, JadeApplicationException;

    /**
     * Méthode qui retourne le container renfermant les listes des récapitulatifs
     * 
     * @param listRecap
     *            liste des récapitulatifs
     * @param dateImpression
     *            date d'impression des récap
     * @param isGEd
     *            arhivage du document
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception levée par la couche métier lorsqu'elle n'a pu effectuer l'opération souhaitée
     * @throws JadePersistenceException
     *             Exception levée lorsque le chargement ou la mise à jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    public ArrayList loadDocuments(ArrayList listRecap, String dateImpression, boolean isGEd)
            throws JadePersistenceException, JadeApplicationException;

}
