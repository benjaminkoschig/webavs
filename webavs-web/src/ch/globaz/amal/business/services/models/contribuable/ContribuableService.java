package ch.globaz.amal.business.services.models.contribuable;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.contribuable.ContribuableHistoriqueRCListeSearch;
import ch.globaz.amal.business.models.contribuable.ContribuableRCListeSearch;
import ch.globaz.amal.business.models.contribuable.ContribuableSearch;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableInfos;
import ch.globaz.pyxis.business.model.AdresseComplexModel;

/**
 * @author CBU
 * 
 */
public interface ContribuableService extends JadeApplicationService {

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(ContribuableSearch search) throws ContribuableException, JadePersistenceException;

    /**
     * Permet la création d'une entité contribuable
     * 
     * @param contribuable
     *            Le contribuable à créer
     * @return Le contribuable créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws FamilleException
     */
    public Contribuable create(Contribuable contribuable) throws JadePersistenceException, ContribuableException,
            FamilleException;

    /**
     * Permet l'effacement d'un contribuable
     * 
     * @param contribuable
     *            Le contribuable à effacer
     * @return
     * @throws ContribuableException
     * @throws JadePersistenceException
     */
    public Contribuable delete(Contribuable contribuable) throws ContribuableException, JadePersistenceException;

    /**
     * Permet l'effacement d'un contribuable Info
     * 
     * @param contribuableInfo
     *            Le contribuableInfo à effacer
     * @return
     * @throws ContribuableException
     * @throws JadePersistenceException
     */
    public SimpleContribuableInfos deleteInfo(SimpleContribuableInfos contribuableInfo) throws ContribuableException,
            JadePersistenceException;

    /**
     * Retrieve les informations de l'adresse du contribuable (adresse AMAL)
     * 
     * @param idTiers
     * @return
     * @throws JadePersistenceException
     */
    public AdresseComplexModel getContribuableAdresse(String idTiers) throws JadePersistenceException;

    /**
     * Récupération des numéro de contribuable dans l'historique, y compris numéro courant
     * 
     * @param idTiers
     * @return
     */
    public ArrayList<String> getContribuableHistoriqueNoContribuable(String idTiers);

    /**
     * Recherche d'un dossier contribuable HISTORIQUE en fonction d'un modèle renseigné ou vide et d'un numéro avs
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction du numéro
     * avs
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @param searchedAVS
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierByAVS(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedAVS);

    /**
     * Recherche d'un dossier contribuable en fonction d'un modèle de recherche renseigné ou vide et d'un numéro avs
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction du numéro
     * avs
     * 
     * @param currentContribuableRCListeSearch
     * @param searchedAVS
     * @return
     */
    public ContribuableRCListeSearch getDossierByAVS(ContribuableRCListeSearch currentContribuableRCListeSearch,
            String searchedAVS);

    /**
     * Recherche d'un dossier contribuable HISTORIQUE en fonction d'un modèle renseigné ou vide et d'une date de
     * naissance YYYYMMDD
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction de la date
     * de naissance
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @param dateNaissanceYYYYMMDD
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierByDateOfBirth(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String dateNaissanceYYYYMMDD);

    /**
     * Recherche d'un dossier contribuable en fonction d'un modèle renseigné ou vide et d'une date de naissance YYYYMMDD
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction de la date
     * de naissance
     * 
     * @param currentContribuableRCListeSearch
     * @param dateNaissanceYYYYMMDD
     * @return
     */
    public ContribuableRCListeSearch getDossierByDateOfBirth(
            ContribuableRCListeSearch currentContribuableRCListeSearch, String dateNaissanceYYYYMMDD);

    /**
     * Recherche d'un dossier contribuable HISTORIQUE en fonction d'un modèle renseigné ou vide et d'un nom de famille
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction du nom de
     * famille
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @param searchedName
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierByFamilyName(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedName);

    /**
     * Recherche d'un dossier contribuable en fonction d'un modèle renseigné ou vide et d'un nom de famille
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction du nom de
     * famille
     * 
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @param searchedName
     * @return
     */
    public ContribuableRCListeSearch getDossierByFamilyName(
            ContribuableRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedName);

