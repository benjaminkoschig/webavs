package ch.globaz.perseus.business.models.statistique;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.models.demande.SimpleDemande;
import ch.globaz.perseus.business.statsmensuelles.StatistiquesMensuellesComptageMontantInterface;

public class StatistiquesMensuellesEnfantDemande extends JadeComplexModel implements
        StatistiquesMensuellesComptageMontantInterface {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDemande demande = null;
    private String dateNaissance = null;

    private String montant;

    public StatistiquesMensuellesEnfantDemande() {
        super();
        demande = new SimpleDemande();
    }

    @Override
    public SimpleDemande getDemande() {
        return demande;
    }

    @Override
    public void setDemande(SimpleDemande demande) {
        this.demande = demande;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public String toString() {
        return "Date de naissance = " + dateNaissance;
    }

    @Override
    public String getId() {
        return demande.getId();
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

    @Override
    public void setMontant(String montant) {
        this.montant = montant;

    }

    @Override
    public String getMontant() {
        return montant;
    }

}
