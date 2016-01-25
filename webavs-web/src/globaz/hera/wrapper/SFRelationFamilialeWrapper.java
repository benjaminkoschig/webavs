/*
 * Créé le 14 août 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.hera.wrapper;

import globaz.hera.api.ISFRelationFamiliale;

/**
 * @author scr
 * 
 *         Pour changer le modèle de ce commentaire de type généré, allez à :
 *         Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code et commentaires
 */
public class SFRelationFamilialeWrapper implements ISFRelationFamiliale {

    private String dateDebut = "";
    private String dateDebutRelation = "";
    private String dateFin = "";
    private String idMembreFamilleFemme = "";
    private String idMembreFamilleHomme = "";
    private String noAvsFemme = "";
    private String noAvsHomme = "";
    private String nomFemme = "";
    private String nomHomme = "";
    private String prenomFemme = "";
    private String prenomHomme = "";
    private String typeLien = "";
    private String typeRelation = "";

    /**
     * @return si le type de lien est de type veuf, la date de début correspondra à la date de décès d'un des conjoints
     *         + 1 jour
     */
    @Override
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateDebutRelation
     */
    @Override
    public String getDateDebutRelation() {
        return dateDebutRelation;
    }

    /**
     * @return
     */
    @Override
    public String getDateFin() {
        return dateFin;
    }

    /**
     * @return
     */
    @Override
    public String getIdMembreFamilleFemme() {
        return idMembreFamilleFemme;
    }

    /**
     * @return
     */
    @Override
    public String getIdMembreFamilleHomme() {
        return idMembreFamilleHomme;
    }

    /**
     * @return
     */
    @Override
    public String getNoAvsFemme() {
        return noAvsFemme;
    }

    /**
     * @return
     */
    @Override
    public String getNoAvsHomme() {
        return noAvsHomme;
    }

    /**
     * @return
     */
    @Override
    public String getNomFemme() {
        return nomFemme;
    }

    /**
     * @return
     */
    @Override
    public String getNomHomme() {
        return nomHomme;
    }

    /**
     * @return
     */
    @Override
    public String getPrenomFemme() {
        return prenomFemme;
    }

    /**
     * @return
     */
    @Override
    public String getPrenomHomme() {
        return prenomHomme;
    }

    /**
     * @return
     */
    @Override
    public String getTypeLien() {
        return typeLien;
    }

    @Override
    public String getTypeRelation() {
        return typeRelation;
    }

    /**
     * @param string
     */
    public void setDateDebut(String string) {
        dateDebut = string;
    }

    /**
     * @param dateDebutRelation
     *            the dateDebutRelation to set
     */
    public void setDateDebutRelation(String dateDebutRelation) {
        this.dateDebutRelation = dateDebutRelation;
    }

    /**
     * @param string
     */
    public void setDateFin(String string) {
        dateFin = string;
    }

    /**
     * @param string
     */
    public void setIdMembreFamilleFemme(String string) {
        idMembreFamilleFemme = string;
    }

    /**
     * @param string
     */
    public void setIdMembreFamilleHomme(String string) {
        idMembreFamilleHomme = string;
    }

    /**
     * @param string
     */
    public void setNoAvsFemme(String string) {
        noAvsFemme = string;
    }

    /**
     * @param string
     */
    public void setNoAvsHomme(String string) {
        noAvsHomme = string;
    }

    /**
     * @param string
     */
    public void setNomFemme(String string) {
        nomFemme = string;
    }

    /**
     * @param string
     */
    public void setNomHomme(String string) {
        nomHomme = string;
    }

    /**
     * @param string
     */
    public void setPrenomFemme(String string) {
        prenomFemme = string;
    }

    /**
     * @param string
     */
    public void setPrenomHomme(String string) {
        prenomHomme = string;
    }

    /**
     * @param string
     */
    public void setTypeLien(String string) {
        typeLien = string;
    }

    public void setTypeRelation(String typeRelation) {
        this.typeRelation = typeRelation;
    }

}
