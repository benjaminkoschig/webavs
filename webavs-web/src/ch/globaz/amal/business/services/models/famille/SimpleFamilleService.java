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
     *             Levée en cas de problème dans la couche de persistence
     * @throws FamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public int count(SimpleFamilleSearch familleSearch) throws JadePersistenceException, FamilleException;

    /**
     * Permet la création d'une famille
     * 
     * @param famille
     *            la famille a créer
     * @return la famille crée
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws FamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public SimpleFamille create(SimpleFamille famille) throws JadePersistenceException, FamilleException;

    /**
     * Permet la suppression d'une entité famille
     * 
     * @param famille
     *            La famille à supprimer
     * @return La famille supprimée
     * @throws FamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
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
     * Permet de charger en mémoire une famille
     * 
     * @param idFamille
     *            L'identifiant de la Famille à charger en mémoire
     * @return La famille chargée en mémoire
     * @throws FamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
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
     * Permet la mise à jour d'une entité Famille
     * 
     * @param famille
     *            La famille à mettre à jour
     * @return La famille mise à jour
     * @throws FamilleException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public SimpleFamille update(SimpleFamille famille) throws FamilleException, JadePersistenceException;
}
