package ch.globaz.pegasus.businessimpl.services.models.annonce.annoncelaprams;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.annonce.AnnonceException;
import ch.globaz.pegasus.business.models.annonce.SimpleAnnonceLapramsDonneeFinanciereHeader;
import ch.globaz.pegasus.business.models.annonce.SimpleAnnonceLapramsDonneeFinanciereHeaderSearch;
import ch.globaz.pegasus.business.services.models.annonce.annoncelaprams.SimpleAnnonceLapramsDoFinHService;

public class SimpleAnnonceLapramsDoFinHServiceImpl implements SimpleAnnonceLapramsDoFinHService {

    @Override
    public SimpleAnnonceLapramsDonneeFinanciereHeader create(
            SimpleAnnonceLapramsDonneeFinanciereHeader simpleAnnonceLapramsDonneeFinanciereHeader)
            throws JadePersistenceException, AnnonceException {
        if (simpleAnnonceLapramsDonneeFinanciereHeader == null) {
            throw new AnnonceException(
                    "Unable to create simpleAnnonceLapramsDonneeFinanciereHeader, the model passed is null!");
        }
        return (SimpleAnnonceLapramsDonneeFinanciereHeader) JadePersistenceManager
                .add(simpleAnnonceLapramsDonneeFinanciereHeader);
    }

    @Override
    public SimpleAnnonceLapramsDonneeFinanciereHeader delete(
            SimpleAnnonceLapramsDonneeFinanciereHeader simpleAnnonceLapramsDonneeFinanciereHeader)
            throws JadePersistenceException, AnnonceException {
        if (simpleAnnonceLapramsDonneeFinanciereHeader == null) {
            throw new AnnonceException(
                    "Unable to delete simpleAnnonceLapramsDonneeFinanciereHeader, the model passed is null!");
        }

        return (SimpleAnnonceLapramsDonneeFinanciereHeader) JadePersistenceManager
                .delete(simpleAnnonceLapramsDonneeFinanciereHeader);
    }

    @Override
    public void deleteByIdAnnonce(List<String> listIdAnnonceLaprams) throws AnnonceException, JadePersistenceException {
        if (listIdAnnonceLaprams == null) {
            throw new AnnonceException("Unable to deleteByIdAnnonce listIdAnnonceDfh, the model passed is null!");
        }
        SimpleAnnonceLapramsDonneeFinanciereHeaderSearch search = new SimpleAnnonceLapramsDonneeFinanciereHeaderSearch();
        search.setInIdsAnnonceHeader(listIdAnnonceLaprams);
        JadePersistenceManager.delete(search);
    }

    @Override
    public SimpleAnnonceLapramsDonneeFinanciereHeader read(String idSimpleAnnonceLapramsDonneeFinanciereHeader)
            throws JadePersistenceException, AnnonceException {
        if (idSimpleAnnonceLapramsDonneeFinanciereHeader == null) {
            throw new AnnonceException(
                    "Unable to read idSimpleAnnonceLapramsDonneeFinanciereHeader, the model passed is null!");
        }
        SimpleAnnonceLapramsDonneeFinanciereHeader simplePrestation = new SimpleAnnonceLapramsDonneeFinanciereHeader();
        simplePrestation.setId(idSimpleAnnonceLapramsDonneeFinanciereHeader);
        return (SimpleAnnonceLapramsDonneeFinanciereHeader) JadePersistenceManager.read(simplePrestation);

    }

    @Override
    public SimpleAnnonceLapramsDonneeFinanciereHeader update(
            SimpleAnnonceLapramsDonneeFinanciereHeader simpleAnnonceLapramsDonneeFinanciereHeader)
            throws JadePersistenceException, AnnonceException {
        if (simpleAnnonceLapramsDonneeFinanciereHeader == null) {
            throw new AnnonceException(
                    "Unable to update simpleAnnonceLapramsDonneeFinanciereHeader, the model passed is null!");
        }

        return (SimpleAnnonceLapramsDonneeFinanciereHeader) JadePersistenceManager
                .update(simpleAnnonceLapramsDonneeFinanciereHeader);
    }

}
