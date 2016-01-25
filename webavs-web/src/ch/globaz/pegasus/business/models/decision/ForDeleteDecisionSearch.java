package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;

public class ForDeleteDecisionSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPcAccordee = null;
    private String forIdVersionDroit = null;
    private Collection<String> inCsTypeDecsion = null;

    public String getForIdPcAccordee() {
        return forIdPcAccordee;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public Collection<String> getInCsTypeDecsion() {
        return inCsTypeDecsion;
    }

    public void setForIdPcAccordee(String forIdPcAccordee) {
        this.forIdPcAccordee = forIdPcAccordee;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setInCsTypeDecsion(Collection<String> inCsTypeDecsion) {
        this.inCsTypeDecsion = inCsTypeDecsion;
    }

    @Override
    public Class<ForDeleteDecision> whichModelClass() {
        return ForDeleteDecision.class;
    }

}
