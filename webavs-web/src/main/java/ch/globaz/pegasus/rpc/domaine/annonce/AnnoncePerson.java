package ch.globaz.pegasus.rpc.domaine.annonce;

import java.util.List;
import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.membreFamille.RoleMembreFamille;
import ch.globaz.pegasus.business.domaine.pca.PcaDecision;
import ch.globaz.pegasus.business.domaine.pca.PcaGenre;
import ch.globaz.pegasus.rpc.businessImpl.converter.ConverterMaritalStatus;
import ch.globaz.pegasus.rpc.businessImpl.converter.ConverterPensionKind;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.PersonsElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcDecisionAnnonceComplete;
import ch.globaz.pegasus.rpc.domaine.RpcVitalNeedsCategory;

public class AnnoncePerson {

    protected PersonsElementsCalcul personsElementsCalcul;
    protected PcaDecision pcaDecision;

    protected AnnonceAddress legalAddress;
    protected AnnonceAddress livingAddress;
    protected AnnoncePersonalCalculationElements personalCalculationElements;

    protected Long vn;
    protected Boolean representative;
    protected Integer pensionKind;
    protected String degreeOfInvalidity;
    protected RpcVitalNeedsCategory vitalNeedsCategory;
    protected String maritalStatus;
    protected PcaGenre housingMode;

    protected PersonElementsCalcul personData;

    public AnnoncePerson(RpcDecisionAnnonceComplete annonce, PersonElementsCalcul personData,
            PersonElementsCalcul requerantData) {
        personsElementsCalcul = annonce.getPersonsElementsCalcul();
        pcaDecision = annonce.getPcaDecision();
        this.personData = personData;
        if (personData.isValidLegalAddress()) {
            legalAddress = new AnnonceAddress(personData, personData.getLegalAddress());
        }
        if (personData.isValidLivingAddress()) {
            livingAddress = new AnnonceAddress(personData, personData.getLivingAddress());
        }
        personalCalculationElements = new AnnoncePersonalCalculationElements(personData, requerantData, pcaDecision,
                annonce.getRpcCalcul());
        vn = personData.getMembreFamille().getPersonne().getNss().formatInLong();
        representative = !RoleMembreFamille.ENFANT.equals(personData.getMembreFamille().getRoleMembreFamille());
        representative = annonce.getRpcCalcul().isCoupleSepare() ? representative : personData.isMandataire();
        pensionKind = ConverterPensionKind.convert(personData.getTypeRenteCS());
        if(ConverterPensionKind.isRentAi(personData.getTypeRenteCS()) && personData.getDegreInvalidite() != null) {
            degreeOfInvalidity = String.format("%.2f",personData.getDegreInvalidite());
        }
        vitalNeedsCategory = annonce.resolveVitalNeedsCategory(personData, annonce.getDemande());
        maritalStatus = ConverterMaritalStatus.convert(personData.getSituationFamiliale());
        housingMode = annonce.resolvePcaGenre(personData);

    }

    public Long getVn() {
        return vn;
    }

    public Boolean getRepresentative() {
        return representative;
    }

    public Integer getPensionKind() {
        return pensionKind;
    }

