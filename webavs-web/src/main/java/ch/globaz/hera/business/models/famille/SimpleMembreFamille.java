package ch.globaz.hera.business.models.famille;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleMembreFamille extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csCanton = null;
    private String csNationalite = null;
    private String csSexe = null;
    private String dateDeces = null;
    private String dateNaissance = null;
    private String idMembreFamille = null;
    private String idTiers = null;
    private String nom = null;
    private String nomForSearch = null;
    private String prenom = null;
    private String prenomForSearch = null;

    /**
     * @return the csCanton
     */
    public String getCsCanton() {
        return csCanton;
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

    @Override
    public String getId() {
        return idMembreFamille;
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
     * @return the nomForSearch
     */
    public String getNomForSearch() {
        return nomForSearch;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @return the prenomForSearch
     */
    public String getPrenomForSearch() {
        return prenomForSearch;
    }

    /**
     * @param csCanton
     *            the csCanton to set
     */
    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
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

    @Override
    public void setId(String id) {
        idMembreFamille = id;
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
     * @param nomForSearch
     *            the nomForSearch to set
     */
    public void setNomForSearch(String nomForSearch) {
        this.nomForSearch = nomForSearch;
    }

    /**
     * @param prenom
     *            the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @param prenomForSearch
     *            the prenomForSearch to set
     */
    public void setPrenomForSearch(String prenomForSearch) {
        this.prenomForSearch = prenomForSearch;
    }

}
