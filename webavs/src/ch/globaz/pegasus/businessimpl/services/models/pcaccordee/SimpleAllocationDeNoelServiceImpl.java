package ch.globaz.pegasus.businessimpl.services.models.pcaccordee;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.AllocationDeNoelException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoelSearch;
import ch.globaz.pegasus.business.models.process.allocationsNoel.PCAccordeePopulation;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.pcaccordee.SimpleAllocationDeNoelService;
import ch.globaz.pegasus.businessimpl.checkers.pcaccordee.SimpleAllocationNoelChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleAllocationDeNoelServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleAllocationDeNoelService {

    @Override
    public BigDecimal computeAndGetMontantAllocation(int nbresMembre, PCAccordee pca, float montantAllocation) {

        BigDecimal montant = new BigDecimal(0);
        // type de PCAccordee, si type --> PC A DOMICILE
        if (pca.getSimplePCAccordee().getCsGenrePC().equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
            montant = new BigDecimal(nbresMembre * montantAllocation);
        } else {
            // nbreMembres = 1;
            montant = new BigDecimal(montantAllocation);
        }

        return montant;
    }

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
    @Override
    public BigDecimal[] computeAndGetMontantAllocation(int nbreMembres, PCAccordeePopulation accordeePopulation,
            Float montantAllocation) throws AllocationDeNoelException {

        BigDecimal montant = new BigDecimal(0);
        BigDecimal montantConjoint = new BigDecimal(0);
        BigDecimal montantTotal = new BigDecimal(0);
        boolean isDom2R = false; // Définit si la pc est un dom2R

        // type de PCAccordee, si type --> PC A DOMICILE
        if (accordeePopulation.getSimplePCAccordee().getCsGenrePC().equals(IPCPCAccordee.CS_GENRE_PC_DOMICILE)) {
            isDom2R = !JadeStringUtil.isBlankOrZero(accordeePopulation.getSimplePCAccordee()
                    .getIdPrestationAccordeeConjoint());

            montant = new BigDecimal((nbreMembres) * montantAllocation);
            montantTotal = new BigDecimal(montant.intValue());

            if (nbreMembres <= 1 && isDom2R) {
                throw new AllocationDeNoelException(
                        "Données incohérentes : Un cas DOM2R a été détecté mais le nombre de personnes comprise dans le calcul est inférieur à deux");
            }
            // il y a une PC conjoint (DOM2R)
            if (isDom2R) {

                // on divise le montant par deux, separation conjoint/requérant
                montant = (montant.divide(new BigDecimal(2)));// / 2;
                montantConjoint = montant;

                int rest = montant.subtract(montant).intValue();

                if (rest > 0) {
                    montant = new BigDecimal(Math.ceil(montant.floatValue()));
                    montantConjoint = new BigDecimal(Math.floor(montantConjoint.floatValue()));
                }
            }
            // pas de conjoint, montant total requérant
        }
        // Si PC home
        else {
            montant = new BigDecimal(montantAllocation);
            montantTotal = new BigDecimal(montant.intValue());
        }
        return new BigDecimal[] { montant, montantConjoint, montantTotal };
    }

    @Override
    public int count(SimpleAllocationNoelSearch simpleAllocationNoelSearch) throws JadePersistenceException,
            AllocationDeNoelException {
        if (simpleAllocationNoelSearch == null) {
            throw new AllocationDeNoelException("Unable to count the allocation de noel, the model passed is null!");
        }
        return JadePersistenceManager.count(simpleAllocationNoelSearch);
    }

    @Override
    public SimpleAllocationNoel create(SimpleAllocationNoel simpleAllocationNoel) throws JadePersistenceException,
            AllocationDeNoelException {
        if (simpleAllocationNoel == null) {
            throw new AllocationDeNoelException("Unable to create the allocation de noel, the model passed is null!");
        }
        SimpleAllocationNoelChecker.checkForCreate(simpleAllocationNoel);
        return (SimpleAllocationNoel) JadePersistenceManager.add(simpleAllocationNoel);
    }

    @Override
    public SimpleAllocationNoel delete(SimpleAllocationNoel simpleAllocationNoel) throws JadePersistenceException,
            AllocationDeNoelException {
        if (simpleAllocationNoel == null) {
            throw new AllocationDeNoelException("Unable to delete the allocation de noel, the model passed is null!");
        }
        SimpleAllocationNoelChecker.checkForDelete(simpleAllocationNoel);
        return (SimpleAllocationNoel) JadePersistenceManager.delete(simpleAllocationNoel);
    }

    @Override
    public int delete(SimpleAllocationNoelSearch search) throws JadePersistenceException, AllocationDeNoelException {
        if (search == null) {
            throw new AllocationDeNoelException("Unable to delete the allocation de noel, the model passed is null!");
        }
        return JadePersistenceManager.delete(search);
    }

    @Override
    public SimpleAllocationNoel read(String idSimpleAllocationNoel) throws JadePersistenceException,
            AllocationDeNoelException {
        if (idSimpleAllocationNoel == null) {
            throw new AllocationDeNoelException("Unable to read the allocation de noel, the id passed is null!");
        }
        SimpleAllocationNoel simpleAllocationNoel = new SimpleAllocationNoel();
        simpleAllocationNoel.setId(idSimpleAllocationNoel);
        return (SimpleAllocationNoel) JadePersistenceManager.read(simpleAllocationNoel);
    }

    @Override
    public SimpleAllocationNoel readAllocationNoelByIdPca(String idPca) throws PCAccordeeException,
            AllocationDeNoelException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleAllocationNoelSearch search = new SimpleAllocationNoelSearch();
        search.setForIdPcAccordee(idPca);
        search = PegasusServiceLocator.getSimpleAllocationDeNoelService().search(search);
        if (search.getSize() > 0) {
            if (search.getSize() > 1) {
                throw new PCAccordeeException("Too many allocationNoel founded with this idPca:" + idPca);
            }
            return (SimpleAllocationNoel) search.getSearchResults()[0];
        }
        return null;
    }

    @Override
    public SimpleAllocationNoelSearch search(SimpleAllocationNoelSearch simpleAllocationNoelSearch)
            throws JadePersistenceException, AllocationDeNoelException {
        if (simpleAllocationNoelSearch == null) {
            throw new AllocationDeNoelException(
                    "Unable to search the allocation de noel, the search model passed is null!");

        }

        return (SimpleAllocationNoelSearch) JadePersistenceManager.search(simpleAllocationNoelSearch);
    }

    @Override
    public SimpleAllocationNoel update(SimpleAllocationNoel simpleAllocationNoel) throws JadePersistenceException,
            AllocationDeNoelException {
        if (simpleAllocationNoel == null) {
            throw new AllocationDeNoelException("Unable to update the allocation de noel, the model passed is null!");

        }
        SimpleAllocationNoelChecker.checkForUpdate(simpleAllocationNoel);
        return (SimpleAllocationNoel) JadePersistenceManager.update(simpleAllocationNoel);
    }

}
