package ch.globaz.amal.business.calcul;

import ch.globaz.amal.business.constantes.IAMCodeSysteme;
import ch.globaz.amal.business.constantes.IAMParametresAnnuels;
import ch.globaz.amal.business.exceptions.models.parametreannuel.ParametreAnnuelException;
import ch.globaz.amal.business.exceptions.models.revenu.RevenuException;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfants;
import ch.globaz.amal.business.models.deductionsfiscalesenfants.SimpleDeductionsFiscalesEnfantsSearch;
import ch.globaz.amal.business.models.famille.SimpleFamilleSearch;
import ch.globaz.amal.business.models.revenu.RevenuFullComplex;
import ch.globaz.amal.business.models.revenu.RevenuHistoriqueComplex;
import ch.globaz.amal.business.services.AmalServiceLocator;
import ch.globaz.amal.businessimpl.utils.parametres.ParametresAnnuelsProvider;
import globaz.globall.util.JANumberFormatter;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.log.JadeLogger;

/**
 * Classe de calcul de revenu déterminant et revenu imposable sourcier
 *
 * TODO: secure all interger.parse !
 *
 * @author cbu
 *
 */
public class CalculsRevenuFormules {
    private int nbRevenus = 0;

    public int PERCENT_FORTUNE_IMPOSABLE = 3;
    private String sCotisationAc = "0";
    private String sCotisationAcSuppl = "0";
    private String sCotisationAvsAijApg = "0";
    private String sDeductionAssurance = "0";
    private String sDeductionAssuranceEnfant = "0";
    private String sDeductionAssuranceJeune = "0";
    private String sDeductionDoubleGain = "0";
    private String sDeductionEnfants = "0";
    private String sDeductionFraisObtention = "0";
    private String sommeDeductionContribAvecEnfantChargeCalcul = "0";
    private String sommeDeductionContribNonCelibSansEnfantChargeCalcul = "0";
    private String sommeDeductionSelonNbreEnfantCalcul = "0";
    private String sommeExcDepPropImmo = "0";
    private String sommeExcedentDepSuccNonPart = "0";
    private String sommeFortuneImposablePercentCalcul = "0";
    private String sommeIntPassifs = "0";
    private String sommePartRendementImmob = "0";
    private String sommeDeductionCouplesMaries = "0";
    private String sommePerteLiquidation = "0";
    private String sommePerteReporteeExCommerciaux = "0";
    private String sommePertesExCommerciaux = "0";
    private String sommeRendementFortuneImmobiliere = "0";
    private String sommeRevenuImposableCalcul = "0";
    private String sPrimeAANP = "0";
    private String sPrimeLPP = "0";
    private String sRevenuPrisEnCompte = "0";
    private String totalRevenuDeterminant = "0";

    /**
     * Default constructor
     */
    public CalculsRevenuFormules() {
        sCotisationAc = "0";
        sCotisationAcSuppl = "0";
        sCotisationAvsAijApg = "0";
        sDeductionAssurance = "0";
        sDeductionAssuranceEnfant = "0";
        sDeductionAssuranceJeune = "0";
        sDeductionDoubleGain = "0";
        sDeductionEnfants = "0";
        sDeductionFraisObtention = "0";
        sommeDeductionContribAvecEnfantChargeCalcul = "0";
        sommeDeductionContribNonCelibSansEnfantChargeCalcul = "0";
        sommeDeductionSelonNbreEnfantCalcul = "0";
        sommeExcDepPropImmo = "0";
        sommeExcedentDepSuccNonPart = "0";
        sommeFortuneImposablePercentCalcul = "0";
        sommeIntPassifs = "0";
        sommePartRendementImmob = "0";
        sommeDeductionCouplesMaries = "0";
        sommePerteLiquidation = "0";
        sommePerteReporteeExCommerciaux = "0";
        sommePertesExCommerciaux = "0";
        sommeRendementFortuneImmobiliere = "0";
        sommeRevenuImposableCalcul = "0";
        sPrimeAANP = "0";
        sPrimeLPP = "0";
        sRevenuPrisEnCompte = "0";
        totalRevenuDeterminant = "0";
        nbRevenus = 0;
        if (ParametresAnnuelsProvider.containerParametres == null) {
            ParametresAnnuelsProvider.initParamAnnuels();
        }
    }

    /**
     * Calcul les éléments du revenus déterminants
     *
     * @param revenuHistoriqueComplex
     */
    public CalculsRevenuFormules(RevenuHistoriqueComplex revenuHistoriqueComplex) {
        sommeRevenuImposableCalcul = revenuHistoriqueComplex.getSimpleRevenuDeterminant().getRevenuImposableCalcul();
        sommeRendementFortuneImmobiliere = revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                .getRendementFortuneImmoCalcul();
        sommePertesExCommerciaux = revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                .getPerteExercicesCommerciauxCalcul();
        sommePerteReporteeExCommerciaux = revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                .getPerteReporteeExercicesCommerciauxCalcul();
        sommePerteLiquidation = revenuHistoriqueComplex.getSimpleRevenuDeterminant().getPerteLiquidationCalcul();
        sommePartRendementImmob = revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                .getPartRendementImmobExedantIntPassifsCalcul();
        sommeDeductionCouplesMaries = revenuHistoriqueComplex.getSimpleRevenuDeterminant().getDeductionCouplesMaries();
        sommeIntPassifs = revenuHistoriqueComplex.getSimpleRevenuDeterminant().getInteretsPassifsCalcul();
        sommeExcedentDepSuccNonPart = revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                .getExcedentDepensesSuccNonPartageesCalcul();
        sommeExcDepPropImmo = revenuHistoriqueComplex.getSimpleRevenuDeterminant().getExcedentDepensesPropImmoCalcul();
        sommeDeductionSelonNbreEnfantCalcul = revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                .getDeductionSelonNbreEnfantCalcul();
        sommeDeductionContribNonCelibSansEnfantChargeCalcul = revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                .getDeductionContribNonCelibSansEnfantChargeCalcul();
        sommeDeductionContribAvecEnfantChargeCalcul = revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                .getDeductionContribAvecEnfantChargeCalcul();
    }

