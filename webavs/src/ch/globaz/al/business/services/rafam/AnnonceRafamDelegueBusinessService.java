package ch.globaz.al.business.services.rafam;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;

public interface AnnonceRafamDelegueBusinessService extends JadeApplicationService {

    public String[] getListeEmployeursDelegue() throws JadeApplicationException, JadePersistenceException;

    public boolean isAnnoncesInDb() throws JadeApplicationException, JadePersistenceException;

    public void validationCafAnnonce(String idAnnonce) throws JadeApplicationException, JadePersistenceException;

    public void validationCafAnnonces(String idEmployeur) throws JadeApplicationException, JadePersistenceException;
}
