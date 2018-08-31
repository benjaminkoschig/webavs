package ch.globaz.vulpecula.web.gson;

import java.io.Serializable;

public class TiersInfoGSON implements Serializable {
    private static final long serialVersionUID = -4555578370087469482L;

    private String idTiersExistant;
    private String idPortail;
    private String nss;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String sexe;
    private String etatCivil;
    private String nationalite;
    private String rue;
    private String rueNumero;
    private String npa;
    private String localite;
    private String iban;
    private String telephone;
    private AdresseInfoGSON adresseInfo;
    private TravailleurInfoGSON travailleurInfo;
    private PosteTravailInfoGSON posteTravailInfo;
    private BanqueInfoGSON adresseBanqueInfo;

    public String getIdTiersExistant() {
        return idTiersExistant;
    }

    public void setIdTiersExistant(String idTiersExistant) {
        this.idTiersExistant = idTiersExistant;
    }

    /**
     * @return the nss
     */
    public String getNss() {
        return nss;
    }

    /**
     * @param nss the nss to set
     */
    public void setNss(String nss) {
        this.nss = nss;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return the dateNaissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @param dateNaissance the dateNaissance to set
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * @return the sexe
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * @param sexe the sexe to set
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    /**
     * @return the etatCivil
     */
    public String getEtatCivil() {
        return etatCivil;
    }

    /**
     * @param etatCivil the etatCivil to set
     */
    public void setEtatCivil(String etatCivil) {
        this.etatCivil = etatCivil;
    }

    /**
     * @return the nationalite
     */
    public String getNationalite() {
        return nationalite;
    }

    /**
     * @param nationalite the nationalite to set
     */
    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    /**
     * @return the rue
     */
    public String getRue() {
        return rue;
    }

    /**
     * @param rue the rue to set
     */
    public void setRue(String rue) {
        this.rue = rue;
    }

    /**
     * @return the rueNumero
     */
    public String getRueNumero() {
        return rueNumero;
    }

    /**
     * @param rueNumero the rueNumero to set
     */
    public void setRueNumero(String rueNumero) {
        this.rueNumero = rueNumero;
    }

    /**
     * @return the npa
     */
    public String getNpa() {
        return npa;
    }

    /**
     * @param npa the npa to set
     */
    public void setNpa(String npa) {
        this.npa = npa;
    }

    /**
     * @return the localite
     */
    public String getLocalite() {
        return localite;
    }

    /**
     * @param localite the localite to set
     */
    public void setLocalite(String localite) {
        this.localite = localite;
    }

    /**
     * @return the iban
     */
    public String getIban() {
        return iban;
    }

    /**
     * @param iban the iban to set
     */
    public void setIban(String iban) {
        this.iban = iban;
    }

    public AdresseInfoGSON getAdresseInfo() {
        return adresseInfo;
    }

    public void setAdresseInfo(AdresseInfoGSON adresseInfo) {
        this.adresseInfo = adresseInfo;
    }

    public TravailleurInfoGSON getTravailleurInfo() {
        return travailleurInfo;
    }

    public void setTravailleurInfo(TravailleurInfoGSON travailleurInfo) {
        this.travailleurInfo = travailleurInfo;
    }

    public PosteTravailInfoGSON getPosteTravailInfo() {
        return posteTravailInfo;
    }

    public void setPosteTravailInfo(PosteTravailInfoGSON posteTravailInfo) {
        this.posteTravailInfo = posteTravailInfo;
    }

    public BanqueInfoGSON getAdresseBanqueInfo() {
        return adresseBanqueInfo;
    }

    public void setAdresseBanqueInfo(BanqueInfoGSON adresseBanqueInfo) {
        this.adresseBanqueInfo = adresseBanqueInfo;
    }

    public String getIdPortail() {
        return idPortail;
    }

    public void setIdPortail(String idPortail) {
        this.idPortail = idPortail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}
