package ch.globaz.perseus.business.services.models.informationfacture;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.informationfacture.InformationFactureException;
import ch.globaz.perseus.business.models.informationfacture.InformationFacture;
import ch.globaz.perseus.business.models.informationfacture.InformationFactureSearchModel;

public interface InformationFactureService extends JadeApplicationService {
    public int count(InformationFactureSearchModel search) throws InformationFactureException, JadePersistenceException;

    public InformationFacture create(InformationFacture informationfacture) throws JadePersistenceException,
            InformationFactureException, JadeApplicationServiceNotAvailableException;

    public InformationFacture delete(InformationFacture informationfacture) throws JadePersistenceException,
            InformationFactureException, JadeApplicationServiceNotAvailableException;

    public InformationFacture read(String idInformationFacture) throws JadePersistenceException,
            InformationFactureException;

    public InformationFactureSearchModel search(InformationFactureSearchModel searchModel)
            throws JadePersistenceException, InformationFactureException;

    public InformationFacture update(InformationFacture informationfacture) throws JadePersistenceException,
            InformationFactureException, JadeApplicationServiceNotAvailableException;

}
