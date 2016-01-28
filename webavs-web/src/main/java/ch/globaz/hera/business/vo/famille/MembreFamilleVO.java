package ch.globaz.hera.business.vo.famille;

import java.io.Serializable;

/**
 * 
 * @author BSC
 * 
 */
public class MembreFamilleVO implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csCantonDomicile = null;
    private String csEtatCivil = null;
    private String csNationalite = null;
    private String csSexe = null;
    private String dateDeces = null;
    private String dateNaissance = null;
    private String idMembreFamille = null;
    private String idTiers = null;
    private String nom = null;
    private String nss = null;
    private String prenom = null;
    private String relationAuRequerant = null;

    /**
     * Creation d'un MembreFamilleVO
     * 
     * @param csCantonDomicile
     * @param csEtatCivil
     * @param csNationalite
     * @param csSexe
     * @param dateDeces
     * @param dateNaissance
     * @param idMembreFamille
     * @param idTiers
     * @param nom
     * @param nss
     * @param prenom
     * @param relationAuRequerant
     */
    public MembreFamilleVO(String csCantonDomicile, String csEtatCivil, String csNationalite, String csSexe,
            String dateDeces, String dateNaissance, String idMembreFamille, String idTiers, String nom, String nss,
            String prenom, String relationAuRequerant) {
        super();
        this.csCantonDomicile = csCantonDomicile;
        this.csEtatCivil = csEtatCivil;
        this.csNationalite = csNationalite;
        this.csSexe = csSexe;
        this.dateDeces = dateDeces;
        this.dateNaissance = dateNaissance;
        this.idMembreFamille = idMembreFamille;
        this.idTiers = idTiers;
        this.nom = nom;
        this.nss = nss;
        this.prenom = prenom;
        this.relationAuRequerant = relationAuRequerant;
    }

    /**
     * @return the csCantonDomicile
     */
    public String getCsCantonDomicile() {
        return csCantonDomicile;
    }

    /**
     * @return the csEtatCivil
     */
    public String getCsEtatCivil() {
        return csEtatCivil;
    }

    /**
     * @return the csNationalite
     */
    public String getCsNationalite() {
        return csNationalite;
    }

    /**
     * @return the csSexe
     */
    public String getCsSexe() {
        return csSexe;
    }

    /**
     * @return the dateDeces
     */
    public String getDateDeces() {
        return dateDeces;
    }

    /**
     * @return the dateNaissance
     */
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return the idMembreFamille
     */
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /**
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
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
     * @return the relationAuRequerant
     */
    public String getRelationAuRequerant() {
        return relationAuRequerant;
    }

    /**
     * @param csCantonDomicile
     *            the csCantonDomicile to set
     */
    public void setCsCantonDomicile(String csCantonDomicile) {
        this.csCantonDomicile = csCantonDomicile;
    }

    /**
     * @param csEtatCivil
     *            the csEtatCivil to set
     */
    public void setCsEtatCivil(String csEtatCivil) {
        this.csEtatCivil = csEtatCivil;
    }

    /**
     * @param csNationalite
     *            the csNationalite to set
     */
    public void setCsNationalite(String csNationalite) {
        this.csNationalite = csNationalite;
    }

    /**
     * @param csSexe
     *            the csSexe to set
     */
    public void setCsSexe(String csSexe) {
        this.csSexe = csSexe;
    }

    /**
     * @param dateDeces
     *            the dateDeces to set
     */
    public void setDateDeces(String dateDeces) {
        this.dateDeces = dateDeces;
    }

    /**
     * @param dateNaissance
     *            the dateNaissance to set
     */
    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * @param idMembreFamille
     *            the idMembreFamille to set
     */
    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    /**
     * @param idTiers
     *            the idTiers to set
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param nom
     *            the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @param nss
     *            the nss to set
     */
    public void setNss(String nss) {
        this.nss = nss;
    }

    /**
     * @param prenom
     *            the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @param relationAuRequerant
     *            the relationAuRequerant to set
     */
    public void setRelationAuRequerant(String relationAuRequerant) {
        this.relationAuRequerant = relationAuRequerant;
    }

}
