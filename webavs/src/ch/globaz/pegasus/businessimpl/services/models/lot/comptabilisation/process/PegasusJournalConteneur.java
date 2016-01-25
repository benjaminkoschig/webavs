package ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.process;

import java.util.List;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.osiris.business.data.JournalConteneur;
import ch.globaz.osiris.business.model.JournalSimpleModel;
import ch.globaz.pegasus.businessimpl.services.models.lot.comptabilisation.ecriture.PrestationOperations;

public class PegasusJournalConteneur extends JournalConteneur {
    private String dateEchance;
    private String dateValeur;
    private SimpleLot lot;
    private List<PrestationOperations> operations;

    public String getDateEchance() {
        return dateEchance;
    }

    public String getDateValeur() {
        return dateValeur;
    }

    public SimpleLot getLot() {
        return lot;
    }

    public List<PrestationOperations> getOperations() {
        return operations;
    }

    public void setDateEchance(String dateEchance) {
        this.dateEchance = dateEchance;
    }

    public void setDateValeur(String dateValeur) {
        this.dateValeur = dateValeur;
    }

    public void setJournal(JournalSimpleModel journal) {
        this.AddJournal(journal);
    }

    public void setLot(SimpleLot lot) {
        this.lot = lot;
    }

    public void setOperations(List<PrestationOperations> operations) {
        this.operations = operations;
    }

}
