package ch.globaz.perseus.business.services.models.donneesfinancieres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.Revenu;
import ch.globaz.perseus.business.models.donneesfinancieres.RevenuSearchModel;

public interface RevenuService extends JadeApplicationService {

    public int count(RevenuSearchModel search) throws DonneesFinancieresException, JadePersistenceException;

    public Revenu create(Revenu revenu) throws JadePersistenceException, DonneesFinancieresException;

    public Revenu delete(Revenu revenu) throws JadePersistenceException, DonneesFinancieresException;

    public Revenu read(String idRevenu) throws JadePersistenceException, DonneesFinancieresException;

    public RevenuSearchModel search(RevenuSearchModel searchModel) throws JadePersistenceException,
            DonneesFinancieresException;

    public Revenu update(Revenu revenu) throws JadePersistenceException, DonneesFinancieresException;

}
