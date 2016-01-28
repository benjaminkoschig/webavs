package ch.globaz.al.business.models.adi;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Classe mod�le de recherche sur les d�compte ADI
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
     * ann�e du d�compte
     */
    private String forAnneeDecompte = null;
    /**
     * Crit�re �tat d�compte
     */
    private String forEtat = null;
    /**
     * Identifiant pour le d�compte ADI
     */
    private String forIdDecompteAdi = null;
    /**
     * Identifiant du dossier
     */
    private String forIdDossier = null;
    /**
     * Recherche sur l'id d'en-t�te de prestation
     */
    private String forIdPrestationAdi = null;
    /**
     * Crit�re de p�riode d�but
     */
    private String forPeriodeDebut = null;

    /**
     * Crit�re de p�riode de fin
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
     *            le crit�re �tat
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
     *            le crit�re p�riode d�but
     */
    public void setForPeriodeDebut(String forPeriodeDebut) {
        this.forPeriodeDebut = forPeriodeDebut;
    }

    /**
     * @param forPeriodeFin
     *            le crit�re p�riode fin
     */
    public void setForPeriodeFin(String forPeriodeFin) {
        this.forPeriodeFin = forPeriodeFin;
    }

    @Override
    public Class<DecompteAdiModel> whichModelClass() {

        return DecompteAdiModel.class;
    }

}
