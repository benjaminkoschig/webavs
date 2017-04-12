package ch.globaz.amal.businessimpl.services.sedexCO;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import ch.globaz.amal.business.services.sedexCO.AnnoncesCOService;
import ch.globaz.pyxis.business.model.AdministrationSearchComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

public class AnnoncesCOServiceImpl extends AnnoncesCOReceptionMessage5232_000202_1 implements AnnoncesCOService {

    @Override
    public AdministrationSearchComplexModel find(AdministrationSearchComplexModel searchModel)
            throws JadePersistenceException, JadeApplicationException {
        return TIBusinessServiceLocator.getAdministrationService().find(searchModel);
    }

}
