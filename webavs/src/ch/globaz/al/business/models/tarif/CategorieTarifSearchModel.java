package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle permettant la recherche de catégories de tarif
 * 
 * @author jts
 * 
 */
public class CategorieTarifSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche sur la catégorie de tarif
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE
     */
    private String forCategorieTarif = null;
    /**
     * Recherche sur l'id de la catégorie de tarif
     */
    private String forIdCategorieTarif = null;
    /**
     * Recherche sur l'id de la législation
     */
    private String forIdLegislation = null;

    /**
     * @return the forCategorieTarif
     */
    public String getForCategorieTarif() {
        return forCategorieTarif;
    }

    /**
     * @return the forIdCategorieTarif
     */
    public String getForIdCategorieTarif() {
        return forIdCategorieTarif;
    }

    /**
     * @return the forIdLegislation
     */
    public String getForIdLegislation() {
        return forIdLegislation;
    }

    /**
     * @param forCategorieTarif
     *            the forCategorieTarif to set
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_CATEGORIE
     */
    public void setForCategorieTarif(String forCategorieTarif) {
        this.forCategorieTarif = forCategorieTarif;
    }

    /**
     * @param forIdCategorieTarif
     *            the forIdCategorieTarif to set
     */
    public void setForIdCategorieTarif(String forIdCategorieTarif) {
        this.forIdCategorieTarif = forIdCategorieTarif;
    }

    /**
     * @param forIdLegislation
     *            the forIdLegislation to set
     */
    public void setForIdLegislation(String forIdLegislation) {
        this.forIdLegislation = forIdLegislation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<CategorieTarifModel> whichModelClass() {
        return CategorieTarifModel.class;
    }

}
