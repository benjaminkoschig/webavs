package ch.globaz.auriga.business.models;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleEnfantDecisionCAP extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String dateNaissance = null;
    private String dateRadiation = null;
    private String idDecision = null;
    private String idEnfantDecision = null;
    private String idTiers = null;
    private String montant = null;

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateRadiation() {
        return dateRadiation;
    }

    @Override
    public String getId() {
        return idEnfantDecision;
    }

    public String getIdDecision() {
        return idDecision;
    }

    public String getIdEnfantDecision() {
        return idEnfantDecision;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getMontant() {
        return montant;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public void setDateRadiation(String dateRadiation) {
        this.dateRadiation = dateRadiation;
    }

    @Override
    public void setId(String idEnfantDecision) {
        this.idEnfantDecision = idEnfantDecision;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    public void setIdEnfantDecision(String idEnfantDecision) {
        this.idEnfantDecision = idEnfantDecision;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}