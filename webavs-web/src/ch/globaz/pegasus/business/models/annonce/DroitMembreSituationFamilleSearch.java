package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class DroitMembreSituationFamilleSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDroit = null;
    private List<String> forInCsRoleFamille = null;

    public String getForIdDroit() {
        return forIdDroit;
    }

    public List<String> getForInCsRoleFamille() {
        return forInCsRoleFamille;
    }

    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    public void setForInCsRoleFamille(List<String> forInCsRoleFamille) {
        this.forInCsRoleFamille = forInCsRoleFamille;
    }

    @Override
    public Class<DroitMembreSituationFamille> whichModelClass() {
        return DroitMembreSituationFamille.class;
    }

}
