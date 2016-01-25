/*
 * Créé le 17 mai 05
 * 
 * Description :
 */
package globaz.apg.module.calcul;

import globaz.framework.util.FWCurrency;
import java.math.BigDecimal;

/**
 * Descpription
 * 
 * @author scr
 * 
 */
public class APBaseCalculSalaireJournalier {

    private BigDecimal nbrHeuresSemaine = null;
    private FWCurrency salaire4Semaines = null;
    private FWCurrency salaireAnnuel = null;
    private FWCurrency salaireHebdomadaire = null;
    private FWCurrency salaireHoraire = null;
    private FWCurrency salaireJournalier = null;

    private FWCurrency salaireMensuel = null;

    public BigDecimal getNbrHeuresSemaine() {
        return nbrHeuresSemaine;
    }

    public FWCurrency getSalaire4Semaines() {
        return salaire4Semaines;
    }

    public FWCurrency getSalaireAnnuel() {
        return salaireAnnuel;
    }

    public FWCurrency getSalaireHebdomadaire() {
        return salaireHebdomadaire;
    }

    public FWCurrency getSalaireHoraire() {
        return salaireHoraire;
    }

    public FWCurrency getSalaireJournalier() {
        return salaireJournalier;
    }

    public FWCurrency getSalaireMensuel() {
        return salaireMensuel;
    }

    public void setNbrHeuresSemaine(BigDecimal nbrHeuresSemaine) {
        this.nbrHeuresSemaine = nbrHeuresSemaine;
    }

    public void setSalaire4Semaines(FWCurrency currency) {
        salaire4Semaines = currency;
    }

    public void setSalaireAnnuel(FWCurrency currency) {
        salaireAnnuel = currency;
    }

    public void setSalaireHebdomadaire(FWCurrency currency) {
        salaireHebdomadaire = currency;
    }

    public void setSalaireHoraire(FWCurrency currency) {
        salaireHoraire = currency;
    }

    public void setSalaireJournalier(FWCurrency currency) {
        salaireJournalier = currency;
    }

    public void setSalaireMensuel(FWCurrency currency) {
        salaireMensuel = currency;
    }
}
