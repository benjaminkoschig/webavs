package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Modèle de recherche sur les adi enfant par mois complexe (jointure sur droitcomplex)
 * 
 * @author GMO
 * 
 */
public class AdiEnfantMoisComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Critère de recherche décompte adi
     */
    private String forIdDecompteAdi = null;
    /**
     * Critère de recherche id du droit
     */
    private String forIdDroit = null;
    /**
     * Critère de recherche id enfant
     */
    private String forIdEnfant = null;
    /**
     * Critère période
     */
    private String forPeriode = null;

    /**
     * @return forIdDecompteAdi
     */
    public String getForIdDecompteAdi() {
        return forIdDecompteAdi;
    }

    /**
     * @return forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * 
     * @return forIdEnfant
     */
    public String getForIdEnfant() {
        return forIdEnfant;
    }

    /**
     * 
     * @return forPeriode
     */
    public String getForPeriode() {
        return forPeriode;
    }

    /**
     * @param forIdDecompteAdi
     *            le critère id décompte adi
     */
    public void setForIdDecompteAdi(String forIdDecompteAdi) {
        this.forIdDecompteAdi = forIdDecompteAdi;
    }

    /**
     * 
     * @param forIdDroit
     *            le critère id du droit
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * 
     * @param forIdEnfant
     *            le critère id enfant
     */
    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    /**
     * 
     * @param forPeriode
     *            le critère période
     */
    public void setForPeriode(String forPeriode) {
        this.forPeriode = forPeriode;
    }

    @Override
    public Class<AdiEnfantMoisComplexModel> whichModelClass() {
        return AdiEnfantMoisComplexModel.class;
    }

}
