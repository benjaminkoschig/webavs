package ch.globaz.pegasus.businessimpl.services.models.assurancemaladie;

import ch.globaz.pegasus.business.exceptions.models.assurancemaladie.PrimeAssuranceMaladieException;
import ch.globaz.pegasus.business.models.assurancemaladie.SimplePrimeAssuranceMaladie;
import ch.globaz.pegasus.business.models.assurancemaladie.SimplePrimeAssuranceMaladieSearch;
import ch.globaz.pegasus.business.services.models.assurancemaladie.SimplePrimeAssuranceMaladieService;
import ch.globaz.pegasus.businessimpl.checkers.assurancemaladie.SimplePrimeAssuranceMaladieChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;

import java.util.List;

public class SimplePrimeAssuranceMaladieServiceImpl extends PegasusAbstractServiceImpl implements SimplePrimeAssuranceMaladieService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. simplePrimeAssuranceMaladie
     * #create(ch.globaz.pegasus.business.models.assurancemaladie .simplePrimeAssuranceMaladie)
     */
    @Override
    public SimplePrimeAssuranceMaladie create(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) throws JadePersistenceException,
            PrimeAssuranceMaladieException {
        if (simplePrimeAssuranceMaladie == null) {
            throw new PrimeAssuranceMaladieException("Unable to create simplePrimeAssuranceMaladie, the model passed is null!");
        }
        SimplePrimeAssuranceMaladieChecker.checkForCreate(simplePrimeAssuranceMaladie);
        return (SimplePrimeAssuranceMaladie) JadePersistenceManager.add(simplePrimeAssuranceMaladie);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. simplePrimeAssuranceMaladie
     * #delete(ch.globaz.pegasus.business.models.assurancemaladie .simplePrimeAssuranceMaladie)
     */
    @Override
    public SimplePrimeAssuranceMaladie delete(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) throws PrimeAssuranceMaladieException,
            JadePersistenceException {
        if (simplePrimeAssuranceMaladie == null) {
            throw new PrimeAssuranceMaladieException("Unable to delete simplePrimeAssuranceMaladie, the model passed is null!");
        }
        if (simplePrimeAssuranceMaladie.isNew()) {
            throw new PrimeAssuranceMaladieException("Unable to delete simplePrimeAssuranceMaladie, the model passed is new!");
        }
        SimplePrimeAssuranceMaladieChecker.checkForDelete(simplePrimeAssuranceMaladie);
        return (SimplePrimeAssuranceMaladie) JadePersistenceManager.delete(simplePrimeAssuranceMaladie);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimplePrimeAssuranceMaladieSearch search = new SimplePrimeAssuranceMaladieSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimplePrimeAssuranceMaladie#read(java.lang.String)
     */
    @Override
    public SimplePrimeAssuranceMaladie read(String idPrimeAssuranceMaladie) throws JadePersistenceException, PrimeAssuranceMaladieException {
        if (JadeStringUtil.isEmpty(idPrimeAssuranceMaladie)) {
            throw new PrimeAssuranceMaladieException("Unable to read simplePrimeAssuranceMaladie, the id passed is not defined!");
        }
        SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie = new SimplePrimeAssuranceMaladie();
        simplePrimeAssuranceMaladie.setId(idPrimeAssuranceMaladie);
        return (SimplePrimeAssuranceMaladie) JadePersistenceManager.read(simplePrimeAssuranceMaladie);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.assurancemaladie. simplePrimeAssuranceMaladieService
     * #update(ch.globaz.pegasus.business.models.assurancemaladie .simplePrimeAssuranceMaladie)
     */
    @Override
    public SimplePrimeAssuranceMaladie update(SimplePrimeAssuranceMaladie simplePrimeAssuranceMaladie) throws JadePersistenceException,
            PrimeAssuranceMaladieException {
        if (simplePrimeAssuranceMaladie == null) {
            throw new PrimeAssuranceMaladieException("Unable to update simplePrimeAssuranceMaladie, the model passed is null!");
        }
        if (simplePrimeAssuranceMaladie.isNew()) {
            throw new PrimeAssuranceMaladieException("Unable to update simplePrimeAssuranceMaladie, the model passed is new!");
        }
        SimplePrimeAssuranceMaladieChecker.checkForUpdate(simplePrimeAssuranceMaladie);
        return (SimplePrimeAssuranceMaladie) JadePersistenceManager.update(simplePrimeAssuranceMaladie);
    }

}
