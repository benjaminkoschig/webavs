package ch.globaz.perseus.business.models.statistique;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.List;

public class StatistiquesMensuellesEnfantDemandeSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private List<String> forIdDemande = null;

    public List<String> getForIdDemande() {
        return forIdDemande;
    }

    public void setForIdDemande(List<String> forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    @Override
    public Class whichModelClass() {
        return StatistiquesMensuellesEnfantDemande.class;
    }

}
