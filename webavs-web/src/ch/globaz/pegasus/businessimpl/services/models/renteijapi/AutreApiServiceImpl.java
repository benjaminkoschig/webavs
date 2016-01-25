package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreApiException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.renteijapi.AutreApi;
import ch.globaz.pegasus.business.models.renteijapi.AutreApiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.renteijapi.AutreApiService;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class AutreApiServiceImpl extends PegasusServiceLocator implements AutreApiService {

    @Override
    public int count(AutreApiSearch search) throws AutreApiException, JadePersistenceException {
        if (search == null) {
            throw new AutreApiException("Unable to count search, the model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public AutreApi create(AutreApi autreApi) throws AutreApiException, JadePersistenceException,
            DonneeFinanciereException {
        if (autreApi == null) {
            throw new AutreApiException("Unable to create autreApi, the model passed is null!");
        }
        try {
            // creation du donneeFinanciereHeader
            autreApi.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .create(autreApi.getSimpleDonneeFinanciereHeader()));

            // creation du simpleAutreApi
            autreApi.setSimpleAutreApi((PegasusImplServiceLocator.getSimpleAutreApiService().create(autreApi
                    .getSimpleAutreApi())));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutreApiException("Service not available - " + e.getMessage());
        }
        return autreApi;
    }

    @Override
    public AutreApi delete(AutreApi autreApi) throws AutreApiException, JadePersistenceException,
            DonneeFinanciereException {
        try {
            // effacement du donneeFinanciereHeader
            autreApi.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .delete(autreApi.getSimpleDonneeFinanciereHeader()));

            // effacement du simpleAutreApi
            autreApi.setSimpleAutreApi(PegasusImplServiceLocator.getSimpleAutreApiService().delete(
                    autreApi.getSimpleAutreApi()));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutreApiException("Service not available - " + e.getMessage());
        }
        return autreApi;
    }

    @Override
    public AutreApi read(String idAutreApi) throws AutreApiException, JadePersistenceException {
        if (idAutreApi == null) {
            throw new AutreApiException("Unable to read idAutreApi, the model passed is null!");
        }
        AutreApi autreApi = new AutreApi();
        autreApi.setId(idAutreApi);
        return (AutreApi) JadePersistenceManager.read(autreApi);
    }

    /**
     * Chargement d'une AutreApi via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws AutreApiException
     * @throws JadePersistenceException
     */
    @Override
    public AutreApi readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws AutreApiException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new AutreApiException("Unable to find AutreApi the idDonneeFinanciereHeader passed si null!");
        }

        AutreApiSearch search = new AutreApiSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (AutreApiSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new AutreApiException("More than one AutreApi find, one was exepcted!");
        }

        return (AutreApi) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((AutreApiSearch) donneeFinanciereSearch);
    }

    @Override
    public AutreApiSearch search(AutreApiSearch autreApiSearch) throws JadePersistenceException, AutreApiException {
        if (autreApiSearch == null) {
            throw new AutreApiException("Unable to search autreApiSearch, the model passed is null!");
        }
        return (AutreApiSearch) JadePersistenceManager.search(autreApiSearch);
    }

    @Override
    public AutreApi update(AutreApi autreApi) throws AutreApiException, JadePersistenceException,
            DonneeFinanciereException {
        if (autreApi == null) {
            throw new AutreApiException("Unable to update autreApi, the model passed is null!");
        }

        try {
            // mise a jour du donneeFinanciereHeader
            autreApi.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator.getSimpleDonneeFinanciereHeaderService()
                    .update(autreApi.getSimpleDonneeFinanciereHeader()));

            // mise a jour du simpleAureApi
            autreApi.setSimpleAutreApi((PegasusImplServiceLocator.getSimpleAutreApiService().update(autreApi
                    .getSimpleAutreApi())));

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new AutreApiException("Service not available - " + e.getMessage());
        }
        return autreApi;
    }

}
