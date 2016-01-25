package ch.globaz.pegasus.businessimpl.services.models.renteijapi;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.renteijapi.AutreApiException;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAutreApi;
import ch.globaz.pegasus.business.models.renteijapi.SimpleAutreApiSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.renteijapi.SimpleAutreApiService;
import ch.globaz.pegasus.businessimpl.checkers.renteijapi.SimpleAutreApiChecker;

public class SimpleAutreApiServiceImpl extends PegasusServiceLocator implements SimpleAutreApiService {

    @Override
    public SimpleAutreApi create(SimpleAutreApi autreApi) throws AutreApiException, JadePersistenceException {
        if (autreApi == null) {
            throw new AutreApiException("Unable to create autreApi, the model passed is null!");
        }
        SimpleAutreApiChecker.checkForCreate(autreApi);
        return (SimpleAutreApi) JadePersistenceManager.add(autreApi);
    }

    @Override
    public SimpleAutreApi delete(SimpleAutreApi simpleAutreApi) throws AutreApiException, JadePersistenceException {
        if (simpleAutreApi == null) {
            throw new AutreApiException("Unable to delete simpleAutreApi, the model passed is null!");
        }
        SimpleAutreApiChecker.checkForDelete(simpleAutreApi);
        return (SimpleAutreApi) JadePersistenceManager.delete(simpleAutreApi);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.renteijapi.SimpleAutreApiService
     * #deleteParListeIdDoFinH(java.lang.String)
     */
    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleAutreApiSearch search = new SimpleAutreApiSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);

    }

    @Override
    public SimpleAutreApi read(String idSimpleAutreApi) throws AutreApiException, JadePersistenceException {
        if (idSimpleAutreApi == null) {
            throw new AutreApiException("Unable to read idSimpleAutreApi, the model passed is null!");
        }

        SimpleAutreApi autreApi = new SimpleAutreApi();
        autreApi.setId(idSimpleAutreApi);
        return (SimpleAutreApi) JadePersistenceManager.read(autreApi);
    }

    @Override
    public SimpleAutreApi update(SimpleAutreApi simpleAutreApi) throws AutreApiException, JadePersistenceException {
        if (simpleAutreApi == null) {
            throw new AutreApiException("Unable to update simpleAutreApi, the model passed is null!");
        }
        SimpleAutreApiChecker.checkForUpdate(simpleAutreApi);
        return (SimpleAutreApi) JadePersistenceManager.update(simpleAutreApi);
    }
}
