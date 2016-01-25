package ch.globaz.perseus.business.services.models.parametres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.parametres.ParametresException;
import ch.globaz.perseus.business.models.parametres.SimpleLoyer;

public interface SimpleLoyerService extends JadeApplicationService {

    public SimpleLoyer create(SimpleLoyer simpleLoyer) throws JadePersistenceException, ParametresException;

    public SimpleLoyer delete(SimpleLoyer simpleLoyer) throws JadePersistenceException, ParametresException;

    public SimpleLoyer read(String idLoyer) throws JadePersistenceException, ParametresException;

    public SimpleLoyer update(SimpleLoyer simpleLoyer) throws JadePersistenceException, ParametresException;

}
