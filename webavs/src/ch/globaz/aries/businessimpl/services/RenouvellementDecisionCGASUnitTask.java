package ch.globaz.aries.businessimpl.services;

import java.util.List;
import ch.globaz.aries.business.beans.decisioncgas.DecisionCGASBean;
import ch.globaz.aries.business.models.SimpleDecisionCGAS;
import ch.globaz.aries.business.services.AriesServiceLocator;
import ch.globaz.common.businessimpl.models.UnitTask;
import ch.globaz.musca.business.models.PassageModel;

public class RenouvellementDecisionCGASUnitTask extends UnitTask {

    private DecisionCGASBean decisionCGASBeanInput = null;
    private DecisionCGASBean decisionCGASBeanOutput = null;
    private boolean hasPassageModuleFacturationDecisionCGAS = false;
    private List<String> listAffiliePlusieursCotisationDansAnneeDuRenouvellement = null;
    private List<SimpleDecisionCGAS> listDecisionCGASDejaExistanteDansAnneeDuRenouvellement = null;
    private String nomPrenomAffilie = null;
    private String numeroAffilie = null;
    private PassageModel passage = null;

    public RenouvellementDecisionCGASUnitTask(DecisionCGASBean theDecisionCGASBeanInput, String theNumeroAffilie,
            String theNomPrenomAffilie, PassageModel thePassage, boolean theHasPassageModuleFacturationDecisionCGAS,
            List<SimpleDecisionCGAS> theListDecisionCGASDejaExistanteDansAnneeDuRenouvellement,
            List<String> theListAffiliePlusieursCotisationDansAnneeDuRenouvellement) {
        super(false);
        decisionCGASBeanInput = theDecisionCGASBeanInput;
        decisionCGASBeanOutput = new DecisionCGASBean();
        numeroAffilie = theNumeroAffilie;
        nomPrenomAffilie = theNomPrenomAffilie;
        passage = thePassage;
        hasPassageModuleFacturationDecisionCGAS = theHasPassageModuleFacturationDecisionCGAS;
        listDecisionCGASDejaExistanteDansAnneeDuRenouvellement = theListDecisionCGASDejaExistanteDansAnneeDuRenouvellement;
        listAffiliePlusieursCotisationDansAnneeDuRenouvellement = theListAffiliePlusieursCotisationDansAnneeDuRenouvellement;
    }

    public DecisionCGASBean getDecisionCGASBeanInput() {
        return decisionCGASBeanInput;
    }

    public DecisionCGASBean getDecisionCGASBeanOutput() {
        return decisionCGASBeanOutput;
    }

    public String getNomPrenomAffilie() {
        return nomPrenomAffilie;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    @Override
    public void work() throws Exception {

        decisionCGASBeanOutput = AriesServiceLocator.getDecisionCGASService().renouvelerDecisionCGAS(
                decisionCGASBeanInput, passage, hasPassageModuleFacturationDecisionCGAS,
                listDecisionCGASDejaExistanteDansAnneeDuRenouvellement,
                listAffiliePlusieursCotisationDansAnneeDuRenouvellement);

    }
}