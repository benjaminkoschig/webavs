package ch.globaz.pegasus.businessimpl.services.models.parametre;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleForfaitPrimesAssuranceMaladie;
import ch.globaz.pegasus.business.models.parametre.SimpleForfaitPrimesAssuranceMaladieSearch;
import ch.globaz.pegasus.business.services.models.parametre.SimpleForfaitPrimesAssuranceMaladieService;
import ch.globaz.pegasus.businessimpl.checkers.parametre.SimpleForfaitPrimesAssuranceMaladieChecker;

/**
 * @author DMA
 * @date 12 nov. 2010
 */
public class SimpleForfaitPrimesAssuranceMaladieServiceImpl implements SimpleForfaitPrimesAssuranceMaladieService {

    @Override
    public int count(SimpleForfaitPrimesAssuranceMaladieSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException {
        if (search == null) {
            throw new ForfaitsPrimesAssuranceMaladieException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleForfaitPrimesAssuranceMaladie create(SimpleForfaitPrimesAssuranceMaladie simpleForfaitAssMal)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (simpleForfaitAssMal == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to create simpleForfaitAssMal, the model passed is null!");
        }
        SimpleForfaitPrimesAssuranceMaladieChecker.checkForCreate(simpleForfaitAssMal);
        return (SimpleForfaitPrimesAssuranceMaladie) JadePersistenceManager.add(simpleForfaitAssMal);
    }

    @Override
    public SimpleForfaitPrimesAssuranceMaladie delete(SimpleForfaitPrimesAssuranceMaladie simpleForfaitAssMal)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException {
        if (simpleForfaitAssMal == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to delete simpleForfaitAssMal, the model passed is null!");
        }

        return (SimpleForfaitPrimesAssuranceMaladie) JadePersistenceManager.delete(simpleForfaitAssMal);
    }

    @Override
    public SimpleForfaitPrimesAssuranceMaladie read(String idSimplePrimeFofaiteAssurenceMaladie)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException {
        if (idSimplePrimeFofaiteAssurenceMaladie == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to read idSimplePrimeFofaiteAssurenceMaladie, the model passed is null!");
        }
        SimpleForfaitPrimesAssuranceMaladie forfait = new SimpleForfaitPrimesAssuranceMaladie();
        forfait.setId(idSimplePrimeFofaiteAssurenceMaladie);
        return (SimpleForfaitPrimesAssuranceMaladie) JadePersistenceManager.read(forfait);
    }

    @Override
    public SimpleForfaitPrimesAssuranceMaladieSearch search(
            SimpleForfaitPrimesAssuranceMaladieSearch simpleForfaitAssMalSearch)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException {
        if (simpleForfaitAssMalSearch == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to search simpleForfaitAssMalSearch, the model passed is null!");
        }
        return (SimpleForfaitPrimesAssuranceMaladieSearch) JadePersistenceManager.search(simpleForfaitAssMalSearch);
    }

    @Override
    public SimpleForfaitPrimesAssuranceMaladie update(SimpleForfaitPrimesAssuranceMaladie simpleForfaitAssMal)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException,
            JadeApplicationServiceNotAvailableException {
        if (simpleForfaitAssMal == null) {
            throw new ForfaitsPrimesAssuranceMaladieException(
                    "Unable to update simpleForfaitAssMal, the model passed is null!");
        }
        SimpleForfaitPrimesAssuranceMaladieChecker.checkForUpdate(simpleForfaitAssMal);
        return (SimpleForfaitPrimesAssuranceMaladie) JadePersistenceManager.update(simpleForfaitAssMal);
    }
}
