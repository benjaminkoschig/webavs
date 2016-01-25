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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(ContribuableSearch search) throws ContribuableException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� contribuable
     * 
     * @param contribuable
     *            Le contribuable � cr�er
     * @return Le contribuable cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws FamilleException
     */
    public Contribuable create(Contribuable contribuable) throws JadePersistenceException, ContribuableException,
            FamilleException;

    /**
     * Permet l'effacement d'un contribuable
     * 
     * @param contribuable
     *            Le contribuable � effacer
     * @return
     * @throws ContribuableException
     * @throws JadePersistenceException
     */
    public Contribuable delete(Contribuable contribuable) throws ContribuableException, JadePersistenceException;

    /**
     * Permet l'effacement d'un contribuable Info
     * 
     * @param contribuableInfo
     *            Le contribuableInfo � effacer
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
     * R�cup�ration des num�ro de contribuable dans l'historique, y compris num�ro courant
     * 
     * @param idTiers
     * @return
     */
    public ArrayList<String> getContribuableHistoriqueNoContribuable(String idTiers);

    /**
     * Recherche d'un dossier contribuable HISTORIQUE en fonction d'un mod�le renseign� ou vide et d'un num�ro avs
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction du num�ro
     * avs
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @param searchedAVS
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierByAVS(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedAVS);

    /**
     * Recherche d'un dossier contribuable en fonction d'un mod�le de recherche renseign� ou vide et d'un num�ro avs
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction du num�ro
     * avs
     * 
     * @param currentContribuableRCListeSearch
     * @param searchedAVS
     * @return
     */
    public ContribuableRCListeSearch getDossierByAVS(ContribuableRCListeSearch currentContribuableRCListeSearch,
            String searchedAVS);

    /**
     * Recherche d'un dossier contribuable HISTORIQUE en fonction d'un mod�le renseign� ou vide et d'une date de
     * naissance YYYYMMDD
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction de la date
     * de naissance
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @param dateNaissanceYYYYMMDD
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierByDateOfBirth(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String dateNaissanceYYYYMMDD);

    /**
     * Recherche d'un dossier contribuable en fonction d'un mod�le renseign� ou vide et d'une date de naissance YYYYMMDD
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction de la date
     * de naissance
     * 
     * @param currentContribuableRCListeSearch
     * @param dateNaissanceYYYYMMDD
     * @return
     */
    public ContribuableRCListeSearch getDossierByDateOfBirth(
            ContribuableRCListeSearch currentContribuableRCListeSearch, String dateNaissanceYYYYMMDD);

    /**
     * Recherche d'un dossier contribuable HISTORIQUE en fonction d'un mod�le renseign� ou vide et d'un nom de famille
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction du nom de
     * famille
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @param searchedName
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierByFamilyName(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedName);

    /**
     * Recherche d'un dossier contribuable en fonction d'un mod�le renseign� ou vide et d'un nom de famille
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction du nom de
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
     * Recherche d'un dossier contribuable HISTORIQUE en fonction d'un mod�le renseign� ou vide et d'un pr�nom
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction du pr�nom
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @param searchedName
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierByGivenName(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedName);

    /**
     * Recherche d'un dossier contribuable en fonction d'un mod�le renseign� ou vide et d'un pr�nom
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction du pr�nom
     * 
     * @param currentContribuableRCListeSearch
     * @param searchedName
     * @return
     */
    public ContribuableRCListeSearch getDossierByGivenName(ContribuableRCListeSearch currentContribuableRCListeSearch,
            String searchedName);

    /**
     * Recherche d'un dossier contribuable en fonction d'un mod�le de recherche renseign� ou vide et d'un num�ro de
     * contribuable
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction du num�ro
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
     * Recherche d'un dossier contribuable en fonction d'un mod�le de recherche renseign� ou vide et d'un num�ro de
     * contribuable
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction du num�ro
     * de contribuabale
     * 
     * @param currentContribuableRCListeSearch
     * @param searchedNoContribuable
     * @return
     */
    public ContribuableRCListeSearch getDossierByNoContribuable(
            ContribuableRCListeSearch currentContribuableRCListeSearch, String searchedNoContribuable);

    /**
     * Recherche d'un dossier contribuable HISTORIQUE en fonction d'un mod�le renseign� ou vide et d'un NSS
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en en fonction du NSS
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @param searchedNSS
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierByNSS(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch, String searchedNSS);

    /**
     * Recherche d'un dossier contribuable en fonction d'un mod�le renseign� ou vide et d'un NSS
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en en fonction du NSS
     * 
     * @param currentContribuableRCListeSearch
     * @param searchedNSS
     * @return
     */
    public ContribuableRCListeSearch getDossierByNSS(ContribuableRCListeSearch currentContribuableRCListeSearch,
            String searchedNSS);

    /**
     * Recherche du dossier contribuable HISTORIQUE ayant le dernier subside actif si le mod�le de recherche contient +
     * de 1 dossier
     * 
     * @param currentContribuableHistoriqueRCListeSearch
     * @return
     */
    public ContribuableHistoriqueRCListeSearch getDossierLastSubside(
            ContribuableHistoriqueRCListeSearch currentContribuableHistoriqueRCListeSearch);

    /**
     * Recherche du dossier contribuable ayant le dernier subside actif si le mod�le de recherche contient + de 1
     * dossier
     * 
     * @param currentContribuableRCListeSearch
     * @return
     */
    public ContribuableRCListeSearch getDossierLastSubside(ContribuableRCListeSearch currentContribuableRCListeSearch);

    /**
     * Permet de charger en m�moire un contribuable
     * 
     * @param idContribuable
     *            L'identifiant du contribuable � charger en m�moire
     * @return Le contribuable charg� en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadeApplicationServiceNotAvailableException
     * @throws FamilleException
     */
    public Contribuable read(String idContribuable) throws JadePersistenceException, ContribuableException,
            FamilleException, JadeApplicationServiceNotAvailableException;

    public SimpleContribuableInfos readInfos(String idContribuable) throws JadePersistenceException,
            ContribuableException;

    /**
     * Permet de chercher des contribuables selon un mod�le de crit�res.
     * 
     * @param contribuableSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ContribuableSearch search(ContribuableSearch contribuableSearch) throws JadePersistenceException,
            ContribuableException;

    /**
     * Permet de chercher des contribuables selon un mod�le de crit�res pour la fusion de contribuable
     * 
     * @param contribuableSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ContribuableSearch searchFusion(ContribuableSearch contribuableSearch) throws JadePersistenceException,
            ContribuableException;

    /**
     * Permet de chercher des contribuables selon un mod�le de crit�res.
     * 
     * @param contribuableSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ContribuableHistoriqueRCListeSearch searchHistoriqueRCListe(
            ContribuableHistoriqueRCListeSearch contribuableHistoriqueRCListeSearch) throws JadePersistenceException,
            ContribuableException;

    /**
     * Permet de chercher des contribuables selon un mod�le de crit�res.
     * 
     * @param contribuableSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public ContribuableRCListeSearch searchRCListe(ContribuableRCListeSearch contribuableRCListeSearch)
            throws JadePersistenceException, ContribuableException;

    /**
     * 
     * Permet la mise � jour d'une entit� contribuable
     * 
     * @param contribuable
     *            Le contribuable � mettre � jour
     * @return Le contribuable mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws ContribuableException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public Contribuable update(Contribuable contribuable) throws JadePersistenceException, ContribuableException;

}
