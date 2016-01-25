package ch.globaz.corvus.business.services.models.blocage;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.corvus.business.exceptions.CorvusException;
import ch.globaz.corvus.business.models.blocage.SimpleEnteteBlocage;

public interface SimpleEnteteBlocageService extends JadeApplicationService {
    public SimpleEnteteBlocage create(SimpleEnteteBlocage model) throws CorvusException, JadePersistenceException;

    public SimpleEnteteBlocage delete(SimpleEnteteBlocage model) throws CorvusException, JadePersistenceException;

    public SimpleEnteteBlocage read(String idInformationsComptabilite) throws CorvusException, JadePersistenceException;

    public SimpleEnteteBlocage update(SimpleEnteteBlocage model) throws CorvusException, JadePersistenceException;
}