    /**
     * Recherche d'un dossier contribuable HISTORIQUE en fonction d'un modèle renseigné ou vide et d'un prénom
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction du prénom
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @param searchedName
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierByGivenName(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedName);

    /**
     * Recherche d'un dossier contribuable en fonction d'un modèle renseigné ou vide et d'un prénom
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction du prénom
     * 
     * @param currentContribuableRCListeSearch
     * @param searchedName
     * @return
     */
    public ContribuableRCListeSearch getDossierByGivenName(ContribuableRCListeSearch currentContribuableRCListeSearch,
            String searchedName);

    /**
     * Recherche d'un dossier contribuable en fonction d'un modèle de recherche renseigné ou vide et d'un numéro de
     * contribuable
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction du numéro
     * de contribuabale
     * 
     * @param currentContribuableRCListeSearch
     * @param searchedNoContribuable
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierByNoContribuable(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch,
            String searchedNoContribuable);

    /**
     * Recherche d'un dossier contribuable en fonction d'un modèle de recherche renseigné ou vide et d'un numéro de
     * contribuable
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction du numéro
     * de contribuabale
     * 
     * @param currentContribuableRCListeSearch
     * @param searchedNoContribuable
     * @return
     */
    public ContribuableRCListeSearch getDossierByNoContribuable(
            ContribuableRCListeSearch currentContribuableRCListeSearch, String searchedNoContribuable);

    /**
     * Recherche d'un dossier contribuable HISTORIQUE en fonction d'un modèle renseigné ou vide et d'un NSS
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en en fonction du NSS
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @param searchedNSS
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierByNSS(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedNSS);

    /**
     * Recherche d'un dossier contribuable en fonction d'un modèle renseigné ou vide et d'un NSS
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en en fonction du NSS
     * 
     * @param currentContribuableRCListeSearch
     * @param searchedNSS
     * @return
     */
    public ContribuableRCListeSearch getDossierByNSS(ContribuableRCListeSearch currentContribuableRCListeSearch,
            String searchedNSS);

    /**
     * Recherche du dossier contribuable HISTORIQUE ayant le dernier subside actif si le modèle de recherche contient +
     * de 1 dossier
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierLastSubside(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch);

    /**
     * Recherche du dossier contribuable ayant le dernier subside actif si le modèle de recherche contient + de 1
     * dossier
     * 
     * @param currentContribuableRCListeSearch
     * @return
     */
    public ContribuableRCListeSearch getDossierLastSubside(ContribuableRCListeSearch currentContribuableRCListeSearch);

    /**
     * Permet de charger en mémoire un contribuable
     * 
     * @param idContribuable
     *            L'identifiant du contribuable à charger en mémoire
     * @return Le contribuable chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadeApplicationServiceNotAvailableException
     * @throws FamilleException
     */
    public Contribuable read(String idContribuable) throws JadePersistenceException, ContribuableException,
            FamilleException, JadeApplicationServiceNotAvailableException;

    public SimpleContribuableInfos readInfos(String idContribuable) throws JadePersistenceException,
            ContribuableException;

    /**
     * Permet de chercher des contribuables selon un modèle de critères.
     * 
     * @param contribuableSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ContribuableSearch search(ContribuableSearch contribuableSearch) throws JadePersistenceException,
            ContribuableException;

    /**
     * Permet de chercher des contribuables selon un modèle de critères pour la fusion de contribuable
     * 
     * @param contribuableSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ContribuableSearch searchFusion(ContribuableSearch contribuableSearch) throws JadePersistenceException,
            ContribuableException;

    /**
     * Permet de chercher des contribuables selon un modèle de critères.
     * 
     * @param contribuableSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ContribuableHistoriqueRCListeSearch searchHistoriqueRCListe(
            ContribuableHistoriqueRCListeSearch contribuableHistoriqueRCListeSearch) throws JadePersistenceException,
            ContribuableException;

    /**
     * Permet de chercher des contribuables selon un modèle de critères.
     * 
     * @param contribuableSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public ContribuableRCListeSearch searchRCListe(ContribuableRCListeSearch contribuableRCListeSearch)
            throws JadePersistenceException, ContribuableException;

    /**
     * 
     * Permet la mise à jour d'une entité contribuable
     * 
     * @param contribuable
     *            Le contribuable à mettre à jour
     * @return Le contribuable mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws ContribuableException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public Contribuable update(Contribuable contribuable) throws JadePersistenceException, ContribuableException;

}
