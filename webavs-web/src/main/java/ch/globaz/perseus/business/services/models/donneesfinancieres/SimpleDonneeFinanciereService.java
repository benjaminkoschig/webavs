/**
 * 
 */
package ch.globaz.perseus.business.services.models.donneesfinancieres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciere;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSearchModel;

/**
 * @author DDE
 * 
 */
public interface SimpleDonneeFinanciereService extends JadeApplicationService {
    public SimpleDonneeFinanciere create(SimpleDonneeFinanciere donneeFinanciere) throws JadePersistenceException,
            DonneesFinancieresException;

    public SimpleDonneeFinanciere delete(SimpleDonneeFinanciere donneeFinanciere) throws JadePersistenceException,
            DonneesFinancieresException;

    public int delete(SimpleDonneeFinanciereSearchModel searchModel) throws JadePersistenceException,
            DonneesFinancieresException;

    public SimpleDonneeFinanciere read(String idDonneeFinanciere) throws JadePersistenceException,
            DonneesFinancieresException;

    public SimpleDonneeFinanciere update(SimpleDonneeFinanciere donneeFinanciere) throws JadePersistenceException,
            DonneesFinancieresException;

}
