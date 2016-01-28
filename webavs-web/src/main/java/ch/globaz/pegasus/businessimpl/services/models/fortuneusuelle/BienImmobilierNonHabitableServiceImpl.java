package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierNonHabitableException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitable;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierNonHabitableSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.BienImmobilierNonHabitableService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class BienImmobilierNonHabitableServiceImpl extends PegasusAbstractServiceImpl implements
        BienImmobilierNonHabitableService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierNonHabitableService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierNonHabitableSearch)
     */
    @Override
    public int count(BienImmobilierNonHabitableSearch search) throws BienImmobilierNonHabitableException,
            JadePersistenceException {
        if (search == null) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to count BienImmobilierNonHabitable, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierNonHabitableService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierNonHabitable)
     */
    @Override
    public BienImmobilierNonHabitable create(BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws JadePersistenceException, BienImmobilierNonHabitableException, DonneeFinanciereException {
        if (bienImmobilierNonHabitable == null) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to create BienImmobilierNonHabitable, the model passed is null!");
        }

        try {
            bienImmobilierNonHabitable.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            bienImmobilierNonHabitable.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleBienImmobilierNonHabitableService().create(
                    bienImmobilierNonHabitable.getSimpleBienImmobilierNonHabitable());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BienImmobilierNonHabitableException("Service not available - " + e.getMessage());
        }

        return bienImmobilierNonHabitable;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierNonHabitableService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierNonHabitable)
     */
    @Override
    public BienImmobilierNonHabitable delete(BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws BienImmobilierNonHabitableException, JadePersistenceException {
        if (bienImmobilierNonHabitable == null) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to delete BienImmobilierNonHabitable, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleBienImmobilierNonHabitableService().delete(
                    bienImmobilierNonHabitable.getSimpleBienImmobilierNonHabitable());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BienImmobilierNonHabitableException("Service not available - " + e.getMessage());
        }

        return bienImmobilierNonHabitable;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses.
     * BienImmobilierNonHabitableService#read(java.lang.String)
     */
    @Override
    public BienImmobilierNonHabitable read(String idBienImmobilierNonHabitable) throws JadePersistenceException,
            BienImmobilierNonHabitableException {
        if (JadeStringUtil.isEmpty(idBienImmobilierNonHabitable)) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to read BienImmobilierNonHabitable, the id passed is null!");
        }
        BienImmobilierNonHabitable BienImmobilierNonHabitable = new BienImmobilierNonHabitable();
        BienImmobilierNonHabitable.setId(idBienImmobilierNonHabitable);
        return (BienImmobilierNonHabitable) JadePersistenceManager.read(BienImmobilierNonHabitable);
    }

    /**
     * Chargement d'un BienImmobilierNonHabitable via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws BienImmobilierNonHabitableException
     * @throws JadePersistenceException
     */
    @Override
    public BienImmobilierNonHabitable readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws BienImmobilierNonHabitableException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to find BienImmobilierNonHabitable the idDonneeFinanciereHeader passed si null!");
        }

        BienImmobilierNonHabitableSearch search = new BienImmobilierNonHabitableSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (BienImmobilierNonHabitableSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new BienImmobilierNonHabitableException(
                    "More than one BienImmobilierNonHabitable find, one was exepcted!");
        }

        return (BienImmobilierNonHabitable) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((BienImmobilierNonHabitableSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierNonHabitableService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierNonHabitableSearch)
     */
    @Override
    public BienImmobilierNonHabitableSearch search(BienImmobilierNonHabitableSearch bienImmobilierNonHabitableSearch)
            throws JadePersistenceException, BienImmobilierNonHabitableException {
        if (bienImmobilierNonHabitableSearch == null) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to search BienImmobilierNonHabitable, the search model passed is null!");
        }
        return (BienImmobilierNonHabitableSearch) JadePersistenceManager.search(bienImmobilierNonHabitableSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierNonHabitableService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierNonHabitable)
     */
    @Override
    public BienImmobilierNonHabitable update(BienImmobilierNonHabitable bienImmobilierNonHabitable)
            throws JadePersistenceException, BienImmobilierNonHabitableException, DonneeFinanciereException {
        if (bienImmobilierNonHabitable == null) {
            throw new BienImmobilierNonHabitableException(
                    "Unable to update BienImmobilierNonHabitable, the model passed is null!");
        }

        try {
            bienImmobilierNonHabitable.setSimpleBienImmobilierNonHabitable(PegasusImplServiceLocator
                    .getSimpleBienImmobilierNonHabitableService().update(
                            bienImmobilierNonHabitable.getSimpleBienImmobilierNonHabitable()));
            bienImmobilierNonHabitable.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            bienImmobilierNonHabitable.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BienImmobilierNonHabitableException("Service not available - " + e.getMessage());
        }

        return bienImmobilierNonHabitable;
    }

}
