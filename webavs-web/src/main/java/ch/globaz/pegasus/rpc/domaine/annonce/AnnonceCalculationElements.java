package ch.globaz.pegasus.rpc.domaine.annonce;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.GregorianCalendar;
import java.util.List;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.constantes.EPCRegionLoyer;
import ch.globaz.pegasus.rpc.businessImpl.converter.ConverterRentRegion;
import ch.globaz.pegasus.rpc.domaine.LivingSituationType;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;
import com.ibm.db2.jcc.a.e;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

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
    protected Montant mortgageDebtsRealProperty;
    protected Montant mortgageDebtsSelfinhabited;
    protected Montant propertyIncome;
    protected Montant mortgageInterest;
    protected Montant maintenanceFees;
    protected Montant interestFeesEligible;

    protected long familySize;

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
    protected String rentRegion;

    protected boolean isDivestedWealth;
    protected String typeDivestedWealth;

    protected boolean isWheelchairSurcharge;

    protected LivingSituationType livingSituationType;

    public AnnonceCalculationElements(RpcDecisionAnnonceComplete annonce) {

        otherWealth = annonce.getRpcCalcul().getAutreFortunes();
        divestedWealth = annonce.getRpcCalcul().getFortuneDessaisie();

        isWheelchairSurcharge = annonce.getRpcCalcul().isWheelchairSurcharge();


        isDivestedWealth = annonce.getRpcCalcul().isDivestedWealth();
        if (isDivestedWealth) {
            typeDivestedWealth = annonce.getRpcCalcul().getTypeDivestedWealth();
        }
        otherDebts = annonce.getRpcCalcul().getAutresDettes();
        wealthDeductible = annonce.getRpcCalcul().getFranchiseSurFortune();
        wealthConsidered = annonce.getRpcCalcul().getFortuneAPrendreEnCompte();
        wealthIncome = annonce.getRpcCalcul().getRevenusDeLaFortune();
        wealthIncomeConsidered = annonce.getRpcCalcul().getRevenusDeLaFortunePrisEnCompte();
        incomeConsideredTotal = annonce.getRpcCalcul().getRevenusTotalAPrendreEnCompte();
        wealthIncomeRate = BigDecimal.valueOf(annonce.getRpcCalcul().getPartDesRevenusdeLaFortunePrisEnCompte());
        vitalNeeds = annonce.getRpcCalcul().getBesoinsVitaux();
        children = annonce.getMembresFamilleWithDonneesFinanciere().getNombreEnfants();

        familySize = annonce.getRpcCalcul().getFamilySize();

        realProperty = setZeroIfNull(annonce.getRpcCalcul().getFortuneImmobiliere());
        mortgageDebts = setZeroIfNull(annonce.getRpcCalcul().getDettesHypothequaires());

        mortgageDebtsSelfinhabited = setZeroIfNull(annonce.getRpcCalcul().getDettesHypothequairesSelfinhabited());
        mortgageDebtsRealProperty = setZeroIfNull(annonce.getRpcCalcul().getDettesHypothequairesRealProperty());

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
        EPCRegionLoyer region = annonce.getRpcCalcul().getLoyerRegion();
        if(region != null) {
            rentRegion = ConverterRentRegion.convert(region);
        }

        List<PersonElementsCalcul> personnesElementsCalcul = annonce.getPersonsElementsCalcul().getPersonsElementsCalcul();
        if (personnesElementsCalcul.stream().anyMatch(personElementsCalcul -> personElementsCalcul.isUsufrutuier())) {
            livingSituationType = LivingSituationType.USUFRUCTUARY;
        } else if (personnesElementsCalcul.stream().anyMatch(personElementsCalcul -> personElementsCalcul.getMembreFamille().getDonneesPersonnelles().getMembreCongregation())) {
            livingSituationType = LivingSituationType.CONGREGATION;
        } else {
            livingSituationType = LivingSituationType.NORMAL;
        }


        if (!realProperty.isZero() || !mortgageDebtsRealProperty.isZero() || !mortgageInterest.isZero()
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

    public Montant getMortgageDebtsRealProperty() {
        return mortgageDebtsRealProperty;
    }

    public Montant getMortgageDebtsSelfinhabited() {
        return mortgageDebtsSelfinhabited;
    }

    public long getFamilySize() {
        return familySize;
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

    public boolean isDivestedWealth() {
        return isDivestedWealth;
    }

    public String getTypeDivestedWealth() {
        return typeDivestedWealth;
    }

    public boolean isWheelchairSurcharge() {
        return isWheelchairSurcharge;
    }

    public LivingSituationType getLivingSituationType() {
        return livingSituationType;
    }

    public String getRentRegion() {
        return rentRegion;
    }

    public void setRentRegion(String rentRegion) {
        this.rentRegion = rentRegion;
    }
}
