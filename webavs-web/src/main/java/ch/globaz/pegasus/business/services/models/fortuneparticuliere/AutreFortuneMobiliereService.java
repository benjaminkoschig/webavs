/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AutreFortuneMobiliereException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AutreFortuneMobiliereSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface AutreFortuneMobiliereService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws AutreFortuneMobiliereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(AutreFortuneMobiliereSearch search) throws AutreFortuneMobiliereException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité autreFortuneMobiliere
     * 
     * @param autreFortuneMobiliere
     *            L'entité autreFortuneMobiliere à créer
     * @return L'entité autreFortuneMobiliere créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutreFortuneMobiliere create(AutreFortuneMobiliere autreFortuneMobiliere) throws JadePersistenceException,
            AutreFortuneMobiliereException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité autreFortuneMobiliere
     * 
     * @param autreFortuneMobiliere
     *            L'entité autreFortuneMobiliere à supprimer
     * @return L'entité autreFortuneMobiliere supprimé
     * @throws AutreFortuneMobiliereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public AutreFortuneMobiliere delete(AutreFortuneMobiliere autreFortuneMobiliere)
            throws AutreFortuneMobiliereException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité autreFortuneMobiliere
     * 
     * @param idAutreFortuneMobiliere
     *            L'identifiant de l'entité autreFortuneMobiliere à charger en mémoire
     * @return L'entité autreFortuneMobiliere chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutreFortuneMobiliere read(String idAutreFortuneMobiliere) throws JadePersistenceException,
            AutreFortuneMobiliereException;

    /**
     * Chargement d'un AutreFortuneMobiliere via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutreFortuneMobiliereException
     * @throws JadePersistenceException
     */
    public AutreFortuneMobiliere readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AutreFortuneMobiliereException, JadePersistenceException;

    /**
     * Permet de chercher des autreFortuneMobiliere selon un modèle de critères.
     * 
     * @param autreFortuneMobiliereSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AutreFortuneMobiliereSearch search(AutreFortuneMobiliereSearch autreFortuneMobiliereSearch)
            throws JadePersistenceException, AutreFortuneMobiliereException;

    /**
     * 
     * Permet la mise à jour d'une entité autreFortuneMobiliere
     * 
     * @param autreFortuneMobiliere
     *            L'entité autreFortuneMobiliere à mettre à jour
     * @return L'entité autreFortuneMobiliere mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AutreFortuneMobiliereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public AutreFortuneMobiliere update(AutreFortuneMobiliere autreFortuneMobiliere) throws JadePersistenceException,
            AutreFortuneMobiliereException, DonneeFinanciereException;
}
