package ch.globaz.pegasus.business.models.fortuneusuelle;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleAssuranceVie extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateEcheance = null;
    private String idAssuranceVie = null;
    private String idDonneeFinanciereHeader = null;
    private String montantValeurRachat = null;
    private String nomCompagnie = null;
    private String numeroPolice = null;

    public String getDateEcheance() {
        return dateEcheance;
    }

    @Override
    public String getId() {
        return idAssuranceVie;
    }

    public String getIdAssuranceVie() {
        return idAssuranceVie;
    }

    public String getIdDonneeFinanciereHeader() {
        return idDonneeFinanciereHeader;
    }

    public String getMontantValeurRachat() {
        return montantValeurRachat;
    }

    public String getNomCompagnie() {
        return nomCompagnie;
    }

    public String getNumeroPolice() {
        return numeroPolice;
    }

    public void setDateEcheance(String dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    @Override
    public void setId(String id) {
        idAssuranceVie = id;
    }

    public void setIdAssuranceVie(String idAssuranceVie) {
        this.idAssuranceVie = idAssuranceVie;
    }

    public void setIdDonneeFinanciereHeader(String idDonneeFinanciereHeader) {
        this.idDonneeFinanciereHeader = idDonneeFinanciereHeader;
    }

    public void setMontantValeurRachat(String montantValeurRachat) {
        this.montantValeurRachat = montantValeurRachat;
    }

    public void setNomCompagnie(String nomCompagnie) {
        this.nomCompagnie = nomCompagnie;
    }

    public void setNumeroPolice(String numeroPolice) {
        this.numeroPolice = numeroPolice;
    }

}
