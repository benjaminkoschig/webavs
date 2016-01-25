/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.donneesfinancieres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisation;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisationSearchModel;
import ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisationService;
import ch.globaz.perseus.businessimpl.checkers.donneesfinancieres.SimpleDonneeFinanciereSpecialisationChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author DDE
 * 
 */
public class SimpleDonneeFinanciereSpecialisationServiceImpl extends PerseusAbstractServiceImpl implements
        SimpleDonneeFinanciereSpecialisationService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisationService#create
     * (ch.globaz. perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisation)
     */
    @Override
    public SimpleDonneeFinanciereSpecialisation create(
            SimpleDonneeFinanciereSpecialisation donneeFinanciereSpecialisation) throws JadePersistenceException,
            DonneesFinancieresException {
        if (donneeFinanciereSpecialisation == null) {
            throw new DonneesFinancieresException(
                    "Unable to create simple donneeFinanciereSpecialisation, the model passed is null");
        }
        SimpleDonneeFinanciereSpecialisationChecker.checkForCreate(donneeFinanciereSpecialisation);
        return (SimpleDonneeFinanciereSpecialisation) JadePersistenceManager.add(donneeFinanciereSpecialisation);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisationService#delete
     * (ch.globaz. perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisation)
     */
    @Override
    public SimpleDonneeFinanciereSpecialisation delete(
            SimpleDonneeFinanciereSpecialisation donneeFinanciereSpecialisation) throws JadePersistenceException,
            DonneesFinancieresException {
        if (donneeFinanciereSpecialisation == null) {
            throw new DonneesFinancieresException(
                    "Unable to delete simple donneeFinanciereSpecialisation, the model passed is null");
        } else if (donneeFinanciereSpecialisation.isNew()) {
            throw new DonneesFinancieresException(
                    "Unable to delete simple donneeFinanciereSpecialisatione, the model passed is new!");
        }
        SimpleDonneeFinanciereSpecialisationChecker.checkForDelete(donneeFinanciereSpecialisation);
        return (SimpleDonneeFinanciereSpecialisation) JadePersistenceManager.delete(donneeFinanciereSpecialisation);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisationService#delete
     * (ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisationSearchModel)
     */
    @Override
    public int delete(SimpleDonneeFinanciereSpecialisationSearchModel searchModel) throws JadePersistenceException,
            DonneesFinancieresException {
        if (searchModel == null) {
            throw new DonneesFinancieresException(
                    "Unable to delete simple donneeFinanciereSpecialisation, the search model passed is null!");
        }
        return JadePersistenceManager.delete(searchModel);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisationService#read
     * (java.lang.String )
     */
    @Override
    public SimpleDonneeFinanciereSpecialisation read(String idDonneeFinanciereSpecialisation)
            throws JadePersistenceException, DonneesFinancieresException {
        if (idDonneeFinanciereSpecialisation == null) {
            throw new DonneesFinancieresException(
                    "Unable to read simple donneeFinanciereSpecialisation, the id passed is null");
        }
        SimpleDonneeFinanciereSpecialisation donneeFinanciereSpecialisation = new SimpleDonneeFinanciereSpecialisation();
        donneeFinanciereSpecialisation.setId(idDonneeFinanciereSpecialisation);
        return (SimpleDonneeFinanciereSpecialisation) JadePersistenceManager.read(donneeFinanciereSpecialisation);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisationService#update
     * (ch.globaz. perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSpecialisation)
     */
    @Override
    public SimpleDonneeFinanciereSpecialisation update(
            SimpleDonneeFinanciereSpecialisation donneeFinanciereSpecialisation) throws JadePersistenceException,
            DonneesFinancieresException {
        if (donneeFinanciereSpecialisation == null) {
            throw new DonneesFinancieresException(
                    "Unable to update simple donneeFinanciereSpecialisation, the model passed is null");
        } else if (donneeFinanciereSpecialisation.isNew()) {
            throw new DonneesFinancieresException(
                    "Unable to update simple donneeFinanciereSpecialisatione, the model passed is new!");
        }
        SimpleDonneeFinanciereSpecialisationChecker.checkForUpdate(donneeFinanciereSpecialisation);
        return (SimpleDonneeFinanciereSpecialisation) JadePersistenceManager.update(donneeFinanciereSpecialisation);
    }
}
