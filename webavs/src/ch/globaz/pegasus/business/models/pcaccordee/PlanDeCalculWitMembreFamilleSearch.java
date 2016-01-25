package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class PlanDeCalculWitMembreFamilleSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forComprisPcal = null;
    private String forCsRoleFamille = null;
    private String forIdPCAccordee = null;
    private String forIdPcal = null;
    private Boolean forIsPlanRetenu = null;
    private List<String> inIdPCAccordee = null;
    private List<String> inCsRoleFamille = null;

    public Boolean getForComprisPcal() {
        return forComprisPcal;
    }

    public String getForCsRoleFamille() {
        return forCsRoleFamille;
    }

    public String getForIdPCAccordee() {
        return forIdPCAccordee;
    }

    public String getForIdPcal() {
        return forIdPcal;
    }

    public void setForComprisPcal(Boolean forComprisPcal) {
        this.forComprisPcal = forComprisPcal;
    }

    public void setForCsRoleFamille(String forCsRoleFamille) {
        this.forCsRoleFamille = forCsRoleFamille;
    }

    public void setForIdPCAccordee(String forIdPCAccordee) {
        this.forIdPCAccordee = forIdPCAccordee;
    }

    public void setForIdPcal(String forIdPcal) {
        this.forIdPcal = forIdPcal;
    }

    @Override
    public Class<PlanDeCalculWitMembreFamille> whichModelClass() {
        return PlanDeCalculWitMembreFamille.class;
    }

    public Boolean getForIsPlanRetenu() {
        return forIsPlanRetenu;
    }

    public void setForIsPlanRetenu(Boolean forIsPlanRetenu) {
        this.forIsPlanRetenu = forIsPlanRetenu;
    }

    public List<String> getInIdPCAccordee() {
        return inIdPCAccordee;
    }

    public void setInIdPCAccordee(List<String> inIdPCAccordee) {
        this.inIdPCAccordee = inIdPCAccordee;
    }

    public List<String> getInCsRoleFamille() {
        return inCsRoleFamille;
    }

    public void setInCsRoleFamille(List<String> inCsRoleFamille) {
        this.inCsRoleFamille = inCsRoleFamille;
    }

}
