package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuHypothetiqueException;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.RevenusFraisGardeChecker;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuActiviteLucrativeIndependanteException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependante;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuActiviteLucrativeIndependanteSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.RevenuActiviteLucrativeIndependanteService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class RevenuActiviteLucrativeIndependanteServiceImpl extends PegasusAbstractServiceImpl implements
        RevenuActiviteLucrativeIndependanteService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuActiviteLucrativeIndependanteService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .RevenuActiviteLucrativeIndependanteSearch)
     */
    @Override
    public int count(RevenuActiviteLucrativeIndependanteSearch search)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException {
        if (search == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to count RevenuActiviteLucrativeIndependante, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuActiviteLucrativeIndependanteService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .RevenuActiviteLucrativeIndependante)
     */
    @Override
    public RevenuActiviteLucrativeIndependante create(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante) throws JadePersistenceException,
            RevenuActiviteLucrativeIndependanteException, DonneeFinanciereException {
        if (revenuActiviteLucrativeIndependante == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to create RevenuActiviteLucrativeIndependante, the model passed is null!");
        }

        try {
            if(!JadeStringUtil.isBlankOrZero(revenuActiviteLucrativeIndependante.getSimpleRevenuActiviteLucrativeIndependante().getFraisDeGarde())){
                RevenusFraisGardeChecker.checkSupositionFraisGarde(revenuActiviteLucrativeIndependante.getSimpleDonneeFinanciereHeader(), IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE);
            }
            revenuActiviteLucrativeIndependante.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            revenuActiviteLucrativeIndependante.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleRevenuActiviteLucrativeIndependanteService().create(
                    revenuActiviteLucrativeIndependante.getSimpleRevenuActiviteLucrativeIndependante());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuActiviteLucrativeIndependanteException("Service not available - " + e.getMessage());
        }

        return revenuActiviteLucrativeIndependante;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuActiviteLucrativeIndependanteService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .RevenuActiviteLucrativeIndependante)
     */
    @Override
    public RevenuActiviteLucrativeIndependante delete(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException {
        if (revenuActiviteLucrativeIndependante == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to delete RevenuActiviteLucrativeIndependante, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleRevenuActiviteLucrativeIndependanteService().delete(
                    revenuActiviteLucrativeIndependante.getSimpleRevenuActiviteLucrativeIndependante());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuActiviteLucrativeIndependanteException("Service not available - " + e.getMessage());
        }

        return revenuActiviteLucrativeIndependante;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses.
     * RevenuActiviteLucrativeIndependanteService#read(java.lang.String)
     */
    @Override
    public RevenuActiviteLucrativeIndependante read(String idRevenuActiviteLucrativeIndependante)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException {
        if (JadeStringUtil.isEmpty(idRevenuActiviteLucrativeIndependante)) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to read RevenuActiviteLucrativeIndependante, the id passed is null!");
        }
        RevenuActiviteLucrativeIndependante RevenuActiviteLucrativeIndependante = new RevenuActiviteLucrativeIndependante();
        RevenuActiviteLucrativeIndependante.setId(idRevenuActiviteLucrativeIndependante);
        return (RevenuActiviteLucrativeIndependante) JadePersistenceManager.read(RevenuActiviteLucrativeIndependante);
    }

    /**
     * Chargement d'une RevenuActiviteLucrativeIndependante via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RevenuActiviteLucrativeIndependanteException
     * @throws JadePersistenceException
     */
    @Override
    public RevenuActiviteLucrativeIndependante readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws RevenuActiviteLucrativeIndependanteException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to find RevenuActiviteLucrativeIndependante the idDonneeFinanciereHeader passed si null!");
        }

        RevenuActiviteLucrativeIndependanteSearch search = new RevenuActiviteLucrativeIndependanteSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (RevenuActiviteLucrativeIndependanteSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "More than one RevenuActiviteLucrativeIndependante find, one was exepcted!");
        }

        return (RevenuActiviteLucrativeIndependante) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((RevenuActiviteLucrativeIndependanteSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuActiviteLucrativeIndependanteService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .RevenuActiviteLucrativeIndependanteSearch)
     */
    @Override
    public RevenuActiviteLucrativeIndependanteSearch search(
            RevenuActiviteLucrativeIndependanteSearch revenuActiviteLucrativeIndependanteSearch)
            throws JadePersistenceException, RevenuActiviteLucrativeIndependanteException {
        if (revenuActiviteLucrativeIndependanteSearch == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to search RevenuActiviteLucrativeIndependante, the search model passed is null!");
        }
        return (RevenuActiviteLucrativeIndependanteSearch) JadePersistenceManager
                .search(revenuActiviteLucrativeIndependanteSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuActiviteLucrativeIndependanteService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .RevenuActiviteLucrativeIndependante)
     */
    @Override
    public RevenuActiviteLucrativeIndependante update(
            RevenuActiviteLucrativeIndependante revenuActiviteLucrativeIndependante) throws JadePersistenceException,
            RevenuActiviteLucrativeIndependanteException, DonneeFinanciereException {
        if (revenuActiviteLucrativeIndependante == null) {
            throw new RevenuActiviteLucrativeIndependanteException(
                    "Unable to update RevenuActiviteLucrativeIndependante, the model passed is null!");
        }

        try {
            if(!JadeStringUtil.isBlankOrZero(revenuActiviteLucrativeIndependante.getSimpleRevenuActiviteLucrativeIndependante().getFraisDeGarde())){
                RevenusFraisGardeChecker.checkSupositionFraisGarde(revenuActiviteLucrativeIndependante.getSimpleDonneeFinanciereHeader(), IPCDroits.CS_REVENU_ACTIVITE_LUCRATIVE_INDEPENDANTE);
            }
            revenuActiviteLucrativeIndependante.setSimpleRevenuActiviteLucrativeIndependante(PegasusImplServiceLocator
                    .getSimpleRevenuActiviteLucrativeIndependanteService().update(
                            revenuActiviteLucrativeIndependante.getSimpleRevenuActiviteLucrativeIndependante()));
            revenuActiviteLucrativeIndependante.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().update(
                            revenuActiviteLucrativeIndependante.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuActiviteLucrativeIndependanteException("Service not available - " + e.getMessage());
        }

        return revenuActiviteLucrativeIndependante;
    }

}