    /**
     * Calcul des déductions cotisations pour un contribuable de type sourcier
     *
     * @param revenuFullComplex
     * @throws ParametreAnnuelException
     */
    private void calculCotisations(RevenuFullComplex revenuFullComplex) throws ParametreAnnuelException {

        if (revenuFullComplex.getSimpleRevenu().isSourcier()) {
            String txCotisationAvsAijApg = this.getParametreAnnuel(
                    IAMParametresAnnuels.CS_IPS_TX_CALCUL_COTI_AVS_AI_APG,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
            String txCotisationAc = this.getParametreAnnuel(IAMParametresAnnuels.CS_IPS_TX_CALCUL_COTI_AC,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
            String limiteCotisationAc = this.getParametreAnnuel(IAMParametresAnnuels.CS_IPS_LIMITE_COTI_AC,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);

            String txCotisationAcSuppl = this.getParametreAnnuel(IAMParametresAnnuels.CS_IPS_TX_CALCUL_COTI_AC_SUPPL,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
            String limiteCotisationAcSuppl = this.getParametreAnnuel(IAMParametresAnnuels.CS_IPS_LIMITE_COTI_AC_SUPPL,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);

            String revPrisEnCompte = this
                    .dequoteAndFormat(revenuFullComplex.getSimpleRevenuSourcier().getRevenuPrisEnCompte());

            sCotisationAvsAijApg = calculPercentage(revPrisEnCompte, txCotisationAvsAijApg);

            // Adaptation au nombre de revenus
            if (nbRevenus == 2) {
                limiteCotisationAc = String.valueOf(Double.valueOf(limiteCotisationAc) * 2);
            }
            if (nbRevenus == 2) {
                limiteCotisationAcSuppl = String.valueOf(Double.valueOf(limiteCotisationAcSuppl) * 2);
            }

            boolean limiteAcSupplAtteinte = false;
            if (Double.valueOf(revPrisEnCompte) > Double.valueOf(limiteCotisationAc)) {
                sCotisationAc = calculPercentage(limiteCotisationAc, txCotisationAc);
                limiteAcSupplAtteinte = true;
            } else {
                sCotisationAc = calculPercentage(revPrisEnCompte, txCotisationAc);
            }

            if (limiteAcSupplAtteinte) {
                Double diffRevenuEtLimite = Double.valueOf(revPrisEnCompte) - Double.valueOf(limiteCotisationAc);

                if (diffRevenuEtLimite > Double.valueOf(limiteCotisationAcSuppl)) {
                    sCotisationAcSuppl = calculPercentage(limiteCotisationAcSuppl, txCotisationAcSuppl);
                } else {
                    sCotisationAcSuppl = calculPercentage(String.valueOf(diffRevenuEtLimite), txCotisationAcSuppl);
                }
            }

            revenuFullComplex.getSimpleRevenuSourcier().setCotisationAvsAiApg(sCotisationAvsAijApg);
            revenuFullComplex.getSimpleRevenuSourcier().setCotisationAc(sCotisationAc);
            revenuFullComplex.getSimpleRevenuSourcier().setCotisationAcSupplementaires(sCotisationAcSuppl);
        } else {
            sCotisationAc = "0";
            sCotisationAcSuppl = "0";
            sCotisationAvsAijApg = "0";
        }

    }

    /**
     * Calcul les déduction assurance pour le contribuable de type sourcier
     *
     * @param revenuFullComplex
     * @param revPrisEnCompte
     * @throws ParametreAnnuelException
     */
    private void calculDeductionAssurance(RevenuFullComplex revenuFullComplex, String revPrisEnCompte)
            throws ParametreAnnuelException {
        if (revenuFullComplex.getSimpleRevenu().isSourcier()) {
            // Calcul déduction assurance
            Boolean isMaried = IAMCodeSysteme.CS_ETAT_CIVIL_MARIED
                    .equals(revenuFullComplex.getSimpleRevenu().getEtatCivil());
            String deductionAssurance = this.getParametreAnnuel(
                    IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_CELIB,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
            if (isMaried) {
                deductionAssurance = this.getParametreAnnuel(
                        IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ASSU_COUPLE,
                        revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
            }
            String limiteDeductionAssurance = this.getParametreAnnuel(IAMParametresAnnuels.CS_IPS_LIMITE_DEDUCTION_ASSU,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);

            sDeductionAssurance = calculPercentage(revPrisEnCompte, deductionAssurance);

            // Adaptation au nombre de revenus
            if (isMaried) {
                limiteDeductionAssurance = String.valueOf(Double.valueOf(limiteDeductionAssurance) * 2);
            }
            if (Double.valueOf(sDeductionAssurance) > Double.valueOf(limiteDeductionAssurance)) {
                sDeductionAssurance = limiteDeductionAssurance;
            }
            // Fin calcul déduction assurance
            revenuFullComplex.getSimpleRevenuSourcier().setDeductionAssurances(sDeductionAssurance);
        } else {
            sDeductionAssurance = "0";
        }
    }

    /**
     * Calcul les déduction assurance enfants jeunes pour le contribuable de type sourcier
     *
     * @param revenuFullComplex
     * @throws RevenuException
     * @throws ParametreAnnuelException
     */
    private void calculDeductionAssurancesEnfantsJeunes(RevenuFullComplex revenuFullComplex)
            throws RevenuException, ParametreAnnuelException {

        if (revenuFullComplex.getSimpleRevenu().isSourcier()) {
            int nbEnfants = 0;
            try {
                nbEnfants = Integer.parseInt(revenuFullComplex.getSimpleRevenu().getNbEnfants());
            } catch (NumberFormatException nfe) {
                nbEnfants = 0;
            }

            // Calcul déduction assurance enfants et jeunes
            int nbChildrenBetween16And25 = getNbChildrenBetween16And25(revenuFullComplex);
            int nbChildrenUntil16 = getNbChildrenUnder16(revenuFullComplex);
            // Si le total des gosses trouvés ne fait pas le total réel, action
            if ((nbEnfants != 0) && (nbEnfants != (nbChildrenUntil16 + nbChildrenBetween16And25))) {
                if (nbChildrenBetween16And25 == 0) {
                    nbChildrenUntil16 = nbEnfants;
                } else if (nbChildrenUntil16 == 0) {
                    nbChildrenBetween16And25 = nbEnfants;
                } else {
                    nbChildrenUntil16 = nbEnfants - nbChildrenBetween16And25;
                }
            }

            if ((nbChildrenUntil16 > 0) && (nbEnfants > 0)) {
                String deductionAssuEnfants = this.getParametreAnnuel(
                        IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUC_ASSU_ENFA,
                        revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
                sDeductionAssuranceEnfant = String.valueOf(nbChildrenUntil16 * Double.valueOf(deductionAssuEnfants));
            }

            if ((nbChildrenBetween16And25 > 0) && (nbEnfants > 0)) {
                String deductionAssuranceJeune = this.getParametreAnnuel(
                        IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUC_ASSU_JEUNE,
                        revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
                sDeductionAssuranceJeune = String
                        .valueOf(nbChildrenBetween16And25 * Double.valueOf(deductionAssuranceJeune));
            }
            // Fin calcul déduction assurance enfants et jeunes

            // Calcul déduction enfants
            String deductionEnfant = this.getParametreAnnuel(
                    IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ENFANT_LESS_EQUAL_2,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
            if (nbEnfants > 2) {
                deductionEnfant = this.getParametreAnnuel(
                        IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUCTION_ENFANT_GREATER_2,
                        revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
            }
            sDeductionEnfants = String.valueOf(Double.valueOf(deductionEnfant) * (nbEnfants));
            // Fin calcul déduction enfants

            revenuFullComplex.getSimpleRevenuSourcier().setDeductionAssurancesEnfant(sDeductionAssuranceEnfant);
            revenuFullComplex.getSimpleRevenuSourcier().setDeductionAssurancesJeunes(sDeductionAssuranceJeune);
            revenuFullComplex.getSimpleRevenuSourcier().setDeductionEnfants(sDeductionEnfants);
        } else {
            sDeductionAssuranceEnfant = "0";
            sDeductionAssuranceJeune = "0";
            sDeductionEnfants = "0";
        }
    }

    /**
     * Calcul la déduction double gain pour un contribuable de type sourcier
     *
     * @param revenuFullComplex
     * @throws ParametreAnnuelException
     */
    private void calculDeductionDoubleGain(RevenuFullComplex revenuFullComplex) throws ParametreAnnuelException {

        if (revenuFullComplex.getSimpleRevenu().isSourcier()) {
            // Calcul déduction double gain
            if (nbRevenus == 2) {
                String deductionDoubleGain = this.getParametreAnnuel(
                        IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUC_DOUBLE_GAIN,
                        revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
                sDeductionDoubleGain = deductionDoubleGain;
            } else {
                sDeductionDoubleGain = "0";
            }
            // Fin calcul déduction double gain
            revenuFullComplex.getSimpleRevenuSourcier().setDeductionDoubleGain(sDeductionDoubleGain);
        } else {
            sDeductionDoubleGain = "0";
        }
    }

    /**
     * Calcul les déductions assurances, frais obtention, double gain pour un contribuable de type sourcier
     *
     * @param revenuFullComplex
     * @throws RevenuException
     * @throws ParametreAnnuelException
     */
    private final void calculDeductions(RevenuFullComplex revenuFullComplex)
            throws RevenuException, ParametreAnnuelException {
        if (revenuFullComplex.getSimpleRevenu().isSourcier()) {
            String revPrisEnCompte = this
                    .dequoteAndFormat(revenuFullComplex.getSimpleRevenuSourcier().getRevenuPrisEnCompte());

            calculDeductionAssurance(revenuFullComplex, revPrisEnCompte);
            calculDeductionAssurancesEnfantsJeunes(revenuFullComplex);
            calculDeductionsFraisObtention(revenuFullComplex, revPrisEnCompte);
            calculDeductionDoubleGain(revenuFullComplex);
        }
    }

    /**
     * Méthode de calcul des déductions AMAL pour enfants et enfants en suspens
     *
     * @param revenuFullComplex
     * @return
     * @throws RevenuException
     */
    public RevenuFullComplex calculDeductionsFiscalesEnfantsPleinTempsEtSuspens(RevenuFullComplex revenuFullComplex)
            throws RevenuException {
        try {
            // Récupération des valeurs
            String anneeTaxation = revenuFullComplex.getSimpleRevenu().getAnneeTaxation();
            String nbEnfants = revenuFullComplex.getSimpleRevenu().getNbEnfants();
            Double nbEnfants_Double = Double.parseDouble(nbEnfants);

            String nbEnfantsSuspens = revenuFullComplex.getSimpleRevenu().getNbEnfantSuspens();
            Double nbEnfantsSuspens_Double = Double.parseDouble(nbEnfantsSuspens);

            String revenuImposable = revenuFullComplex.getSimpleRevenuContribuable().getRevenuImposable();
            Double revenuImposable_Double = Double.parseDouble(revenuImposable);

            String revenuTaux = revenuFullComplex.getSimpleRevenuContribuable().getRevenuTaux();
            Double revenuTaux_Double = Double.parseDouble(revenuTaux);

            String persChargeEnf = revenuFullComplex.getSimpleRevenuContribuable().getPersChargeEnf();
            Double persChargeEnf_Double = Double.parseDouble(persChargeEnf);

            Double totalRevenuPersCharge = 0.0;

            int nbEnfantsTotal = Integer.parseInt(nbEnfants) + Integer.parseInt(nbEnfantsSuspens);

            if (JadeStringUtil.isNull(revenuImposable)) {
                revenuImposable = "0.00";
            }
            if (JadeStringUtil.isNull(revenuTaux)) {
                revenuTaux = "0.00";
            }

            // On vérifie si un 'revenuTaux' existe. Si oui, on utilisera ce montant la (rubrique 695)
            // On additionne le revenuImposable (ou revenuTaux) avec le montant dans la rubrique 620 pour avoir le total
            boolean tauxExist = false;
            if (Double.parseDouble(revenuTaux) > 0) {
                tauxExist = true;
                totalRevenuPersCharge = revenuTaux_Double + persChargeEnf_Double;
            } else {
                totalRevenuPersCharge = revenuImposable_Double + persChargeEnf_Double;
            }
            if (nbEnfantsTotal > 0) {
                // Récupération des montants pour les déductions fiscales par enfant pour l'année taxation utilisée.
                SimpleDeductionsFiscalesEnfantsSearch simpleDeductionsFiscalesEnfantsSearch = new SimpleDeductionsFiscalesEnfantsSearch();
                simpleDeductionsFiscalesEnfantsSearch.setForAnneeTaxationLOE(anneeTaxation);
                simpleDeductionsFiscalesEnfantsSearch.setForNbEnfantLOE(String.valueOf(nbEnfantsTotal));

                simpleDeductionsFiscalesEnfantsSearch = AmalServiceLocator.getDeductionsFiscalesEnfantsService()
                        .search(simpleDeductionsFiscalesEnfantsSearch);

                Double mtDeductionParEnfant = 0.0;
                SimpleDeductionsFiscalesEnfants simpleDeductionsFiscalesEnfants = (SimpleDeductionsFiscalesEnfants) simpleDeductionsFiscalesEnfantsSearch
                        .getSearchResults()[0];

                mtDeductionParEnfant = Double.valueOf(simpleDeductionsFiscalesEnfants.getMontantDeductionParEnfant());
                Double mtDeductionPersChargeEnf_Double = mtDeductionParEnfant * nbEnfants_Double;

                // Si un nombre est présent dans la rubrique 695...
                if (tauxExist) {
                    // RevenuTaux = Montant total - Déductions trouvée
                    revenuTaux_Double = totalRevenuPersCharge - mtDeductionPersChargeEnf_Double;
                    revenuFullComplex.getSimpleRevenuContribuable().setRevenuTaux(String.valueOf(revenuTaux_Double));
                } else {
                    revenuImposable_Double = totalRevenuPersCharge - mtDeductionPersChargeEnf_Double;
                    revenuFullComplex.getSimpleRevenuContribuable()
                            .setRevenuImposable(String.valueOf(revenuImposable_Double));
                }
                revenuFullComplex.getSimpleRevenuContribuable()
                        .setPersChargeEnf(String.valueOf(mtDeductionPersChargeEnf_Double));

                // Mêmes calculs pour les enfants en suspens
                if (!JadeStringUtil.isBlankOrZero(nbEnfantsSuspens)) {
                    Double persCharEnfSuspens_Double = (mtDeductionParEnfant * nbEnfantsSuspens_Double) / 2;

                    persCharEnfSuspens_Double = persCharEnfSuspens_Double
                            + Double.valueOf(revenuFullComplex.getSimpleRevenuContribuable().getPersChargeEnf());
                    if (tauxExist) {
                        revenuTaux_Double = totalRevenuPersCharge - persCharEnfSuspens_Double;
                        revenuFullComplex.getSimpleRevenuContribuable()
                                .setRevenuTaux(String.valueOf(revenuTaux_Double));
                    } else {
                        revenuImposable_Double = totalRevenuPersCharge - persCharEnfSuspens_Double;
                        revenuFullComplex.getSimpleRevenuContribuable()
                                .setRevenuImposable(String.valueOf(revenuImposable_Double));
                    }
                    revenuFullComplex.getSimpleRevenuContribuable()
                            .setPersChargeEnf(String.valueOf(persCharEnfSuspens_Double));
                }
            } else {
                revenuFullComplex.getSimpleRevenuContribuable().setPersChargeEnf("0");
                if (tauxExist) {
                    revenuFullComplex.getSimpleRevenuContribuable().setRevenuTaux(totalRevenuPersCharge.toString());
                } else {
                    revenuFullComplex.getSimpleRevenuContribuable()
                            .setRevenuImposable(totalRevenuPersCharge.toString());
                }
            }
        } catch (Exception e) {
            throw new RevenuException("Fatal error while searching RevenuFullComplex for calcul !!");
        }
        return revenuFullComplex;
    }

    /**
     * Calcul les déductions de frais d'obtention pour un contribuable de type sourcier
     *
     * @param revenuFullComplex
     * @param revPrisEnCompte
     * @throws ParametreAnnuelException
     */
    private void calculDeductionsFraisObtention(RevenuFullComplex revenuFullComplex, String revPrisEnCompte)
            throws ParametreAnnuelException {
        if (revenuFullComplex.getSimpleRevenu().isSourcier()) {
            String deductionFraisObtention = this.getParametreAnnuel(
                    IAMParametresAnnuels.CS_IPS_TX_CALCUL_DEDUC_FRAIS_OBTENTION,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
            String limiteDeductionFraisObtention = this.getParametreAnnuel(
                    IAMParametresAnnuels.CS_IPS_LIMITE_DEDUC_FRAIS_OBTENTION,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
            sDeductionFraisObtention = calculPercentage(revPrisEnCompte, deductionFraisObtention);

            // Adaptation au nombre de revenus
            if (nbRevenus == 2) {
                limiteDeductionFraisObtention = String.valueOf(Double.valueOf(limiteDeductionFraisObtention) * 2);
            }

            if (Double.valueOf(sDeductionFraisObtention) >= Double.valueOf(limiteDeductionFraisObtention)) {
                sDeductionFraisObtention = String.valueOf(Double.valueOf(limiteDeductionFraisObtention));
            }

            revenuFullComplex.getSimpleRevenuSourcier().setDeductionFraisObtention(sDeductionFraisObtention);
        } else {
            sDeductionFraisObtention = "0";
        }
    }

    /**
     * Utilitaire pour calculer le pourcentage d'un nombre
     *
     * @param number
     * @param percentage
     * @return
     */
    private String calculPercentage(String number, String percentage) {
        Float num = Float.valueOf(this.dequoteAndFormat(number));
        Float per = Float.valueOf(this.dequoteAndFormat(percentage, false, false, false, 2));

        Float result = num;
        if (per > 0) {
            result = (num * per) / 100;
        }

        return JANumberFormatter.fmt(String.valueOf(JANumberFormatter.round(result, 1, 0, JANumberFormatter.SUP)),
                false, true, false, 2);

    }

    /**
     * Calcul des primes LPP ET AANP pour un contribuable de type sourcier
     *
     * @param revenuFullComplex
     * @throws ParametreAnnuelException
     */
    private final void calculPrimes(RevenuFullComplex revenuFullComplex) throws ParametreAnnuelException {

        if (revenuFullComplex.getSimpleRevenu().isSourcier()) {
            String revPrisEnCompte = this
                    .dequoteAndFormat(revenuFullComplex.getSimpleRevenuSourcier().getRevenuPrisEnCompte());

            String txPrimesAANP = this.getParametreAnnuel(IAMParametresAnnuels.CS_IPS_TX_CALCUL_PRIME_AANP,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
            String limitePrimesAANP = this.getParametreAnnuel(IAMParametresAnnuels.CS_IPS_LIMITE_PRIME_AANP,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);
            String txPrimesLPP = this.getParametreAnnuel(IAMParametresAnnuels.CS_IPS_TX_CALCUL_PRIME_LPP,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation(), 2);

            sPrimeAANP = calculPercentage(revPrisEnCompte, txPrimesAANP);

            // Adaptation au nombre de revenus
            if (nbRevenus == 2) {
                limitePrimesAANP = String.valueOf(Double.valueOf(limitePrimesAANP) * 2);
            }
            if (Double.valueOf(sPrimeAANP) > Double.valueOf(limitePrimesAANP)) {
                sPrimeAANP = limitePrimesAANP;
            }

            sPrimeLPP = calculPercentage(revPrisEnCompte, txPrimesLPP);

            revenuFullComplex.getSimpleRevenuSourcier().setPrimesAANP(sPrimeAANP);
            revenuFullComplex.getSimpleRevenuSourcier().setPrimesLPP(sPrimeLPP);
        } else {
            sPrimeAANP = "0";
            sPrimeLPP = "0";
        }
    }

    /**
     * Calcul du revenu prise en compte (montant par mois * nb mois) pour un contribuable de type sourcier
     *
     * @param revenuFullComplex
     * @throws ParametreAnnuelException
     */
    private final void calculRevenuPrisEnCompte(RevenuFullComplex revenuFullComplex) throws ParametreAnnuelException {
        if (revenuFullComplex.getSimpleRevenu().isSourcier()) {
            String revenuAnnuelEpoux = JANumberFormatter.round(
                    this.dequoteAndFormat(revenuFullComplex.getSimpleRevenuSourcier().getRevenuEpouxAnnuel()), 1, 0,
                    JANumberFormatter.SUP);
            String revenuMensuelEpoux = JANumberFormatter.round(
                    this.dequoteAndFormat(revenuFullComplex.getSimpleRevenuSourcier().getRevenuEpouxMensuel()), 1, 0,
                    JANumberFormatter.SUP);
            String revenuAnnuelEpouse = JANumberFormatter.round(
                    this.dequoteAndFormat(revenuFullComplex.getSimpleRevenuSourcier().getRevenuEpouseAnnuel()), 1, 0,
                    JANumberFormatter.SUP);
            String revenuMensuelEpouse = JANumberFormatter.round(
                    this.dequoteAndFormat(revenuFullComplex.getSimpleRevenuSourcier().getRevenuEpouseMensuel()), 1, 0,
                    JANumberFormatter.SUP);
            String nbMois = this.dequoteAndFormat(revenuFullComplex.getSimpleRevenuSourcier().getNombreMois());

            String PercentageRevenu = this.getParametreAnnuel(IAMParametresAnnuels.CS_IPS_TX_CALCUL_REVENU,
                    revenuFullComplex.getSimpleRevenu().getAnneeTaxation());

            Float calRev = Float.valueOf(revenuMensuelEpouse) * Integer.parseInt(nbMois);
            calRev += Float.valueOf(revenuMensuelEpoux) * Integer.parseInt(nbMois);
            calRev += Float.valueOf(revenuAnnuelEpouse);
            calRev += Float.valueOf(revenuAnnuelEpoux);

            sRevenuPrisEnCompte = calculPercentage(String.valueOf(calRev), PercentageRevenu);

            revenuFullComplex.getSimpleRevenuSourcier().setRevenuPrisEnCompte(sRevenuPrisEnCompte);

            // On vérifie combien de revenu il y a
            if (!JadeStringUtil.isBlankOrZero(revenuMensuelEpouse)
                    || !JadeStringUtil.isBlankOrZero(revenuAnnuelEpouse)) {
                nbRevenus++;
            }

            if (!JadeStringUtil.isBlankOrZero(revenuMensuelEpoux) || !JadeStringUtil.isBlankOrZero(revenuAnnuelEpoux)) {
                nbRevenus++;
            }
        } else {
            sRevenuPrisEnCompte = "0";
        }

    }

    /**
     * Utilitaire de formatage
     *
     * @param value
     * @return
     */
    private String dequoteAndFormat(String value) {
        return this.dequoteAndFormat(value, false, false, false, 0);
    }

    /**
     * Utilitaire de formatage
     *
     * @param value
     * @return
     */
    private String dequoteAndFormat(String value, boolean wantQuote, boolean wantDecimalsIfZero,
            boolean wantBlankIfZero, int nDecimals) {
        if (value == null) {
            value = "0";
        }
        String val = JANumberFormatter.deQuote(value);
        val = JANumberFormatter.fmt(val, wantQuote, wantDecimalsIfZero, wantBlankIfZero, nDecimals);

        return val;
    }

    /**
     * Calcul du revenu déterminant en fonction d'informations de revenus complètes
     *
     * @param revenuHistoriqueComplex
     * @return
     * @throws ParametreAnnuelException
     * @throws NumberFormatException
     */
    public final RevenuHistoriqueComplex doCalcul(RevenuHistoriqueComplex revenuHistoriqueComplex)
            throws NumberFormatException {
        try {
            // Variables to use
            // --------------------------------------------------
            String excedentDepensesPropImmoCalcul = "0.0";
            String rendementFortuneImmoCalcul = "0.0";
            String excDepSuccNp = "0.0";
            String intPassifs = "0.0";
            String perteExercicesCommerciaux = "0";
            String perteReporteeExercicesCommerciauxCalcul = "0.0";
            String perteLiquidation = "0.0";
            String partRendementImmob = "0.0";
            String deductionCoupleMarie = "0.0";
            String revenuImposableCalcul = "0.0";
            String fortuneImposablePercentCalcul = "0.0";
            String fortuneImposableCalcul = "0.0";

            // Do the calcul for normal contribuable
            // --------------------------------------------------
            if (!IAMCodeSysteme.CS_TYPE_SOURCIER
                    .equals(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getTypeRevenu())) {
                excedentDepensesPropImmoCalcul = getSommeExcDepPropImmo(revenuHistoriqueComplex);
                rendementFortuneImmoCalcul = getSommeRendementFortuneImmobiliere(revenuHistoriqueComplex);
                excDepSuccNp = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                        .getExcDepSuccNp();
                excDepSuccNp = JadeStringUtil.isBlankOrZero(excDepSuccNp) ? "0" : excDepSuccNp;
                perteReporteeExercicesCommerciauxCalcul = revenuHistoriqueComplex.getRevenuFullComplex()
                        .getSimpleRevenuContribuable().getPerteCommercial();
                perteReporteeExercicesCommerciauxCalcul = JadeStringUtil.isBlankOrZero(
                        perteReporteeExercicesCommerciauxCalcul) ? "0" : perteReporteeExercicesCommerciauxCalcul;
                intPassifs = getSommeIntPassifs(revenuHistoriqueComplex);
                perteExercicesCommerciaux = getSommePertesExCommerciaux(revenuHistoriqueComplex);
                partRendementImmob = getSommePartRendementImmob(revenuHistoriqueComplex);
                deductionCoupleMarie = getSommeDeductionCouplesMaries(revenuHistoriqueComplex);
                fortuneImposablePercentCalcul = getFortuneImposablePercentCalcul(revenuHistoriqueComplex);

                // On prend 'Fortune taux' s'il existe, sinon 'Fortune Imposable'
                fortuneImposableCalcul = JadeStringUtil.isBlankOrZero(
                        revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable().getFortuneTaux())
                                ? revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                                        .getFortuneImposable()
                                : revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                                        .getFortuneTaux();
                // On enlève les quotes sinon ca pète dans le parseInt ou dans le round
                fortuneImposableCalcul = this.dequoteAndFormat(fortuneImposableCalcul);
                // Si la fortune est plus grande que 1000 on arrondi
                if (Integer.parseInt(fortuneImposableCalcul) > 1000) {
                    fortuneImposableCalcul = JadeStringUtil.isBlankOrZero(fortuneImposableCalcul) ? "0"
                            : JANumberFormatter.round(fortuneImposableCalcul, 1000, 0, JANumberFormatter.INF);
                }

            }
            sommeExcedentDepSuccNonPart = excDepSuccNp;
            sommePerteReporteeExCommerciaux = perteReporteeExercicesCommerciauxCalcul;
            sommePerteLiquidation = perteLiquidation;

            // Do common (sourcier-standard) calculation
            // --------------------------------------------------
            revenuImposableCalcul = getRevenuImposableCalcul(revenuHistoriqueComplex);

            String deductionSelonNbreEnfantCalcul = getDeductionSelonNbreEnfantCalcul(revenuHistoriqueComplex);
            String deductionContribAvecEnfantChargeCalcul = getDeductionContribAvecEnfantChargeCalcul(
                    revenuHistoriqueComplex);
            String deductionContribNonCelibSansEnfantChargeCalcul = getDeductionContribNonCelibSansEnfantChargeCalcul(
                    revenuHistoriqueComplex);
            String nbEnfantsPlusEnfantsSuspens = groupChildrens(revenuHistoriqueComplex);

            // Set the revenu historique complex with new values
            // --------------------------------------------------
            revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                    .setExcedentDepensesPropImmoCalcul(excedentDepensesPropImmoCalcul);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                    .setRendementFortuneImmoCalcul(rendementFortuneImmoCalcul);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                    .setExcedentDepensesSuccNonPartageesCalcul(excDepSuccNp);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant().setInteretsPassifsCalcul(intPassifs);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                    .setPerteExercicesCommerciauxCalcul(perteExercicesCommerciaux);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                    .setPerteReporteeExercicesCommerciauxCalcul(perteReporteeExercicesCommerciauxCalcul);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant().setPerteLiquidationCalcul(perteLiquidation);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                    .setPartRendementImmobExedantIntPassifsCalcul(partRendementImmob);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant().setDeductionCouplesMaries(deductionCoupleMarie);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant().setFortuneImposableCalcul(fortuneImposableCalcul);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                    .setFortuneImposablePercentCalcul(fortuneImposablePercentCalcul);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant().setRevenuImposableCalcul(revenuImposableCalcul);
            revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                    .setPerteExercicesComm(perteExercicesCommerciaux);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                    .setDeductionContribAvecEnfantChargeCalcul(deductionContribAvecEnfantChargeCalcul);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                    .setDeductionSelonNbreEnfantCalcul(deductionSelonNbreEnfantCalcul);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                    .setDeductionContribNonCelibSansEnfantChargeCalcul(deductionContribNonCelibSansEnfantChargeCalcul);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant()
                    .setDeductionContribNonCelibSansEnfantChargeCalcul(deductionContribNonCelibSansEnfantChargeCalcul);
            revenuHistoriqueComplex.getSimpleRevenuDeterminant().setNbEnfants(nbEnfantsPlusEnfantsSuspens);

            // Etape finale, récupération du calcul du revenu déterminant
            // ----------------------------------------------------------
            String revenuDeterminantCalcul = getTotalRevenuDeterminant();
            revenuHistoriqueComplex.getSimpleRevenuDeterminant().setRevenuDeterminantCalcul(revenuDeterminantCalcul);

            return revenuHistoriqueComplex;
        } catch (ParametreAnnuelException pae) {
            // Il manque des paramètres pour le calcul du revenu déterminant, on retourne NULL
            JadeThread.logWarn("CalculsRevenuFormule.doCalcul()", pae.getMessage());
            return null;
        }
    }

    /**
     * Calcul du revenu prise en compte, du revenu imposable sourcier ainsi que de ses déductions
     *
     * @param revenuFullComplex
     * @return
     * @throws RevenuException
     * @throws ParametreAnnuelException
     */
    public final RevenuFullComplex doCalculSourcier(RevenuFullComplex revenuFullComplex) throws RevenuException {
        try {
            calculRevenuPrisEnCompte(revenuFullComplex);
            calculCotisations(revenuFullComplex);
            calculPrimes(revenuFullComplex);
            calculDeductions(revenuFullComplex);

            String[] values = new String[12];
            values[0] = revenuFullComplex.getSimpleRevenuSourcier().getRevenuPrisEnCompte();
            values[1] = "-" + revenuFullComplex.getSimpleRevenuSourcier().getCotisationAvsAiApg();
            values[2] = "-" + revenuFullComplex.getSimpleRevenuSourcier().getCotisationAc();
            values[3] = "0";
            values[4] = "-" + revenuFullComplex.getSimpleRevenuSourcier().getPrimesAANP();
            values[5] = "-" + revenuFullComplex.getSimpleRevenuSourcier().getPrimesLPP();
            values[6] = "-" + revenuFullComplex.getSimpleRevenuSourcier().getDeductionAssurances();
            values[7] = "-" + revenuFullComplex.getSimpleRevenuSourcier().getDeductionAssurancesEnfant();
            values[8] = "-" + revenuFullComplex.getSimpleRevenuSourcier().getDeductionAssurancesJeunes();
            values[9] = "-" + revenuFullComplex.getSimpleRevenuSourcier().getDeductionEnfants();
            values[10] = "-" + revenuFullComplex.getSimpleRevenuSourcier().getDeductionFraisObtention();
            values[11] = "-" + revenuFullComplex.getSimpleRevenuSourcier().getDeductionDoubleGain();

            sommeRevenuImposableCalcul = getSommeInt(values);
            revenuFullComplex.getSimpleRevenuSourcier().setRevenuImposable(sommeRevenuImposableCalcul);

            return revenuFullComplex;
        } catch (ParametreAnnuelException pae) {
            return null;
        }
    }

    /**
     * Calcul de la déduction contribuable avec enfant à charge
     *
     * @param revenuHistoriqueComplex
     * @return
     * @throws ParametreAnnuelException
     */
    public final String getDeductionContribAvecEnfantChargeCalcul(RevenuHistoriqueComplex revenuHistoriqueComplex)
            throws ParametreAnnuelException {
        // Si on a une valeur dans "Personnes a charge ou enfants"
        // ou
        // si sourcier et nbEnfant > 0
        // alors on calcul la déduction
        if (!JadeStringUtil.isBlankOrZero(
                revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable().getPersChargeEnf())
                || (IAMCodeSysteme.CS_TYPE_SOURCIER
                        .equals(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getTypeRevenu())
                        && !JadeStringUtil.isBlankOrZero(
                                revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getNbEnfants()))) {
            sommeDeductionContribAvecEnfantChargeCalcul = this.getParametreAnnuel(
                    IAMParametresAnnuels.CS_MONTANT_AVEC_ENFANT_CHARGE,
                    revenuHistoriqueComplex.getSimpleRevenuHistorique().getAnneeHistorique());
        } else {
            sommeDeductionContribAvecEnfantChargeCalcul = "0";
        }

        return sommeDeductionContribAvecEnfantChargeCalcul;
    }

    /**
     * Calcul de la déduction contribuable non célibataire sans enfant à charge
     *
     * @param revenuHistoriqueComplex
     * @return
     * @throws ParametreAnnuelException
     */
    public final String getDeductionContribNonCelibSansEnfantChargeCalcul(
            RevenuHistoriqueComplex revenuHistoriqueComplex) throws ParametreAnnuelException {

        if (revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getTypeRevenu()
                .equals(IAMCodeSysteme.CS_TYPE_CONTRIBUABLE)) {
            // ---------------------------------------------
            // Contribuable, check chiffre 620 et état civil
            // ---------------------------------------------
            if (!IAMCodeSysteme.CS_ETAT_CIVIL_CELIBATAIRE
                    .equals(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getEtatCivil())
                    && JadeStringUtil.isBlankOrZero(revenuHistoriqueComplex.getRevenuFullComplex()
                            .getSimpleRevenuContribuable().getPersChargeEnf())) {
                sommeDeductionContribNonCelibSansEnfantChargeCalcul = this.getParametreAnnuel(
                        IAMParametresAnnuels.CS_MONTANT_SANS_ENFANT_CHARGE,
                        revenuHistoriqueComplex.getSimpleRevenuHistorique().getAnneeHistorique());
            } else {
                sommeDeductionContribNonCelibSansEnfantChargeCalcul = "0";
            }
        } else if (revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getTypeRevenu()
                .equals(IAMCodeSysteme.CS_TYPE_SOURCIER)) {
            // ---------------------------------------------
            // Sourcier, check info annoncée et état civil
            // ---------------------------------------------
            if (!IAMCodeSysteme.CS_ETAT_CIVIL_CELIBATAIRE
                    .equals(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getEtatCivil())
                    && JadeStringUtil.isBlankOrZero(
                            revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getNbEnfants())) {
                sommeDeductionContribNonCelibSansEnfantChargeCalcul = this.getParametreAnnuel(
                        IAMParametresAnnuels.CS_MONTANT_SANS_ENFANT_CHARGE,
                        revenuHistoriqueComplex.getSimpleRevenuHistorique().getAnneeHistorique());
            } else {
                sommeDeductionContribNonCelibSansEnfantChargeCalcul = "0";
            }

        } else {
            sommeDeductionContribNonCelibSansEnfantChargeCalcul = "0";
        }

        return sommeDeductionContribNonCelibSansEnfantChargeCalcul;
    }

    /**
     * Calcul de la déduction selon le nombre d'enfant
     *
     * @param revenuHistoriqueComplex
     * @return
     * @throws ParametreAnnuelException
     */
    public final String getDeductionSelonNbreEnfantCalcul(RevenuHistoriqueComplex revenuHistoriqueComplex)
            throws ParametreAnnuelException {

        boolean bNbEnfantsNeedTreatment = false;

        if (revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getTypeRevenu()
                .equals(IAMCodeSysteme.CS_TYPE_CONTRIBUABLE)) {
            // ---------------------------------------------
            // Contribuable, check chiffre 620
            // ---------------------------------------------
            if (!JadeStringUtil.isBlankOrZero(
                    revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable().getPersChargeEnf())) {
                bNbEnfantsNeedTreatment = true;
            } else {
                bNbEnfantsNeedTreatment = false;
            }
        } else if (revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getTypeRevenu()
                .equals(IAMCodeSysteme.CS_TYPE_SOURCIER)) {
            // ---------------------------------------------
            // Sourcier, check info annoncée
            // ---------------------------------------------
            if (!JadeStringUtil
                    .isBlankOrZero(revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getNbEnfants())) {
                bNbEnfantsNeedTreatment = true;
            } else {
                bNbEnfantsNeedTreatment = false;
            }
        } else {
            bNbEnfantsNeedTreatment = false;
        }

        if (bNbEnfantsNeedTreatment) {
            String montantSmallerThree = this.getParametreAnnuel(
                    IAMParametresAnnuels.CS_MONTANT_NOMBRE_ENFANT_PLUS_PETIT_3,
                    revenuHistoriqueComplex.getSimpleRevenuHistorique().getAnneeHistorique());
            String montantGreaterEqualThree = this.getParametreAnnuel(
                    IAMParametresAnnuels.CS_MONTANT_NOMBRE_ENFANT_PLUS_GRAND_EGAL_3,
                    revenuHistoriqueComplex.getSimpleRevenuHistorique().getAnneeHistorique());

            int nbEnfantsInt = 0;
            try {
                nbEnfantsInt = Integer.parseInt(this.dequoteAndFormat(
                        revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getNbEnfants()));
            } catch (NumberFormatException nfe) {
                nbEnfantsInt = 0;
            }

            int nbEnfantsSuspensInt = 0;
            try {
                nbEnfantsSuspensInt = Integer.parseInt(this.dequoteAndFormat(
                        revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getNbEnfantSuspens()));
            } catch (NumberFormatException nfe) {
                nbEnfantsSuspensInt = 0;
            }

            // Si pas d'enfants en suspens
            if (nbEnfantsSuspensInt == 0) {
                // Si moins de 3, nbEnfants * DeductionEnfants<3
                if (nbEnfantsInt < 3) {
                    int deductionEnfants = Integer.parseInt(this.dequoteAndFormat(montantSmallerThree));
                    sommeDeductionSelonNbreEnfantCalcul = String.valueOf(nbEnfantsInt * deductionEnfants);
                } else {
                    // Si plus de 3, nbEnfants * DeductionEnfants<3 pour les 2 premiers, puis on ajoute le reste av ec
                    // nbEnfants*DeductionEnfant>3
                    int deduction2Enfants = Integer.parseInt(this.dequoteAndFormat(montantSmallerThree)) * 2;
                    nbEnfantsInt = nbEnfantsInt - 2;
                    int sommeRestant = nbEnfantsInt * Integer.parseInt(this.dequoteAndFormat(montantGreaterEqualThree));
                    sommeDeductionSelonNbreEnfantCalcul = String.valueOf(deduction2Enfants + sommeRestant);
                }
            } else {
                int deductionEnfantsSmallerThree = Integer.parseInt(this.dequoteAndFormat(montantSmallerThree));
                int deductionEnfantsGreaterEqualThree = Integer
                        .parseInt(this.dequoteAndFormat(montantGreaterEqualThree));
                int deductionDemiEnfantsSmallerThree = Integer.parseInt(this.dequoteAndFormat(montantSmallerThree)) / 2;
                int deductionDemiEnfantsGreaterEqualThree = Integer
                        .parseInt(this.dequoteAndFormat(montantGreaterEqualThree)) / 2;
                // Si il n'y a que des enfants en suspens
                if (nbEnfantsInt == 0) {
                    // Si moins de 3 ==> Standard
                    if (nbEnfantsSuspensInt < 3) {
                        sommeDeductionSelonNbreEnfantCalcul = String
                                .valueOf(nbEnfantsSuspensInt * deductionDemiEnfantsSmallerThree);
                    } else {
                        // Sinon, calcul différents entre les 2 premiers et les suivants
                        int deductions2DemiEnfants = 2 * deductionDemiEnfantsSmallerThree;
                        nbEnfantsSuspensInt = nbEnfantsSuspensInt - 2;
                        int sommeRestant = nbEnfantsSuspensInt * deductionDemiEnfantsGreaterEqualThree;
                        sommeDeductionSelonNbreEnfantCalcul = String.valueOf(deductions2DemiEnfants + sommeRestant);
                    }
                } else {
                    // Si Enfants 100% ET Enfants en suspens
                    // On fait le total
                    int sommeTotalEnfants = nbEnfantsInt + nbEnfantsSuspensInt;
                    // Si 1 enfant 100% et 1 enfant 50%
                    if (sommeTotalEnfants < 3) {
                        // Si on vient ici, c'est qu'on a au moins 1 enfant en suspens ET 1 enfant à 100%
                        // Le calcul se fera donc, soit par x1, soit par x0
                        int sommeDeductionsSelonNbreEnfantEntier = nbEnfantsInt * deductionEnfantsSmallerThree;
                        int sommeDeductionsSelonNbreEnfantDemi = nbEnfantsSuspensInt * deductionDemiEnfantsSmallerThree;

                        sommeDeductionSelonNbreEnfantCalcul = String
                                .valueOf(sommeDeductionsSelonNbreEnfantEntier + sommeDeductionsSelonNbreEnfantDemi);
                    } else {
                        int sommeDeductionsSelonNbreEnfantEntier = 0;
                        int sommeDeductionsSelonNbreEnfantDemi = 0;
                        if (nbEnfantsInt == 2) {
                            sommeDeductionsSelonNbreEnfantEntier = 2 * deductionEnfantsSmallerThree;
                            sommeDeductionsSelonNbreEnfantDemi = nbEnfantsSuspensInt
                                    * deductionDemiEnfantsGreaterEqualThree;
                            sommeDeductionSelonNbreEnfantCalcul = String
                                    .valueOf(sommeDeductionsSelonNbreEnfantEntier + sommeDeductionsSelonNbreEnfantDemi);
                        } else if (nbEnfantsInt == 1) {
                            // On calcul pour 1 enfant 100%
                            sommeDeductionsSelonNbreEnfantEntier = deductionEnfantsSmallerThree;
                            // on ajout 1 enfant 50%
                            sommeDeductionsSelonNbreEnfantEntier += deductionDemiEnfantsSmallerThree;
                            // On déduit 1 du total des enfants 50%
                            nbEnfantsSuspensInt = nbEnfantsSuspensInt - 1;
                            // On calcul le montant pour le reste des enfants a 50%
                            sommeDeductionsSelonNbreEnfantDemi = nbEnfantsSuspensInt
                                    * deductionDemiEnfantsGreaterEqualThree;
                            // et on fait le total
                            sommeDeductionSelonNbreEnfantCalcul = String
                                    .valueOf(sommeDeductionsSelonNbreEnfantEntier + sommeDeductionsSelonNbreEnfantDemi);
                        } else {
                            // X enfants 100% et X enfant 50%
                            // On calcul pour les 2 premiier enfant 100%
                            sommeDeductionsSelonNbreEnfantEntier = 2 * deductionEnfantsSmallerThree;
                            nbEnfantsInt = nbEnfantsInt - 2;
                            sommeDeductionsSelonNbreEnfantEntier += nbEnfantsInt * deductionEnfantsGreaterEqualThree;
                            sommeDeductionsSelonNbreEnfantDemi = nbEnfantsSuspensInt
                                    * deductionDemiEnfantsGreaterEqualThree;
                            // et on fait le total
                            sommeDeductionSelonNbreEnfantCalcul = String
                                    .valueOf(sommeDeductionsSelonNbreEnfantEntier + sommeDeductionsSelonNbreEnfantDemi);
                        }
                    }
                }
            }
            return sommeDeductionSelonNbreEnfantCalcul;
        } else {
            sommeDeductionSelonNbreEnfantCalcul = "0";
            return "0";
        }
    }

    /**
     * Calcul du pourcentage de la fortune imposable à prendre en compte
     *
     * @param revenuHistoriqueComplex
     * @return
     * @throws ParametreAnnuelException
     * @throws NumberFormatException
     */
    public final String getFortuneImposablePercentCalcul(RevenuHistoriqueComplex revenuHistoriqueComplex)
            throws NumberFormatException, ParametreAnnuelException {
        if (revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().isSourcier()) {
            return "0";
        }

        String fortuneImposableCalcul = this.dequoteAndFormat(
                revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable().getFortuneImposable());

        if (!JadeStringUtil.isBlankOrZero(
                revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable().getFortuneTaux())) {
            fortuneImposableCalcul = this.dequoteAndFormat(
                    revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable().getFortuneTaux());
        }

        if (Integer.parseInt(fortuneImposableCalcul) > 1000) {
            fortuneImposableCalcul = JANumberFormatter.round(this.dequoteAndFormat(fortuneImposableCalcul), 1000, 0,
                    JANumberFormatter.INF);
        }

        if (JadeStringUtil.isBlankOrZero(fortuneImposableCalcul)) {
            fortuneImposableCalcul = "0";
        }

        int fortuneImposableCalculInt = Integer.parseInt(fortuneImposableCalcul);

        PERCENT_FORTUNE_IMPOSABLE = Integer
                .parseInt(this.getParametreAnnuel(IAMParametresAnnuels.CS_TAUX_CALCUL_FORTUNE_IMPOSABLE,
                        revenuHistoriqueComplex.getSimpleRevenuHistorique().getAnneeHistorique()));

        fortuneImposableCalculInt = (fortuneImposableCalculInt / 100) * PERCENT_FORTUNE_IMPOSABLE;

        sommeFortuneImposablePercentCalcul = String.valueOf(fortuneImposableCalculInt);

        return sommeFortuneImposablePercentCalcul;
    }

    /**
     * Récupération du nombre d'enfant entre 16 et 25 ans
     *
     * @param revenuFullComplex
     * @return
     * @throws RevenuException
     */
    private int getNbChildrenBetween16And25(RevenuFullComplex revenuFullComplex) throws RevenuException {
        try {
            SimpleFamilleSearch simpleFamilleSearch = new SimpleFamilleSearch();
            simpleFamilleSearch.setForIdContribuable(revenuFullComplex.getSimpleRevenu().getIdContribuable());
            // TODO Voir si il faut prendre année taxation ou année historique
            int annee = Integer.parseInt(revenuFullComplex.getSimpleRevenu().getAnneeTaxation());
            int annee25 = annee - 25;
            int annee16 = annee - 16;
            simpleFamilleSearch.setForDateNaissanceGOE("01.01." + String.valueOf(annee25));
            simpleFamilleSearch.setForDateNaissanceLOE("31.12." + String.valueOf(annee16));
            simpleFamilleSearch.setForPereMereEnfant(IAMCodeSysteme.CS_TYPE_ENFANT);
            simpleFamilleSearch.setForFinDefinitiveGOE("12." + annee);
            simpleFamilleSearch.setForFinDefinitive("0");
            // le where calcul récupère aussi tout les "findefinitif" à NULL
            simpleFamilleSearch.setWhereKey("subsides");
            return AmalServiceLocator.getFamilleContribuableService().count(simpleFamilleSearch);
        } catch (Exception e) {
            throw new RevenuException("Error while searching simpleFamille ! ID : "
                    + revenuFullComplex.getSimpleRevenu().getIdContribuable());
        }
    }

    /**
     * Récupération du nombre d'enfant entre 0 et 16 ans
     *
     * @param revenuFullComplex
     * @return
     * @throws RevenuException
     */
    private int getNbChildrenUnder16(RevenuFullComplex revenuFullComplex) throws RevenuException {
        try {
            SimpleFamilleSearch simpleFamilleSearch = new SimpleFamilleSearch();
            simpleFamilleSearch.setForIdContribuable(revenuFullComplex.getSimpleRevenu().getIdContribuable());
            // TODO Voir si il faut prendre année taxation ou année historique
            int annee = Integer.parseInt(revenuFullComplex.getSimpleRevenu().getAnneeTaxation());
            int annee16 = annee - 15;
            simpleFamilleSearch.setForDateNaissanceGOE("01.01." + String.valueOf(annee16));
            simpleFamilleSearch.setForPereMereEnfant(IAMCodeSysteme.CS_TYPE_ENFANT);
            simpleFamilleSearch.setForFinDefinitiveGOE("12." + annee);
            simpleFamilleSearch.setForFinDefinitive("0");
            // le where calcul récupère aussi tout les "findefinitif" à NULL
            simpleFamilleSearch.setWhereKey("subsides");
            return AmalServiceLocator.getFamilleContribuableService().count(simpleFamilleSearch);
        } catch (Exception e) {
            throw new RevenuException("Error while searching simpleFamille ! ID : "
                    + revenuFullComplex.getSimpleRevenu().getIdContribuable());
        }
    }

    private String getParametreAnnuel(String type, String year) throws ParametreAnnuelException {
        return this.getParametreAnnuel(type, year, 0);
    }

    private String getParametreAnnuel(String type, String year, int nDecimals) throws ParametreAnnuelException {
        return this.getParametreAnnuel(type, year, null, nDecimals);
    }

    private String getParametreAnnuel(String type, String year, String defaultValue, int nDecimals)
            throws ParametreAnnuelException {
        return ParametresAnnuelsProvider.containerParametres.getParametresAnnuelsProvider().getListeParametresAnnuels()
                .get(type).getFormatedValueByYear(year, defaultValue, nDecimals);
    }

    /**
     * Récupération de la valeur du revenu imposable
     *
     * @param revenuHistoriqueComplex
     * @return
     */
    public final String getRevenuImposableCalcul(RevenuHistoriqueComplex revenuHistoriqueComplex) {
        String revenuImposableCalcul = "0";

        if (!revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().isSourcier()) {
            revenuImposableCalcul = this.dequoteAndFormat(
                    revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuImposable());

            if (!JadeStringUtil.isBlankOrZero(
                    revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuTaux())) {
                revenuImposableCalcul = this.dequoteAndFormat(
                        revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable().getRevenuTaux());
            }
        } else {
            revenuImposableCalcul = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuSourcier()
                    .getRevenuImposable();
            if (revenuImposableCalcul.indexOf('.') >= 0) {
                revenuImposableCalcul = revenuImposableCalcul.substring(0, revenuImposableCalcul.indexOf('.'));
            }
        }

        if (JadeStringUtil.isBlankOrZero(revenuImposableCalcul)) {
            revenuImposableCalcul = "0";
        }

        sommeRevenuImposableCalcul = String.valueOf((Integer.parseInt(revenuImposableCalcul) / 100) * 100);

        return sommeRevenuImposableCalcul;
    }

    /**
     * Get somme 310, 330, 330c
     *
     * @param revenuFullComplex
     *
     * @return
     */
    public final String getSommeExcDepPropImmo(RevenuHistoriqueComplex revenuHistoriqueComplex) {
        if (!revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().isSourcier()) {
            String[] values = new String[2];
            values[0] = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                    .getExcedDepPropImmoPriv();
            values[1] = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                    .getExcedDepPropImmoComm();

            sommeExcDepPropImmo = getSommeInt(values);
        } else {
            sommeExcDepPropImmo = "0";
        }

        return sommeExcDepPropImmo;
    }

    /**
     * Calcul la somme de 1 à n string (int)
     *
     * @return la somme recherchée
     */
    private String getSommeInt(String[] values) {

        int iTotal = 0;
        for (int iIndex = 0; iIndex < values.length; iIndex++) {
            try {
                if ((!"-null".equals(values[iIndex])) && !JadeStringUtil.isBlankOrZero((values[iIndex]))) {
                    values[iIndex] = this.dequoteAndFormat(values[iIndex]);

                    iTotal += JadeStringUtil.toInt(values[iIndex]);
                }
            } catch (Exception e) {
                JadeLogger.error(this, "Exception in getSommeInt, input " + values[iIndex] + " - " + e.getMessage());
            }
        }

        return String.format("%d", iTotal);
    }

    /**
     * Get somme 530, 535
     *
     * @return
     */
    public final String getSommeIntPassifs(RevenuHistoriqueComplex revenuHistoriqueComplex) {
        if (!revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().isSourcier()) {
            String[] values = new String[2];
            values[0] = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                    .getInteretsPassifsPrive();
            values[1] = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                    .getInteretsPassifsComm();
            sommeIntPassifs = getSommeInt(values);
        } else {
            sommeIntPassifs = "0";
        }

        return sommeIntPassifs;
    }

    /**
     * Get somme 300, 320, 320c - 530,535,310,330,330c,390
     *
     * @return
     */
    public final String getSommePartRendementImmob(RevenuHistoriqueComplex revenuHistoriqueComplex) {

        String[] values = new String[4];
        values[0] = getSommeRendementFortuneImmobiliere(revenuHistoriqueComplex);
        values[1] = "-" + getSommeIntPassifs(revenuHistoriqueComplex);
        values[2] = "-" + getSommeExcDepPropImmo(revenuHistoriqueComplex);
        if (!revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().isSourcier()) {
            values[3] = "-"
                    + revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable().getExcDepSuccNp();
        }
        String csReturn = getSommeInt(values);
        if (csReturn.contains("-")) {
            csReturn = "0";
        }

        sommePartRendementImmob = csReturn;

        return sommePartRendementImmob;

    }

    /**
     * Get somme 140,140c,150,150c,160,160c,170,170c
     *
     * @param anneeHistorique
     *
     * @return
     */
    public final String getSommePertesExCommerciaux(RevenuHistoriqueComplex revenuHistoriqueComplex) {
        if (!revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().isSourcier()) {
            if (Integer.parseInt(revenuHistoriqueComplex.getSimpleRevenuHistorique().getAnneeHistorique()) <= 2005) {
                sommePertesExCommerciaux = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                        .getPerteExercicesComm();
            } else {
                String[] values = new String[4];
                values[0] = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                        .getPerteActIndep();
                values[1] = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                        .getPerteActAgricole();
                values[2] = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                        .getPerteSociete();
                values[3] = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                        .getPerteActAccInd();
                sommePertesExCommerciaux = getSommeInt(values);
            }
        } else {
            sommePertesExCommerciaux = "0";
        }

        return sommePertesExCommerciaux;
    }

    /**
     * Get somme 300, 320, 320c
     *
     * @return
     */
    public final String getSommeRendementFortuneImmobiliere(RevenuHistoriqueComplex revenuHistoriqueComplex) {
        if (!revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().isSourcier()) {

            String[] values = new String[2];
            values[0] = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                    .getRendFortImmobPrive();
            values[1] = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                    .getRendFortImmobComm();

            sommeRendementFortuneImmobiliere = getSommeInt(values);
        } else {
            sommeRendementFortuneImmobiliere = "0";
        }

        return sommeRendementFortuneImmobiliere;
    }

    /**
     * Get somme 680
     *
     * @return
     */
    public String getSommeDeductionCouplesMaries(RevenuHistoriqueComplex revenuHistoriqueComplex) {
        if (!revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().isSourcier()) {

            String[] values = new String[1];
            values[0] = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenuContribuable()
                    .getDeductionCouplesMaries();

            sommeDeductionCouplesMaries = getSommeInt(values);
        } else {
            sommeDeductionCouplesMaries = "0";
        }

        return sommeDeductionCouplesMaries;
    }

    /**
     * Calcul du revenu déterminant
     *
     * @return
     */
    public final String getTotalRevenuDeterminant() {
        String[] values = new String[14];
        values[0] = sommeRevenuImposableCalcul;
        values[1] = "-" + sommeRendementFortuneImmobiliere;
        values[2] = sommeExcDepPropImmo;
        values[3] = sommeExcedentDepSuccNonPart;
        values[4] = sommeIntPassifs;
        values[5] = sommePertesExCommerciaux;
        values[6] = sommePerteReporteeExCommerciaux;
        values[7] = sommePerteLiquidation;
        values[8] = sommePartRendementImmob;
        values[9] = sommeDeductionCouplesMaries;
        values[10] = "-" + sommeDeductionContribNonCelibSansEnfantChargeCalcul;
        values[11] = "-" + sommeDeductionContribAvecEnfantChargeCalcul;
        values[12] = "-" + sommeDeductionSelonNbreEnfantCalcul;
        values[13] = sommeFortuneImposablePercentCalcul;

        totalRevenuDeterminant = getSommeInt(values);

        return totalRevenuDeterminant;

    }

    /**
     * Groupe le nombre d'enfant
     *
     * @param nbEnfants
     * @param nbEnfantsSuspens
     * @return
     */
    public String groupChildrens(RevenuHistoriqueComplex revenuHistoriqueComplex) {
        String nbEnfants = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getNbEnfants();
        String nbEnfantsSuspens = revenuHistoriqueComplex.getRevenuFullComplex().getSimpleRevenu().getNbEnfantSuspens();
        if (nbEnfants == null) {
            nbEnfants = "0";
        }
        if (nbEnfantsSuspens == null) {
            nbEnfantsSuspens = "0";
        }
        Double nbEnfants_double = Double.parseDouble(nbEnfants);
        Double nbEnfantsSuspens_double = Double.parseDouble(nbEnfantsSuspens);
        return String.valueOf(nbEnfants_double + (nbEnfantsSuspens_double / 2));
    }
}
