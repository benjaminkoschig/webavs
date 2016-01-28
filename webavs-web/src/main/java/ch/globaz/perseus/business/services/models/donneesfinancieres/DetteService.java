package ch.globaz.perseus.business.services.models.donneesfinancieres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.Dette;
import ch.globaz.perseus.business.models.donneesfinancieres.DetteSearchModel;

public interface DetteService extends JadeApplicationService {

    public int count(DetteSearchModel search) throws DonneesFinancieresException, JadePersistenceException;

    public Dette create(Dette dette) throws JadePersistenceException, DonneesFinancieresException;

    public Dette delete(Dette dette) throws JadePersistenceException, DonneesFinancieresException;

    public Dette read(String idDette) throws JadePersistenceException, DonneesFinancieresException;

    public DetteSearchModel search(DetteSearchModel searchModel) throws JadePersistenceException,
            DonneesFinancieresException;

    public Dette update(Dette dette) throws JadePersistenceException, DonneesFinancieresException;

}
