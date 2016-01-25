/*
 * Créé le 14 août 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.wrapper;

import globaz.hera.api.ISFMembreFamilleRequerant;

/**
 * @author scr
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class SFMembreFamilleRequerantWrapper implements ISFMembreFamilleRequerant {

    private String csCantonDomicile = "";
    private String csEtatCivil = "";
    private String csNationalite = "";
    private String csSexe = "";
    private String dateDeces = "";
    private String dateNaissance = "";
    private String idMembreFamille = "";
    private String idTiers = "";
    private String nom = "";
    private String pays;
    private String nss = "";
    private String prenom = "";

    private String relationAuRequerant = "";

    /**
     * @return
     */
    @Override
    public String getCsCantonDomicile() {
        return csCantonDomicile;
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
     * @return
     */
    @Override
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /**
     * @return
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

    /**
     * @return
     */
    @Override
    public String getRelationAuRequerant() {
        return relationAuRequerant;
    }

    /**
     * @param string
     */
    public void setCsCantonDomicile(String string) {
        csCantonDomicile = string;
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
     * @param string
     */
    public void setIdMembreFamille(String string) {
        idMembreFamille = string;
    }

    /**
     * @param string
     */
    public void setIdTiers(String string) {
        idTiers = string;
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

    /**
     * @param string
     */
    public void setRelationAuRequerant(String string) {
        relationAuRequerant = string;
    }

    @Override
    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

}
