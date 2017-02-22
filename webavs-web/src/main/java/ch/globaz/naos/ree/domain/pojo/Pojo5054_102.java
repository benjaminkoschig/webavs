package ch.globaz.naos.ree.domain.pojo;

public class Pojo5054_102 {

    /**
     * "compensationOffice", --> numéro caisse et agence,
     * "validityValue", --> basé sur le montant
     * "assuredPersonType", -->
     */

    // //////////////////////////////////////////////////////////

    // private String genreEcriture;
    // private String genreCodeSpeciale;
    // private String codeExtourne;
    // private String anneeEcriture;
    // private String dateAnnonceCentrale;
    // private String idParticularite;
    // private String dateCreationSpy;

    /**
     * accountNumberArG
     */
    private String numeroAffilie;
    /**
     * vn
     */
    private String nss;
    /**
     * dateOfBirth
     */
    private String dateNaissance;
    /**
     * sex
     */
    private String sexe;
    /**
     * nationality
     */
    private String pays;
    /**
     * start
     */
    private String dateDebutPeriode;
    /**
     * end
     */
    private String dateFinPeriode;
    /**
     * salary
     */
    private String montant;

    /**
     * kindOfDecision
     */
    private String genreDecision;

    /**
     * assuredPersonType
     */
    private String genreAffilie;

    /**
     * activity
     */
    private String activity;
    /**
     * decisionDate
     */
    private String dateDecision;

    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    public String getNss() {
        return nss;
    }

    public void setNss(String nss) {
        this.nss = nss;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    public String getGenreDecision() {
        return genreDecision;
    }

    public void setGenreDecision(String genreDecision) {
        this.genreDecision = genreDecision;
    }

    /**
     * @return the dateDecision
     */
    public final String getDateDecision() {
        return dateDecision;
    }

    /**
     * @param dateDecision the dateDecision to set
     */
    public final void setDateDecision(String dateDecision) {
        this.dateDecision = dateDecision;
    }

    /**
     * @return the activity
     */
    public final String getActivity() {
        return activity;
    }

    /**
     * @param activity the activity to set
     */
    public final void setActivity(String activity) {
        this.activity = activity;
    }

    /**
     * @return the genreAffilie
     */
    public final String getGenreAffilie() {
        return genreAffilie;
    }

    /**
     * @param genreAffilie the genreAffilie to set
     */
    public final void setGenreAffilie(String genreAffilie) {
        this.genreAffilie = genreAffilie;
    }

    //
    // public String getGenreEcriture() {
    // return genreEcriture;
    // }
    //
    // public void setGenreEcriture(String genreEcriture) {
    // this.genreEcriture = genreEcriture;
    // }
    //
    // public String getGenreCodeSpeciale() {
    // return genreCodeSpeciale;
    // }
    //
    // public void setGenreCodeSpeciale(String genreCodeSpeciale) {
    // this.genreCodeSpeciale = genreCodeSpeciale;
    // }
    //
    // public String getCodeExtourne() {
    // return codeExtourne;
    // }
    //
    // public void setCodeExtourne(String codeExtourne) {
    // this.codeExtourne = codeExtourne;
    // }
    //
    // public String getAnneeEcriture() {
    // return anneeEcriture;
    // }
    //
    // public void setAnneeEcriture(String anneeEcriture) {
    // this.anneeEcriture = anneeEcriture;
    // }
    //
    //
    //
    // public String getIdParticularite() {
    // return idParticularite;
    // }
    //
    // public void setIdParticularite(String idParticularite) {
    // this.idParticularite = idParticularite;
    // }
    //
    //
    // public String getDateAnnonceCentrale() {
    // return dateAnnonceCentrale;
    // }
    //
    // public void setDateAnnonceCentrale(String dateAnnonceCentrale) {
    // this.dateAnnonceCentrale = dateAnnonceCentrale;
    // }
    //
    //
    // /**
    // * Remplacé par date de décision
    // *
    // * @param dateCreationSpy
    // */
    // @Deprecated
    // public String getDateCreationSpy() {
    // return dateCreationSpy;
    // }
    //
    // /**
    // * Remplacé par date de décision
    // *
    // * @param dateCreationSpy
    // */
    // @Deprecated
    // public void setDateCreationSpy(String dateCreationSpy) {
    // this.dateCreationSpy = dateCreationSpy;
    // }
}
