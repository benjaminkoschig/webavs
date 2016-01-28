package ch.globaz.ij.business.models;

import globaz.jade.persistence.model.JadeAbstractSearchModel;

public class IJPrononceJointDemandeSearchModel extends JadeAbstractSearchModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdPrononce;
    private String forIdTiers;
    private String forPrononceSelectionne;

    public IJPrononceJointDemandeSearchModel() {
        forIdPrononce = "";
        forIdTiers = "";
        forPrononceSelectionne = "";
    }

    public String getForIdPrononce() {
        return forIdPrononce;
    }

    public String getForIdTiers() {
        return forIdTiers;
    }

    public final String getForPrononceSelectionne() {
        return forPrononceSelectionne;
    }

    public void setForIdPrononce(String forIdPrononce) {
        this.forIdPrononce = forIdPrononce;
    }

    public void setForIdTiers(String forIdTiers) {
        this.forIdTiers = forIdTiers;
    }

    public final void setForPrononceSelectionne(String forPrononceSelectionne) {
        this.forPrononceSelectionne = forPrononceSelectionne;
    }

    @Override
    public Class<?> whichModelClass() {
        return IJPrononceJointDemande.class;
    }

}
