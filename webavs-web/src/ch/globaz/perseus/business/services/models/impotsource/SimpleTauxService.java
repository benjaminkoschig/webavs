package ch.globaz.perseus.business.services.models.impotsource;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.impotsource.TauxException;
import ch.globaz.perseus.business.models.impotsource.SimpleTaux;
import ch.globaz.perseus.business.models.impotsource.SimpleTauxSearchModel;

public interface SimpleTauxService extends JadeApplicationService {

    public SimpleTaux create(SimpleTaux simpleTaux) throws JadePersistenceException, TauxException;

    public SimpleTaux delete(SimpleTaux simpleTaux) throws JadePersistenceException, TauxException;

    public int delete(SimpleTauxSearchModel simpleTauxSearchModel) throws JadePersistenceException, TauxException;

    public SimpleTaux read(String idTaux) throws JadePersistenceException, TauxException;

    public SimpleTaux update(SimpleTaux simpleTaux) throws JadePersistenceException, TauxException;

}
