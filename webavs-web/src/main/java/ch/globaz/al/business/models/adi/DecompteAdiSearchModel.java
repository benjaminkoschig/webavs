package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Classe modèle de recherche sur les décompte ADI
 * 
 * @author PTA
 * 
 */
public class DecompteAdiSearchModel extends JadeSearchSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * année du décompte
     */
    private String forAnneeDecompte = null;
    /**
     * Critère état décompte
     */
    private String forEtat = null;
    /**
     * Identifiant pour le décompte ADI
     */
    private String forIdDecompteAdi = null;
    /**
     * Identifiant du dossier
     */
    private String forIdDossier = null;
    /**
     * Recherche sur l'id d'en-tête de prestation
     */
    private String forIdPrestationAdi = null;
    /**
     * Critère de période début
     */
    private String forPeriodeDebut = null;

    /**
     * Critère de période de fin
     */
    private String forPeriodeFin = null;

    /**
     * @return the forAnnee
     */
    public String getForAnneeDecompte() {
        return forAnneeDecompte;
    }

    /**
     * @return forEtat
     */
    public String getForEtat() {
        return forEtat;
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
     * @return the forIdPrestationAdi
     */
    public String getForIdPrestationAdi() {
        return forIdPrestationAdi;
    }

    /**
     * @return forPeriodeDebut
     */
    public String getForPeriodeDebut() {
        return forPeriodeDebut;
    }

    /**
     * @return forPeriodeFin
     */
    public String getForPeriodeFin() {
        return forPeriodeFin;
    }

    /**
     * @param forAnneeDecompte
     *            the forAnneeDecompte to set
     */
    public void setForAnneeDecompte(String forAnneeDecompte) {
        this.forAnneeDecompte = forAnneeDecompte;
    }

    /**
     * 
     * @param forEtat
     *            le critère état
     */
    public void setForEtat(String forEtat) {
        this.forEtat = forEtat;
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
     * @param forIdPrestationAdi
     *            the forIdPrestationAdi to set
     */
    public void setForIdPrestationAdi(String forIdPrestationAdi) {
        this.forIdPrestationAdi = forIdPrestationAdi;
    }

    /**
     * @param forPeriodeDebut
     *            le critère période début
     */
    public void setForPeriodeDebut(String forPeriodeDebut) {
        this.forPeriodeDebut = forPeriodeDebut;
    }

    /**
     * @param forPeriodeFin
     *            le critère période fin
     */
    public void setForPeriodeFin(String forPeriodeFin) {
        this.forPeriodeFin = forPeriodeFin;
    }

    @Override
    public Class<DecompteAdiModel> whichModelClass() {

        return DecompteAdiModel.class;
    }

}
