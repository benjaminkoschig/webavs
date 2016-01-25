package ch.globaz.pegasus.business.services.models.revenusdepenses;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.AllocationsFamilialesException;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesEtendu;
import ch.globaz.pegasus.business.models.revenusdepenses.AllocationsFamilialesEtenduSearch;

public interface AllocationsFamilialesEtenduService extends JadeApplicationService {
    /**
     * Permet de compter le nombre d'enregistrements correspondant au modèle de recherche
     * 
     * @param search
     *            modèle de recherche
     * @return nombre d'enregistrements trouvés
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     */
    public int count(AllocationsFamilialesEtenduSearch search) throws AllocationsFamilialesException,
            JadePersistenceException;

    /**
     * Permet de charger en mémoire d'une entité AllocationsFamiliales
     * 
     * @param idAllocationsFamiliales
     *            L'identifiant de l'entité AllocationsFamiliales à charger en mémoire
     * @return L'entité AllocationsFamiliales chargée en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AllocationsFamilialesEtendu read(String idAllocationsFamiliales) throws JadePersistenceException,
            AllocationsFamilialesException;

    /**
     * Permet de chercher des AllocationsFamiliales selon un modèle de critères.
     * 
     * @param AllocationsFamilialesSearch
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationsFamilialesException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public AllocationsFamilialesEtenduSearch search(AllocationsFamilialesEtenduSearch allocationsFamilialesSearch)
            throws JadePersistenceException, AllocationsFamilialesException;

}