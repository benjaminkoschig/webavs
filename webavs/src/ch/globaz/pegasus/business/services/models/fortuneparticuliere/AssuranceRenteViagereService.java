/**
 * 
 */
package ch.globaz.pegasus.business.services.models.fortuneparticuliere;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneparticuliere.AssuranceRenteViagereException;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagere;
import ch.globaz.pegasus.business.models.fortuneparticuliere.AssuranceRenteViagereSearch;
import ch.globaz.pegasus.business.services.models.droit.AbstractDonneeFinanciereService;

/**
 * @author BSC
 * 
 */
public interface AssuranceRenteViagereService extends JadeApplicationService, AbstractDonneeFinanciereService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(AssuranceRenteViagereSearch search) throws AssuranceRenteViagereException,
            JadePersistenceException;

    /**
     * Permet la création d'une entité assuranceRenteViagere
     * 
     * @param assuranceRenteViagere
     *            L'entité assuranceRenteViagere à créer
     * @return L'entité assuranceRenteViagere créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AssuranceRenteViagere create(AssuranceRenteViagere assuranceRenteViagere) throws JadePersistenceException,
            AssuranceRenteViagereException, DonneeFinanciereException;

    /**
     * Permet la suppression d'une entité assuranceRenteViagere
     * 
     * @param assuranceRenteViagere
     *            L'entité assuranceRenteViagere à supprimer
     * @return L'entité assuranceRenteViagere supprimé
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public AssuranceRenteViagere delete(AssuranceRenteViagere assuranceRenteViagere)
            throws AssuranceRenteViagereException, JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité assuranceRenteViagere
     * 
     * @param idAssuranceRenteViagere
     *            L'identifiant de l'entité assuranceRenteViagere à charger en mémoire
     * @return L'entité assuranceRenteViagere chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AssuranceRenteViagere read(String idAssuranceRenteViagere) throws JadePersistenceException,
            AssuranceRenteViagereException;

    /**
     * Chargement d'un AssuranceRenteViagere via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AssuranceRenteViagereException
     * @throws JadePersistenceException
     */
    public AssuranceRenteViagere readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws AssuranceRenteViagereException, JadePersistenceException;

    /**
     * Permet de chercher des assuranceRenteViagere selon un modèle de critères.
     * 
     * @param assuranceRenteViagereSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AssuranceRenteViagereSearch search(AssuranceRenteViagereSearch assuranceRenteViagereSearch)
            throws JadePersistenceException, AssuranceRenteViagereException;

    /**
     * 
     * Permet la mise à jour d'une entité assuranceRenteViagere
     * 
     * @param assuranceRenteViagere
     *            L'entité assuranceRenteViagere à mettre à jour
     * @return L'entité assuranceRenteViagere mise à jour
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AssuranceRenteViagereException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws DonneeFinanciereException
     */
    public AssuranceRenteViagere update(AssuranceRenteViagere assuranceRenteViagere) throws JadePersistenceException,
            AssuranceRenteViagereException, DonneeFinanciereException;
}
