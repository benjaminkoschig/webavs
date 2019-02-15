package ch.globaz.pegasus.rpc.domaine.annonce;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import ch.globaz.common.domaine.Date;
import ch.globaz.common.domaine.Montant;
import ch.globaz.naos.ree.tools.InfoCaisse;
import ch.globaz.pegasus.business.domaine.decision.Decision;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.pca.Pca;
import ch.globaz.pegasus.business.domaine.pca.PcaEtatCalcul;
import ch.globaz.pegasus.rpc.businessImpl.converter.ConverterDecisionCause;
import ch.globaz.pegasus.rpc.businessImpl.converter.ConverterDecisionKind;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;

public class AnnonceDecision {

    protected String pcaDecisionId;
    protected String decisionId;
    protected Date decisionDate;
    protected Date validFrom;
    protected AnnonceDeliveryOffice deliveryOffice;

    protected AnnonceElAmounts elAmounts;
    protected AnnonceCalculationElements calculationElements;
    protected List<AnnoncePerson> persons;
    protected AnnoncePerson personRequerant;
    protected AnnoncePerson personRepresentative;

    protected BigInteger decisionKind;
    protected BigInteger decisionCause;
    protected Date validTo;
    protected String decisionIdPartnerDecision;
    
    protected Boolean coupleSepare;

    protected RpcDecisionAnnonceComplete annonce;

    public AnnonceDecision(RpcDecisionAnnonceComplete annonce) {
        this.annonce = annonce;
        final Decision decision = annonce.getPcaDecision().getDecision();
        final PcaEtatCalcul etatCalculFederal = decision.getType().isRefusSansCalcul() ? PcaEtatCalcul.REFUSE
                : annonce.getRpcCalcul().getEtatCalculFederal();
        final Pca pca = annonce.getPcaDecision().getPca();
        if (pca != null) {
            pcaDecisionId = pca.getId();
        }

        decisionId = decision.getId();
        decisionDate = decision.getDateDecision();
        validFrom = decision.getDateDebut();

        decisionKind = ConverterDecisionKind.convert(decision.getType(), decision.getMotif(), etatCalculFederal);
        // null pour les annonces partielles
        if (annonce.getVersionDroit() != null) {
            decisionCause = ConverterDecisionCause.convert(annonce.getVersionDroit(), decision.getMotif());
        }
        if (annonce.hasDateFin()) {
            validTo = decision.getDateFin();
        }
        if (annonce.isNotRefus()) {
            elAmounts = new AnnonceElAmounts(annonce);
        }
        // null pour les annonces partielles
        if (annonce.getRpcCalcul() != null) {
            calculationElements = new AnnonceCalculationElements(annonce);
            initPersons(annonce);
        }

        if (annonce.hasPartner()) {
            decisionIdPartnerDecision = annonce.getDecisionIdPartner();
        }
        coupleSepare = decision.getType().isRefusSansCalcul() ? false : annonce.getRpcCalcul().isCoupleSepare();

    }

    private void initPersons(RpcDecisionAnnonceComplete annonce) {
        persons = new ArrayList<AnnoncePerson>();

        PersonElementsCalcul requerantData = new PersonElementsCalcul();

        for (PersonElementsCalcul personData : annonce.getPersonsElementsCalcul().getPersonsElementsCalcul()) {

            if (personData.getMembreFamille().getRoleMembreFamille() == RoleMembreFamille.REQUERANT) {
                requerantData = personData;
                break;
            }
        }

        for (PersonElementsCalcul personData : annonce.getPersonsElementsCalcul().getPersonsElementsCalcul()) {
            AnnoncePerson person = new AnnoncePerson(annonce, personData, requerantData);

            if (personData.equals(requerantData)) {
                personRequerant = person;
            }
            
            if(person.getRepresentative()) {
                personRepresentative = person;
            }

            persons.add(person);
        }      
    }
    
    public void setDeliveryOffice(InfoCaisse infoCaisse) {
        deliveryOffice = new AnnonceDeliveryOffice(infoCaisse);
    }

    public String getDecisionId() {
        return decisionId;
    }

