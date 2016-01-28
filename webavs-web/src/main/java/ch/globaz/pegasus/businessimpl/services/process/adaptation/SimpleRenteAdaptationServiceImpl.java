package ch.globaz.pegasus.businessimpl.services.process.adaptation;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.process.RenteAdapationDemandeException;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleRenteAdaptation;
import ch.globaz.pegasus.business.models.process.adaptation.SimpleRenteAdaptationSearch;
import ch.globaz.pegasus.business.services.process.adaptation.SimpleRenteAdaptationService;

public class SimpleRenteAdaptationServiceImpl implements SimpleRenteAdaptationService {

    @Override
    public int count(SimpleRenteAdaptationSearch search) throws RenteAdapationDemandeException,
            JadePersistenceException {
        if (search == null) {
            throw new RenteAdapationDemandeException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleRenteAdaptation create(SimpleRenteAdaptation simpleRenteAdaptation)
            throws RenteAdapationDemandeException, JadePersistenceException {
        if (simpleRenteAdaptation == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to create simpleRenteAdaptation, the model passed is null!");
        }
        return (SimpleRenteAdaptation) JadePersistenceManager.add(simpleRenteAdaptation);
    }

    @Override
    public SimpleRenteAdaptation delete(SimpleRenteAdaptation simpleRenteAdaptation)
            throws RenteAdapationDemandeException, JadePersistenceException {
        if (simpleRenteAdaptation == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to delete simpleRenteAdaptation, the model passed is null!");
        }

        return (SimpleRenteAdaptation) JadePersistenceManager.delete(simpleRenteAdaptation);
    }

    @Override
    public SimpleRenteAdaptation read(String idsimpleRenteAdaptation) throws RenteAdapationDemandeException,
            JadePersistenceException {
        if (idsimpleRenteAdaptation == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to read idsimpleRenteAdaptation, the model passed is null!");
        }
        SimpleRenteAdaptation SimpleRenteAdaptation = new SimpleRenteAdaptation();
        SimpleRenteAdaptation.setId(idsimpleRenteAdaptation);
        return (SimpleRenteAdaptation) JadePersistenceManager.read(SimpleRenteAdaptation);

    }

    @Override
    public SimpleRenteAdaptationSearch search(SimpleRenteAdaptationSearch simpleRenteAdaptationSearch)
            throws RenteAdapationDemandeException, JadePersistenceException {
        if (simpleRenteAdaptationSearch == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to search simpleRenteAdaptationSearch, the model passed is null!");
        }
        return (SimpleRenteAdaptationSearch) JadePersistenceManager.search(simpleRenteAdaptationSearch);
    }

    @Override
    public SimpleRenteAdaptation update(SimpleRenteAdaptation simpleRenteAdaptation)
            throws RenteAdapationDemandeException, JadePersistenceException {
        if (simpleRenteAdaptation == null) {
            throw new RenteAdapationDemandeException(
                    "Unable to update simpleRenteAdaptation, the model passed is null!");
        }

        return (SimpleRenteAdaptation) JadePersistenceManager.update(simpleRenteAdaptation);
    }

}
