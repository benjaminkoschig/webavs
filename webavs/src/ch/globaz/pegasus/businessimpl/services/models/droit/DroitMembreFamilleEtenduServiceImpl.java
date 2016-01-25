package ch.globaz.pegasus.businessimpl.services.models.droit;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtendu;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamilleEtenduSearch;
import ch.globaz.pegasus.business.services.models.droit.DroitMembreFamilleEtenduService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class DroitMembreFamilleEtenduServiceImpl extends PegasusAbstractServiceImpl implements
        DroitMembreFamilleEtenduService {

    @Override
    public int count(DroitMembreFamilleEtenduSearch search) throws DroitException, JadePersistenceException {
        if (search == null) {
            throw new DroitException("Unable to count search, the model passed is null!");
        }

        return JadePersistenceManager.count(search);
    }

    @Override
    public DroitMembreFamilleEtendu read(String idDroitMembreFamilleEtendu) throws DroitException,
            JadePersistenceException {
        if (idDroitMembreFamilleEtendu == null) {
            throw new DroitException("Unable to read idDroitMembreFamilleEtendu, the model passed is null!");
        }
        DroitMembreFamilleEtendu droitMembreFamilleEtendu = new DroitMembreFamilleEtendu();
        droitMembreFamilleEtendu.setId(idDroitMembreFamilleEtendu);
        return (DroitMembreFamilleEtendu) JadePersistenceManager.read(droitMembreFamilleEtendu);

    }

    @Override
    public DroitMembreFamilleEtenduSearch search(DroitMembreFamilleEtenduSearch droitMembreFamilleEtenduSearch)
            throws DroitException, JadePersistenceException {
        if (droitMembreFamilleEtenduSearch == null) {
            throw new DroitException("Unable to search droitMembreFamilleEtenduSearch, the model passed is null!");
        }
        return (DroitMembreFamilleEtenduSearch) JadePersistenceManager.search(droitMembreFamilleEtenduSearch);
    }

}
