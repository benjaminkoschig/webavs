package ch.globaz.pegasus.businessimpl.services.models.habitat;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.habitat.TaxeJournaliereHomeException;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeEtendu;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeEtenduSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.habitat.TaxeJournaliereHomeEtenduService;

public class TaxeJournaliereHomeEtenduServiceImpl extends PegasusServiceLocator implements
        TaxeJournaliereHomeEtenduService {

    @Override
    public int count(TaxeJournaliereHomeEtenduSearch search) throws JadePersistenceException,
            TaxeJournaliereHomeException {
        if (search == null) {
            throw new TaxeJournaliereHomeException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public TaxeJournaliereHomeEtendu read(String idTaxeJournaliereHome) throws TaxeJournaliereHomeException,
            JadePersistenceException {
        if (idTaxeJournaliereHome == null) {
            throw new TaxeJournaliereHomeException("Unable to read idTaxeJournaliereHome, the model passed is null!");
        }
        TaxeJournaliereHomeEtendu taxeJournaliereHome = new TaxeJournaliereHomeEtendu();
        taxeJournaliereHome.setId(idTaxeJournaliereHome);
        return (TaxeJournaliereHomeEtendu) JadePersistenceManager.read(taxeJournaliereHome);
    }

    @Override
    public TaxeJournaliereHomeEtenduSearch search(TaxeJournaliereHomeEtenduSearch taxeJournaliereHomeSearch)
            throws JadePersistenceException, TaxeJournaliereHomeException {
        if (taxeJournaliereHomeSearch == null) {
            throw new TaxeJournaliereHomeException(
                    "Unable to search taxeJournaliereHomeSearch, the model passed is null!");
        }
        return (TaxeJournaliereHomeEtenduSearch) JadePersistenceManager.search(taxeJournaliereHomeSearch);
    }

}
