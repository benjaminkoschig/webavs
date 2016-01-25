package ch.globaz.perseus.business.services.models.parametres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.parametres.ParametresException;
import ch.globaz.perseus.business.models.parametres.SimpleLienLocalite;

public interface SimpleLienLocaliteService extends JadeApplicationService {

    public SimpleLienLocalite create(SimpleLienLocalite simpleLienLocalite) throws JadePersistenceException,
            ParametresException;

    public SimpleLienLocalite delete(SimpleLienLocalite simpleLienLocalite) throws JadePersistenceException,
            ParametresException;

    public SimpleLienLocalite read(String idLienLocalite) throws JadePersistenceException, ParametresException;

    public SimpleLienLocalite update(SimpleLienLocalite simpleLienLocalite) throws JadePersistenceException,
            ParametresException;

}