    public RpcVitalNeedsCategory getVitalNeedsCategory() {
        return vitalNeedsCategory;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public PcaGenre getHousingMode() {
        return housingMode;
    }

    public AnnonceAddress getLegalAddress() {
        return legalAddress;
    }

    public AnnonceAddress getLivingAddress() {
        return livingAddress;
    }

    public AnnoncePersonalCalculationElements getPersonalCalculationElements() {
        return personalCalculationElements;
    }

    public PersonElementsCalcul getPersonData() {
        return personData;
    }

    // Accesseurs AnnoncePersonalCalculationElements

    public Montant getHcLcaAllowance() {
        return personalCalculationElements.getHcLcaAllowance();
    }

    public Montant getLucrativeGrossIncome() {
        return personalCalculationElements.getLucrativeGrossIncome();
    }

    public Montant getHypotheticalGrossIncome() {
        return personalCalculationElements.getHypotheticalGrossIncome();
    }

    public Montant getTotalPension() {
        return personalCalculationElements.getTotalPension();
    }

    public Montant getLppPension() {
        return personalCalculationElements.getLppPension();
    }

    public Montant getForeignPension() {
        return personalCalculationElements.getForeignPension();
    }

    public Montant getOtherIncomes() {
        return personalCalculationElements.getOtherIncomes();
    }

    public Montant getLppWithdrawalAmount() {
        return personalCalculationElements.getLppWithdrawalAmount();
    }

    public String getPatientContributionCategory() {
        return personalCalculationElements.getPatientContributionCategory();
    }

    public Montant getHcFlatHelp() {
        return personalCalculationElements.getHcFlatHelp();
    }

    public Montant getHcEffectiveHelp() {
        return personalCalculationElements.getHcEffectiveHelp();
    }

    public Montant getOtherExpenses() {
        return personalCalculationElements.getOtherExpenses();
    }

    // Accesseurs AnnoncePersonalCalculationElements.AnnoncePensionCategory.AnnoncePension

    public Montant getAvsAipension() {
        return personalCalculationElements.getPensionCategory().getPension() != null ? personalCalculationElements
                .getPensionCategory().getPension().getAvsAipension() : null;
    }

    public Montant getDisabledAllowance() {
        return personalCalculationElements.getPensionCategory().getPension() != null ? personalCalculationElements
                .getPensionCategory().getPension().getDisabledAllowance() : null;
    }

    public Montant getDailyAllowance() {
        return personalCalculationElements.getPensionCategory().getPension() != null ? personalCalculationElements
                .getPensionCategory().getPension().getDailyAllowance() : null;
    }

    // Accesseurs AnnoncePersonalCalculationElements.AnnoncePensionCategory.AnnoncePension.AnnonceCompensationOffice

    public int getCompensationOffice() {
        return personalCalculationElements.getPensionCategory().getPension() != null ? personalCalculationElements
                .getPensionCategory().getPension().getCompensationOffice() != null ? personalCalculationElements
                .getPensionCategory().getPension().getCompensationOffice().getCompensationOffice() : null : null;
    }

    public Integer getCompensationAgency() {
        return personalCalculationElements.getPensionCategory().getPension() != null ? personalCalculationElements
                .getPensionCategory().getPension().getCompensationOffice() != null ? personalCalculationElements
                .getPensionCategory().getPension().getCompensationOffice().getCompensationAgency() : null : null;
    }

    // Accesseurs AnnoncePersonalCalculationElements.AnnonceResidenceCosts

    public Montant getResidenceCostsLodging() {
        return personalCalculationElements.getResidenceCosts() != null ? personalCalculationElements
                .getResidenceCosts().getResidenceCostsLodging() : null;
    }

    public Montant getResidenceCostsCare() {
        return personalCalculationElements.getResidenceCosts() != null ? personalCalculationElements
                .getResidenceCosts().getResidenceCostsCare() : null;
    }

    public Montant getResidenceCostsAssistance() {
        return personalCalculationElements.getResidenceCosts() != null ? personalCalculationElements
                .getResidenceCosts().getResidenceCostsAssistance() : null;
    }

    public Montant getResidenceCostsPatientContribution() {
        return personalCalculationElements.getResidenceCosts() != null ? personalCalculationElements
                .getResidenceCosts().getResidenceCostsPatientContribution() : null;
    }

    public Montant getResidenceCostsTotal() {
        return personalCalculationElements.getResidenceCosts() != null ? personalCalculationElements
                .getResidenceCosts().getResidenceCostsTotal() : null;
    }

    public Montant getResidenceCostsConsidered() {
        return personalCalculationElements.getResidenceCosts() != null ? personalCalculationElements
                .getResidenceCosts().getResidenceCostsConsidered() : null;
    }

    public Montant getResidencePatientContribution() {
        return personalCalculationElements.getResidenceCosts() != null ? personalCalculationElements
                .getResidenceCosts().getResidencePatientContribution() : null;
    }

    public Montant getResidencePatientExpenses() {
        return personalCalculationElements.getResidenceCosts() != null ? personalCalculationElements
                .getResidenceCosts().getResidencePatientExpenses() : null;
    }

    public List<PersonElementsCalcul> getPersonsElementsCalcul() {
        return personsElementsCalcul.getPersonsElementsCalcul();
    }

    public String getDegreeOfInvalidity() {
        return degreeOfInvalidity;
    }

}
