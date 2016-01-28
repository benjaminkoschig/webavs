package ch.globaz.pegasus.businessimpl.services.models.annonce.annoncelaprams;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.lot.PrestationException;
import ch.globaz.pegasus.business.models.annonce.SimpleAnnonceLaprams;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.SimpleAnnonceLapramsService;
import ch.globaz.pegasus.businessimpl.checkers.annonce.annoncelaprams.SimpleAnnonceLapramsChecker;

public class SimpleAnnonceLapramsServiceImpl implements SimpleAnnonceLapramsService {

    @Override
    public SimpleAnnonceLaprams create(SimpleAnnonceLaprams simpleAnnonceLapramsDonneeFinanciereHeader)
            throws JadePersistenceException, PrestationException {
        if (simpleAnnonceLapramsDonneeFinanciereHeader == null) {
            throw new PrestationException("Unable to create simpleAnnonceLaprams, the model passed is null!");
        }
        SimpleAnnonceLapramsChecker.checkForCreate(simpleAnnonceLapramsDonneeFinanciereHeader);
        return (SimpleAnnonceLaprams) JadePersistenceManager.add(simpleAnnonceLapramsDonneeFinanciereHeader);
    }

    @Override
    public SimpleAnnonceLaprams delete(SimpleAnnonceLaprams simpleAnnonceLapramsDonneeFinanciereHeader)
            throws JadePersistenceException, PrestationException {
        if (simpleAnnonceLapramsDonneeFinanciereHeader == null) {
            throw new PrestationException("Unable to delete simpleAnnonceLaprams, the model passed is null!");
        }
        SimpleAnnonceLapramsChecker.checkForDelete(simpleAnnonceLapramsDonneeFinanciereHeader);

        return (SimpleAnnonceLaprams) JadePersistenceManager.delete(simpleAnnonceLapramsDonneeFinanciereHeader);
    }

    @Override
    public SimpleAnnonceLaprams read(String idSimpleAnnonceLaprams) throws JadePersistenceException,
            PrestationException {
        if (idSimpleAnnonceLaprams == null) {
            throw new PrestationException("Unable to read idSimpleAnnonceLaprams, the model passed is null!");
        }
        SimpleAnnonceLaprams simplePrestation = new SimpleAnnonceLaprams();
        simplePrestation.setId(idSimpleAnnonceLaprams);
        return (SimpleAnnonceLaprams) JadePersistenceManager.read(simplePrestation);

    }

    @Override
    public SimpleAnnonceLaprams update(SimpleAnnonceLaprams simpleAnnonceLaprams) throws JadePersistenceException,
            PrestationException {
        if (simpleAnnonceLaprams == null) {
            throw new PrestationException("Unable to update simpleAnnonceLaprams, the model passed is null!");
        }
        SimpleAnnonceLapramsChecker.checkForUpdate(simpleAnnonceLaprams);

        return (SimpleAnnonceLaprams) JadePersistenceManager.update(simpleAnnonceLaprams);
    }

}
