package ch.globaz.pegasus.businessimpl.services.models.annonce.communicationocc;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.common.domaine.Checkers;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.annonce.SimpleCommunicationOCC;
import ch.globaz.pegasus.business.models.annonce.SimpleCommunicationOCCSearch;
import ch.globaz.pegasus.business.services.models.annonce.communicationocc.SimpleCommunicationOCCService;
import ch.globaz.pegasus.businessimpl.checkers.annonce.communicationocc.SimpleCommunicationOCCChecker;

public class SimpleCommunicationOCCServiceImpl implements SimpleCommunicationOCCService {

    @Override
    public SimpleCommunicationOCC create(SimpleCommunicationOCC simpleCommunicationOCC) throws JadePersistenceException {
        Checkers.checkNotNull(simpleCommunicationOCC, "simpleCommunicationOCC");
        SimpleCommunicationOCCChecker.checkForCreate(simpleCommunicationOCC);
        return (SimpleCommunicationOCC) JadePersistenceManager.add(simpleCommunicationOCC);
    }

    @Override
    public SimpleCommunicationOCC delete(SimpleCommunicationOCC simpleCommunicationOCC)
            throws JadePersistenceException, PrestationException {
        if (simpleCommunicationOCC == null) {
            throw new PrestationException("Unable to delete simpleCommunicationOCC, the model passed is null!");
        }
        SimpleCommunicationOCCChecker.checkForDelete(simpleCommunicationOCC);

        return (SimpleCommunicationOCC) JadePersistenceManager.delete(simpleCommunicationOCC);
    }

    @Override
    public int delete(SimpleCommunicationOCCSearch search) throws JadePersistenceException, PrestationException {
        if (search == null) {
            throw new PrestationException("Unable to delete simpleCommunicationOCC, the model passed is null!");
        }
        return JadePersistenceManager.delete(search);
    }

    @Override
    public SimpleCommunicationOCC read(String idSimpleCommunicationOCC) throws JadePersistenceException,
            PrestationException {
        if (idSimpleCommunicationOCC == null) {
            throw new PrestationException("Unable to read idSimpleCommunicationOCC, the model passed is null!");
        }
        SimpleCommunicationOCC simplePrestation = new SimpleCommunicationOCC();
        simplePrestation.setId(idSimpleCommunicationOCC);
        return (SimpleCommunicationOCC) JadePersistenceManager.read(simplePrestation);

    }

    @Override
    public SimpleCommunicationOCC update(SimpleCommunicationOCC simpleCommunicationOCC)
            throws JadePersistenceException, PrestationException {
        if (simpleCommunicationOCC == null) {
            throw new PrestationException("Unable to update simpleCommunicationOCC, the model passed is null!");
        }
        SimpleCommunicationOCCChecker.checkForUpdate(simpleCommunicationOCC);

        return (SimpleCommunicationOCC) JadePersistenceManager.update(simpleCommunicationOCC);
    }

}
