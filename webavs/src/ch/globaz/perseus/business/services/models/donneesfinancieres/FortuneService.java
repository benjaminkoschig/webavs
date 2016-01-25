package ch.globaz.perseus.business.services.models.donneesfinancieres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.Fortune;
import ch.globaz.perseus.business.models.donneesfinancieres.FortuneSearchModel;

public interface FortuneService extends JadeApplicationService {

    public int count(FortuneSearchModel search) throws DonneesFinancieresException, JadePersistenceException;

    public Fortune create(Fortune fortune) throws JadePersistenceException, DonneesFinancieresException;

    public Fortune delete(Fortune fortune) throws JadePersistenceException, DonneesFinancieresException;

    public Fortune read(String idFortune) throws JadePersistenceException, DonneesFinancieresException;

    public FortuneSearchModel search(FortuneSearchModel searchModel) throws JadePersistenceException,
            DonneesFinancieresException;

    public Fortune update(Fortune fortune) throws JadePersistenceException, DonneesFinancieresException;

}
