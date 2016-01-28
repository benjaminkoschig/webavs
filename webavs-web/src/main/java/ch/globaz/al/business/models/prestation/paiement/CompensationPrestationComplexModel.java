package ch.globaz.al.business.models.prestation.paiement;

/**
 * Mod�le de base pour la compensation sur facture de prestations AF. Ce mod�le est le parent de tous les mod�les li�s
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
     * Num�ro de l'affili�
     */
    private String numeroAffilie = null;

    /**
     * Num�ro de rubrique comptable
     */
    private String numeroCompte = null;

    /**
     * Num�ro de facture de la r�cap
     */
    private String numeroFacture = null;
    /**
     * Fin de la p�riode de la r�cap
     */
    private String periodeRecapA = null;

    /**
     * D�but de la p�riode de la r�cap
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
