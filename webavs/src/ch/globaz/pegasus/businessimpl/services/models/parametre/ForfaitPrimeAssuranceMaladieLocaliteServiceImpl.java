package ch.globaz.pegasus.businessimpl.services.models.parametre;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.ForfaitPrimeAssuranceMaladieLocaliteSearch;
import ch.globaz.pegasus.business.services.models.parametre.ForfaitPrimeAssuranceMaladieLocaliteService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class ForfaitPrimeAssuranceMaladieLocaliteServiceImpl extends PegasusAbstractServiceImpl implements
        ForfaitPrimeAssuranceMaladieLocaliteService {

    @Override
    public int count(ForfaitPrimeAssuranceMaladieLocaliteSearch search) throws ForfaitsPrimesAssuranceMaladieException,
            JadePersistenceException {
        if (search == null) {
            throw new ForfaitsPrimesAssuranceMaladieException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public ForfaitPrimeAssuranceMaladieLocaliteSearch search(ForfaitPrimeAssuranceMaladieLocaliteSearch search)
            throws ForfaitsPrimesAssuranceMaladieException, JadePersistenceException {
        if (search == null) {
            throw new ForfaitsPrimesAssuranceMaladieException("Unable to search search, the model passed is null!");
        }
        if (JadeStringUtil.isEmpty(search.getForDateFin()) || search.getForDateFin().equals("0")) {
            search.setForDateFin("99999999");
        }
        return (ForfaitPrimeAssuranceMaladieLocaliteSearch) JadePersistenceManager.search(search);
    }
}
