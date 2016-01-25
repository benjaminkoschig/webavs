package ch.globaz.pegasus.business.services.models.annonce.annoncelaprams;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.annonce.AnnonceLaprams;
import ch.globaz.pegasus.business.models.annonce.AnnonceLapramsMediatorDonneeFinanciereSearch;
import ch.globaz.pegasus.business.models.annonce.AnnonceLapramsSearch;
import ch.globaz.pegasus.business.models.annonce.RechercheHomeSashSearch;

public interface AnnonceLapramsService extends JadeApplicationService {
    /**
     * Permet d'effectuer sur la base des critères de recherche un count en DB
     * 
     * @param search
     *            Le modèle encapsulant les critères de recherche
     * @return Le nombre de résulats correspondant aux critères
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceException
     */
    public int count(AnnonceLapramsSearch search) throws PrestationException, JadePersistenceException,
            AnnonceException;

    /**
     * Permet de supprimer les annonces liées aux décisions
     * 
     * @param idsDecision
     * @throws AnnonceException
     * @throws JadePersistenceException
     * @throws PrestationException
     * @throws JadeApplicationServiceNotAvailableException
     */
    public void deleteByIdsDecision(List<String> idsDecision) throws AnnonceException, JadePersistenceException,
            PrestationException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire une annonce LAPRAMS
     * 
     * @param idCommunication
     *            L'identifiant de la communication à charger en mémoire
     * @return La communication chargée en mémoire
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AnnonceException
     */
    public AnnonceLaprams read(String idAnnonceLaprams) throws JadePersistenceException, PrestationException,
            AnnonceException;

    /**
     * Permet de chercher des annonces selon un modèle de critères.
     * 
     * @param search
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AnnonceLapramsSearch search(AnnonceLapramsSearch search) throws JadePersistenceException,
            PrestationException;

    public AnnonceLapramsMediatorDonneeFinanciereSearch searchDonneesFinancieresDetail(
            AnnonceLapramsMediatorDonneeFinanciereSearch dfMediatorSearch) throws AnnonceException,
            JadePersistenceException;

    /**
     * Permet de chercher des données financières de taxe journalière de type SASH/SPAS pour un droit donné
     * 
     * @param searchModel
     *            Le modèle de recherche
     * @return Le modèle avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws PrestationException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws AnnonceException
     */
    public RechercheHomeSashSearch searchHomesSASH_SPAS(RechercheHomeSashSearch searchModel)
            throws JadePersistenceException, AnnonceException;

}
