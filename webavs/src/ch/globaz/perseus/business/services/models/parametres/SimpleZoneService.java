package ch.globaz.perseus.business.services.models.parametres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.parametres.ParametresException;
import ch.globaz.perseus.business.models.parametres.SimpleZone;
import ch.globaz.perseus.business.models.parametres.SimpleZoneSearchModel;

public interface SimpleZoneService extends JadeApplicationService {

    public int count(SimpleZoneSearchModel search) throws ParametresException, JadePersistenceException;

    public SimpleZone create(SimpleZone simpleZone) throws JadePersistenceException, ParametresException;

    public SimpleZone delete(SimpleZone simpleZone) throws JadePersistenceException, ParametresException;

    public SimpleZone read(String idSimpleZone) throws JadePersistenceException, ParametresException;

    public SimpleZoneSearchModel search(SimpleZoneSearchModel searchModel) throws JadePersistenceException,
            ParametresException;

    public SimpleZone update(SimpleZone simpleZone) throws JadePersistenceException, ParametresException;

}
