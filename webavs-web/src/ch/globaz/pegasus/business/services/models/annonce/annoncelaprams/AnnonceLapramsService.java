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
     * Permet d'effectuer sur la base des crit�res de recherche un count en DB
     * 
     * @param search
     *            Le mod�le encapsulant les crit�res de recherche
     * @return Le nombre de r�sulats correspondant aux crit�res
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceException
     */
    public int count(AnnonceLapramsSearch search) throws PrestationException, JadePersistenceException,
            AnnonceException;

    /**
     * Permet de supprimer les annonces li�es aux d�cisions
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
     * Permet de charger en m�moire une annonce LAPRAMS
     * 
     * @param idCommunication
     *            L'identifiant de la communication � charger en m�moire
     * @return La communication charg�e en m�moire
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AnnonceException
     */
    public AnnonceLaprams read(String idAnnonceLaprams) throws JadePersistenceException, PrestationException,
            AnnonceException;

    /**
     * Permet de chercher des annonces selon un mod�le de crit�res.
     * 
     * @param search
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public AnnonceLapramsSearch search(AnnonceLapramsSearch search) throws JadePersistenceException,
            PrestationException;

    public AnnonceLapramsMediatorDonneeFinanciereSearch searchDonneesFinancieresDetail(
            AnnonceLapramsMediatorDonneeFinanciereSearch dfMediatorSearch) throws AnnonceException,
            JadePersistenceException;

    /**
     * Permet de chercher des donn�es financi�res de taxe journali�re de type SASH/SPAS pour un droit donn�
     * 
     * @param searchModel
     *            Le mod�le de recherche
     * @return Le mod�le avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws PrestationException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws AnnonceException
     */
    public RechercheHomeSashSearch searchHomesSASH_SPAS(RechercheHomeSashSearch searchModel)
            throws JadePersistenceException, AnnonceException;

}
