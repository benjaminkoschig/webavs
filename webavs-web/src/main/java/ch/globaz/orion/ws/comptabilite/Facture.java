package ch.globaz.orion.ws.comptabilite;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Représente une facture au niveau de Orion
 * 
 */
public class Facture {
    private Integer idFacture;
    private String numeroFacture;
    private String referenceBvr;
    private Date date;
    private Date dateEcheance;
    private Integer nbJoursRetard;
    private String description;
    private BigDecimal montant;
    private BigDecimal solde;
    private String remarque;
    private FactureStatut statut;

    public Integer getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(Integer idFacture) {
        this.idFacture = idFacture;
    }

    public String getNumeroFacture() {
        return numeroFacture;
    }

    public void setNumeroFacture(String numeroFacture) {
        this.numeroFacture = numeroFacture;
    }

    public String getReferenceBvr() {
        return referenceBvr;
    }

    public void setReferenceBvr(String referenceBvr) {
        this.referenceBvr = referenceBvr;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDateEcheance() {
        return dateEcheance;
    }

    public void setDateEcheance(Date dateEcheance) {
        this.dateEcheance = dateEcheance;
    }

    public Integer getNbJoursRetard() {
        return nbJoursRetard;
    }

    public void setNbJoursRetard(Integer nbJoursRetard) {
        this.nbJoursRetard = nbJoursRetard;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public BigDecimal getSolde() {
        return solde;
    }

    public void setSolde(BigDecimal solde) {
        this.solde = solde;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public FactureStatut getStatut() {
        return statut;
    }

    public void setStatut(FactureStatut statut) {
        this.statut = statut;
    }
}
