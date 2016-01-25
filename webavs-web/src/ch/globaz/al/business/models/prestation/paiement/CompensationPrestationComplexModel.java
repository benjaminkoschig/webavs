package ch.globaz.al.business.models.prestation.paiement;

/**
 * Modèle de base pour la compensation sur facture de prestations AF. Ce modèle est le parent de tous les modèles liés
 * aux compensation de prestations
 * 
 * @author jts
 * 
 */
public class CompensationPrestationComplexModel extends CompensationPaiementPrestationComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Genre d'assurance
     */
    private String genreAssurance = null;
    /**
     * Numéro de l'affilié
     */
    private String numeroAffilie = null;

    /**
     * Numéro de rubrique comptable
     */
    private String numeroCompte = null;

    /**
     * Numéro de facture de la récap
     */
    private String numeroFacture = null;
    /**
     * Fin de la période de la récap
     */
    private String periodeRecapA = null;

    /**
     * Début de la période de la récap
     */
    private String periodeRecapDe = null;

    /**
     * @return the genreAssurance
     */
    public String getGenreAssurance() {
        return genreAssurance;
    }

    /**
     * @return the numeroAffilie
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * @return the numeroCompte
     */
    public String getNumeroCompte() {
        return numeroCompte;
    }

    /**
     * @return the numeroFacture
     */
    public String getNumeroFacture() {
        return numeroFacture;
    }

    /**
     * @return the periodeRecapA
     */
    public String getPeriodeRecapA() {
        return periodeRecapA;
    }

    /**
     * @return the periodeRecapDe
     */
    public String getPeriodeRecapDe() {
        return periodeRecapDe;
    }

    /**
     * @param genreAssurance
     *            the genreAssurance to set
     */
    public void setGenreAssurance(String genreAssurance) {
        this.genreAssurance = genreAssurance;
    }

    /**
     * @param numeroAffilie
     *            the numeroAffilie to set
     */
    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    /**
     * @param numeroCompte
     *            the numeroCompte to set
     */
    public void setNumeroCompte(String numeroCompte) {
        this.numeroCompte = numeroCompte;
    }

    /**
     * @param numeroFacture
     *            the numeroFacture to set
     */
    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    /**
     * @param periodeRecapA
     *            the periodeRecapA to set
     */
    public void setPeriodeRecapA(String periodeRecapA) {
        this.periodeRecapA = periodeRecapA;
    }

    /**
     * @param periodeRecapDe
     *            the periodeRecapDe to set
     */
    public void setPeriodeRecapDe(String periodeRecapDe) {
        this.periodeRecapDe = periodeRecapDe;
    }
}
