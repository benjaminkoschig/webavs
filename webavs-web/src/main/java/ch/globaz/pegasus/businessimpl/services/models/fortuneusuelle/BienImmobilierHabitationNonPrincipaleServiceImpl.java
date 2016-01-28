package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.BienImmobilierHabitationNonPrincipaleService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class BienImmobilierHabitationNonPrincipaleServiceImpl extends PegasusAbstractServiceImpl implements
        BienImmobilierHabitationNonPrincipaleService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierHabitationNonPrincipaleService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierHabitationNonPrincipaleSearch)
     */
    @Override
    public int count(BienImmobilierHabitationNonPrincipaleSearch search)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException {
        if (search == null) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to count BienImmobilierHabitationNonPrincipale, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierHabitationNonPrincipaleService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierHabitationNonPrincipale)
     */
    @Override
    public BienImmobilierHabitationNonPrincipale create(
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException, DonneeFinanciereException {
        if (bienImmobilierHabitationNonPrincipale == null) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to create BienImmobilierHabitationNonPrincipale, the model passed is null!");
        }

        try {
            bienImmobilierHabitationNonPrincipale.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            bienImmobilierHabitationNonPrincipale.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleBienImmobilierHabitationNonPrincipaleService().create(
                    bienImmobilierHabitationNonPrincipale.getSimpleBienImmobilierHabitationNonPrincipale());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BienImmobilierHabitationNonPrincipaleException("Service not available - " + e.getMessage());
        }

        return bienImmobilierHabitationNonPrincipale;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierHabitationNonPrincipaleService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierHabitationNonPrincipale)
     */
    @Override
    public BienImmobilierHabitationNonPrincipale delete(
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException {
        if (bienImmobilierHabitationNonPrincipale == null) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to delete BienImmobilierHabitationNonPrincipale, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleBienImmobilierHabitationNonPrincipaleService().delete(
                    bienImmobilierHabitationNonPrincipale.getSimpleBienImmobilierHabitationNonPrincipale());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BienImmobilierHabitationNonPrincipaleException("Service not available - " + e.getMessage());
        }

        return bienImmobilierHabitationNonPrincipale;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses.
     * BienImmobilierHabitationNonPrincipaleService#read(java.lang.String)
     */
    @Override
    public BienImmobilierHabitationNonPrincipale read(String idBienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException {
        if (JadeStringUtil.isEmpty(idBienImmobilierHabitationNonPrincipale)) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to read BienImmobilierHabitationNonPrincipale, the id passed is null!");
        }
        BienImmobilierHabitationNonPrincipale BienImmobilierHabitationNonPrincipale = new BienImmobilierHabitationNonPrincipale();
        BienImmobilierHabitationNonPrincipale.setId(idBienImmobilierHabitationNonPrincipale);
        return (BienImmobilierHabitationNonPrincipale) JadePersistenceManager
                .read(BienImmobilierHabitationNonPrincipale);
    }

    /**
     * Chargement d'une BienImmobilierHabitationNonPrincipale via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws BienImmobilierHabitationNonPrincipaleException
     * @throws JadePersistenceException
     */
    @Override
    public BienImmobilierHabitationNonPrincipale readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws BienImmobilierHabitationNonPrincipaleException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to find BienImmobilierHabitationNonPrincipale the idDonneeFinanciereHeader passed si null!");
        }

        BienImmobilierHabitationNonPrincipaleSearch search = new BienImmobilierHabitationNonPrincipaleSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (BienImmobilierHabitationNonPrincipaleSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "More than one BienImmobilierHabitationNonPrincipale find, one was exepcted!");
        }

        return (BienImmobilierHabitationNonPrincipale) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((BienImmobilierHabitationNonPrincipaleSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierHabitationNonPrincipaleService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierHabitationNonPrincipaleSearch)
     */
    @Override
    public BienImmobilierHabitationNonPrincipaleSearch search(
            BienImmobilierHabitationNonPrincipaleSearch bienImmobilierHabitationNonPrincipaleSearch)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException {
        if (bienImmobilierHabitationNonPrincipaleSearch == null) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to search BienImmobilierHabitationNonPrincipale, the search model passed is null!");
        }
        return (BienImmobilierHabitationNonPrincipaleSearch) JadePersistenceManager
                .search(bienImmobilierHabitationNonPrincipaleSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierHabitationNonPrincipaleService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierHabitationNonPrincipale)
     */
    @Override
    public BienImmobilierHabitationNonPrincipale update(
            BienImmobilierHabitationNonPrincipale bienImmobilierHabitationNonPrincipale)
            throws JadePersistenceException, BienImmobilierHabitationNonPrincipaleException, DonneeFinanciereException {
        if (bienImmobilierHabitationNonPrincipale == null) {
            throw new BienImmobilierHabitationNonPrincipaleException(
                    "Unable to update BienImmobilierHabitationNonPrincipale, the model passed is null!");
        }

        try {
            bienImmobilierHabitationNonPrincipale
                    .setSimpleBienImmobilierHabitationNonPrincipale(PegasusImplServiceLocator
                            .getSimpleBienImmobilierHabitationNonPrincipaleService().update(
                                    bienImmobilierHabitationNonPrincipale
                                            .getSimpleBienImmobilierHabitationNonPrincipale()));
            bienImmobilierHabitationNonPrincipale.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            bienImmobilierHabitationNonPrincipale.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BienImmobilierHabitationNonPrincipaleException("Service not available - " + e.getMessage());
        }

        return bienImmobilierHabitationNonPrincipale;
    }

}
