package ch.globaz.corvus.business.models.ordresversements;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleCompensationInterDecision extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idCompensationInterDecision;
    private String idOrdreVersementCompense;
    private String idOrdreVersementPonctionne;
    private String idTiersDecisionPonctionnee;
    private String montant;

    @Override
    public String getId() {
        return idCompensationInterDecision;
    }

    public String getIdCompensationInterDecision() {
        return idCompensationInterDecision;
    }

    public String getIdOrdreVersementCompense() {
        return idOrdreVersementCompense;
    }

    public String getIdOrdreVersementPonctionne() {
        return idOrdreVersementPonctionne;
    }

    public String getIdTiersDecisionPonctionnee() {
        return idTiersDecisionPonctionnee;
    }

    public String getMontant() {
        return montant;
    }

    @Override
    public void setId(String id) {
        idCompensationInterDecision = id;
    }

    public void setIdCompensationInterDecision(String idCompensationInterDecision) {
        this.idCompensationInterDecision = idCompensationInterDecision;
    }

    public void setIdOrdreVersementCompense(String idOrdreVersementCompense) {
        this.idOrdreVersementCompense = idOrdreVersementCompense;
    }

    public void setIdOrdreVersementPonctionne(String idOrdreVersementPonctionne) {
        this.idOrdreVersementPonctionne = idOrdreVersementPonctionne;
    }

    public void setIdTiersDecisionPonctionnee(String idTiersDecisionPonctionnee) {
        this.idTiersDecisionPonctionnee = idTiersDecisionPonctionnee;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
