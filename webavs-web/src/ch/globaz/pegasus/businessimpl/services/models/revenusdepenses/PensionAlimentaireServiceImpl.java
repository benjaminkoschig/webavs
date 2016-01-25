package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.PensionAlimentaireException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaire;
import ch.globaz.pegasus.business.models.revenusdepenses.PensionAlimentaireSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.PensionAlimentaireService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class PensionAlimentaireServiceImpl extends PegasusAbstractServiceImpl implements PensionAlimentaireService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. PensionAlimentaireService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .PensionAlimentaireSearch)
     */
    @Override
    public int count(PensionAlimentaireSearch search) throws PensionAlimentaireException, JadePersistenceException {
        if (search == null) {
            throw new PensionAlimentaireException(
                    "Unable to count PensionAlimentaire, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. PensionAlimentaireService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .PensionAlimentaire)
     */
    @Override
    public PensionAlimentaire create(PensionAlimentaire pensionAlimentaire) throws JadePersistenceException,
            PensionAlimentaireException, DonneeFinanciereException {
        if (pensionAlimentaire == null) {
            throw new PensionAlimentaireException("Unable to create PensionAlimentaire, the model passed is null!");
        }

        try {
            pensionAlimentaire.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            pensionAlimentaire.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimplePensionAlimentaireService().create(
                    pensionAlimentaire.getSimplePensionAlimentaire());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PensionAlimentaireException("Service not available - " + e.getMessage());
        }

        return pensionAlimentaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. PensionAlimentaireService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .PensionAlimentaire)
     */
    @Override
    public PensionAlimentaire delete(PensionAlimentaire pensionAlimentaire) throws PensionAlimentaireException,
            JadePersistenceException {
        if (pensionAlimentaire == null) {
            throw new PensionAlimentaireException("Unable to delete PensionAlimentaire, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimplePensionAlimentaireService().delete(
                    pensionAlimentaire.getSimplePensionAlimentaire());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PensionAlimentaireException("Service not available - " + e.getMessage());
        }

        return pensionAlimentaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. PensionAlimentaireService#read(java.lang.String)
     */
    @Override
    public PensionAlimentaire read(String idPensionAlimentaire) throws JadePersistenceException,
            PensionAlimentaireException {
        if (JadeStringUtil.isEmpty(idPensionAlimentaire)) {
            throw new PensionAlimentaireException("Unable to read PensionAlimentaire, the id passed is null!");
        }
        PensionAlimentaire PensionAlimentaire = new PensionAlimentaire();
        PensionAlimentaire.setId(idPensionAlimentaire);
        return (PensionAlimentaire) JadePersistenceManager.read(PensionAlimentaire);
    }

    /**
     * Chargement d'une PensionAlimentaireSearch via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws PensionAlimentaireSearch
     *             Exception
     * @throws JadePersistenceException
     */
    @Override
    public PensionAlimentaire readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws PensionAlimentaireException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new PensionAlimentaireException(
                    "Unable to find PensionAlimentaire the idDonneeFinanciereHeader passed si null!");
        }

        PensionAlimentaireSearch search = new PensionAlimentaireSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (PensionAlimentaireSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new PensionAlimentaireException("More than one PensionAlimentaire find, one was exepcted!");
        }

        return (PensionAlimentaire) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((PensionAlimentaireSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. PensionAlimentaireService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .PensionAlimentaireSearch)
     */
    @Override
    public PensionAlimentaireSearch search(PensionAlimentaireSearch pensionAlimentaireSearch)
            throws JadePersistenceException, PensionAlimentaireException {
        if (pensionAlimentaireSearch == null) {
            throw new PensionAlimentaireException(
                    "Unable to search PensionAlimentaire, the search model passed is null!");
        }
        return (PensionAlimentaireSearch) JadePersistenceManager.search(pensionAlimentaireSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. PensionAlimentaireService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .PensionAlimentaire)
     */
    @Override
    public PensionAlimentaire update(PensionAlimentaire pensionAlimentaire) throws JadePersistenceException,
            PensionAlimentaireException, DonneeFinanciereException {
        if (pensionAlimentaire == null) {
            throw new PensionAlimentaireException("Unable to update PensionAlimentaire, the model passed is null!");
        }

        try {
            pensionAlimentaire.setSimplePensionAlimentaire(PegasusImplServiceLocator
                    .getSimplePensionAlimentaireService().update(pensionAlimentaire.getSimplePensionAlimentaire()));
            pensionAlimentaire.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            pensionAlimentaire.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new PensionAlimentaireException("Service not available - " + e.getMessage());
        }

        return pensionAlimentaire;
    }

}
