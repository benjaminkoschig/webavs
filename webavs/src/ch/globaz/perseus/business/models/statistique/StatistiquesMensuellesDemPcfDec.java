package ch.globaz.perseus.business.models.statistique;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.models.demande.SimpleDemande;
import ch.globaz.perseus.business.statsmensuelles.StatistiquesMensuellesComptageMontantInterface;

public class StatistiquesMensuellesDemPcfDec extends JadeComplexModel implements
        StatistiquesMensuellesComptageMontantInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private SimpleDemande demande = null;

    private String csEtatPCFAccordee = null;
    private String montant = null;

    @Override
    public String toString() {
        return "StatistiquesMensuellesDemPcfDec [dateDemandeSaisie = " + demande.getDateDemandeSaisie()
                + ", timeDemandeSaisie = " + demande.getTimeDemandeSaisie() + ", dateValidation = " + dateValidation
                + ", numeroDecision = " + numeroDecision + "]";
    }

    // Décision
    private String dateValidation = null;
    private String csEtatDecision = null;
    private String csTypeDecision = null;
    private String numeroDecision = null;

    public StatistiquesMensuellesDemPcfDec() {
        super();
        demande = new SimpleDemande();
    }

    @Override
    public String getId() {
        return demande.getId();
    }

    @Override
    public SimpleDemande getDemande() {
        return demande;
    }

    @Override
    public void setDemande(SimpleDemande demande) {
        this.demande = demande;
    }

    public String getCsEtatPCFAccordee() {
        return csEtatPCFAccordee;
    }

    public void setCsEtatPCFAccordee(String csEtatPCFAccordee) {
        this.csEtatPCFAccordee = csEtatPCFAccordee;
    }

    @Override
    public String getMontant() {
        return montant;
    }

    @Override
    public void setMontant(String montant) {
        this.montant = montant;
    }

    public String getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(String dateValidation) {
        this.dateValidation = dateValidation;
    }

    public String getCsEtatDecision() {
        return csEtatDecision;
    }

    public void setCsEtatDecision(String csEtatDecision) {
        this.csEtatDecision = csEtatDecision;
    }

    public String getCsTypeDecision() {
        return csTypeDecision;
    }

    public void setCsTypeDecision(String csTypeDecision) {
        this.csTypeDecision = csTypeDecision;
    }

    @Override
    public String getSpy() {
        return demande.getSpy();
    }

    @Override
    public void setId(String id) {
        demande.setId(id);
    }

    @Override
    public void setSpy(String spy) {
        demande.setSpy(spy);
    }

    public String getNumeroDecision() {
        return numeroDecision;
    }

    public void setNumeroDecision(String numeroDecision) {
        this.numeroDecision = numeroDecision;
    }
}
