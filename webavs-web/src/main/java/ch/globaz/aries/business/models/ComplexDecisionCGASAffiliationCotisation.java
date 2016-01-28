package ch.globaz.aries.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AdhesionSimpleModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.naos.business.model.CotisationSimpleModel;

/**
 * @author MMO
 * @date 04.02.2013
 */
public class ComplexDecisionCGASAffiliationCotisation extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AdhesionSimpleModel adhesion = null;
    private AffiliationSimpleModel affiliation = null;
    private AssuranceSimpleModel assurance = null;
    private CotisationSimpleModel cotisation = null;
    private SimpleDecisionCGAS decisionCGAS = null;

    public ComplexDecisionCGASAffiliationCotisation() {
        super();
        decisionCGAS = new SimpleDecisionCGAS();
        affiliation = new AffiliationSimpleModel();
        adhesion = new AdhesionSimpleModel();
        assurance = new AssuranceSimpleModel();
        cotisation = new CotisationSimpleModel();
    }

    public AdhesionSimpleModel getAdhesion() {
        return adhesion;
    }

    public AffiliationSimpleModel getAffiliation() {
        return affiliation;
    }

    public AssuranceSimpleModel getAssurance() {
        return assurance;
    }

    public CotisationSimpleModel getCotisation() {
        return cotisation;
    }

    public SimpleDecisionCGAS getDecisionCGAS() {
        return decisionCGAS;
    }

    @Override
    public String getId() {
        return decisionCGAS.getId();
    }

    @Override
    public String getSpy() {
        return decisionCGAS.getSpy();
    }

    public void setAdhesion(AdhesionSimpleModel adhesion) {
        this.adhesion = adhesion;
    }

    public void setAffiliation(AffiliationSimpleModel affiliation) {
        this.affiliation = affiliation;
    }

    public void setAssurance(AssuranceSimpleModel assurance) {
        this.assurance = assurance;
    }

    public void setCotisation(CotisationSimpleModel cotisation) {
        this.cotisation = cotisation;
    }

    public void setDecisionCGAS(SimpleDecisionCGAS decisionCGAS) {
        this.decisionCGAS = decisionCGAS;
    }

    @Override
    public void setId(String id) {
        decisionCGAS.setId(id);

    }

    @Override
    public void setSpy(String spy) {
        decisionCGAS.setSpy(spy);

    }

}
