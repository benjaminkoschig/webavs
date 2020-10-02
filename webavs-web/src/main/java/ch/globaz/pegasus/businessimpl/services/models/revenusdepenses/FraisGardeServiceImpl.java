package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.FraisGardeException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.FraisGarde;
import ch.globaz.pegasus.business.models.revenusdepenses.FraisGardeSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.FraisGardeService;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.RevenusFraisGardeChecker;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

public class FraisGardeServiceImpl extends PegasusAbstractServiceImpl implements FraisGardeService {
    private String msgService = "Service not available - ";
    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. FraisGardeService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .FraisGardeSearch)
     */
    @Override
    public int count(FraisGardeSearch search) throws FraisGardeException, JadePersistenceException {
        if (search == null) {
            throw new FraisGardeException("Unable to count FraisGarde, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. FraisGardeService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .FraisGarde)
     */
    @Override
    public FraisGarde create(FraisGarde fraisGarde) throws JadePersistenceException, FraisGardeException,
            DonneeFinanciereException {
        if (fraisGarde == null) {
            throw new FraisGardeException("Unable to create FraisGarde, the model passed is null!");
        }

        try {
            if(!JadeStringUtil.isBlankOrZero(fraisGarde.getSimpleFraisGarde().getMontant())){
                RevenusFraisGardeChecker.checkSupositionFraisGarde(fraisGarde.getSimpleDonneeFinanciereHeader(), IPCDroits.CS_FRAIS_GARDE);
                RevenusFraisGardeChecker.checkCanHaveFraisGarde(fraisGarde);
            }
            fraisGarde.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(fraisGarde.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleFraisGardeService().create(fraisGarde.getSimpleFraisGarde());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FraisGardeException(msgService + e.getMessage());
        }

        return fraisGarde;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. FraisGardeService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .FraisGarde)
     */
    @Override
    public FraisGarde delete(FraisGarde fraisGarde) throws FraisGardeException, JadePersistenceException {
        if (fraisGarde == null) {
            throw new FraisGardeException("Unable to delete FraisGarde, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleFraisGardeService().delete(fraisGarde.getSimpleFraisGarde());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FraisGardeException(msgService + e.getMessage());
        }

        return fraisGarde;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. FraisGardeService#read(java.lang.String)
     */
    @Override
    public FraisGarde read(String idFraisGarde) throws JadePersistenceException, FraisGardeException {
        if (JadeStringUtil.isEmpty(idFraisGarde)) {
            throw new FraisGardeException("Unable to read FraisGarde, the id passed is null!");
        }
        FraisGarde fraisGarde = new FraisGarde();
        fraisGarde.setId(idFraisGarde);
        return (FraisGarde) JadePersistenceManager.read(fraisGarde);
    }

    /**
     * Chargement d'un FraisGarde via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws FraisGardeException
     * @throws JadePersistenceException
     */
    @Override
    public FraisGarde readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) throws FraisGardeException,
            JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new FraisGardeException(
                    "Unable to find FraisGarde the idDonneeFinanciereHeader passed si null!");
        }

        FraisGardeSearch search = new FraisGardeSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (FraisGardeSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new FraisGardeException("More than one FraisGarde find, one was exepcted!");
        }

        return (FraisGarde) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((FraisGardeSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. FraisGardeService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .FraisGardeSearch)
     */
    @Override
    public FraisGardeSearch search(FraisGardeSearch fraisGardeSearch) throws JadePersistenceException,
            FraisGardeException {
        if (fraisGardeSearch == null) {
            throw new FraisGardeException("Unable to search FraisGarde, the search model passed is null!");
        }
        return (FraisGardeSearch) JadePersistenceManager.search(fraisGardeSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. FraisGardeService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .FraisGarde)
     */
    @Override
    public FraisGarde update(FraisGarde fraisGarde) throws JadePersistenceException, FraisGardeException,
            DonneeFinanciereException {
        if (fraisGarde == null) {
            throw new FraisGardeException("Unable to update FraisGarde, the model passed is null!");
        }

        try {
            if(!JadeStringUtil.isBlankOrZero(fraisGarde.getSimpleFraisGarde().getMontant())){
                RevenusFraisGardeChecker.checkSupositionFraisGarde(fraisGarde.getSimpleDonneeFinanciereHeader(), IPCDroits.CS_FRAIS_GARDE);
            }
            RevenusFraisGardeChecker.checkCanHaveFraisGarde(fraisGarde);

            fraisGarde.setSimpleFraisGarde(PegasusImplServiceLocator.getSimpleFraisGardeService().update(
                    fraisGarde.getSimpleFraisGarde()));
            fraisGarde.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(fraisGarde.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new FraisGardeException(msgService + e.getMessage());
        }

        return fraisGarde;
    }

}
