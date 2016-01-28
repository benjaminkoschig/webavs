package ch.globaz.auriga.businessimpl.services;

import java.util.List;
import ch.globaz.auriga.business.beans.decisioncap.DecisionCAPBean;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;
import ch.globaz.auriga.business.services.AurigaServiceLocator;
import ch.globaz.common.businessimpl.models.UnitTask;
import ch.globaz.musca.business.models.PassageModel;

public class RenouvellementDecisionCAPUnitTask extends UnitTask {

    private DecisionCAPBean decisionCAPBeanInput = null;
    private DecisionCAPBean decisionCAPBeanOutput = null;
    private boolean hasPassageModuleFacturationDecisionCAP = false;
    private String idAssurance = null;
    private List<String> listAffiliePlusieursCotisationDansAnneeDuRenouvellement = null;
    private List<SimpleDecisionCAP> listDecisionCAPDejaExistanteDansAnneeDuRenouvellement = null;
    private String nomPrenomAffilie = null;
    private String numeroAffilie = null;
    private PassageModel passage = null;
    private String typeAssurance = null;

    public RenouvellementDecisionCAPUnitTask(DecisionCAPBean theDecisionCAPBeanInput, String theNumeroAffilie,
            String theNomPrenomAffilie, PassageModel thePassage, boolean theHasPassageModuleFacturationDecisionCAP,
            String theTypeAssurance, List<SimpleDecisionCAP> theListDecisionCAPDejaExistanteDansAnneeDuRenouvellement,
            List<String> theListAffiliePlusieursCotisationDansAnneeDuRenouvellement, String theIdAssurance) {
        super(false);
        decisionCAPBeanInput = theDecisionCAPBeanInput;
        decisionCAPBeanOutput = new DecisionCAPBean();
        numeroAffilie = theNumeroAffilie;
        nomPrenomAffilie = theNomPrenomAffilie;
        passage = thePassage;
        hasPassageModuleFacturationDecisionCAP = theHasPassageModuleFacturationDecisionCAP;
        typeAssurance = theTypeAssurance;
        listDecisionCAPDejaExistanteDansAnneeDuRenouvellement = theListDecisionCAPDejaExistanteDansAnneeDuRenouvellement;
        listAffiliePlusieursCotisationDansAnneeDuRenouvellement = theListAffiliePlusieursCotisationDansAnneeDuRenouvellement;
        idAssurance = theIdAssurance;
    }

    public DecisionCAPBean getDecisionCAPBeanInput() {
        return decisionCAPBeanInput;
    }

    public String getNomPrenomAffilie() {
        return nomPrenomAffilie;
    }

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    @Override
    public void work() throws Exception {

        decisionCAPBeanOutput = AurigaServiceLocator.getDecisionCAPService().renouvelerDecisionCAP(
                decisionCAPBeanInput, passage, hasPassageModuleFacturationDecisionCAP, typeAssurance,
                listDecisionCAPDejaExistanteDansAnneeDuRenouvellement,
                listAffiliePlusieursCotisationDansAnneeDuRenouvellement, idAssurance);

    }
}