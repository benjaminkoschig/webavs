package ch.globaz.pegasus.businessimpl.services.models.habitat;

import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.habitat.SejourMoisPartielHomeException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHome;
import ch.globaz.pegasus.business.models.habitat.SejourMoisPartielHomeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.habitat.SejourMoisPartielHomeService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class SejourMoisPartielHomeServiceImpl extends PegasusServiceLocator implements SejourMoisPartielHomeService {

    @Override
    public int count(SejourMoisPartielHomeSearch search) throws JadePersistenceException, SejourMoisPartielHomeException {
        if (search == null) {
            throw new SejourMoisPartielHomeException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public SejourMoisPartielHome create(SejourMoisPartielHome sejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException, DonneeFinanciereException {

        if (sejourMoisPartielHome == null) {
            throw new SejourMoisPartielHomeException("Unable to create sejourMoisPartielHome, the model passed is null!");
        }
        try {
            // creation du donneeFinanciereHeader
            sejourMoisPartielHome.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            sejourMoisPartielHome.getSimpleDonneeFinanciereHeader()));

            if (JadeStringUtil.isEmpty(sejourMoisPartielHome.getSimpleDonneeFinanciereHeader().getDateFin())) {
                JadeThread.logError(sejourMoisPartielHome.getSimpleDonneeFinanciereHeader().getClass().getName(),
                        "pegasus.sejourMoisPartielHome.datefin.mandatory");
            }

            // creation du simpleSejourMoisPartielHome
            sejourMoisPartielHome.setSimpleSejourMoisPartielHome((PegasusImplServiceLocator
                    .getSimpleSejourMoisPartielHomeService().create(sejourMoisPartielHome.getSimpleSejourMoisPartielHome())));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SejourMoisPartielHomeException("Service not available - " + e.getMessage());
        }
        return sejourMoisPartielHome;
    }

    @Override
    public SejourMoisPartielHome delete(SejourMoisPartielHome sejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException, DonneeFinanciereException {
        try {
            // effacement du donneeFinanciereHeader
            sejourMoisPartielHome.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().delete(
                            sejourMoisPartielHome.getSimpleDonneeFinanciereHeader()));

            // effacement du simpleSejourMoisPartielHome
            sejourMoisPartielHome.setSimpleSejourMoisPartielHome(PegasusImplServiceLocator
                    .getSimpleSejourMoisPartielHomeService().delete(sejourMoisPartielHome.getSimpleSejourMoisPartielHome()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SejourMoisPartielHomeException("Service not available - " + e.getMessage());
        }
        return sejourMoisPartielHome;
    }

    @Override
    public SejourMoisPartielHome read(String idSejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException {
        if (idSejourMoisPartielHome == null) {
            throw new SejourMoisPartielHomeException("Unable to read idSejourMoisPartielHome, the model passed is null!");
        }
        SejourMoisPartielHome sejourMoisPartielHome = new SejourMoisPartielHome();
        sejourMoisPartielHome.setId(idSejourMoisPartielHome);
        return (SejourMoisPartielHome) JadePersistenceManager.read(sejourMoisPartielHome);
    }

    /**
     * Chargement d'une SejourMoisPartielHome via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws SejourMoisPartielHomeException
     * @throws JadePersistenceException
     */
    @Override
    public SejourMoisPartielHome readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws SejourMoisPartielHomeException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new SejourMoisPartielHomeException(
                    "Unable to find SejourMoisPartielHome the idDonneeFinanciereHeader passed si null!");
        }

        SejourMoisPartielHomeSearch search = new SejourMoisPartielHomeSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (SejourMoisPartielHomeSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new SejourMoisPartielHomeException("More than one SejourMoisPartielHome find, one was exepcted!");
        }

        return (SejourMoisPartielHome) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((SejourMoisPartielHomeSearch) donneeFinanciereSearch);
    }

    @Override
    public SejourMoisPartielHomeSearch search(SejourMoisPartielHomeSearch sejourMoisPartielHomeSearch)
            throws JadePersistenceException, SejourMoisPartielHomeException {
        if (sejourMoisPartielHomeSearch == null) {
            throw new SejourMoisPartielHomeException(
                    "Unable to search sejourMoisPartielHomeSearch, the model passed is null!");
        }
        return (SejourMoisPartielHomeSearch) JadePersistenceManager.search(sejourMoisPartielHomeSearch);
    }

    @Override
    public SejourMoisPartielHome update(SejourMoisPartielHome sejourMoisPartielHome) throws SejourMoisPartielHomeException,
            JadePersistenceException, DonneeFinanciereException {

        if (sejourMoisPartielHome == null) {
            throw new SejourMoisPartielHomeException("Unable to update sejourMoisPartielHome, the model passed is null!");
        }

        try {
            // mise a jour du donneeFinanciereHeader
            sejourMoisPartielHome.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            sejourMoisPartielHome.getSimpleDonneeFinanciereHeader()));

            if (JadeStringUtil.isEmpty(sejourMoisPartielHome.getSimpleDonneeFinanciereHeader().getDateFin())) {
                JadeThread.logError(sejourMoisPartielHome.getSimpleDonneeFinanciereHeader().getClass().getName(),
                        "pegasus.sejourMoisPartielHome.datefin.mandatory");
            }

            // mise a jour du simpleSejourMoisPartielHome
            sejourMoisPartielHome.setSimpleSejourMoisPartielHome((PegasusImplServiceLocator
                    .getSimpleSejourMoisPartielHomeService().update(sejourMoisPartielHome.getSimpleSejourMoisPartielHome())));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new SejourMoisPartielHomeException("Service not available - " + e.getMessage());
        }
        return sejourMoisPartielHome;
    }

}
