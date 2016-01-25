/**
 * 
 */
package ch.globaz.perseus.businessimpl.services.models.donneesfinancieres;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.donneesfinancieres.DonneesFinancieresException;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciere;
import ch.globaz.perseus.business.models.donneesfinancieres.SimpleDonneeFinanciereSearchModel;
import ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereService;
import ch.globaz.perseus.businessimpl.checkers.donneesfinancieres.SimpleDonneeFinanciereChecker;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

/**
 * @author DDE
 * 
 */
public class SimpleDonneeFinanciereServiceImpl extends PerseusAbstractServiceImpl implements
        SimpleDonneeFinanciereService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereService#create(ch.globaz.
     * perseus.business.models.donneesfinancieres.SimpleDonneeFinanciere)
     */
    @Override
    public SimpleDonneeFinanciere create(SimpleDonneeFinanciere donneeFinanciere) throws JadePersistenceException,
            DonneesFinancieresException {
        if (donneeFinanciere == null) {
            throw new DonneesFinancieresException("Unable to create simple donneFinanciere, the model passed is null");
        }
        SimpleDonneeFinanciereChecker.checkForCreate(donneeFinanciere);
        return (SimpleDonneeFinanciere) JadePersistenceManager.add(donneeFinanciere);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereService#delete(ch.globaz.
     * perseus.business.models.donneesfinancieres.SimpleDonneeFinanciere)
     */
    @Override
    public SimpleDonneeFinanciere delete(SimpleDonneeFinanciere donneeFinanciere) throws JadePersistenceException,
            DonneesFinancieresException {
        if (donneeFinanciere == null) {
            throw new DonneesFinancieresException("Unable to delete simple donneFinanciere, the model passed is null");
        } else if (donneeFinanciere.isNew()) {
            throw new DonneesFinancieresException("Unable to delete simple donneFinancieree, the model passed is new!");
        }
        SimpleDonneeFinanciereChecker.checkForDelete(donneeFinanciere);
        return (SimpleDonneeFinanciere) JadePersistenceManager.delete(donneeFinanciere);
    }

    @Override
    public int delete(SimpleDonneeFinanciereSearchModel searchModel) throws JadePersistenceException,
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
     * ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereService#read(java.lang.String
     * )
     */
    @Override
    public SimpleDonneeFinanciere read(String idDonneeFinanciere) throws JadePersistenceException,
            DonneesFinancieresException {
        if (idDonneeFinanciere == null) {
            throw new DonneesFinancieresException("Unable to read simple donneFinanciere, the id passed is null");
        }
        SimpleDonneeFinanciere donneeFinanciere = new SimpleDonneeFinanciere();
        donneeFinanciere.setId(idDonneeFinanciere);
        return (SimpleDonneeFinanciere) JadePersistenceManager.read(donneeFinanciere);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.perseus.business.services.models.donneesfinancieres.SimpleDonneeFinanciereService#update(ch.globaz.
     * perseus.business.models.donneesfinancieres.SimpleDonneeFinanciere)
     */
    @Override
    public SimpleDonneeFinanciere update(SimpleDonneeFinanciere donneeFinanciere) throws JadePersistenceException,
            DonneesFinancieresException {
        if (donneeFinanciere == null) {
            throw new DonneesFinancieresException("Unable to update simple donneFinanciere, the model passed is null");
        } else if (donneeFinanciere.isNew()) {
            throw new DonneesFinancieresException("Unable to update simple donneFinancieree, the model passed is new!");
        }
        SimpleDonneeFinanciereChecker.checkForUpdate(donneeFinanciere);
        return (SimpleDonneeFinanciere) JadePersistenceManager.update(donneeFinanciere);
    }

}
