package ch.globaz.pegasus.businessimpl.services.models.home;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.home.PrixChambreException;
import ch.globaz.pegasus.business.models.home.PrixChambre;
import ch.globaz.pegasus.business.models.home.PrixChambreSearch;
import ch.globaz.pegasus.business.services.models.home.PrixChambreService;
import ch.globaz.pegasus.businessimpl.checkers.home.PrixChambreChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class PrixChambreServiceImpl extends PegasusAbstractServiceImpl implements PrixChambreService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.PrixChambreService#count
     * (ch.globaz.pegasus.business.models.home.PrixChambreSearch)
     */
    @Override
    public int count(PrixChambreSearch search) throws PrixChambreException, JadePersistenceException {
        if (search == null) {
            throw new PrixChambreException("Unable to count prixChambres, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.PrixChambreService#create
     * (ch.globaz.pegasus.business.models.home.PrixChambre)
     */
    @Override
    public PrixChambre create(PrixChambre prixChambre) throws JadePersistenceException, PrixChambreException {
        if (prixChambre == null) {
            throw new PrixChambreException("Unable to create prixChambre, the given model is null!");
        }

        try {

            PrixChambreChecker.checkForCreate(prixChambre);

            PegasusImplServiceLocator.getSimplePrixChambreService().create(prixChambre.getSimplePrixChambre());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PrixChambreException("Service not available - " + e.getMessage());
        }

        return prixChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.PrixChambreService#delete
     * (ch.globaz.pegasus.business.models.home.PrixChambre)
     */
    @Override
    public PrixChambre delete(PrixChambre prixChambre) throws PrixChambreException, JadePersistenceException {
        if (prixChambre == null) {
            throw new PrixChambreException("Unable to delete prixChambre, the given model is null!");
        }
        try {

            PrixChambreChecker.checkForDelete(prixChambre);

            prixChambre.setSimplePrixChambre(PegasusImplServiceLocator.getSimplePrixChambreService().delete(
                    prixChambre.getSimplePrixChambre()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PrixChambreException("Service not available - " + e.getMessage());
        }

        return prixChambre;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.PrixChambreService#read (java.lang.String)
     */
    @Override
    public PrixChambre read(String idPrixChambre) throws JadePersistenceException, PrixChambreException {
        if (JadeStringUtil.isEmpty(idPrixChambre)) {
            throw new PrixChambreException("Unable to read prixChambre, the id passed is null!");
        }
        PrixChambre prixChambre = new PrixChambre();
        prixChambre.setId(idPrixChambre);
        return (PrixChambre) JadePersistenceManager.read(prixChambre);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.PrixChambreService#search
     * (ch.globaz.pegasus.business.models.home.PrixChambreSearch)
     */
    @Override
    public PrixChambreSearch search(PrixChambreSearch prixChambreSearch) throws JadePersistenceException,
            PrixChambreException {
        if (prixChambreSearch == null) {
            throw new PrixChambreException("Unable to search prixChambre, the search model passed is null!");
        }
        String whereKey = prixChambreSearch.getWhereKey();
        prixChambreSearch.setWhereKey(JadeStringUtil.isEmpty(prixChambreSearch.getForDateValable()) ? whereKey
                : "withDateValable");

        return (PrixChambreSearch) JadePersistenceManager.search(prixChambreSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.home.PrixChambreService#update
     * (ch.globaz.pegasus.business.models.home.PrixChambre)
     */
    @Override
    public PrixChambre update(PrixChambre prixChambre) throws JadePersistenceException, PrixChambreException {
        if (prixChambre == null) {
            throw new PrixChambreException("Unable to update prixChambre, the given model is null!");
        }

        try {
            PrixChambreChecker.checkForUpdate(prixChambre);

            // la mise a jour ne se fait que sur le simplePrixChambre
            PegasusImplServiceLocator.getSimplePrixChambreService().update(prixChambre.getSimplePrixChambre());

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PrixChambreException("Service not available - " + e.getMessage());
        }

        return prixChambre;
    }

}
