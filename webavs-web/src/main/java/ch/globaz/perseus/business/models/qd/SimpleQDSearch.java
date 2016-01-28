package ch.globaz.perseus.business.models.qd;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleQDSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdMembreFamille;
    private String forIdQDMain;
    private String forType;

    public String getForIdMembreFamille() {
        return forIdMembreFamille;
    }

    public String getForIdQDMain() {
        return forIdQDMain;
    }

    public String getForType() {
        return forType;
    }

    public void setForIdMembreFamille(String forIdMembreFamille) {
        this.forIdMembreFamille = forIdMembreFamille;
    }

    public void setForIdQDMain(String forIdQDMain) {
        this.forIdQDMain = forIdQDMain;
    }

    public void setForType(String forType) {
        this.forType = forType;
    }

    @Override
    public Class<SimpleQD> whichModelClass() {
        return SimpleQD.class;
    }

}
