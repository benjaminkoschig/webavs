package ch.globaz.pegasus.businessimpl.services.models.lot;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.lot.OrdreVersementException;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersement;
import ch.globaz.pegasus.business.models.lot.SimpleOrdreVersementSearch;
import ch.globaz.pegasus.business.services.models.lot.SimpleOrdreVersementService;

/**
 * @author DMA
 * 
 */
public class SimpleOrdreVersementServiceImpl implements SimpleOrdreVersementService {

    @Override
    public int count(SimpleOrdreVersementSearch search) throws OrdreVersementException, JadePersistenceException {
        if (search == null) {
            throw new OrdreVersementException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleOrdreVersement create(SimpleOrdreVersement simpleOrdreVersement) throws OrdreVersementException,
            JadePersistenceException {
        if (simpleOrdreVersement == null) {
            throw new OrdreVersementException("Unable to create simpleOrdreVersement, the model passed is null!");
        }
        return (SimpleOrdreVersement) JadePersistenceManager.add(simpleOrdreVersement);
    }

    @Override
    public SimpleOrdreVersement delete(SimpleOrdreVersement simpleOrdreVersement) throws OrdreVersementException,
            JadePersistenceException {
        if (simpleOrdreVersement == null) {
            throw new OrdreVersementException("Unable to delete simpleOrdreVersement, the model passed is null!");
        }

        return (SimpleOrdreVersement) JadePersistenceManager.delete(simpleOrdreVersement);
    }

    @Override
    public int delete(SimpleOrdreVersementSearch simpleOrdreVersementSearch) throws OrdreVersementException,
            JadePersistenceException {
        if (simpleOrdreVersementSearch == null) {
            throw new OrdreVersementException("Unable to delete simpleOrdreVersementSearch, the model passed is null!");
        }

        return JadePersistenceManager.delete(simpleOrdreVersementSearch);
    }

    @Override
    public SimpleOrdreVersement read(String idSimpleOrdreVersement) throws OrdreVersementException,
            JadePersistenceException {
        if (idSimpleOrdreVersement == null) {
            throw new OrdreVersementException("Unable to read idSimpleOrdreVersement, the model passed is null!");
        }
        SimpleOrdreVersement simpleOrdreVersement = new SimpleOrdreVersement();
        simpleOrdreVersement.setId(idSimpleOrdreVersement);
        return (SimpleOrdreVersement) JadePersistenceManager.read(simpleOrdreVersement);

    }

    @Override
    public SimpleOrdreVersementSearch search(SimpleOrdreVersementSearch simpleOrdreVersementSearch)
            throws OrdreVersementException, JadePersistenceException {
        if (simpleOrdreVersementSearch == null) {
            throw new OrdreVersementException("Unable to search simpleOrdreVersementSearch, the model passed is null!");
        }
        return (SimpleOrdreVersementSearch) JadePersistenceManager.search(simpleOrdreVersementSearch);
    }

    @Override
    public SimpleOrdreVersement update(SimpleOrdreVersement simpleOrdreVersement) throws OrdreVersementException,
            JadePersistenceException {
        if (simpleOrdreVersement == null) {
            throw new OrdreVersementException("Unable to update simpleOrdreVersement, the model passed is null!");
        }

        return (SimpleOrdreVersement) JadePersistenceManager.update(simpleOrdreVersement);
    }

}
