package ch.globaz.common.business.models;

public class InfosPersonResponseType {

    private String dateNaissance;
    private String nationalite;
    private String nom;
    private String nss;
    private String prenom;
    private String sexe;
    private String codeErreur;
    private String messageErreur;

    public InfosPersonResponseType() {
    }

    /**
     * @return the dateNaissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return the nationalite
     */
    public String getNationalite() {
        return nationalite;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return the nss
     */
    public String getNss() {
        return nss;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @return the sexe
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * @param dateNaissance
     *                          the dateNaissance to set
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * @param nationalite
     *                        the nationalite to set
     */
    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    /**
     * @param nom
     *                the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @param nss
     *                the nss to set
     */
    public void setNss(String nss) {
        this.nss = nss;
    }

    /**
     * @param prenom
     *                   the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @param sexe
     *                 the sexe to set
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getCodeErreur() {
        return codeErreur;
    }

    public void setCodeErreur(String codeErreur) {
        this.codeErreur = codeErreur;
    }

    public String getMessageErreur() {
        return messageErreur;
    }

    public void setMessageErreur(String messageErreur) {
        this.messageErreur = messageErreur;
    }

}
