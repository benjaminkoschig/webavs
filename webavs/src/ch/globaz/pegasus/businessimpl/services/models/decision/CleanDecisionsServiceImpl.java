package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.decision.DecisionSuppressionSearch;
import ch.globaz.pegasus.business.models.decision.ForDeleteDecisionSearch;
import ch.globaz.pegasus.business.services.models.decision.CleanDecisionsService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;

public class CleanDecisionsServiceImpl extends PegasusAbstractServiceImpl implements CleanDecisionsService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.globaz.pegasus.business.services.models.decision.CleanDecisionsService#deleteDecisionsApresCalculForVersion
     * (java.lang.String)
     */
    @Override
    public void deleteDecisionsApresCalculForVersion(String idVersionDroit) throws JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException {

        if (idVersionDroit == null) {
            throw new DecisionException("Unable to delete decision for versionDroit, the id version droit is null");
        }

        ForDeleteDecisionSearch search = new ForDeleteDecisionSearch();
        // recherche des décision apresCalcul selon versiondroit
        search.setForIdVersionDroit(idVersionDroit);
        search.setInCsTypeDecsion(new ArrayList<String>() {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            {
                this.add(IPCDecision.CS_TYPE_ADAPTATION_AC);
                this.add(IPCDecision.CS_TYPE_OCTROI_AC);
                this.add(IPCDecision.CS_TYPE_PARTIEL_AC);
                this.add(IPCDecision.CS_TYPE_REFUS_AC);
            }
        });

        search = (ForDeleteDecisionSearch) JadePersistenceManager.search(search);

        PegasusImplServiceLocator.getDecisionApresCalculService().delete(search);
    }

    @Override
    public void deleteDecisionsForVersion(String idVersionDroit) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        deleteDecisionsApresCalculForVersion(idVersionDroit);
        deleteDecisionsSuppressionForVersion(idVersionDroit);
    }

    @Override
    public void deleteDecisionsSuppressionForVersion(String idVersionDroit) throws JadePersistenceException,
            DecisionException, JadeApplicationServiceNotAvailableException {

        if (idVersionDroit == null) {
            throw new DecisionException("Unable to delete decision for versionDroit, the id version droit is null");
        }
        // recherche des décision de suppression
        DecisionSuppressionSearch decisionSuppressionSearch = new DecisionSuppressionSearch();

        decisionSuppressionSearch.setForIdVersionDroit(idVersionDroit);
        decisionSuppressionSearch = (DecisionSuppressionSearch) JadePersistenceManager
                .search(decisionSuppressionSearch);

        String idDecisionHeader = null;

        // Supression des décisions
        for (JadeAbstractModel dac : decisionSuppressionSearch.getSearchResults()) {

            // si l'id n'est pas setter, et qu'il n'est pas egal à l'id, on supprime
            if ((idDecisionHeader == null)
                    || !idDecisionHeader.equals(((DecisionSuppression) dac).getDecisionHeader()
                            .getSimpleDecisionHeader().getIdDecisionHeader())) {
                PegasusImplServiceLocator.getDecisionSuppressionService().delete((DecisionSuppression) dac);
            }
            // Id decision header
            idDecisionHeader = ((DecisionSuppression) dac).getDecisionHeader().getSimpleDecisionHeader().getId();

        }

    }

}
