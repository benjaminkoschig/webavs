package ch.globaz.amal.business.services.models.famille;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import java.util.Collection;
import ch.globaz.amal.business.exceptions.models.famille.FamilleException;
import ch.globaz.amal.business.models.famille.SimpleFamille;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;

/**
 * @author CBU
 */
public interface SimpleFamilleService extends JadeApplicationService {
    /**
     * Permet de compter le nombre de membre famille
     * 
     * @param famille
     *            la famille a compter
     * @return le nombre de membres
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public int count(SimpleFamilleSearch familleSearch) throws JadePersistenceException, FamilleException;

    /**
     * Permet la cr�ation d'une famille
     * 
     * @param famille
     *            la famille a cr�er
     * @return la famille cr�e
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws FamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public SimpleFamille create(SimpleFamille famille) throws JadePersistenceException, FamilleException;

    /**
     * Permet la suppression d'une entit� famille
     * 
     * @param famille
     *            La famille � supprimer
     * @return La famille supprim�e
     * @throws FamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleFamille delete(SimpleFamille Famille) throws FamilleException, JadePersistenceException;

    /**
     * Retourne le chef de famille (contribuable principal)
     * 
     * @param idFamille
     * @return
     * @throws FamilleException
     * @throws JadePersistenceException
     */
    public SimpleFamilleSearch getChefDeFamille(String idContribuable) throws FamilleException,
            JadePersistenceException;

    /**
     * Retourne la liste de enfant (contribuable principal)
     * 
     * @param idContribuable
     * @return
     * @throws FamilleException
     * @throws JadePersistenceException
     */
    public Collection<SimpleFamille> getFamilleListEnfants(String idContribuable) throws FamilleException,
            JadePersistenceException;

    /**
     * Permet de charger en m�moire une famille
     * 
     * @param idFamille
     *            L'identifiant de la Famille � charger en m�moire
     * @return La famille charg�e en m�moire
     * @throws FamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleFamille read(String idFamille) throws FamilleException, JadePersistenceException;

    /**
     * Permet la recherche des membres de la famille
     * 
     * @param simpleFamilleSearch
     * @return les membres de la famille
     * @throws FamilleException
     * @throws JadePersistenceException
     */
    public SimpleFamilleSearch search(SimpleFamilleSearch simpleFamilleSearch) throws FamilleException,
            JadePersistenceException;

    /**
     * Permet la mise � jour d'une entit� Famille
     * 
     * @param famille
     *            La famille � mettre � jour
     * @return La famille mise � jour
     * @throws FamilleException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     */
    public SimpleFamille update(SimpleFamille famille) throws FamilleException, JadePersistenceException;
}
