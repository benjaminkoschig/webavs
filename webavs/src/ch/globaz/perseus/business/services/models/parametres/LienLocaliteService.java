package ch.globaz.perseus.business.services.models.parametres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.parametres.ParametresException;
import ch.globaz.perseus.business.models.parametres.LienLocalite;
import ch.globaz.perseus.business.models.parametres.LienLocaliteSearchModel;

public interface LienLocaliteService extends JadeApplicationService {

    public int count(LienLocaliteSearchModel search) throws ParametresException, JadePersistenceException;

    public LienLocalite create(LienLocalite lienLocalite) throws JadePersistenceException, ParametresException;

    public LienLocalite delete(LienLocalite lienLocalite) throws JadePersistenceException, ParametresException;

    public LienLocalite read(String idLienLocalite) throws JadePersistenceException, ParametresException;

    public LienLocaliteSearchModel search(LienLocaliteSearchModel searchModel) throws JadePersistenceException,
            ParametresException;

    public LienLocalite update(LienLocalite lienLocalite) throws JadePersistenceException, ParametresException;

}
