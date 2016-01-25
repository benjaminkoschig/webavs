/**
 * 
 */
package ch.globaz.amal.business.services.models.famille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.contribuable.Contribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuable;
import ch.globaz.amal.business.models.famille.FamilleContribuableSearch;
import ch.globaz.amal.business.models.famille.FamilleContribuableView;
import ch.globaz.amal.business.models.famille.FamilleContribuableViewSearch;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;

/**
 * @author CBU
 * 
 */
public interface FamilleContribuableService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws FamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(FamilleContribuableSearch search) throws FamilleException, JadePersistenceException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws FamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(SimpleFamilleSearch search) throws FamilleException, JadePersistenceException;

    /**
     * Permet la création d'une entité familleContribuable
     * 
     * @param contribuable
     *            La famille à créer
     * @return Le contribuable créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws FamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public FamilleContribuable create(FamilleContribuable familleContribuable) throws JadePersistenceException,
            FamilleException;

    /**
     * Permet l'effacement d'un contribuable
     * 
     * @param contribuable
     *            La famille à effacer
     * @return
     * @throws FamilleException
     * @throws JadePersistenceException
     */
    public FamilleContribuable delete(FamilleContribuable familleContribuable) throws FamilleException,
            JadePersistenceException;

    /**
     * Récupère la liste des membres de la famille pour le calcul des subsides
     * 
     * @param idContribuable
     * @param year
     * @return
     * @throws JadePersistenceException
     * @throws FamilleException
     */
    public ArrayList<Contribuable> famillyListSubside(String idContribuable, String year)
            throws JadePersistenceException, FamilleException;

    /**
     * Recherche d'un membre famille en fonction d'un modèle renseigné ou vide et d'un numéro AVS
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction du numéro
     * AVS
     * 
     * @param currentFamilleSearch
     * @param searchedAVS
     * @return
     */
    public SimpleFamilleSearch getFamilleByAVS(SimpleFamilleSearch currentFamilleSearch, String searchedAVS);

    /**
     * Recherche d'un membre famille en fonction d'un modèle renseigné ou vide et d'une date de naissance
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction d'une date
     * de naissance
     * 
     * @param currentFamilleSearch
     * @param dateNaissanceYYYYMMDD
     * @return
     */
    public SimpleFamilleSearch getFamilleByDateOfBirth(SimpleFamilleSearch currentFamilleSearch,
            String dateNaissanceYYYYMMDD);

    /**
     * Recherche d'un membre famille en fonction d'un modèle renseigné ou vide et d'un nom-prénom
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction du nom -
     * prénom
     * 
     * @param currentFamilleSearch
     * @param searchedFamilyName
     * @param searchedGivenName
     * @return
     */
    public SimpleFamilleSearch getFamilleByFamilyNameGivenName(SimpleFamilleSearch currentFamilleSearch,
            String searchedFamilyName, String searchedGivenName);

    /**
     * Recherche d'un membre famille en fonction d'un modèle renseigné ou vide et d'un numéro NSS
     * 
     * Si le modèle de recherche contient déjà des résultats, parcours des résultats pour affiner en fonction du numéro
     * NSS
     * 
     * @param currentFamilleSearch
     * @param searchedNSS
     * @return
     */
    public SimpleFamilleSearch getFamilleByNSS(SimpleFamilleSearch currentFamilleSearch, String searchedNSS);

    /**
     * Récupère la liste des subsides par membres de la famille pour l'affichage sur la page contribuable
     * 
     * @param idContribuable
     * @param year
     * @return
     * @throws JadePersistenceException
     * @throws FamilleException
     */
    public ArrayList<FamilleContribuableView> getListSubsideMember(String year, String idContribuable)
            throws JadePersistenceException, FamilleException, JadeApplicationServiceNotAvailableException;

    /**
     * Permet de charger en mémoire une familleContribuable
     * 
     * @param idFamilleContribuable
     *            L'identifiant de la famillecontribuable à charger en mémoire
     * @return La famille chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws FamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public FamilleContribuable read(String idFamilleContribuable) throws JadePersistenceException, FamilleException;

    /**
     * Permet de chercher des familles de contribuable selon un modèle de critères.
     * 
     * @param revenuSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws FamilleException
     */
    public FamilleContribuableSearch search(FamilleContribuableSearch familleContribuableSearch)
            throws JadePersistenceException, FamilleException;

    /**
     * Permet de chercher des familles de contribuable selon un modèle de critères.
     * 
     * @param revenuSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RevenuException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws FamilleException
     */
    public FamilleContribuableViewSearch search(FamilleContribuableViewSearch familleContribuableViewSearch)
            throws JadePersistenceException, FamilleException;

    /**
     * Permet la recherche des membres de la famille
     * 
     * @param simpleFamilleSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws FamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleFamilleSearch search(SimpleFamilleSearch simpleFamilleSearch) throws JadePersistenceException,
            FamilleException;

    /**
     * 
     * Permet la mise à jour d'une entité familleContribuable
     * 
     * @param contribuable
     *            La famille à mettre à jour
     * @return Le contribuable mis à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws FamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public FamilleContribuable update(FamilleContribuable familleContribuable) throws JadePersistenceException,
            FamilleException;

}
