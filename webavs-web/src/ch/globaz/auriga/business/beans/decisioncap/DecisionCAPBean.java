package ch.globaz.auriga.business.beans.decisioncap;

import java.util.ArrayList;
import java.util.List;
import ch.globaz.auriga.business.models.SimpleDecisionCAP;

public class DecisionCAPBean {

    private SimpleDecisionCAP decisionCAP;
    private List<String> listIdEnfants;

    public DecisionCAPBean() {
        decisionCAP = new SimpleDecisionCAP();
        listIdEnfants = new ArrayList<String>();
    }

    public SimpleDecisionCAP getDecisionCAP() {
        return decisionCAP;
    }

    public List<String> getListIdEnfants() {
        return listIdEnfants;
    }

    public void setDecisionCAP(SimpleDecisionCAP decisionCAP) {
        this.decisionCAP = decisionCAP;
    }

    public void setListIdEnfants(List<String> listIdEnfants) {
        this.listIdEnfants = listIdEnfants;
    }

}