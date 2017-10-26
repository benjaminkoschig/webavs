package ch.globaz.pegasus.rpc.domaine.annonce;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;

public class AnnonceCalculationElements {

    protected Montant otherWealth;
    protected Montant divestedWealth;
    protected Montant otherDebts;
    protected Montant wealthDeductible;
    protected Montant wealthConsidered;
    protected Montant wealthIncome;
    protected Montant usufructIncome;
    protected Montant wealthIncomeConsidered;
    protected Montant incomeConsideredTotal;
    protected BigDecimal wealthIncomeRate;
    protected Montant vitalNeeds;
    protected int children;
    protected AnnonceRealProperty realProperty;
    protected AnnonceHousingOwner housingOwner;
    protected AnnonceRents rents;

    public AnnonceCalculationElements(RpcDecisionAnnonceComplete annonce) {

        otherWealth = annonce.getRpcCalcul().getAutreFortunes();
        divestedWealth = annonce.getRpcCalcul().getFortuneDessaisie();
        otherDebts = annonce.getRpcCalcul().getAutresDettes();
        wealthDeductible = annonce.getRpcCalcul().getFranchiseSurFortune();
        wealthConsidered = annonce.getRpcCalcul().getFortuneAPrendreEnCompte();
        wealthIncome = annonce.getRpcCalcul().getRevenusDeLaFortune();
        usufructIncome = annonce.getPersonsElementsCalcul().sumUsufructIncome();
        wealthIncomeConsidered = annonce.getRpcCalcul().getRevenusDeLaFortunePrisEnCompte();
        incomeConsideredTotal = annonce.getRpcCalcul().getRevenusTotalAPrendreEnCompte();
        wealthIncomeRate = BigDecimal.valueOf(annonce.getRpcCalcul().getPartDesRevenusdeLaFortunePrisEnCompte());
        vitalNeeds = annonce.getRpcCalcul().getBesoinsVitaux();
        children = annonce.getMembresFamilleWithDonneesFinanciere().getNombreEnfants();
        if (annonce.hasImmobilier() || annonce.hasInteretsHypotecaire()) {
            realProperty = new AnnonceRealProperty(annonce);
        }
        if (annonce.isProrietaire()) {
            housingOwner = new AnnonceHousingOwner(annonce);
        }
        if (annonce.hasLoyers()) {
            rents = new AnnonceRents(annonce);
        }

    }

    public Montant getOtherWealth() {
        return otherWealth;
    }

    public Montant getDivestedWealth() {
        return divestedWealth;
    }

    public Montant getOtherDebts() {
        return otherDebts;
    }

    public Montant getWealthDeductible() {
        return wealthDeductible;
    }

    public Montant getWealthConsidered() {
        return wealthConsidered;
    }

    public Montant getWealthIncome() {
        return wealthIncome;
    }

    public Montant getUsufructIncome() {
        return usufructIncome;
    }

    public Montant getWealthIncomeConsidered() {
        return wealthIncomeConsidered;
    }

    public Montant getIncomeConsideredTotal() {
        return incomeConsideredTotal;
    }

    public BigDecimal getWealthIncomeRate() {
        return wealthIncomeRate;
    }

    public Montant getVitalNeeds() {
        return vitalNeeds;
    }

    public int getChildren() {
        return children;
    }

    public AnnonceRealProperty getRealProperty() {
        return realProperty;
    }

    public AnnonceHousingOwner getHousingOwner() {
        return housingOwner;
    }

    public AnnonceRents getRents() {
        return rents;
    }

}
