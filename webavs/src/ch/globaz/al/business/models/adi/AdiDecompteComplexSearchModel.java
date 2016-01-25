package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * Modèle de recherche pour les décomptes globaux ADI
 * 
 * @author PTA
 * 
 */
public class AdiDecompteComplexSearchModel extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * annee du decompte
     */
    private String forAnnnee = null;
    /**
     * idntifiant du décompte
     */
    private String forIdDecompteAdi = null;

    /**
     * identifiant du dossier
     */
    private String forIdDossier = null;

    /**
     * identifiant du droit
     */
    private String forIdDroit = null;

    /**
     * @return the forAnnnee
     */
    public String getForAnnnee() {
        return forAnnnee;
    }

    /**
     * @return the forIdDecompteAdi
     */
    public String getForIdDecompteAdi() {
        return forIdDecompteAdi;
    }

    /**
     * @return the forIdDossier
     */
    public String getForIdDossier() {
        return forIdDossier;
    }

    /**
     * @return the forIdDroit
     */
    public String getForIdDroit() {
        return forIdDroit;
    }

    /**
     * @param forAnnnee
     *            the forAnnnee to set
     */
    public void setForAnnnee(String forAnnnee) {
        this.forAnnnee = forAnnnee;
    }

    /**
     * @param forIdDecompteAdi
     *            the forIdDecompteAdi to set
     */
    public void setForIdDecompteAdi(String forIdDecompteAdi) {
        this.forIdDecompteAdi = forIdDecompteAdi;
    }

    /**
     * @param forIdDossier
     *            the forIdDossier to set
     */
    public void setForIdDossier(String forIdDossier) {
        this.forIdDossier = forIdDossier;
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
        return AdiDecompteComplexModel.class;
    }

}
