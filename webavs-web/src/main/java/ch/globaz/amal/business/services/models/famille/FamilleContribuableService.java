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
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws FamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(FamilleContribuableSearch search) throws FamilleException, JadePersistenceException;

    /**
     * Permet de compter le nombre d'enregistrements correspondant au mod�le de recherche
     * 
     * @param search
     *            mod�le de recherche
     * @return nombre d'enregistrements trouv�s
     * @throws FamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public int count(SimpleFamilleSearch search) throws FamilleException, JadePersistenceException;

    /**
     * Permet la cr�ation d'une entit� familleContribuable
     * 
     * @param contribuable
     *            La famille � cr�er
     * @return Le contribuable cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public FamilleContribuable create(FamilleContribuable familleContribuable) throws JadePersistenceException,
            FamilleException;

    /**
     * Permet l'effacement d'un contribuable
     * 
     * @param contribuable
     *            La famille � effacer
     * @return
     * @throws FamilleException
     * @throws JadePersistenceException
     */
    public FamilleContribuable delete(FamilleContribuable familleContribuable) throws FamilleException,
            JadePersistenceException;

    /**
     * R�cup�re la liste des membres de la famille pour le calcul des subsides
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
     * Recherche d'un membre famille en fonction d'un mod�le renseign� ou vide et d'un num�ro AVS
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction du num�ro
     * AVS
     * 
     * @param currentFamilleSearch
     * @param searchedAVS
     * @return
     */
    public SimpleFamilleSearch getFamilleByAVS(SimpleFamilleSearch currentFamilleSearch, String searchedAVS);

    /**
     * Recherche d'un membre famille en fonction d'un mod�le renseign� ou vide et d'une date de naissance
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction d'une date
     * de naissance
     * 
     * @param currentFamilleSearch
     * @param dateNaissanceYYYYMMDD
     * @return
     */
    public SimpleFamilleSearch getFamilleByDateOfBirth(SimpleFamilleSearch currentFamilleSearch,
            String dateNaissanceYYYYMMDD);

    /**
     * Recherche d'un membre famille en fonction d'un mod�le renseign� ou vide et d'un nom-pr�nom
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction du nom -
     * pr�nom
     * 
     * @param currentFamilleSearch
     * @param searchedFamilyName
     * @param searchedGivenName
     * @return
     */
    public SimpleFamilleSearch getFamilleByFamilyNameGivenName(SimpleFamilleSearch currentFamilleSearch,
            String searchedFamilyName, String searchedGivenName);

    /**
     * Recherche d'un membre famille en fonction d'un mod�le renseign� ou vide et d'un num�ro NSS
     * 
     * Si le mod�le de recherche contient d�j� des r�sultats, parcours des r�sultats pour affiner en fonction du num�ro
     * NSS
     * 
     * @param currentFamilleSearch
     * @param searchedNSS
     * @return
     */
    public SimpleFamilleSearch getFamilleByNSS(SimpleFamilleSearch currentFamilleSearch, String searchedNSS);

    /**
     * R�cup�re la liste des subsides par membres de la famille pour l'affichage sur la page contribuable
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
     * Permet de charger en m�moire une familleContribuable
     * 
     * @param idFamilleContribuable
     *            L'identifiant de la famillecontribuable � charger en m�moire
     * @return La famille charg� en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public FamilleContribuable read(String idFamilleContribuable) throws JadePersistenceException, FamilleException;

    /**
     * Permet de chercher des familles de contribuable selon un mod�le de crit�res.
     * 
     * @param revenuSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws FamilleException
     */
    public FamilleContribuableSearch search(FamilleContribuableSearch familleContribuableSearch)
            throws JadePersistenceException, FamilleException;

    /**
     * Permet de chercher des familles de contribuable selon un mod�le de crit�res.
     * 
     * @param revenuSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RevenuException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws FamilleException
     */
    public FamilleContribuableViewSearch search(FamilleContribuableViewSearch familleContribuableViewSearch)
            throws JadePersistenceException, FamilleException;

    /**
     * Permet la recherche des membres de la famille
     * 
     * @param simpleFamilleSearch
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleFamilleSearch search(SimpleFamilleSearch simpleFamilleSearch) throws JadePersistenceException,
            FamilleException;

    /**
     * 
     * Permet la mise � jour d'une entit� familleContribuable
     * 
     * @param contribuable
     *            La famille � mettre � jour
     * @return Le contribuable mis � jour
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public FamilleContribuable update(FamilleContribuable familleContribuable) throws JadePersistenceException,
            FamilleException;

}