    public Date getDecisionDate() {
        return decisionDate;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public AnnonceDeliveryOffice getDeliveryOffice() {
        return deliveryOffice;
    }

    public BigInteger getDecisionKind() {
        return decisionKind;
    }

    public BigInteger getDecisionCause() {
        return decisionCause;
    }

    public Date getValidTo() {
        return validTo;
    }

    public AnnonceElAmounts getElAmounts() {
        return elAmounts;
    }

    public AnnonceCalculationElements getCalculationElements() {
        return calculationElements;
    }

    public List<AnnoncePerson> getPersons() {
        return persons;
    }

    public String getDecisionIdPartnerDecision() {
        return decisionIdPartnerDecision;
    }

    public RpcDecisionAnnonceComplete getAnnonce() {
        return annonce;
    }

    public AnnoncePerson getPersonRequerant() {
        return personRequerant;
    }

    public String getPcaDecisionId() {
        return pcaDecisionId;
    }

    // Accesseurs AnnonceCalculationElements

    public Montant getOtherWealth() {
        return calculationElements.getOtherWealth();
    }

    public Montant getDivestedWealth() {
        return calculationElements.getDivestedWealth();
    }

    public Montant getOtherDebts() {
        return calculationElements.getOtherDebts();
    }

    public Montant getWealthDeductible() {
        return calculationElements.getWealthDeductible();
    }

    public Montant getWealthConsidered() {
        return calculationElements.getWealthConsidered();
    }

    public Montant getWealthIncome() {
        return calculationElements.getWealthIncome();
    }

    public Montant getUsufructIncome() {
        return calculationElements.getUsufructIncome();
    }

    public Montant getWealthIncomeConsidered() {
        return calculationElements.getWealthIncomeConsidered();
    }

    public Montant getIncomeConsideredTotal() {
        return calculationElements.getIncomeConsideredTotal();
    }

    public BigDecimal getWealthIncomeRate() {
        return calculationElements.getWealthIncomeRate();
    }

    public Montant getVitalNeeds() {
        return calculationElements.getVitalNeeds();
    }

    public int getChildren() {
        return calculationElements.getChildren();
    }

    // Accesseurs AnnonceCalculationElements.AnnonceRealProperty

    public Montant getRealProperty() {
        return calculationElements.getRealProperty();
    }

    public Montant getMortgageDebts() {
        return calculationElements.getMortgageDebts();
    }

    public Montant getPropertyIncome() {
        return calculationElements.getPropertyIncome();
    }

    public Montant getMortgageInterest() {
        return calculationElements.getMortgageInterest();
    }

    public Montant getMaintenanceFees() {
        return calculationElements.getMaintenanceFees();
    }

    public Montant getInterestFeesEligible() {
        return calculationElements.getInterestFeesEligible();
    }

    // Accesseurs AnnonceCalculationElements.AnnonceHousingOwner

    public Montant getSelfInhabitedProperty() {
        return calculationElements.getSelfInhabitedProperty();
    }

    public Montant getSelfInhabitedPropertyDeductible() {
        return calculationElements.getSelfInhabitedPropertyDeductible();
    }

    public Montant getRentalValue() {
        return calculationElements.getRentalValue();
    }

    // Accesseurs AnnonceCalculationElements.AnnonceRents

    public Montant getGrossRental() {
        return calculationElements.getGrossRental();
    }

    public String getRentCategory() {
        return calculationElements.getRentCategory();
    }

    public Montant getRentGrossTotal() {
        return calculationElements.getRentGrossTotal();
    }

    public Montant getRentGrossTotalPart() {
        return calculationElements.getRentGrossTotalPart();
    }

    public Montant getMaxRent() {
        return calculationElements.getMaxRent();
    }

    // Accesseurs AnnonceElAmounts

    public Montant getAmountNoHC() {
        return elAmounts != null ? elAmounts.getAmountNoHC() : Montant.ZERO_ANNUEL;
    }

    public Montant getAmountWithHC() {
        return elAmounts != null ? elAmounts.getAmountWithHC() : Montant.ZERO_ANNUEL;
    }

    public int getElLimit() {
        return elAmounts != null ? elAmounts.getElLimit() : 0;
    }

    public Montant getSumavsAipension() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            AnnoncePensionCategory pensionCategory = person.getPersonalCalculationElements().getPensionCategory();

            if (pensionCategory != null && pensionCategory.getPension() != null
                    && pensionCategory.getPension().getAvsAipension() != null) {
                sum = sum.add(
                        person.getPersonalCalculationElements().getPensionCategory().getPension().getAvsAipension());
            }
        }

