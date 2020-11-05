package ch.globaz.pegasus.rpc.domaine.annonce;

import ch.globaz.common.domaine.Montant;
import ch.globaz.pegasus.business.domaine.pca.PcaDecision;
import ch.globaz.pegasus.rpc.domaine.PersonElementsCalcul;
import ch.globaz.pegasus.rpc.domaine.RpcCalcul;

public class AnnoncePersonalCalculationElements {

    /**
     * for all data not in WebAVS
     */
    private static final String XSD_PCC_PARTIE_DE_LA_TAXE_HOME = "INSIDE_RESIDENCE_COSTS";
    private static final String XSD_PCC_EN_SUS_DE_LA_TAXE_HOME = "IN_ADDITION_RESIDENCE_COSTS";
    private static final String XSD_PCC_NON_PRIS_EN_COMPTE = "IGNORED";

    PersonElementsCalcul personData;
    PersonElementsCalcul requerantData;
    PcaDecision pcaDecision;

    protected Montant hcLcaAllowance;
    protected Montant lucrativeGrossIncome;
    protected Montant hypotheticalGrossIncome;
    protected Montant totalPension;
    protected Montant lppPension;
    protected Montant foreignPension;
    protected Montant otherIncomes;
    protected Montant lppWithdrawalAmount;
    protected String patientContributionCategory;
    protected Montant hcFlatHelp;
    protected Montant hcEffectiveHelp;
    protected Montant otherExpenses;
    protected Montant disabledAllowance;
    protected Montant childrenCostsAssitanceNet;
    protected Montant individualPremiumReduction;
    protected Boolean disabledAllowanceRecipient;

    protected AnnoncePensionCategory pensionCategory;
    protected AnnonceResidenceCosts residenceCosts;

    public AnnoncePersonalCalculationElements(PersonElementsCalcul personData, PersonElementsCalcul requerantData,
                                              PcaDecision pcaDecision, RpcCalcul calcul) {
        this.personData = personData;
        this.requerantData = requerantData;
        this.pcaDecision = pcaDecision;
        pensionCategory = new AnnoncePensionCategory(personData, requerantData, pcaDecision);
        if (personData.hasResidenceCosts()) {
            residenceCosts = new AnnonceResidenceCosts(personData, calcul);
        }
        hcLcaAllowance = personData.getHomeContributionLca();
        individualPremiumReduction = personData.getMontantRIP();
        lucrativeGrossIncome = personData.getRevenuBruteActiviteLucrative();
        hypotheticalGrossIncome = personData.getRevenuBrutHypothetique();
        totalPension = personData.getTotalRentes();
        lppPension = personData.hasLppPension() ? personData.getLpp() : null;
        foreignPension = personData.hasForeignPension() ? personData.getRenteEtrangere() : null;
        otherIncomes = personData.getAutresRevenus();
        // Nous ne disposons pas de cette info
        lppWithdrawalAmount = Montant.ZERO;
        if (pcaDecision.getPca().getGenre().isDomicile()) {
            patientContributionCategory = XSD_PCC_NON_PRIS_EN_COMPTE;
        } else {
            patientContributionCategory = XSD_PCC_PARTIE_DE_LA_TAXE_HOME;
            if (personData.hasResidenceContributions()) {
                patientContributionCategory = XSD_PCC_EN_SUS_DE_LA_TAXE_HOME;
            }
            disabledAllowance = personData.getRenteApi();
        }
        hcFlatHelp = personData.getPrimeLamal();
        hcEffectiveHelp = personData.getPrimeEffective();
        otherExpenses = personData.getAutresDepenses();
        childrenCostsAssitanceNet = personData.getFraisGarde();
        disabledAllowanceRecipient = personData.isRentHasApi();
    }

    public Montant getHcLcaAllowance() {
        return hcLcaAllowance;
    }

    public Montant getLucrativeGrossIncome() {
        return lucrativeGrossIncome;
    }

    public Montant getHypotheticalGrossIncome() {
        return hypotheticalGrossIncome;
    }

    public Montant getTotalPension() {
        return totalPension;
    }

    public Montant getLppPension() {
        return lppPension;
    }

    public Montant getForeignPension() {
        return foreignPension;
    }

    public Montant getOtherIncomes() {
        return otherIncomes;
    }

    public Montant getLppWithdrawalAmount() {
        return lppWithdrawalAmount;
    }

    public String getPatientContributionCategory() {
        return patientContributionCategory;
    }

    public Montant getHcFlatHelp() {
        return hcFlatHelp;
    }

    public Montant getHcEffectiveHelp() {
        return hcEffectiveHelp;
    }

    public Montant getOtherExpenses() {
        return otherExpenses;
    }

    public AnnoncePensionCategory getPensionCategory() {
        return pensionCategory;
    }

    public AnnonceResidenceCosts getResidenceCosts() {
        return residenceCosts;
    }

    public Montant getDisabledAllowance() {
        return disabledAllowance;
    }

    public Montant getChildrenCostsAssitanceNet() {
        return childrenCostsAssitanceNet;
    }

    public Montant getIndividualPremiumReduction() {
        return individualPremiumReduction;
    }

    public Boolean getDisabledAllowanceRecipient() {
        return disabledAllowanceRecipient;
    }


}
