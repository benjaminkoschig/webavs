package ch.globaz.pegasus.business.services.models.pcaccordee;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.AllocationDeNoelException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoelSearch;
import ch.globaz.pegasus.business.models.process.allocationsNoel.PCAccordeePopulation;

/**
 * Declaration du service de gestion des allocations de noel
 * 
 * @author sce
 * 
 */
public interface SimpleAllocationDeNoelService extends JadeApplicationService {

    public BigDecimal computeAndGetMontantAllocation(int nbresMembre, PCAccordee pca, float montantAllocation);

    /**
     * Calucl du total de l'allocation de noel et retour de la valeur
     * 
     * @param accordeePopulation
     *            instance de PCAccordeepopulation
     * @param montantAllocation
     *            montant de l'allocation standard
     * @return monta, la montant de l'allocation pour la PC au format String
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws AllocationDeNoelException
     */
    public BigDecimal[] computeAndGetMontantAllocation(int nbreMembres, PCAccordeePopulation accordeePopulation,
            Float montantAllocation) throws AllocationDeNoelException;

    /**
     * Permet de compter des simpleAllocationNoel selon un modèle de critères.
     * 
     * @param simpleAllocationNoelSerach
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     */
    public int count(SimpleAllocationNoelSearch simpleAllocationNoelSearch) throws JadePersistenceException,
            AllocationDeNoelException;

    /**
     * Permet la création d'une entité simpleAllocationNoel
     * 
     * @param simpleAllocationNoel
     *            Le simpleJoursAppoint à créer
     * @return La simpleAllocationNoel créé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationDeNoelException
     */
    public SimpleAllocationNoel create(SimpleAllocationNoel simpleAllocationNoel) throws JadePersistenceException,
            AllocationDeNoelException;

    /**
     * Permet la suppression d'une entité simpleAllocationNoel
     * 
     * @param simpleAllocationNoel
     *            La simpleAllocationNoel à supprimer
     * @return Le simpleAllocationNoel supprimé
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationDeNoelException
     */
    public SimpleAllocationNoel delete(SimpleAllocationNoel simpleAllocationNoel) throws JadePersistenceException,
            AllocationDeNoelException;

    public int delete(SimpleAllocationNoelSearch search) throws JadePersistenceException, AllocationDeNoelException;

    /**
     * Permet de charger en mémoire une simpleAllocationNoel
     * 
     * @param idSimpleAllocationNoel
     *            L'identifiant de simpleAllocationNoel à charger en mémoire
     * @return La simpleAllocationNoel chargé en mémoire
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationDeNoelException
     */
    public SimpleAllocationNoel read(String idSimpleAllocationNoel) throws PCAccordeeException,
            JadePersistenceException, AllocationDeNoelException;

    public SimpleAllocationNoel readAllocationNoelByIdPca(String idPca) throws PCAccordeeException,
            AllocationDeNoelException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet de chercher des simpleAllocationNoel selon un modèle de critères.
     * 
     * @param simpleAllocationNoelSerach
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationDeNoelException
     */
    public SimpleAllocationNoelSearch search(SimpleAllocationNoelSearch simpleAllocationNoelSearch)
            throws JadePersistenceException, AllocationDeNoelException;

    /**
     * Permet la mise à jour d'une entité simpleA
     * 
     * @param simpleAllocationNoel
     *            La simpleAllocationNoel à mettre à jour
     * @return La simpleAllocationNoel mis à jour
     * 
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws AllocationDeNoelException
     */
    public SimpleAllocationNoel update(SimpleAllocationNoel simpleAllocationNoel) throws JadePersistenceException,
            AllocationDeNoelException;
}
