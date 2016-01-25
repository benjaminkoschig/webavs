package ch.globaz.aries.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;

public class ComplexSortieCGASDecisionCGASAffiliation extends JadeComplexModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AffiliationSimpleModel affiliation;
    private SimpleDecisionCGAS decisionCgas;
    private SimpleDecisionCGAS decisionCgasRectifiee;
    private SimpleSortieCGAS sortieCgas;

    public ComplexSortieCGASDecisionCGASAffiliation() {
        super();
        sortieCgas = new SimpleSortieCGAS();
        decisionCgas = new SimpleDecisionCGAS();
        affiliation = new AffiliationSimpleModel();
        setDecisionCgasRectifiee(new SimpleDecisionCGAS());
    }

    public AffiliationSimpleModel getAffiliation() {
        return affiliation;
    }

    public SimpleDecisionCGAS getDecisionCgas() {
        return decisionCgas;
    }

    @Override
    public String getId() {
        return sortieCgas.getIdSortie();
    }

    public SimpleSortieCGAS getSortieCgas() {
        return sortieCgas;
    }

    @Override
    public String getSpy() {
        return sortieCgas.getSpy();
    }

    public void setAffiliation(AffiliationSimpleModel affiliation) {
        this.affiliation = affiliation;
    }

    public void setDecisionCgas(SimpleDecisionCGAS decisionCgas) {
        this.decisionCgas = decisionCgas;
    }

    @Override
    public void setId(String id) {
        sortieCgas.setIdSortie(id);
    }

    public void setSortieCgas(SimpleSortieCGAS sortieCgas) {
        this.sortieCgas = sortieCgas;
    }

    @Override
    public void setSpy(String spy) {
        sortieCgas.setSpy(spy);
    }

    public SimpleDecisionCGAS getDecisionCgasRectifiee() {
        return decisionCgasRectifiee;
    }

    public void setDecisionCgasRectifiee(SimpleDecisionCGAS decisionCgasRectifiee) {
        this.decisionCgasRectifiee = decisionCgasRectifiee;
    }

}
