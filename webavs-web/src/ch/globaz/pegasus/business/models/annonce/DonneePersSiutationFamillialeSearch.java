package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class DonneePersSiutationFamillialeSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String foridDroitMbrFam = null;
    private List<String> inIdDroitMbrFam = null;

    public String getForidDroitMbrFam() {
        return foridDroitMbrFam;
    }

    public void setForidDroitMbrFam(String foridDroitMbrFam) {
        this.foridDroitMbrFam = foridDroitMbrFam;
    }

    @Override
    public Class<DonneePersSiutationFamilliale> whichModelClass() {
        return DonneePersSiutationFamilliale.class;
    }

    public List<String> getInIdDroitMbrFam() {
        return inIdDroitMbrFam;
    }

    public void setInIdDroitMbrFam(List<String> inIdDroitMbrFam) {
        this.inIdDroitMbrFam = inIdDroitMbrFam;
    }

}
