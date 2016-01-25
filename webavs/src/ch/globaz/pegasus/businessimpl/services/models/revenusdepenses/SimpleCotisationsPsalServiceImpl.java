package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.CotisationsPsalException;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleCotisationsPsal;
import ch.globaz.pegasus.business.models.revenusdepenses.SimpleCotisationsPsalSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.SimpleCotisationsPsalService;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.SimpleCotisationsPsalChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleCotisationsPsalServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleCotisationsPsalService {
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleCotisationsPsalService
     * #create(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleCotisationsPsal)
     */
    @Override
    public SimpleCotisationsPsal create(SimpleCotisationsPsal simpleCotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException {
        if (simpleCotisationsPsal == null) {
            throw new CotisationsPsalException("Unable to create simpleCotisationsPsal, the model passed is null!");
        }
        SimpleCotisationsPsalChecker.checkForCreate(simpleCotisationsPsal);
        return (SimpleCotisationsPsal) JadePersistenceManager.add(simpleCotisationsPsal);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleCotisationsPsalService
     * #delete(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleCotisationsPsal)
     */
    @Override
    public SimpleCotisationsPsal delete(SimpleCotisationsPsal simpleCotisationsPsal) throws CotisationsPsalException,
            JadePersistenceException {
        if (simpleCotisationsPsal == null) {
            throw new CotisationsPsalException("Unable to delete simpleCotisationsPsal, the model passed is null!");
        }
        if (simpleCotisationsPsal.isNew()) {
            throw new CotisationsPsalException("Unable to delete simpleCotisationsPsal, the model passed is new!");
        }
        SimpleCotisationsPsalChecker.checkForDelete(simpleCotisationsPsal);
        return (SimpleCotisationsPsal) JadePersistenceManager.delete(simpleCotisationsPsal);
    }

    @Override
    public void deleteParListeIdDoFinH(List<String> listeIDString) throws JadePersistenceException {
        SimpleCotisationsPsalSearch search = new SimpleCotisationsPsalSearch();
        search.setForListIdDonneeFinanciere(listeIDString);

        JadePersistenceManager.delete(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere.
     * SimpleCotisationsPsalService#read(java.lang.String)
     */
    @Override
    public SimpleCotisationsPsal read(String idCotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException {
        if (JadeStringUtil.isEmpty(idCotisationsPsal)) {
            throw new CotisationsPsalException("Unable to read simpleCotisationsPsal, the id passed is not defined!");
        }
        SimpleCotisationsPsal simpleCotisationsPsal = new SimpleCotisationsPsal();
        simpleCotisationsPsal.setId(idCotisationsPsal);
        return (SimpleCotisationsPsal) JadePersistenceManager.read(simpleCotisationsPsal);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.fortuneparticuliere. SimpleCotisationsPsalService
     * #update(ch.globaz.pegasus.business.models.fortuneparticuliere .SimpleCotisationsPsal)
     */
    @Override
    public SimpleCotisationsPsal update(SimpleCotisationsPsal simpleCotisationsPsal) throws JadePersistenceException,
            CotisationsPsalException {
        if (simpleCotisationsPsal == null) {
            throw new CotisationsPsalException("Unable to update simpleCotisationsPsal, the model passed is null!");
        }
        if (simpleCotisationsPsal.isNew()) {
            throw new CotisationsPsalException("Unable to update simpleCotisationsPsal, the model passed is new!");
        }
        SimpleCotisationsPsalChecker.checkForUpdate(simpleCotisationsPsal);
        return (SimpleCotisationsPsal) JadePersistenceManager.update(simpleCotisationsPsal);
    }

}
