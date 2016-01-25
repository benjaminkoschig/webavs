package globaz.pegasus.utils;

import globaz.globall.db.BSession;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.parametre.ForfaitsPrimesAssuranceMaladieException;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaits;
import ch.globaz.pegasus.business.models.parametre.SimpleZoneForfaitsSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;

public class PCParametreHandler {

    public static String getDescriptionZone(SimpleZoneForfaits simpleZoneForfaits, BSession objSession) {
        String descZone = null;
        if (simpleZoneForfaits != null) {
            descZone = simpleZoneForfaits.getDesignation() + " - "
                    + objSession.getCodeLibelle(simpleZoneForfaits.getCsCanton());
        }
        return descZone;
    }

    public static JadeAbstractModel[] getListZoneFofaits() throws ForfaitsPrimesAssuranceMaladieException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleZoneForfaitsSearch search = new SimpleZoneForfaitsSearch();
        PegasusServiceLocator.getParametreServicesLocator().getSimpleZoneForfaitsService().search(search);
        return search.getSearchResults();
    }

}
