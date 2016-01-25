package ch.globaz.prestation.business.models.echance;

import globaz.jade.persistence.model.JadeSimpleModel;

@SuppressWarnings("serial")
public class SimpleEcheance extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String idExterne;
    private String idTiers;
    private String csDomaine;
    private String csEtat;
    private String csTypeEcheance;
    private String dateDeTraitement;
    private String dateEcheance;
    private String libelle;
    private String remarque;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdExterne() {
        return idExterne;
    }

    public void setIdExterne(String idExterne) {
        this.idExterne = idExterne;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public String getCsDomaine() {
        return csDomaine;
    }

    public void setCsDomaine(String csDomaine) {
        this.csDomaine = csDomaine;
    }

    public String getCsEtat() {
        return csEtat;
    }

    public void setCsEtat(String csEtat) {
        this.csEtat = csEtat;
    }

    public String getCsTypeEcheance() {
        return csTypeEcheance;
    }

    public void setCsTypeEcheance(String csTypeEcheance) {
        this.csTypeEcheance = csTypeEcheance;
    }

    public String getDateDeTraitement() {
        return dateDeTraitement;
    }

    public void setDateDeTraitement(String dateDeTraitement) {
        this.dateDeTraitement = dateDeTraitement;
    }

    public String getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    @Override
    public String toString() {
        return "SimpleEcheance [id=" + id + ", idExterne=" + idExterne + ", idTiers=" + idTiers + ", csDomaine="
                + csDomaine + ", csEtat=" + csEtat + ", csTypeEcheance=" + csTypeEcheance + ", dateDeTraitement="
                + dateDeTraitement + ", dateEcheance=" + dateEcheance + ", libelle=" + libelle + ", remarque="
                + remarque + "]";
    }

}