        return sum.arrondiAUnIntierSupperior();
    }
    
    public Montant getSumavsAipensionNotChild() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            AnnoncePensionCategory pensionCategory = person.getPersonalCalculationElements().getPensionCategory();

            if (!RoleMembreFamille.ENFANT.equals(person.getPersonData().getMembreFamille().getRoleMembreFamille())
                    && pensionCategory != null && pensionCategory.getPension() != null
                    && pensionCategory.getPension().getAvsAipension() != null) {
                sum = sum.add(
                        person.getPersonalCalculationElements().getPensionCategory().getPension().getAvsAipension());
            }
        }

        return sum.arrondiAUnIntierSupperior();
    }

    public Montant getSumRenteIj() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            AnnoncePensionCategory pensionCategory = person.getPersonalCalculationElements().getPensionCategory();

            if (pensionCategory != null && pensionCategory.getPension() != null
                    && pensionCategory.getPension().getDailyAllowance() != null) {
                sum = sum.add(
                        person.getPersonalCalculationElements().getPensionCategory().getPension().getDailyAllowance());
            }
        }

        return sum;
    }
    
    public Montant getSumRenteIjNotChild() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            AnnoncePensionCategory pensionCategory = person.getPersonalCalculationElements().getPensionCategory();

            if (!RoleMembreFamille.ENFANT.equals(person.getPersonData().getMembreFamille().getRoleMembreFamille())
                    && pensionCategory != null && pensionCategory.getPension() != null
                    && pensionCategory.getPension().getDailyAllowance() != null) {
                sum = sum.add(
                        person.getPersonalCalculationElements().getPensionCategory().getPension().getDailyAllowance());
            }
        }

        return sum;
    }

    public Montant getSumTotalRentes() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            if (person.getPersonalCalculationElements().getTotalPension() != null) {
                sum = sum.add(person.getPersonalCalculationElements().getTotalPension());
            }
        }

        return sum.arrondiAUnIntier();
    }
    
    public Montant getSumTotalRentesNotChild() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            if (!RoleMembreFamille.ENFANT.equals(person.getPersonData().getMembreFamille().getRoleMembreFamille())
                    && person.getPersonalCalculationElements().getTotalPension() != null) {
                sum = sum.add(person.getPersonalCalculationElements().getTotalPension());
            }
        }

        return sum.arrondiAUnIntier();
    }

    public Montant getDisabledAllowance() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            if (person.getPersonalCalculationElements() != null
                    && person.getPersonalCalculationElements().getPensionCategory().getPension() != null) {
                sum = sum.add(person.getPersonalCalculationElements().getPensionCategory().getPension().getDisabledAllowance());
            }
        }

        return sum.arrondiAUnIntier();

    }

    public Montant getSumContributionsAssuranceMaladie() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            if (person.getPersonalCalculationElements().getHcLcaAllowance() != null) {
                sum = sum.add(person.getPersonalCalculationElements().getHcLcaAllowance());
            }
        }

        return sum.arrondiAUnIntier();
    }
    
    public Montant getSumResidencePatientContribution() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            AnnonceResidenceCosts residenceCosts = person.getPersonalCalculationElements().getResidenceCosts();

            if (residenceCosts != null && residenceCosts.getResidencePatientContribution() != null) {
                sum = sum.add(residenceCosts.getResidencePatientContribution());
            }
        }

        return sum.arrondiAUnIntier();
    }

    public Montant getSumResidencePatientExpenses() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            AnnonceResidenceCosts residenceCosts = person.getPersonalCalculationElements().getResidenceCosts();

            if (residenceCosts != null && residenceCosts.getResidencePatientExpenses() != null) {
                sum = sum
                        .add(person.getPersonalCalculationElements().getResidenceCosts().getResidencePatientExpenses());
            }
        }

        return sum.arrondiAUnIntier();
    }

    public Montant getSumAutresRevenus() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            if (person.getPersonalCalculationElements().getOtherIncomes() != null) {
                sum = sum.add(person.getPersonalCalculationElements().getOtherIncomes());
            }
        }

        return sum.arrondiAUnIntierSupperior();
    }
    
    public Montant getSumAutresRevenusNotChild() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            if (!RoleMembreFamille.ENFANT.equals(person.getPersonData().getMembreFamille().getRoleMembreFamille())
                    && person.getPersonalCalculationElements().getOtherIncomes() != null) {
                sum = sum.add(person.getPersonalCalculationElements().getOtherIncomes());
            }
        }

        return sum.arrondiAUnIntierSupperior();
    }

    public Montant getSumHomeTaxeHomePrisEnCompte() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            if (person.getResidenceCostsConsidered() != null) {
                sum = sum.add(person.getResidenceCostsConsidered());
            }
        }

        // la calculateur fait un arrondi inf�rieur(Ceci me parait pas juste!!!) on est oblig� de faire le m�me arrondi
        return sum.arrondiAUnIntier();
    }

    public Montant getSumHomeParticipationAuxCoutDesPatients() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            if (person.getResidencePatientContribution() != null) {
                sum = sum.add(person.getResidencePatientContribution());
            }
        }

        return sum.arrondiAUnIntier();
    }

    public Montant getSumPrimeLamal() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            if (person.getHcFlatHelp() != null) {
                sum = sum.add(person.getHcFlatHelp());
            }
        }

        return sum.arrondiAUnIntier();
    }

    public Montant sumAutresDepenses() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            if (person.getOtherExpenses() != null) {
                sum = sum.add(person.getOtherExpenses());
            }
        }

        return sum.arrondiAUnIntier();
    }

    public Montant getSumRevenuBrutHypothetique() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            sum = sum.add(person.getPersonalCalculationElements().getHypotheticalGrossIncome());
        }

        return sum;
    }

    public Montant getSumRevenuBruteActiviteLucrative() {
        Montant sum = Montant.ZERO_ANNUEL;

        for (AnnoncePerson person : persons) {
            sum = sum.add(person.getPersonalCalculationElements().getLucrativeGrossIncome());
        }

        return sum;
    }
    
    public Boolean getCoupleSepare() {
        return coupleSepare;
    }

    public void setCoupleSepare(Boolean coupleSepare) {
        this.coupleSepare = coupleSepare;
    }

    public AnnoncePerson getPersonRepresentative() {
        return personRepresentative;
    }


}
