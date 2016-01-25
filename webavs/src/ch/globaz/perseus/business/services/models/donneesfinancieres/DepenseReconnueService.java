package ch.globaz.perseus.business.services.models.donneesfinancieres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnue;
import ch.globaz.perseus.business.models.donneesfinancieres.DepenseReconnueSearchModel;

public interface DepenseReconnueService extends JadeApplicationService {

    public int count(DepenseReconnueSearchModel search) throws DonneesFinancieresException, JadePersistenceException;

    public DepenseReconnue create(DepenseReconnue depenseReconnue) throws JadePersistenceException,
            DonneesFinancieresException;

    public DepenseReconnue delete(DepenseReconnue depenseReconnue) throws JadePersistenceException,
            DonneesFinancieresException;

    public DepenseReconnue read(String idDepenseReconnue) throws JadePersistenceException, DonneesFinancieresException;

    public DepenseReconnueSearchModel search(DepenseReconnueSearchModel searchModel) throws JadePersistenceException,
            DonneesFinancieresException;

    public DepenseReconnue update(DepenseReconnue depenseReconnue) throws JadePersistenceException,
            DonneesFinancieresException;

}
