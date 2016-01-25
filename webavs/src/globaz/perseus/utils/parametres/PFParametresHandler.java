package globaz.perseus.utils.parametres;

import globaz.jade.persistence.model.JadeAbstractModel;
import ch.globaz.perseus.business.models.parametres.SimpleZoneSearchModel;
import ch.globaz.perseus.business.services.PerseusServiceLocator;

public class PFParametresHandler {

    public static JadeAbstractModel[] getListZone() throws Exception {
        SimpleZoneSearchModel simpleSearchMopdel = new SimpleZoneSearchModel();

        simpleSearchMopdel = PerseusServiceLocator.getSimpleZoneService().search(simpleSearchMopdel);

        return simpleSearchMopdel.getSearchResults();
    }

}
