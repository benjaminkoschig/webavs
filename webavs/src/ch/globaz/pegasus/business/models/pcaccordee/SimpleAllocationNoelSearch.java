package ch.globaz.pegasus.business.models.pcaccordee;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.List;

public class SimpleAllocationNoelSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnnnee = null;
    private String forIdDemande = null;
    private String forIdPcAccordee = null;
    private List<String> inIdsPcAccordee = null;

    public String getForAnnnee() {
        return forAnnnee;
    }

    public String getForIdDemande() {
        return forIdDemande;
    }

    public String getForIdPcAccordee() {
        return forIdPcAccordee;
    }

    public List<String> getInIdsPcAccordee() {
        return inIdsPcAccordee;
    }

    public void setForAnnnee(String forAnnnee) {
        this.forAnnnee = forAnnnee;
    }

    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    public void setForIdPcAccordee(String forIdPcAccordee) {
        this.forIdPcAccordee = forIdPcAccordee;
    }

    public void setInIdsPcAccordee(List<String> inIdsPcAccordee) {
        this.inIdsPcAccordee = inIdsPcAccordee;
    }

    @Override
    public Class<SimpleAllocationNoel> whichModelClass() {
        return SimpleAllocationNoel.class;
    }

}
