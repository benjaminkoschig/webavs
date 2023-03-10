package ch.globaz.al.business.services.recapitulatifs;

import ch.globaz.al.business.models.prestation.RecapitulatifEntrepriseImpressionComplexSearchModel;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

import java.util.List;
import java.util.Map;

/**
 * Service pour les r?capitulatifs d'entreprise (destin?s aux employeurs) ? imprimer
 * 
 * @author PTA
 * 
 */
public interface RecapitulatifEntrepriseImpressionService extends JadeApplicationService {
    /**
     * Ex?cute le calcul pour chaque dossier de la liste pour la p?riode annonc?e
     * 
     * @param dossiersToGenerate
     * @param periode
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    RecapitulatifEntrepriseImpressionComplexSearchModel calculPrestationsStoreInRecapsDocs(
            List<String> dossiersToGenerate, String periode, String bonification) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * M?thode qui retourne la liste des recap ? sortir en pdf
     * 
     * @param listRecap
     *            liste totale des r?capitulatifs
     * @return ArrayLsit liste des r?cap ? sortir en pdf
     * @throws JadeApplicationException
     *             Exception lev?e par la couche m?tier lorsqu'elle n'a pu effectuer l'op?ration souhait?e
     * @throws JadePersistenceException
     *             Exception lev?e lorsque le chargement ou la mise ? jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    List loadArrayListCsv(List listRecap) throws JadePersistenceException, JadeApplicationException;

    /**
     * M?thode qui retourne la liste des recap ? sortir en csv
     * 
     * @param listRecap
     *            liste totale des r?capitulatifs
     * @return ArrayLsit liste des r?cap ? sortir en pdf
     * @throws JadeApplicationException
     *             Exception lev?e par la couche m?tier lorsqu'elle n'a pu effectuer l'op?ration souhait?e
     * @throws JadePersistenceException
     *             Exception lev?e lorsque le chargement ou la mise ? jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    List loadArrayListDocData(List listRecap) throws JadePersistenceException,
            JadeApplicationException;

    /**
     * M?thode qui charge les donn?es d'un r?cap dont la destination est un fichier CSV
     * 
     * @param recapList
     *            liste des r?cap
     * @return une HashMap
     * @throws JadeApplicationException
     *             Exception lev?e par la couche m?tier lorsqu'elle n'a pu effectuer l'op?ration souhait?e
     * @throws JadePersistenceException
     *             Exception lev?e lorsque le chargement ou la mise ? jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    
    Map loadCSVDocument(List recapList, Boolean isCharNssRecap) throws JadePersistenceException, JadeApplicationException;

    /**
     * M?thode qui retourne le container renfermant les listes des r?capitulatifs
     * 
     * @param listRecap
     *            liste des r?capitulatifs
     * @param dateImpression
     *            date d'impression des r?cap
     * @param isGEd
     *            arhivage du document
     * @return JadePrintDocumentContainer
     * @throws JadeApplicationException
     *             Exception lev?e par la couche m?tier lorsqu'elle n'a pu effectuer l'op?ration souhait?e
     * @throws JadePersistenceException
     *             Exception lev?e lorsque le chargement ou la mise ? jour en DB par la couche de persistence n'a pu se
     *             faire
     */
    List loadDocuments(List listRecap, String dateImpression, boolean isGEd)
            throws JadePersistenceException, JadeApplicationException;

}
