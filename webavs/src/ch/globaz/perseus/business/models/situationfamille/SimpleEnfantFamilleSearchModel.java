package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

public class SimpleEnfantFamilleSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdEnfant = null;
    private String forIdSituationFamiliale = null;

    /**
     * @return the forIdEnfant
     */
    public String getForIdEnfant() {
        return forIdEnfant;
    }

    /**
     * @return the forIdSituationFamiliale
     */
    public String getForIdSituationFamiliale() {
        return forIdSituationFamiliale;
    }

    /**
     * @param forIdEnfant
     *            the forIdEnfant to set
     */
    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    /**
     * @param forIdSituationFamiliale
     *            the forIdSituationFamiliale to set
     */
    public void setForIdSituationFamiliale(String forIdSituationFamiliale) {
        this.forIdSituationFamiliale = forIdSituationFamiliale;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleEnfantFamille.class;
    }

}
