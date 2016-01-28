package ch.globaz.pegasus.process.adapationHome.validerDecision;

import globaz.globall.db.BSessionUtil;
import globaz.globall.util.JACalendar;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import java.util.Map;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.droit.VersionDroit;
import ch.globaz.pegasus.business.models.droit.VersionDroitSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;
import ch.globaz.pegasus.process.util.AbstractEntity;

public class PCProcessAdaptationHomeEntityHandler extends AbstractEntity implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PCProcessAdapationEnum>, JadeProcessEntityNeedProperties {

    @Override
    public Map<PCProcessAdapationEnum, String> getValueToSave() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {

        VersionDroitSearch searchVersionDroit = new VersionDroitSearch();
        searchVersionDroit.setForIdDemandePc(getEntity().getIdRef());
        searchVersionDroit.setWhereKey(VersionDroitSearch.CURRENT_VERSION);
        searchVersionDroit = PegasusServiceLocator.getDroitService().searchVersionDroit(searchVersionDroit);
        if (searchVersionDroit.getSearchResults().length == 1) {
            VersionDroit versionDroit = (VersionDroit) searchVersionDroit.getSearchResults()[0];

            DecisionApresCalcul decisionApresCalcul = new DecisionApresCalcul();
            decisionApresCalcul.setVersionDroit(versionDroit);
            decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                    .setDateDecision(JACalendar.todayJJsMMsAAAA());
            decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                    .setPreparationPar(BSessionUtil.getSessionFromThreadContext().getUserId());

            // date decision amal
            // AmalUtilsForDecisionsPC amalUtil = new AmalUtilsForDecisionsPC();
            // amalUtil.checkAndGenerateWarningCoherenceWithAmal(versionDroit.getSimpleVersionDroit().getIdVersionDroit(),
            // BSessionUtil.getSessionFromThreadContext());
            // decisionApresCalcul.getSimpleDecisionApresCalcul().setDateDecisionAmal(amalUtil.getDateDecisionAmal());

            try {
                // TODO retourner les decisions
                PegasusServiceLocator.getDecisionApresCalculService().createStandardDecision(decisionApresCalcul);
            } catch (Exception e) {
                throw new AdaptationException("Unable to create the decision", e);
            }

            // Recherche les decisions
            DecisionApresCalculSearch decisionApresCalculSearch = new DecisionApresCalculSearch();
            // decisionApresCalculSearch.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            // decisionApresCalculSearch.setWhereKey(DecisionApresCalculSearch.WITH_CURRENT_VERSION);
            decisionApresCalculSearch.setForIdVersionDroit(versionDroit.getSimpleVersionDroit().getIdVersionDroit());
            decisionApresCalculSearch = PegasusServiceLocator.getDecisionApresCalculService().search(
                    decisionApresCalculSearch);

            // Prévalidationd
            for (JadeAbstractModel model : decisionApresCalculSearch.getSearchResults()) {
                DecisionApresCalcul dec = (DecisionApresCalcul) model;
                PegasusServiceLocator.getValidationDecisionService().preValidDecisionApresCalcul(dec);
            }

            PegasusServiceLocator.getValidationDecisionService().validerToutesLesDesionsionApresCalcule(
                    versionDroit.getSimpleVersionDroit().getIdVersionDroit(), false);
        } else {
            if (searchVersionDroit.getSize() > 1) {
                JadeThread.logError(this.getClass().getName(), "process.adaptationHome.tropDeDroitTrouve");
            } else {
                JadeThread.logError(this.getClass().getName(), "process.adaptationHome.pasDeDroitDroitTrouve");
            }
        }

    }

    @Override
    public void setProperties(Map<Enum<?>, String> arg0) {
        // TODO Auto-generated method stub

    }

}
