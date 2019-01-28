package ch.globaz.pegasus.rpc.domaine.annonce;

import java.math.BigDecimal;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;

public class AnnonceCalculationElements {
    
    private static final String XSD_RENTCATEGORY_ANNUAL_GROSS = "ANNUAL_GROSS";
    private static final String XSD_RENTCATEGORY_RENTAL_VALUE = "RENTAL_VALUE";

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
    
    protected boolean property;
    protected Montant realProperty;
    protected Montant mortgageDebts;
    protected Montant propertyIncome;
    protected Montant mortgageInterest;
    protected Montant maintenanceFees;
    protected Montant interestFeesEligible;
    
    protected boolean housingOwner;
    protected Montant selfInhabitedProperty;
    protected Montant selfInhabitedPropertyDeductible;
    protected Montant rentalValue;
    
    protected boolean rent;
    protected Montant grossRental;
    protected String rentCategory;
    protected Montant rentGrossTotal;
    protected Montant rentGrossTotalPart;
    protected Montant maxRent;

    public AnnonceCalculationElements(RpcDecisionAnnonceComplete annonce) {

        otherWealth = annonce.getRpcCalcul().getAutreFortunes();
        divestedWealth = annonce.getRpcCalcul().getFortuneDessaisie();
        otherDebts = annonce.getRpcCalcul().getAutresDettes();
        wealthDeductible = annonce.getRpcCalcul().getFranchiseSurFortune();
        wealthConsidered = annonce.getRpcCalcul().getFortuneAPrendreEnCompte();
        wealthIncome = annonce.getRpcCalcul().getRevenusDeLaFortune();
        wealthIncomeConsidered = annonce.getRpcCalcul().getRevenusDeLaFortunePrisEnCompte();
        incomeConsideredTotal = annonce.getRpcCalcul().getRevenusTotalAPrendreEnCompte();
        wealthIncomeRate = BigDecimal.valueOf(annonce.getRpcCalcul().getPartDesRevenusdeLaFortunePrisEnCompte());
        vitalNeeds = annonce.getRpcCalcul().getBesoinsVitaux();
        children = annonce.getMembresFamilleWithDonneesFinanciere().getNombreEnfants();
        
        realProperty = setZeroIfNull(annonce.getRpcCalcul().getFortuneImmobiliere());
        mortgageDebts = setZeroIfNull(annonce.getRpcCalcul().getDettesHypothequaires());
        mortgageInterest = setZeroIfNull(annonce.getRpcCalcul().getInteretsHypothequaires());
        maintenanceFees = setZeroIfNull(annonce.getRpcCalcul().getFraisEntretien());
        interestFeesEligible = setZeroIfNull(annonce.getRpcCalcul().getInteretsHypothequairesFraisMaintenance());
        propertyIncome = setZeroIfNull(annonce.getRpcCalcul().getRevenusFortuneImmobiliere());
         
        selfInhabitedProperty = setZeroIfNull(annonce.getRpcCalcul().getValeurImmeubleHabitation());
        selfInhabitedPropertyDeductible = setZeroIfNull(annonce.getRpcCalcul().getFranchiseImmeubleHabitation());
        usufructIncome = setZeroIfNull(annonce.getRpcCalcul().getUsufruit());
        rentalValue = setZeroIfNull(annonce.getRpcCalcul().getDepensesLoyerValeurLocativeAppHabite());
        
        grossRental = setZeroIfNull(annonce.getRpcCalcul().getLoyerBrutEnCompte());
        rentCategory = annonce.getMembresFamilleWithDonneesFinanciere().isLoyerValeurLocative() ? XSD_RENTCATEGORY_RENTAL_VALUE
                : XSD_RENTCATEGORY_ANNUAL_GROSS;
        rentGrossTotal = setZeroIfNull(annonce.resolveLoyerTotalBrut());
        rentGrossTotalPart = setZeroIfNull(annonce.getRpcCalcul().getPartLoyerTotatBrut());
        maxRent = setZeroIfNull(annonce.getRpcCalcul().getLoyerMaximum());
        
        
        if (!realProperty.isZero() || !mortgageDebts.isZero() || !mortgageInterest.isZero()
                || !maintenanceFees.isZero() || !interestFeesEligible.isZero() || !propertyIncome.isZero()) {
            property = true;
        } else {
            property = false;
        }
        if (!selfInhabitedProperty.isZero() || !rentGrossTotal.isZero() || !rentalValue.isZero()) {
            housingOwner = true;
        } else {
            housingOwner = false;
        }
        
        if (!grossRental.isZero() || !selfInhabitedPropertyDeductible.isZero() || !rentGrossTotalPart.isZero() || !maxRent.isZero()) {
            rent = true;
        } else {
            rent = false;
        }
        
    }
    
    public Montant setZeroIfNull(Montant value) {
        return value == null ? Montant.ZERO : value; 
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
   
    public Montant getSelfInhabitedProperty() {
        return selfInhabitedProperty;
    }

    public Montant getSelfInhabitedPropertyDeductible() {
        return selfInhabitedPropertyDeductible;
    }

    public Montant getRentalValue() {
        return rentalValue;
    }
    
    public Montant getRealProperty() {
        return realProperty;
    }

    public Montant getMortgageDebts() {
        return mortgageDebts;
    }

    public Montant getPropertyIncome() {
        return propertyIncome;
    }

    public Montant getMortgageInterest() {
        return mortgageInterest;
    }

    public Montant getMaintenanceFees() {
        return maintenanceFees;
    }

    public Montant getInterestFeesEligible() {
        return interestFeesEligible;
    }

    public Montant getGrossRental() {
        return grossRental;
    }

    public String getRentCategory() {
        return rentCategory;
    }

    public Montant getRentGrossTotal() {
        return rentGrossTotal;
    }

    public Montant getRentGrossTotalPart() {
        return rentGrossTotalPart;
    }

    public Montant getMaxRent() {
        return maxRent;
    }
    
    public boolean isProperty() {
        return property;
    }

    public boolean isHousingOwner() {
        return housingOwner;
    }
    
    public boolean isRent() {
        return rent;
    }
    
}
