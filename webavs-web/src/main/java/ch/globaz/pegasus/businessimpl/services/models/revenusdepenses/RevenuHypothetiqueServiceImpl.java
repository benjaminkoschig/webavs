package ch.globaz.pegasus.businessimpl.services.models.revenusdepenses;

import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.businessimpl.checkers.PegasusAbstractChecker;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.RevenusFraisGardeChecker;
import ch.globaz.pegasus.businessimpl.checkers.revenusdepenses.SimpleRevenuHypothetiqueChecker;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.PegasusException;
import ch.globaz.pegasus.business.exceptions.models.droit.DonneeFinanciereException;
import ch.globaz.pegasus.business.exceptions.models.revenusdepenses.RevenuHypothetiqueException;
import ch.globaz.pegasus.business.models.droit.AbstractDonneeFinanciereSearchModel;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetique;
import ch.globaz.pegasus.business.models.revenusdepenses.RevenuHypothetiqueSearch;
import ch.globaz.pegasus.business.services.models.revenusdepenses.RevenuHypothetiqueService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class RevenuHypothetiqueServiceImpl extends PegasusAbstractServiceImpl implements RevenuHypothetiqueService {

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuHypothetiqueService
     * #count(ch.globaz.pegasus.business.models.revenusdepenses .RevenuHypothetiqueSearch)
     */
    @Override
    public int count(RevenuHypothetiqueSearch search) throws RevenuHypothetiqueException, JadePersistenceException {
        if (search == null) {
            throw new RevenuHypothetiqueException(
                    "Unable to count RevenuHypothetique, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuHypothetiqueService
     * #create(ch.globaz.pegasus.business.models.revenusdepenses .RevenuHypothetique)
     */
    @Override
    public RevenuHypothetique create(RevenuHypothetique revenuHypothetique) throws JadePersistenceException,
            RevenuHypothetiqueException, DonneeFinanciereException {
        if (revenuHypothetique == null) {
            throw new RevenuHypothetiqueException("Unable to create RevenuHypothetique, the model passed is null!");
        }

        try {
            RevenusFraisGardeChecker.checkSupositionFraisGarde(revenuHypothetique.getSimpleDonneeFinanciereHeader(), IPCDroits.CS_REVENU_HYPOTHETIQUE);
            revenuHypothetique.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                    .getSimpleDonneeFinanciereHeaderService().create(
                            revenuHypothetique.getSimpleDonneeFinanciereHeader()));
            PegasusImplServiceLocator.getSimpleRevenuHypothetiqueService().create(
                    revenuHypothetique.getSimpleRevenuHypothetique());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuHypothetiqueException("Service not available - " + e.getMessage());
        }

        return revenuHypothetique;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuHypothetiqueService
     * #delete(ch.globaz.pegasus.business.models.revenusdepenses .RevenuHypothetique)
     */
    @Override
    public RevenuHypothetique delete(RevenuHypothetique revenuHypothetique) throws RevenuHypothetiqueException,
            JadePersistenceException {
        if (revenuHypothetique == null) {
            throw new RevenuHypothetiqueException("Unable to delete RevenuHypothetique, the model passed is null!");
        }

        try {
            PegasusImplServiceLocator.getSimpleRevenuHypothetiqueService().delete(
                    revenuHypothetique.getSimpleRevenuHypothetique());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuHypothetiqueException("Service not available - " + e.getMessage());
        }

        return revenuHypothetique;
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuHypothetiqueService#read(java.lang.String)
     */
    @Override
    public RevenuHypothetique read(String idRevenuHypothetique) throws JadePersistenceException,
            RevenuHypothetiqueException {
        if (JadeStringUtil.isEmpty(idRevenuHypothetique)) {
            throw new RevenuHypothetiqueException("Unable to read RevenuHypothetique, the id passed is null!");
        }
        RevenuHypothetique RevenuHypothetique = new RevenuHypothetique();
        RevenuHypothetique.setId(idRevenuHypothetique);
        return (RevenuHypothetique) JadePersistenceManager.read(RevenuHypothetique);
    }

    /**
     * Chargement d'une RevenuHypothetique via l'id donnee financiere header
     * 
     * @param idDonneeFinanciereHeader
     * @return
     * @throws RevenuHypothetiqueException
     * @throws JadePersistenceException
     */
    @Override
    public RevenuHypothetique readByIdDonneeFinanciereHeader(String idDonneeFinanciereHeader)
            throws RevenuHypothetiqueException, JadePersistenceException {

        if (idDonneeFinanciereHeader == null) {
            throw new RevenuHypothetiqueException(
                    "Unable to find RevenuHypothetique the idDonneeFinanciereHeader passed si null!");
        }

        RevenuHypothetiqueSearch search = new RevenuHypothetiqueSearch();
        search.setForIdDonneeFinanciereHeader(idDonneeFinanciereHeader);
        search = (RevenuHypothetiqueSearch) JadePersistenceManager.search(search);

        if (search.getSearchResults().length != 1) {
            throw new RevenuHypothetiqueException("More than one RevenuHypothetique find, one was exepcted!");
        }

        return (RevenuHypothetique) search.getSearchResults()[0];
    }

    @Override
    public AbstractDonneeFinanciereSearchModel search(AbstractDonneeFinanciereSearchModel donneeFinanciereSearch)
            throws PegasusException, JadePersistenceException {
        return this.search((RevenuHypothetiqueSearch) donneeFinanciereSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuHypothetiqueService
     * #search(ch.globaz.pegasus.business.models.revenusdepenses .RevenuHypothetiqueSearch)
     */
    @Override
    public RevenuHypothetiqueSearch search(RevenuHypothetiqueSearch revenuHypothetiqueSearch)
            throws JadePersistenceException, RevenuHypothetiqueException {
        if (revenuHypothetiqueSearch == null) {
            throw new RevenuHypothetiqueException(
                    "Unable to search RevenuHypothetique, the search model passed is null!");
        }
        return (RevenuHypothetiqueSearch) JadePersistenceManager.search(revenuHypothetiqueSearch);
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.globaz.pegasus.business.services.models.revenusdepenses. RevenuHypothetiqueService
     * #update(ch.globaz.pegasus.business.models.revenusdepenses .RevenuHypothetique)
     */
    @Override
    public RevenuHypothetique update(RevenuHypothetique revenuHypothetique) throws JadePersistenceException,
            RevenuHypothetiqueException, DonneeFinanciereException {
        if (revenuHypothetique == null) {
            throw new RevenuHypothetiqueException("Unable to update RevenuHypothetique, the model passed is null!");
        }

        try {
            if(!JadeStringUtil.isBlankOrZero(revenuHypothetique.getSimpleRevenuHypothetique().getFraisDeGarde())){
                RevenusFraisGardeChecker.checkSupositionFraisGarde(revenuHypothetique.getSimpleDonneeFinanciereHeader(), IPCDroits.CS_REVENU_HYPOTHETIQUE);
            }
                revenuHypothetique.setSimpleRevenuHypothetique(PegasusImplServiceLocator
                        .getSimpleRevenuHypothetiqueService().update(revenuHypothetique.getSimpleRevenuHypothetique()));
                revenuHypothetique.setSimpleDonneeFinanciereHeader(PegasusImplServiceLocator
                        .getSimpleDonneeFinanciereHeaderService().update(
                                revenuHypothetique.getSimpleDonneeFinanciereHeader()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RevenuHypothetiqueException("Service not available - " + e.getMessage());
        }

        return revenuHypothetique;
    }

}
