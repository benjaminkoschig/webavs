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
     * Permet de compter des simpleAllocationNoel selon un mod�le de crit�res.
     * 
     * @param simpleAllocationNoelSerach
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     */
    public int count(SimpleAllocationNoelSearch simpleAllocationNoelSearch) throws JadePersistenceException,
            AllocationDeNoelException;

    /**
     * Permet la cr�ation d'une entit� simpleAllocationNoel
     * 
     * @param simpleAllocationNoel
     *            Le simpleJoursAppoint � cr�er
     * @return La simpleAllocationNoel cr��
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AllocationDeNoelException
     */
    public SimpleAllocationNoel create(SimpleAllocationNoel simpleAllocationNoel) throws JadePersistenceException,
            AllocationDeNoelException;

    /**
     * Permet la suppression d'une entit� simpleAllocationNoel
     * 
     * @param simpleAllocationNoel
     *            La simpleAllocationNoel � supprimer
     * @return Le simpleAllocationNoel supprim�
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AllocationDeNoelException
     */
    public SimpleAllocationNoel delete(SimpleAllocationNoel simpleAllocationNoel) throws JadePersistenceException,
            AllocationDeNoelException;

    public int delete(SimpleAllocationNoelSearch search) throws JadePersistenceException, AllocationDeNoelException;

    /**
     * Permet de charger en m�moire une simpleAllocationNoel
     * 
     * @param idSimpleAllocationNoel
     *            L'identifiant de simpleAllocationNoel � charger en m�moire
     * @return La simpleAllocationNoel charg� en m�moire
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AllocationDeNoelException
     */
    public SimpleAllocationNoel read(String idSimpleAllocationNoel) throws PCAccordeeException,
            JadePersistenceException, AllocationDeNoelException;

    public SimpleAllocationNoel readAllocationNoelByIdPca(String idPca) throws PCAccordeeException,
            AllocationDeNoelException, JadeApplicationServiceNotAvailableException, JadePersistenceException;

    /**
     * Permet de chercher des simpleAllocationNoel selon un mod�le de crit�res.
     * 
     * @param simpleAllocationNoelSerach
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AllocationDeNoelException
     */
    public SimpleAllocationNoelSearch search(SimpleAllocationNoelSearch simpleAllocationNoelSearch)
            throws JadePersistenceException, AllocationDeNoelException;

    /**
     * Permet la mise � jour d'une entit� simpleA
     * 
     * @param simpleAllocationNoel
     *            La simpleAllocationNoel � mettre � jour
     * @return La simpleAllocationNoel mis � jour
     * 
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws AllocationDeNoelException
     */
    public SimpleAllocationNoel update(SimpleAllocationNoel simpleAllocationNoel) throws JadePersistenceException,
            AllocationDeNoelException;
}
