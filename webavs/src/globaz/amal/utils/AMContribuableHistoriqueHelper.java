package globaz.amal.utils;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.amal.business.exceptions.models.contribuable.ContribuableException;
import ch.globaz.amal.business.models.contribuable.SimpleContribuableInfos;
import ch.globaz.amal.business.services.AmalServiceLocator;

public class AMContribuableHistoriqueHelper {
    private static SimpleContribuableInfos contribuableInfos = null;

    public static SimpleContribuableInfos getContribuableInfos() {
        return AMContribuableHistoriqueHelper.contribuableInfos;
    }

    public static SimpleContribuableInfos loadDataFromHistorique(String idContribuable) throws ContribuableException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        AMContribuableHistoriqueHelper.contribuableInfos = new SimpleContribuableInfos();
        AMContribuableHistoriqueHelper.contribuableInfos.setIdContribuable(idContribuable);
        AMContribuableHistoriqueHelper.contribuableInfos = AmalServiceLocator.getContribuableService().readInfos(
                idContribuable);
        return AMContribuableHistoriqueHelper.contribuableInfos;
    }
}
