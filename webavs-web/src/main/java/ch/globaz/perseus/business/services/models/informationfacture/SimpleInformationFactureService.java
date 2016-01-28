package ch.globaz.perseus.business.services.models.informationfacture;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.informationfacture.InformationFactureException;
import ch.globaz.perseus.business.models.informationfacture.SimpleInformationFacture;
import ch.globaz.perseus.business.models.informationfacture.SimpleInformationFactureSearchModel;

public interface SimpleInformationFactureService extends JadeApplicationService {
    public int count(SimpleInformationFactureSearchModel searchModel) throws JadePersistenceException,
            InformationFactureException;

    public SimpleInformationFacture create(SimpleInformationFacture informationFacture)
            throws JadePersistenceException, InformationFactureException;

    public SimpleInformationFacture delete(SimpleInformationFacture informationFacture)
            throws JadePersistenceException, InformationFactureException;

    public SimpleInformationFacture read(String idInformationFacture) throws JadePersistenceException,
            InformationFactureException;

    public SimpleInformationFactureSearchModel search(SimpleInformationFactureSearchModel searchModel)
            throws JadePersistenceException, InformationFactureException;

    public SimpleInformationFacture update(SimpleInformationFacture informationFacture)
            throws JadePersistenceException, InformationFactureException;
}
