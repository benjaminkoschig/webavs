/**
 * 
 */
package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.models.decision.CopiesDecision;
import ch.globaz.pegasus.business.models.decision.CopiesDecisionSearch;
import ch.globaz.pegasus.business.models.decision.DecisionHeader;
import ch.globaz.pegasus.business.models.decision.DecisionHeaderSearch;
import ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision;
import ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecisionSearch;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionSuppression;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.business.services.models.decision.DecisionHeaderService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

/**
 * @author SCE
 * 
 *         15 juil. 2010
 */
public class DecisionHeaderServiceImpl extends PegasusAbstractServiceImpl implements DecisionHeaderService {

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.decision.DecisionService#count
     * (ch.globaz.pegasus.business.models.decision.DecisionSearch)
     */
    @Override
    public int count(DecisionHeaderSearch search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count decisions, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.decision.DecisionHeaderService
     * #create(ch.globaz.pegasus.business.models.decision.DecisionHeader)
     */
    @Override
    public DecisionHeader create(DecisionHeader decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        if (decision == null) {
            throw new DecisionException("Unable to create decisions, the search model passed is null!");
        }

        decision.setSimpleDecisionHeader(PegasusImplServiceLocator.getSimpleDecisionHeaderService().create(
                decision.getSimpleDecisionHeader()));

        // TODO appeler updateAnnexesCopies

        // Ajout annexes si pas null et pas vide
        if ((decision.getListeAnnexes() != null) && (decision.getListeAnnexes().size() != 0)) {
            // pour chaques annexes
            for (SimpleAnnexesDecision annexesDecision : decision.getListeAnnexes()) {
                annexesDecision.setIdDecisionHeader(decision.getId());
                PegasusImplServiceLocator.getSimpleAnnexesDecisionService().create(annexesDecision);
            }
        }

        // Ajout copies
        if ((decision.getListeCopies() != null) && (decision.getListeCopies().size() != 0)) {
            // pour chaques copies
            for (CopiesDecision copieDecision : decision.getListeCopies()) {
                copieDecision.getSimpleCopiesDecision().setIdDecisionHeader(decision.getId());
                PegasusImplServiceLocator.getCopiesDecisionsService().create(copieDecision);
            }
        }
        return decision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.decision.DecisionHeaderService
     * #delete(ch.globaz.pegasus.business.models.decision.DecisionHeader)
     */
    @Override
    public DecisionHeader delete(DecisionHeader decision) throws DecisionException, JadePersistenceException {
        if (decision == null) {
            throw new DecisionException("unable to delete decisionHeader, the model passed is null");
        }

        try {
            PegasusImplServiceLocator.getSimpleDecisionHeaderService().delete(decision.getSimpleDecisionHeader());
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }
        return decision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.decision.DecisionService#read (java.lang.String)
     */
    @Override
    public DecisionHeader read(String idDecision) throws JadePersistenceException, DecisionException {
        if (JadeStringUtil.isEmpty(idDecision)) {
            throw new DecisionException("Unable to read decision, the id passed is null!");
        }
        DecisionHeader decision = new DecisionHeader();
        decision.setId(idDecision);
        return (DecisionHeader) JadePersistenceManager.read(decision);

    }

    @Override
    public DecisionHeader readAnnexesCopies(DecisionHeader decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        // recherche des annexes et copies
        // annexes
        SimpleAnnexesDecisionSearch annexesDecisionSearch = new SimpleAnnexesDecisionSearch();
        annexesDecisionSearch.setForIdDecisionHeader(decision.getSimpleDecisionHeader().getIdDecisionHeader());
        annexesDecisionSearch = PegasusServiceLocator.getSimpleAnnexesDecisionsService().search(annexesDecisionSearch);
        // Parcours des objets
        for (JadeAbstractModel searchModel : annexesDecisionSearch.getSearchResults()) {
            decision.addToListeAnnexes((SimpleAnnexesDecision) searchModel);
        }

        // copies
        CopiesDecisionSearch copiesDecisionSearch = new CopiesDecisionSearch();
        copiesDecisionSearch.setForIdDecisionHeader(decision.getSimpleDecisionHeader().getIdDecisionHeader());
        copiesDecisionSearch = PegasusServiceLocator.getCopiesDecisionsService().search(copiesDecisionSearch);
        // Parcours des objets
        for (JadeAbstractModel searchModel : copiesDecisionSearch.getSearchResults()) {
            decision.addToListCopies((CopiesDecision) searchModel);
        }

        return decision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.globaz.pegasus.business.services.models.decision.DecisionService#search
     * (ch.globaz.pegasus.business.models.decision.DecisionSearch)
     */
    @Override
    public DecisionHeaderSearch search(DecisionHeaderSearch decisionSearch) throws JadePersistenceException,
            DecisionException {
        if (decisionSearch == null) {
            throw new DecisionException("Unable to search decision, the search model passed is null!");
        }
        return (DecisionHeaderSearch) JadePersistenceManager.search(decisionSearch);
    }

    @Override
    public SimpleVersionDroit loadSimpleVersionDroit(SimpleDecisionHeader decision) throws DecisionException,
            JadePersistenceException {
        String idVersionDroit = null;

        if (decision.getType().isTypeApresCalcul()) {
            SimpleDecisionApresCalcul decisionApresCalcul;
            try {
                decisionApresCalcul = PegasusImplServiceLocator.getSimpleDecisionApresCalculService()
                        .readByIdDecisionHeader(decision.getIdDecisionHeader());
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new RuntimeException(e);
            }
            idVersionDroit = decisionApresCalcul.getIdVersionDroit();
        } else if (decision.getType().isSuppression()) {
            try {
                SimpleDecisionSuppression simpleDecisionSuppression = PegasusImplServiceLocator
                        .getSimpleDecisionSuppressionService().readByIdDecisionHeader(decision.getIdDecisionHeader());
                idVersionDroit = simpleDecisionSuppression.getIdVersionDroit();
            } catch (JadeApplicationServiceNotAvailableException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new DecisionException("Type(" + decision.getCsTypeDecision()
                    + ") of décision not supported ! for this id decisionHeader: " + decision.getId());
        }

        SimpleVersionDroit simpleVersionDroit = null;
        try {
            simpleVersionDroit = PegasusImplServiceLocator.getSimpleVersionDroitService().read(idVersionDroit);
        } catch (DroitException e) {
            throw new RuntimeException(e);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new RuntimeException(e);
        }
        return simpleVersionDroit;
    }

    @Override
    public DecisionHeader updateAnnexesCopies(DecisionHeader decision) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if (decision == null) {
            throw new DecisionException("Unable to update decisions, the search model passed is null!");
        }

        PegasusImplServiceLocator.getSimpleAnnexesDecisionService().deleteForDecision(
                decision.getSimpleDecisionHeader().getIdDecisionHeader());
        if ((decision.getListeAnnexes() != null) && (decision.getListeAnnexes().size() != 0)) {
            // pour chaques annexes
            for (SimpleAnnexesDecision annexesDecision : decision.getListeAnnexes()) {
                annexesDecision.setIdDecisionHeader(decision.getId());
                PegasusImplServiceLocator.getSimpleAnnexesDecisionService().create(annexesDecision);
            }
        }

        PegasusImplServiceLocator.getSimpleCopiesDecisionsService().deleteForDecision(
                decision.getSimpleDecisionHeader().getIdDecisionHeader());
        if ((decision.getListeCopies() != null) && (decision.getListeCopies().size() != 0)) {
            // pour chaques copies
            for (CopiesDecision copieDecision : decision.getListeCopies()) {
                copieDecision.getSimpleCopiesDecision().setIdDecisionHeader(decision.getId());
                PegasusImplServiceLocator.getCopiesDecisionsService().create(copieDecision);
            }
        }

        return decision;
    }

}
