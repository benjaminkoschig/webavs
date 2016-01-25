package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche pour AdiEnfantMoisModel;
 * 
 * @author PTA
 * 
 */
public class AdiEnfantMoisSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * identifiant du decompteId
     */
    private String forIdDecompteAdi = null;
    /**
     * identifiant du droit
     */
    private String forIdDroit = null;

    /**
     * @return the forIdDecompteAdi
     */
    public String getForIdDecompteAdi() {
        return forIdDecompteAdi;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @param forIdDecompteAdi
     *            the forIdDecompteAdi to set
     */
    public void setForIdDecompteAdi(String forIdDecompteAdi) {
        this.forIdDecompteAdi = forIdDecompteAdi;
    }

    /**
     * @param forIdDroit
     *            the forIdDroit to set
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    @Override
    public Class whichModelClass() {

        return AdiEnfantMoisModel.class;
    }

}
