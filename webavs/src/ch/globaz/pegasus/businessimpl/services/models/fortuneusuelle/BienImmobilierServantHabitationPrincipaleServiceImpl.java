package ch.globaz.pegasus.businessimpl.services.models.fortuneusuelle;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipale;
import ch.globaz.pegasus.business.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleSearch;
import ch.globaz.pegasus.business.services.models.fortuneusuelle.BienImmobilierServantHabitationPrincipaleService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class BienImmobilierServantHabitationPrincipaleServiceImpl extends PegasusAbstractServiceImpl implements
        BienImmobilierServantHabitationPrincipaleService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierServantHabitationPrincipaleService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierServantHabitationPrincipaleSearch)
     */
    @Override
    public int count(BienImmobilierServantHabitationPrincipaleSearch search)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException {
        if (search == null) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to count BienImmobilierServantHabitationPrincipale, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierServantHabitationPrincipaleService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierServantHabitationPrincipale)
     */
    @Override
    public BienImmobilierServantHabitationPrincipale create(
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException,
            DonneeFinanciereException {
        if (bienImmobilierServantHabitationPrincipale == null) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to create BienImmobilierServantHabitationPrincipale, the model passed is null!");
        }

        try {
            bienImmobilierServantHabitationPrincipale.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            bienImmobilierServantHabitationPrincipale.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleBienImmobilierServantHabitationPrincipaleService().create(
                    bienImmobilierServantHabitationPrincipale.getSimpleBienImmobilierServantHabitationPrincipale());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BienImmobilierServantHabitationPrincipaleException("Service not available - " + e.getMessage());
        }

        return bienImmobilierServantHabitationPrincipale;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierServantHabitationPrincipaleService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierServantHabitationPrincipale)
     */
    @Override
    public BienImmobilierServantHabitationPrincipale delete(
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException {
        if (bienImmobilierServantHabitationPrincipale == null) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to delete BienImmobilierServantHabitationPrincipale, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleBienImmobilierServantHabitationPrincipaleService().delete(
                    bienImmobilierServantHabitationPrincipale.getSimpleBienImmobilierServantHabitationPrincipale());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BienImmobilierServantHabitationPrincipaleException("Service not available - " + e.getMessage());
        }

        return bienImmobilierServantHabitationPrincipale;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses.
     * BienImmobilierServantHabitationPrincipaleService#read(java.lang.String)
     */
    @Override
    public BienImmobilierServantHabitationPrincipale read(String idBienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException {
        if (JadeStringUtil.isEmpty(idBienImmobilierServantHabitationPrincipale)) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to read BienImmobilierServantHabitationPrincipale, the id passed is null!");
        }
        BienImmobilierServantHabitationPrincipale BienImmobilierServantHabitationPrincipale = new BienImmobilierServantHabitationPrincipale();
        BienImmobilierServantHabitationPrincipale.setId(idBienImmobilierServantHabitationPrincipale);
        return (BienImmobilierServantHabitationPrincipale) JadePersistenceManager
                .read(BienImmobilierServantHabitationPrincipale);
    }

    /**
     * Chargement d'une BienImmobilierServantHabitationPrincipale via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws BienImmobilierServantHabitationPrincipaleException
     * @throws JadePersistenceException
     */
    @Override
    public BienImmobilierServantHabitationPrincipale readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws BienImmobilierServantHabitationPrincipaleException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to find BienImmobilierServantHabitationPrincipale the idDonneeFinanciereHeader passed si null!");
        }

        BienImmobilierServantHabitationPrincipaleSearch search = new BienImmobilierServantHabitationPrincipaleSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (BienImmobilierServantHabitationPrincipaleSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "More than one BienImmobilierServantHabitationPrincipale find, one was exepcted!");
        }

        return (BienImmobilierServantHabitationPrincipale) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((BienImmobilierServantHabitationPrincipaleSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierServantHabitationPrincipaleService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierServantHabitationPrincipaleSearch)
     */
    @Override
    public BienImmobilierServantHabitationPrincipaleSearch search(
            BienImmobilierServantHabitationPrincipaleSearch bienImmobilierServantHabitationPrincipaleSearch)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException {
        if (bienImmobilierServantHabitationPrincipaleSearch == null) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to search BienImmobilierServantHabitationPrincipale, the search model passed is null!");
        }
        return (BienImmobilierServantHabitationPrincipaleSearch) JadePersistenceManager
                .search(bienImmobilierServantHabitationPrincipaleSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. BienImmobilierServantHabitationPrincipaleService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .BienImmobilierServantHabitationPrincipale)
     */
    @Override
    public BienImmobilierServantHabitationPrincipale update(
            BienImmobilierServantHabitationPrincipale bienImmobilierServantHabitationPrincipale)
            throws JadePersistenceException, BienImmobilierServantHabitationPrincipaleException,
            DonneeFinanciereException {
        if (bienImmobilierServantHabitationPrincipale == null) {
            throw new BienImmobilierServantHabitationPrincipaleException(
                    "Unable to update BienImmobilierServantHabitationPrincipale, the model passed is null!");
        }

        try {
            bienImmobilierServantHabitationPrincipale
                    .setSimpleBienImmobilierServantHabitationPrincipale(PegasusImplServiceLocator
                            .getSimpleBienImmobilierServantHabitationPrincipaleService().update(
                                    bienImmobilierServantHabitationPrincipale
                                            .getSimpleBienImmobilierServantHabitationPrincipale()));
            bienImmobilierServantHabitationPrincipale.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            bienImmobilierServantHabitationPrincipale.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new BienImmobilierServantHabitationPrincipaleException("Service not available - " + e.getMessage());
        }

        return bienImmobilierServantHabitationPrincipale;
    }

}
