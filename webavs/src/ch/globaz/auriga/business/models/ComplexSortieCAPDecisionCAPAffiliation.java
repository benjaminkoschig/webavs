package ch.globaz.auriga.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;

public class ComplexSortieCAPDecisionCAPAffiliation extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AffiliationSimpleModel affiliation;
    private SimpleDecisionCAP decisionCap;
    private SimpleDecisionCAP decisionCapRectifiee;
    private SimpleSortieCAP sortieCap;

    public ComplexSortieCAPDecisionCAPAffiliation() {
        super();
        sortieCap = new SimpleSortieCAP();
        decisionCap = new SimpleDecisionCAP();
        affiliation = new AffiliationSimpleModel();
        decisionCapRectifiee = new SimpleDecisionCAP();
    }

    public AffiliationSimpleModel getAffiliation() {
        return affiliation;
    }

    public SimpleDecisionCAP getDecisionCap() {
        return decisionCap;
    }

    public SimpleDecisionCAP getDecisionCapRectifiee() {
        return decisionCapRectifiee;
    }

    @Override
    public String getId() {
        return sortieCap.getIdSortie();
    }

    public SimpleSortieCAP getSortieCap() {
        return sortieCap;
    }

    @Override
    public String getSpy() {
        return sortieCap.getSpy();
    }

    public void setAffiliation(AffiliationSimpleModel affiliation) {
        this.affiliation = affiliation;
    }

    public void setDecisionCap(SimpleDecisionCAP decisionCap) {
        this.decisionCap = decisionCap;
    }

    public void setDecisionCapRectifiee(SimpleDecisionCAP decisionCapRectifiee) {
        this.decisionCapRectifiee = decisionCapRectifiee;
    }

    @Override
    public void setId(String id) {
        sortieCap.setIdSortie(id);
    }

    public void setSortieCap(SimpleSortieCAP sortieCap) {
        this.sortieCap = sortieCap;
    }

    @Override
    public void setSpy(String spy) {
        sortieCap.setSpy(spy);
    }

}
