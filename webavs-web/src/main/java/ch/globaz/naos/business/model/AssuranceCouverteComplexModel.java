package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.AdministrationSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;

public class AssuranceCouverteComplexModel extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private AdhesionSimpleModel adhesion = new AdhesionSimpleModel();
    private AffiliationSimpleModel affiliation = new AffiliationSimpleModel();
    private AssuranceSimpleModel assurance = new AssuranceSimpleModel();
    private AdministrationSimpleModel caisseProf = new AdministrationSimpleModel();
    private CotisationSimpleModel cotisation = new CotisationSimpleModel();
    private ParametreAssuranceSimpleModel parametreAssurance = new ParametreAssuranceSimpleModel();
    private PlanCaisseSimpleModel planCaisse = new PlanCaisseSimpleModel();
    private TiersSimpleModel tiersAffiliation = new TiersSimpleModel();

    public AdhesionSimpleModel getAdhesion() {
        return adhesion;
    }

    public AffiliationSimpleModel getAffiliation() {
        return affiliation;
    }

    public AssuranceSimpleModel getAssurance() {
        return assurance;
    }

    public AdministrationSimpleModel getCaisseProf() {
        return caisseProf;
    }

    public CotisationSimpleModel getCotisation() {
        return cotisation;
    }

    @Override
    public String getId() {
        return assurance.getAssuranceId();
    }

    public ParametreAssuranceSimpleModel getParametreAssurance() {
        return parametreAssurance;
    }

    public PlanCaisseSimpleModel getPlanCaisse() {
        return planCaisse;
    }

    @Override
    public String getSpy() {
        return assurance.getSpy();
    }

    public TiersSimpleModel getTiersAffiliation() {
        return tiersAffiliation;
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

    public void setCaisseProf(AdministrationSimpleModel caisseProf) {
        this.caisseProf = caisseProf;
    }

    public void setCotisation(CotisationSimpleModel cotisation) {
        this.cotisation = cotisation;
    }

    @Override
    public void setId(String id) {
        assurance.setAssuranceId(id);
    }

    public void setParametreAssurance(ParametreAssuranceSimpleModel parametreAssurance) {
        this.parametreAssurance = parametreAssurance;
    }

    public void setPlanCaisse(PlanCaisseSimpleModel planCaisse) {
        this.planCaisse = planCaisse;
    }

    @Override
    public void setSpy(String spy) {
        assurance.setSpy(spy);
    }

    public void setTiersAffiliation(TiersSimpleModel tiersAffiliation) {
        this.tiersAffiliation = tiersAffiliation;
    }

}
