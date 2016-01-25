package globaz.corvus.vb.decisions;

import globaz.globall.db.BSession;
import java.io.Serializable;

/**
 * VO Détail des infos des bénéficiaires
 * 
 * @author scr
 */

public class REBeneficiaireInfoVO implements Comparable<REBeneficiaireInfoVO>, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateNaissanceBeneficiaire;
    private String descriptionBeneficiare = "";
    private String fraction = "";
    private String genrePrestation = "";
    private Long idPrestationDue = null;
    private Long idRenteAccordee = null;
    private Long idTiersBeneficiaire = null;
    private String montant = "";
    private String periodeAu = "";
    private String periodeDu = "";
    private String typeMontant = "";

    public REBeneficiaireInfoVO(BSession session, String typeMontant, Long idRenteAccordee, String periodeDu,
            String periodeAu) throws Exception {

        this.typeMontant = typeMontant;
        this.idRenteAccordee = idRenteAccordee;
        this.periodeDu = periodeDu;
        this.periodeAu = periodeAu;
    }

    @Override
    public int compareTo(REBeneficiaireInfoVO o) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * @return the dateNaissanceBeneficiaire
     */
    public String getDateNaissanceBeneficiaire() {

        return dateNaissanceBeneficiaire;
    }

    public String getDescriptionBeneficiare() {
        return descriptionBeneficiare;
    }

    public String getFraction() {
        return fraction;
    }

    public String getGenrePrestation() {
        return genrePrestation;
    }

    public Long getIdPrestationDue() {
        return idPrestationDue;
    }

    public Long getIdRenteAccordee() {
        return idRenteAccordee;
    }

    public Long getIdTiersBeneficiaire() {
        return idTiersBeneficiaire;
    }

    public String getMontant() {
        return montant;
    }

    public String getPeriodeAu() {
        return periodeAu;
    }

    public String getPeriodeDu() {
        return periodeDu;
    }

    public String getTypeMontant() {
        return typeMontant;
    }

    /**
     * @param dateNaissanceBeneficiaire
     *            the dateNaissanceBeneficiaire to set
     */
    public void setDateNaissanceBeneficiaire(String dateNaissanceBeneficiaire) {
        this.dateNaissanceBeneficiaire = dateNaissanceBeneficiaire;
    }

    public void setDescriptionBeneficiare(String descriptionBeneficiare) {
        this.descriptionBeneficiare = descriptionBeneficiare;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    public void setGenrePrestation(String genrePrestation) {
        this.genrePrestation = genrePrestation;
    }

    public void setIdPrestationDue(Long idPrestationDue) {
        this.idPrestationDue = idPrestationDue;
    }

    public void setIdTiersBeneficiaire(Long idTiersBeneficiaire) {
        this.idTiersBeneficiaire = idTiersBeneficiaire;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
