package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Mod�le de recherche sur les adi enfant par mois complexe (jointure sur droitcomplex)
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
     * Crit�re de recherche d�compte adi
     */
    private String forIdDecompteAdi = null;
    /**
     * Crit�re de recherche id du droit
     */
    private String forIdDroit = null;
    /**
     * Crit�re de recherche id enfant
     */
    private String forIdEnfant = null;
    /**
     * Crit�re p�riode
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
     *            le crit�re id d�compte adi
     */
    public void setForIdDecompteAdi(String forIdDecompteAdi) {
        this.forIdDecompteAdi = forIdDecompteAdi;
    }

    /**
     * 
     * @param forIdDroit
     *            le crit�re id du droit
     */
    public void setForIdDroit(String forIdDroit) {
        this.forIdDroit = forIdDroit;
    }

    /**
     * 
     * @param forIdEnfant
     *            le crit�re id enfant
     */
    public void setForIdEnfant(String forIdEnfant) {
        this.forIdEnfant = forIdEnfant;
    }

    /**
     * 
     * @param forPeriode
     *            le crit�re p�riode
     */
    public void setForPeriode(String forPeriode) {
        this.forPeriode = forPeriode;
    }

    @Override
    public Class<AdiEnfantMoisComplexModel> whichModelClass() {
        return AdiEnfantMoisComplexModel.class;
    }

}
