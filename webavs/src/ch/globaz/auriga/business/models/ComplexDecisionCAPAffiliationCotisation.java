package ch.globaz.auriga.business.models;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.naos.business.model.AdhesionSimpleModel;
import ch.globaz.naos.business.model.AffiliationSimpleModel;
import ch.globaz.naos.business.model.AssuranceSimpleModel;
import ch.globaz.naos.business.model.CotisationSimpleModel;

/**
 * @author MMO
 * @date 20.02.2013
 */
public class ComplexDecisionCAPAffiliationCotisation extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AdhesionSimpleModel adhesion = null;
    private AffiliationSimpleModel affiliation = null;
    private AssuranceSimpleModel assurance = null;
    private CotisationSimpleModel cotisation = null;
    private SimpleDecisionCAP decisionCAP = null;

    public ComplexDecisionCAPAffiliationCotisation() {
        super();
        decisionCAP = new SimpleDecisionCAP();
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

    public SimpleDecisionCAP getDecisionCAP() {
        return decisionCAP;
    }

    @Override
    public String getId() {
        return decisionCAP.getId();
    }

    @Override
    public String getSpy() {
        return decisionCAP.getSpy();
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

    public void setDecisionCAP(SimpleDecisionCAP decisionCAP) {
        this.decisionCAP = decisionCAP;
    }

    @Override
    public void setId(String id) {
        decisionCAP.setId(id);

    }

    @Override
    public void setSpy(String spy) {
        decisionCAP.setSpy(spy);

    }

}
