package ch.globaz.auriga.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleSortieCAP extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateSortie = null;
    private String etat = null;
    private String idDecision = null;
    private String idPassageFacturation = null;
    private String idSortie = null;
    private String montantExtourne = null;

    public String getDateSortie() {
        return dateSortie;
    }

    public String getEtat() {
        return etat;
    }

    @Override
    public String getId() {
        return idSortie;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public String getIdSortie() {
        return idSortie;
    }

    public String getMontantExtourne() {
        return montantExtourne;
    }

    public void setDateSortie(String dateSortie) {
        this.dateSortie = dateSortie;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    @Override
    public void setId(String idSortie) {
        this.idSortie = idSortie;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdPassageFacturation(String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public void setIdSortie(String idSortie) {
        this.idSortie = idSortie;
    }

    public void setMontantExtourne(String montantExtourne) {
        this.montantExtourne = montantExtourne;
    }
}