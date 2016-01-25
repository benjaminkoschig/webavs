/**
 * 
 */
package ch.globaz.perseus.business.services.models.donneesfinancieres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisation;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisationSearchModel;

/**
 * @author DDE
 * 
 */
public interface SimpleDonneeFinanciereSpecialisationService extends JadeApplicationService {
    public SimpleDonneeFinanciereSpecialisation create(
            SimpleDonneeFinanciereSpecialisation donneeFinanciereSpecialisation) throws JadePersistenceException,
            DonneesFinancieresException;

    public SimpleDonneeFinanciereSpecialisation delete(
            SimpleDonneeFinanciereSpecialisation donneeFinanciereSpecialisation) throws JadePersistenceException,
            DonneesFinancieresException;

    public int delete(SimpleDonneeFinanciereSpecialisationSearchModel searchModel) throws JadePersistenceException,
            DonneesFinancieresException;

    public SimpleDonneeFinanciereSpecialisation read(String idDonneeFinanciereSpecialisation)
            throws JadePersistenceException, DonneesFinancieresException;

    public SimpleDonneeFinanciereSpecialisation update(
            SimpleDonneeFinanciereSpecialisation donneeFinanciereSpecialisation) throws JadePersistenceException,
            DonneesFinancieresException;

}
