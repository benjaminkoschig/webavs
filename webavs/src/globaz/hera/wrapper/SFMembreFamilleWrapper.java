/*
 * Créé le 14 août 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.wrapper;

import globaz.hera.api.ISFMembreFamille;

/**
 * @author scr
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class SFMembreFamilleWrapper implements ISFMembreFamille {

    private String csCantonDomicile = "";
    private String csDomaine = "";
    private String csEtatCivil = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";
    private String dateNaissance = "";
    private String idTiers = "";
    private String nom = "";
    private String nss = "";
    private String prenom = "";
    private String relationAuLiant = "";
    private String pays;

    /**
     * @return
     */
    @Override
    public String getCsCantonDomicile() {
        return csCantonDomicile;
    }

    @Override
    public String getCsDomaine() {
        return csDomaine;
    }

    /**
     * @return
     */
    @Override
    public String getCsEtatCivil() {
        return csEtatCivil;
    }

    /**
     * @return
     */
    @Override
    public String getCsNationalite() {
        return csNationalite;
    }

    /**
     * @return
     */
    @Override
    public String getCsSexe() {
        return csSexe;
    }

    /**
     * @return
     */
    @Override
    public String getDateDeces() {
        return dateDeces;
    }

    /**
     * @return
     */
    @Override
    public String getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @return the idTiers
     */
    @Override
    public String getIdTiers() {
        return idTiers;
    }

    /**
     * @return
     */
    @Override
    public String getNom() {
        return nom;
    }

    /**
     * @return
     */
    @Override
    public String getNss() {
        return nss;
    }

    /**
     * @return
     */
    @Override
    public String getPrenom() {
        return prenom;
    }

    @Override
    public String getRelationAuLiant() {
        return relationAuLiant;
    }

    /**
     * @param string
     */
    public void setCsCantonDomicile(String string) {
        csCantonDomicile = string;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    /**
     * @param string
     */
    public void setCsEtatCivil(String string) {
        csEtatCivil = string;
    }

    /**
     * @param string
     */
    public void setCsNationalite(String string) {
        csNationalite = string;
    }

    /**
     * @param string
     */
    public void setCsSexe(String string) {
        csSexe = string;
    }

    /**
     * @param string
     */
    public void setDateDeces(String string) {
        dateDeces = string;
    }

    /**
     * @param string
     */
    public void setDateNaissance(String string) {
        dateNaissance = string;
    }

    /**
     * @param idTiers
     *            the idTiers to set
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    /**
     * @param string
     */
    public void setNom(String string) {
        nom = string;
    }

    /**
     * @param string
     */
    public void setNss(String string) {
        nss = string;
    }

    /**
     * @param string
     */
    public void setPrenom(String string) {
        prenom = string;
    }

    public void setRelationAuLiant(String relationAuLiant) {
        this.relationAuLiant = relationAuLiant;
    }

    @Override
    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

}
