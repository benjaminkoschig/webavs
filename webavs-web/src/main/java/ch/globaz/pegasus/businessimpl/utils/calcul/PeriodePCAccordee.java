/**
 *
 */
package ch.globaz.pegasus.businessimpl.utils.calcul;

import ch.globaz.pegasus.business.constantes.*;
import ch.globaz.pegasus.business.constantes.donneesfinancieres.IPCRenteAvsAi;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculBusinessException;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesCC;
import ch.globaz.pegasus.business.models.calcul.CalculDonneesHome;
import ch.globaz.pegasus.business.models.droit.DonneesPersonnelles;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitMembreFamille;
import ch.globaz.pegasus.business.models.droit.SimpleDroitMembreFamille;
import ch.globaz.pegasus.business.models.parametre.SimpleForfaitPrimesAssuranceMaladie;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.calcul.CalculContext.Attribut;
import ch.globaz.pegasus.businessimpl.utils.calcul.ForfaitPrimeMoyenneAssuranceMaladie.TypesForfait;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurMonnaieEtrangere;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.ControlleurVariablesMetier;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategieCalcul;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategie.StrategiesFactory;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.*;
import ch.globaz.pegasus.businessimpl.utils.calcul.strategiesFinalisation.fortune.strategiesFinalFortuneImmobiliere.UtilFortune;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * @author ECO
 *
 */
public class PeriodePCAccordee implements Serializable, IPeriodePCAccordee {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Pourcentage d'abattement utiliser pour calcul la pc minimal à partir de la prime moyenne
     */
    private static final float POURCENTAGE_PRIME_MOY_PC_MINIMAL = 0.6f;


    private enum TypeCalculCC {
        CALCUL_CC_NON_SEPARE,
        CALCUL_CC_SEPARE_AVEC_ENFANTS,
        CALCUL_CC_SEPARE_COMMUN,
        CALCUL_CC_SEPARE_SEUL
    }

    public enum TypeSeparationCC {
        CALCUL_DOM2_PRINCIPALE(IPCValeursPlanCalcul.STATUS_CALCUL_DOM2_PRINCIPALE),
        CALCUL_SANS_SEPARATION(IPCValeursPlanCalcul.STATUS_CALCUL_SANS_SEPARATION),
        CALCUL_SEPARE_MALADIE(IPCValeursPlanCalcul.STATUS_CALCUL_SEPARE_MALADIE);

        private String val;

        TypeSeparationCC(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }

    private static final int AGE_ADULTE = 26;

    private static final int AGE_JEUNE_ADULTE = 19;

    private final static StrategiesFactory[] factories = {StrategiesFactory.getDepenseFactory(),
            StrategiesFactory.getFortuneFactory(), StrategiesFactory.getRevenuFactory()};

    private final static StrategiesFactory[] conjointFactories = {StrategiesFactory.getDepenseFactory(),
            StrategiesFactory.getFortuneFactory(), StrategiesFactory.getRevenuConjointFacotry()};

    private final static StrategiesFactory[] enfantFactories = {StrategiesFactory.getDepenseFactory(),
            StrategiesFactory.getFortuneFactory(), StrategiesFactory.getRevenuEnfantFacotry()};

    private final static Map<String, Attribut> mappageLegendesVarMetFinalisation = new HashMap<String, Attribut>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            put(IPCVariableMetier.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE,
                    Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_LEGENDE);
            put(IPCVariableMetier.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME,
                    Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME_LEGENDE);
            put(IPCVariableMetier.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON,
                    Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON_LEGENDE);
            put(IPCVariableMetier.CS_FRACTIONS_FORTUNE_VIEILLESSE_ENFANT,
                    Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_ENFANT_LEGENDE);
            put(IPCVariableMetier.CS_FRACTIONS_FORTUNE_VIEILLESSE_HOME,
                    Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_HOME_LEGENDE);
            put(IPCVariableMetier.CS_FRACTIONS_FORTUNE_VIEILLESSE_MAISON,
                    Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_MAISON_LEGENDE);
            put(IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_SEPARE_VIEILLESSE_HOME,
                    Attribut.CS_REFORME_FRACTIONS_FORTUNE_SEPARE_VIEILLESSE_HOME);
            put(IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_SEPARE_NON_VIEILLESSE_HOME,
                    Attribut.CS_REFORME_FRACTIONS_FORTUNE_SEPARE_NON_VIEILLESSE_HOME);
            put(IPCVariableMetier.CS_FRACTION_REVENUS_PRIVILEGIES, Attribut.CS_FRACTION_REVENUS_PRIVILEGIES_LEGENDE);
            put(IPCVariableMetier.CS_PLAFOND_ANNUEL_EMS, Attribut.CS_PLAFOND_ANNUEL_EMS);
            put(IPCVariableMetier.CS_PLAFOND_ANNUEL_INSTITUTION, Attribut.CS_PLAFOND_ANNUEL_INSTITUTION);
            put(IPCVariableMetier.CS_PLAFOND_ANNUEL_LITS_ATTENTE, Attribut.CS_PLAFOND_ANNUEL_LITS_ATTENTE);

            ;
            put(IPCVariableMetier.CS_MONTANT_TYPE_CHAMBRE_EPS, Attribut.MONTANT_TYPE_CHAMBRE_EPS);
            put(IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_HOME, Attribut.CS_REFORME_FRACTIONS_FORTUNE_HOME_LEGENDE);
            put(IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_RESIDENT, Attribut.CS_REFORME_FRACTIONS_FORTUNE_RESIDENT_LEGENDE);
            put(IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_MOITIE, Attribut.CS_REFORME_FRACTIONS_FORTUNE_MOITIE_LEGENDE);
            put(IPCVariableMetier.CS_REFORME_TAUX_REVENUS_NON_RENTIER, Attribut.CS_REFORME_TAUX_REVENUS_NON_RENTIER_LEGENDE);
            put(IPCVariableMetier.CS_REFORME_TAUX_REVENUS_IJAI, Attribut.CS_REFORME_TAUX_REVENUS_IJAI_LEGENDE);
            put(IPCVariableMetier.CS_REFORME_FRACTION_REVENUS_PRIVILEGIES_ENFANT, Attribut.CS_REFORME_FRACTION_REVENUS_PRIVILEGIES_ENFANT_LEGENDE);
        }
    };

    private final static Map<String, Attribut> mappageVarMet = new HashMap<String, Attribut>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            put(IPCVariableMetier.CS_2091_DPC, Attribut.CS_2091_DPC);
            put(IPCVariableMetier.CS_FORFAIT_FRAIS_CHAUFFAGE, Attribut.CS_FORFAIT_FRAIS_CHAUFFAGE);
            put(IPCVariableMetier.CS_FORFAIT_CHARGES, Attribut.CS_FORFAIT_CHARGES);
            put(IPCVariableMetier.CS_TAUX_PENSION_NON_RECONNUE, Attribut.TAUX_PENSION_NON_RECONNUE);
            put(IPCVariableMetier.MENSUALISATION_IJ_CHOMAGE, Attribut.MENSUALISATION_IJ_CHOMAGE);
            put(IPCVariableMetier.CS_TAUX_IMPUTATION_LOYER_FRAIS_ACQUISITION,
                    Attribut.CS_TAUX_IMPUTATION_LOYER_FRAIS_ACQUISITION);
            put(IPCVariableMetier.CS_TAUX_IMPUTATION_SOUSLOCATIONS_FRAIS_ACUISITION,
                    Attribut.CS_TAUX_IMPUTATION_SOUSLOCATIONS_FRAIS_ACUISITION);
            put(IPCVariableMetier.CS_PLAFOND_ANNUEL_EMS, Attribut.CS_PLAFOND_ANNUEL_EMS);
            put(IPCVariableMetier.CS_PLAFOND_ANNUEL_INSTITUTION, Attribut.CS_PLAFOND_ANNUEL_INSTITUTION);
            put(IPCVariableMetier.CS_PLAFOND_ANNUEL_LITS_ATTENTE, Attribut.CS_PLAFOND_ANNUEL_LITS_ATTENTE);
            put(IPCVariableMetier.CS_FRACTIONS_FRAIS_ENTRETIEN_IMMEUBLE, Attribut.FRAIS_ENTRETIEN_IMMEUBLE);
            put(IPCVariableMetier.CS_FRACTIONS_FRAIS_ENTRETIEN_IMMEUBLE_MOINS_10_ANS,
                    Attribut.FRAIS_ENTRETIEN_IMMEUBLE_MOINS_10_ANS);
            put(IPCVariableMetier.CS_MONTANT_TYPE_CHAMBRE_EPS, Attribut.MONTANT_TYPE_CHAMBRE_EPS);
            put(IPCVariableMetier.TAUX_IMPUTATIONS_VALEUR_LOCATIVE_BRUT,
                    Attribut.TAUX_BIEN_IMMO_FRACTION_VALEUR_LOCATIVE_BRUTE);
            put(IPCVariableMetier.TAUX_IMPUTATIONS_VALEUR_LOCATIVE_BRUT_M10,
                    Attribut.TAUX_BIEN_IMMO_FRACTION_VALEUR_LOCATIVE_BRUTE_M10);
            put(IPCVariableMetier.TAUX_IMPUTATIONS_LOYER_EFFECTIF, Attribut.TAUX_BIEN_IMMO_FRACTION_LOYER_EFFECTIF);
            put(IPCVariableMetier.CS_REFORME_SEUIL_FORTUNE_SEUL, Attribut.CS_REFORME_SEUIL_FORTUNE_SEUL);
            put(IPCVariableMetier.CS_REFORME_SEUIL_FORTUNE_COUPLE, Attribut.CS_REFORME_SEUIL_FORTUNE_COUPLE);
            put(IPCVariableMetier.CS_REFORME_SEUIL_FORTUNE_ENFANT, Attribut.CS_REFORME_SEUIL_FORTUNE_ENFANT);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_1, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_1);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_2, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_2);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_3, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_3);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_4, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_4);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_5, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_5);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_2, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_2);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_4, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_4);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_5, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_5);
        }
    };
    private final static Map<String, Attribut> mappageVarMetFinalisation = new HashMap<String, Attribut>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            put(IPCVariableMetier.CS_2091_DPC, Attribut.CS_2091_DPC);
            put(IPCVariableMetier.CS_DEDUCTION_FORAITAIRE_IMMOBILIER_ASSURE,
                    Attribut.CS_DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE);
            put(IPCVariableMetier.CS_DEDUCTION_FORAITAIRE_FORTUNE_CELIBATAIRES,
                    Attribut.CS_DEDUCTION_FORTUNE_CELIBATAIRE);
            put(IPCVariableMetier.CS_DEDUCTION_FORAITAIRE_FORTUNE_COUPLES, Attribut.CS_DEDUCTION_FORTUNE_COUPLE);
            put(IPCVariableMetier.CS_DEDUCTION_FORAITAIRE_FORTUNE_ENFANTS, Attribut.CS_DEDUCTION_FORTUNE_ENFANT);
            put(IPCVariableMetier.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE, Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE);
            put(IPCVariableMetier.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME,
                    Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME);
            put(IPCVariableMetier.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON,
                    Attribut.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON);
            put(IPCVariableMetier.CS_FRACTIONS_FORTUNE_VIEILLESSE_ENFANT,
                    Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_ENFANT);
            put(IPCVariableMetier.CS_FRACTIONS_FORTUNE_VIEILLESSE_HOME, Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_HOME);
            put(IPCVariableMetier.CS_FRACTIONS_FORTUNE_VIEILLESSE_MAISON,
                    Attribut.CS_FRACTIONS_FORTUNE_VIEILLESSE_MAISON);
            put(IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_SEPARE_VIEILLESSE_HOME,
                    Attribut.CS_REFORME_FRACTIONS_FORTUNE_SEPARE_VIEILLESSE_HOME);
            put(IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_SEPARE_NON_VIEILLESSE_HOME,
                    Attribut.CS_REFORME_FRACTIONS_FORTUNE_SEPARE_NON_VIEILLESSE_HOME);
            put(IPCVariableMetier.CS_FRANCHISE_REVENUS_PRIVILEGIERS_CELIBATAIRES,
                    Attribut.CS_FRANCHISE_REVENUS_PRIVILEGIERS_CELIBATAIRES);
            put(IPCVariableMetier.CS_FRANCHISE_REVENUS_PRIVILEGIERS_FAMILLE,
                    Attribut.CS_FRANCHISE_REVENUS_PRIVILEGIERS_FAMILLE);
            put(IPCVariableMetier.CS_BESOINS_VITAUX_2_ENFANTS, Attribut.CS_BESOINS_VITAUX_2_ENFANTS);
            put(IPCVariableMetier.CS_CATEGORIE_ARGENT_POCHE_LVPC_ESE_HANDICAP_PHYSIQUE,
                    Attribut.CS_CATEGORIE_ARGENT_POCHE_LVPC_ESE_HANDICAP_PHYSIQUE);
            put(IPCVariableMetier.CS_CATEGORIE_ARGENT_POCHE_LVPC_EMS_NONMED_PSY,
                    Attribut.CS_CATEGORIE_ARGENT_POCHE_LVPC_EMS_NONMED_PSY);
            put(IPCVariableMetier.CS_CATEGORIE_ARGENT_POCHE_LVPC_EMS_NOMMED_AGE_AVANCE,
                    Attribut.CS_CATEGORIE_ARGENT_POCHE_LVPC_EMS_NOMMED_AGE_AVANCE);
            put(IPCVariableMetier.CS_BESOINS_VITAUX_4_ENFANTS, Attribut.CS_BESOINS_VITAUX_4_ENFANTS);
            put(IPCVariableMetier.CS_BESOINS_VITAUX_5_ENFANTS, Attribut.CS_BESOINS_VITAUX_5_ENFANTS);
            put(IPCVariableMetier.CS_BESOINS_VITAUX_CELIBATAIRES, Attribut.CS_BESOINS_VITAUX_CELIBATAIRES);
            put(IPCVariableMetier.CS_BESOINS_VITAUX_COUPLES, Attribut.CS_BESOINS_VITAUX_COUPLES);
            put(IPCVariableMetier.CS_ARGENT_POCHE_MEDICALISE, Attribut.CS_ARGENT_POCHE_MEDICALISE);
            put(IPCVariableMetier.CS_ARGENT_POCHE_NON_MEDICALISE, Attribut.CS_ARGENT_POCHE_NON_MEDICALISE);
            put(IPCVariableMetier.CS_ARGENT_POCHE_HOME_AVS_ANNUEL, Attribut.CS_ARGENT_POCHE_HOME_AVS_ANNUEL);
            put(IPCVariableMetier.CS_ARGENT_POCHE_HOME_AI_ANNUEL, Attribut.CS_ARGENT_POCHE_HOME_AI_ANNUEL);
            put(IPCVariableMetier.CS_FRACTION_REVENUS_PRIVILEGIES, Attribut.CS_FRACTION_REVENUS_PRIVILEGIES);
            put(IPCVariableMetier.CS_FORFAIT_CHARGES, Attribut.CS_FORFAIT_CHARGES);
            put(IPCVariableMetier.DEPENSE_LOYER_PLAFOND_COUPLE, Attribut.DEPENSE_LOYER_PLAFOND_COUPLE);
            put(IPCVariableMetier.DEPENSE_LOYER_PLAFOND_CELIBATAIRE, Attribut.DEPENSE_LOYER_PLAFOND_CELIBATAIRE);
            put(IPCVariableMetier.DEPENSE_LOYER_PLAFOND_FAUTEUIL_ROULANT,
                    Attribut.DEPENSE_LOYER_PLAFOND_FAUTEUIL_ROULANT);
            put(IPCVariableMetier.CS_REFORME_DEPENSE_LOYER_PLAFOND_FAUTEUIL_ROULANT,
                    Attribut.CS_REFORME_DEPENSE_LOYER_PLAFOND_FAUTEUIL_ROULANT);
            put(IPCVariableMetier.MONTANT_MINIMALE_PC, Attribut.MONTANT_MINIMALE_PC);
            put(IPCVariableMetier.CS_FORFAIT_REVENU_NATURE_TENUE_MENAGE, Attribut.CS_FORFAIT_REVENU_NATURE_TENUE_MENAGE);
            put(IPCVariableMetier.CS_DEDUCTION_FORAITAIRE_IMMOBILIER_ASSURE_HOME_API,
                    Attribut.CS_DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE_HOME_API);
            put(IPCVariableMetier.CS_AMORTISSEMENT_ANNUEL_DESSAISISSEMENT_FORTUNE,
                    Attribut.CS_AMORTISSEMENT_ANNUEL_DESSAISISSEMENT_FORTUNE);
            put(IPCVariableMetier.MENSUALISATION_IJ_CHOMAGE, Attribut.MENSUALISATION_IJ_CHOMAGE);
            put(IPCVariableMetier.CS_TAUX_IMPUTATION_SOUSLOCATIONS_FRAIS_ACUISITION,
                    Attribut.CS_TAUX_IMPUTATION_SOUSLOCATIONS_FRAIS_ACUISITION);
            put(IPCVariableMetier.CS_PLAFOND_ANNUEL_EMS, Attribut.CS_PLAFOND_ANNUEL_EMS);
            put(IPCVariableMetier.CS_FRACTIONS_FRAIS_ENTRETIEN_IMMEUBLE, Attribut.FRAIS_ENTRETIEN_IMMEUBLE);

            put(IPCVariableMetier.CS_FRACTIONS_FRAIS_ENTRETIEN_IMMEUBLE_MOINS_10_ANS,
                    Attribut.FRAIS_ENTRETIEN_IMMEUBLE_MOINS_10_ANS);
            put(IPCVariableMetier.CS_MONTANT_TYPE_CHAMBRE_EPS, Attribut.MONTANT_TYPE_CHAMBRE_EPS);

            put(IPCVariableMetier.TAUX_IMPUTATIONS_VALEUR_LOCATIVE_BRUT,
                    Attribut.TAUX_BIEN_IMMO_FRACTION_VALEUR_LOCATIVE_BRUTE);
            put(IPCVariableMetier.TAUX_IMPUTATIONS_VALEUR_LOCATIVE_BRUT_M10,
                    Attribut.TAUX_BIEN_IMMO_FRACTION_VALEUR_LOCATIVE_BRUTE_M10);
            put(IPCVariableMetier.TAUX_IMPUTATIONS_LOYER_EFFECTIF, Attribut.TAUX_BIEN_IMMO_FRACTION_LOYER_EFFECTIF);
            put(IPCVariableMetier.CS_PLAFOND_ANNUEL_INSTITUTION, Attribut.CS_PLAFOND_ANNUEL_INSTITUTION);
            put(IPCVariableMetier.CS_PLAFOND_ANNUEL_LITS_ATTENTE, Attribut.CS_PLAFOND_ANNUEL_LITS_ATTENTE);
            put(IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_HOME, Attribut.CS_REFORME_FRACTIONS_FORTUNE_HOME);
            put(IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_RESIDENT, Attribut.CS_REFORME_FRACTIONS_FORTUNE_RESIDENT);
            put(IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_MOITIE, Attribut.CS_REFORME_FRACTIONS_FORTUNE_MOITIE);
            put(IPCVariableMetier.CS_REFORME_SEUIL_FORTUNE_SEUL, Attribut.CS_REFORME_SEUIL_FORTUNE_SEUL);
            put(IPCVariableMetier.CS_REFORME_SEUIL_FORTUNE_COUPLE, Attribut.CS_REFORME_SEUIL_FORTUNE_COUPLE);
            put(IPCVariableMetier.CS_REFORME_SEUIL_FORTUNE_ENFANT, Attribut.CS_REFORME_SEUIL_FORTUNE_ENFANT);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_1, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_1);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_2, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_2);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_3, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_3);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_4, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_4);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_5, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_5);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_2, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_2);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_4, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_4);
            put(IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_5, Attribut.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_5);
            put(IPCVariableMetier.CS_REFORME_TAUX_REVENUS_NON_RENTIER, Attribut.CS_REFORME_TAUX_REVENUS_NON_RENTIER);
            put(IPCVariableMetier.CS_REFORME_TAUX_REVENUS_IJAI, Attribut.CS_REFORME_TAUX_REVENUS_IJAI);
            put(IPCVariableMetier.CS_REFORME_FRACTION_REVENUS_PRIVILEGIES_ENFANT, Attribut.CS_REFORME_FRACTION_REVENUS_PRIVILEGIES_ENFANT);
        }
    };

    private final static StrategiesFactory[] mixteFactories = {StrategiesFactory.getRevenuMixteFactory()};

    final private List<CalculComparatif> calculsComparatifs = new ArrayList<>();

    final private List<CalculComparatif> calculsComparatifsConjoint = new ArrayList<>();

    final private List<CalculComparatif> calculsComparatifsReforme = new ArrayList<>();

    final private List<CalculComparatif> calculsComparatifsConjointReforme = new ArrayList<>();

    private boolean calculReforme = false;

    private boolean isFratrie = false;

    private String codeRente = null;

    final private transient Map<String, ControlleurMonnaieEtrangere> controlleurMonnaiesEtrangere = new HashMap<String, ControlleurMonnaieEtrangere>();

    final private transient Map<String, ControlleurVariablesMetier> controlleurVariablesMetier = new HashMap<String, ControlleurVariablesMetier>();

    private Date dateDebut = null;

    private Date dateFin = null;
    private String dateFinDemandeToClosePca = null;
    private final List<CalculDonneesHome> donneesHomes = new ArrayList<CalculDonneesHome>();
    final private transient Map<ForfaitPrimeMoyenneAssuranceMaladie.TypesForfait, ForfaitPrimeMoyenneAssuranceMaladie> forfaitsPrimeMaladie = new HashMap<ForfaitPrimeMoyenneAssuranceMaladie.TypesForfait, ForfaitPrimeMoyenneAssuranceMaladie>();
    private String idSimplePcAccordee = null;
    private String idSimplePcAccordeeConjoint = null;

    public String getIdSimplePcAccordeeConjoint() {
        return idSimplePcAccordeeConjoint;
    }

    public void setIdSimplePcAccordeeConjoint(String idSimplePcAccordeeConjoint) {
        this.idSimplePcAccordeeConjoint = idSimplePcAccordeeConjoint;
    }

    private boolean isCalculRetro = false;

    private boolean isHome = false;

    /**
     *
     */
    private SimpleJoursAppoint joursAppointConjoint = null;
    private SimpleJoursAppoint joursAppointRequerant = null;

    final private transient Map<String, String> legendesVariablesMetier = new HashMap<String, String>();
    private Map<String, Float> listeTauxMonnaies = new HashMap<String, Float>();

    private float montantJournalierJoursAppoint;

    private float montantJoursAppoint;

    private int nbrJoursAppoint = 0;

    final private Map<String, PersonnePCAccordee> personnes = new HashMap<String, PersonnePCAccordee>();

    private String strDateDebut = null;

    private String strDateFin = null;
    private TypeSeparationCC typeSeparationCC = null;

    /**
     * Ajoute un forfait de primes moyennes d'assurance maladie à la liste de forfaits concernant la période
     *
     * @param simpleForfaitPrimesAssuranceMaladie
     *            le forfait à ajouter
     * @throws CalculException
     *             en cas d'erreur lors du calcul
     */
    public void addForfaitPrimeMaladie(SimpleForfaitPrimesAssuranceMaladie simpleForfaitPrimesAssuranceMaladie)
            throws CalculException {
        final String csTypePrime = simpleForfaitPrimesAssuranceMaladie.getCsTypePrime();

        TypesForfait typeForfait;
        if (IPCTypePrimeAssuranceMaladie.CS_TYPE_PRIME_ADULTE.equals(csTypePrime)) {
            typeForfait = TypesForfait.FORFAIT_ADULTE;
        } else if (IPCTypePrimeAssuranceMaladie.CS_TYPE_PRIME_JEUNE_ADULTE.equals(csTypePrime)) {
            typeForfait = TypesForfait.FORFAIT_JEUNE_ADULTE;
        } else if (IPCTypePrimeAssuranceMaladie.CS_TYPE_PRIME_ENFANT.equals(csTypePrime)) {
            typeForfait = TypesForfait.FORFAIT_ENFANT;
        } else {
            throw new CalculException("Unknown type of Prime Assurance Maladie : " + csTypePrime);
        }
        ForfaitPrimeMoyenneAssuranceMaladie forfait;
        if (StringUtils.isNotEmpty(simpleForfaitPrimesAssuranceMaladie.getMontantPrimeReductionMaxCanton())) {
            forfait = new ForfaitPrimeMoyenneAssuranceMaladie(csTypePrime, simpleForfaitPrimesAssuranceMaladie.getMontantPrimeMoy(), simpleForfaitPrimesAssuranceMaladie.getMontantPrimeReductionMaxCanton());
        } else {
            forfait = new ForfaitPrimeMoyenneAssuranceMaladie(csTypePrime, simpleForfaitPrimesAssuranceMaladie.getMontantPrimeMoy());
        }

        forfaitsPrimeMaladie.put(typeForfait, forfait);

    }

    /**
     * Ajoute les variables metier dans le contexte
     *
     * @param context
     *            contexte de calcul
     */
    private void ajoutVariablesMetier(CalculContext context) {
        for (String cleVarMet : PeriodePCAccordee.mappageVarMetFinalisation.keySet()) {
            if (controlleurVariablesMetier.get(cleVarMet) != null) {
                context.getValeurs().put(PeriodePCAccordee.mappageVarMetFinalisation.get(cleVarMet),
                        controlleurVariablesMetier.get(cleVarMet));
                if (legendesVariablesMetier.get(cleVarMet) != null) {
                    context.getValeurs().put(PeriodePCAccordee.mappageLegendesVarMetFinalisation.get(cleVarMet),
                            legendesVariablesMetier.get(cleVarMet));
                }

            }
        }
    }

    // public void arronditMontants() {
    // for (CalculComparatif cc : this.calculsComparatifs) {
    // cc.arronditMontants();
    // }
    // }

    /**
     * Execute le calcul du PCA pour la combinaison de personnes donnée en paramètre
     *
     * @param typeCalculCC
     *            type de calcul
     * @param combinaisonPersonnes
     *            liste de personnes incluses dans le calcul
     * @param context
     *            contexte de calcul
     * @return le résultat de calcul
     * @throws CalculException
     *             en cas d'erreur de calcul
     */
    private CalculComparatif calculeCC(TypeCalculCC typeCalculCC, List<PersonnePCAccordee> combinaisonPersonnes,
                                       CalculContext context) throws CalculException {
        return calculeCC(typeCalculCC, combinaisonPersonnes, context, null);
    }

    /**
     * Execute le calcul du PCA pour la combinaison de personnes donnée en paramètre
     *
     * @param typeCalculCC
     *            type de calcul
     * @param combinaisonPersonnes
     *            liste de personnes incluses dans le calcul
     * @param context
     *            contexte de calcul
     * @return le résultat de calcul
     * @throws CalculException
     *             en cas d'erreur de calcul
     */
    private CalculComparatif calculeCC(TypeCalculCC typeCalculCC, List<PersonnePCAccordee> combinaisonPersonnes,
                                       CalculContext context, PersonnePCAccordee personneEnCours) throws CalculException {
        TupleDonneeRapport root = prepareCalculCC(combinaisonPersonnes, context, personneEnCours);
        switch (typeCalculCC) {
            case CALCUL_CC_NON_SEPARE:
                CalculFinal(context, root);
                break;
            case CALCUL_CC_SEPARE_AVEC_ENFANTS:
                CalculFinalSepare(context, root);
                break;
            case CALCUL_CC_SEPARE_COMMUN:
                CalculFinalCommun(context, root);
                break;
            case CALCUL_CC_SEPARE_SEUL:
                CalculFinalSeul(context, root);
                break;
        }
        return finaliseCalculCC(combinaisonPersonnes, root);
    }

    /**
     * Determine et stocke dans le contexte le nombre de personnes et la repartition des enfants/parents
     *
     * @param combinaisonPersonnes
     *            liste de personnes
     * @param context
     *            contexte du calcul
     */
    private void calculeNombrePersonnes(List<PersonnePCAccordee> combinaisonPersonnes, CalculContext context) throws CalculException {
        // determine le nombre d'enfants et parents et stocke les resultats en
        // memoire de contexte
        context.put(CalculContext.Attribut.NB_PERSONNES, combinaisonPersonnes.size());
        String dateDebutPeriode = (String) context.get(Attribut.DATE_DEBUT_PERIODE);
        int nbParents = 0;
        int nbEnfants = 0;
        int nbEnfantsSup11 = 0;
        int nbEnfantsInf11 = 0;
        for (PersonnePCAccordee personnePCAccordee : combinaisonPersonnes) {
            if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(personnePCAccordee.getCsRoleFamille())
                    || IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(personnePCAccordee.getCsRoleFamille())) {
                if (((Boolean) context.get(Attribut.IS_FRATRIE) && IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(personnePCAccordee.getCsRoleFamille()))) {
                    if (JadeDateUtil.getNbYearsBetween(personnePCAccordee.getDateNaissance(), dateDebutPeriode) < 11) {
                        nbEnfantsInf11++;
                    } else {
                        nbEnfantsSup11++;
                    }
                }
                nbParents++;
            } else if (IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(personnePCAccordee.getCsRoleFamille())) {
                nbEnfants++;
                if (JadeDateUtil.getNbYearsBetween(personnePCAccordee.getDateNaissance(), dateDebutPeriode) < 11) {
                    nbEnfantsInf11++;
                } else {
                    nbEnfantsSup11++;
                }
            }
        }

        context.put(CalculContext.Attribut.NB_PARENTS, nbParents);
        context.put(CalculContext.Attribut.NB_ENFANTS, nbEnfants);
        context.put(CalculContext.Attribut.NB_ENFANTS_INF_11, nbEnfantsInf11);
        context.put(CalculContext.Attribut.NB_ENFANTS_EGAL_SUP_11, nbEnfantsSup11);

    }

    /**
     * Calcule la prime moyenne des assurances maladie du groupe
     *
     * @param combinaisonPersonnes
     *            liste de personnes
     * @throws CalculException
     */
    private float[] calculePrimeAssuranceMaladie(List<PersonnePCAccordee> combinaisonPersonnes) throws CalculException {

        float[] montantPrimeAssuranceMaladie = new float[2];

        for (PersonnePCAccordee personnePCAccordee : combinaisonPersonnes) {
            ForfaitPrimeMoyenneAssuranceMaladie forfait = getForfaitPrimeMoyenneAssuranceMaladie(personnePCAccordee);
            // si requerant ajout de la valeur requerant aussi
            if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(personnePCAccordee.getCsRoleFamille())) {
                montantPrimeAssuranceMaladie[0] = forfait.getMontantPrimeMoy();
            }
            montantPrimeAssuranceMaladie[1] += forfait.getMontantPrimeMoy();

            // Plafonnement de la prime d'assurance maladie
            TupleDonneeRapport personneRoot = personnePCAccordee.getRootDonneesConsolidees();

            float somme = personneRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_PRIME_ASSURANCE_MALADIE_TOTAL);
            if (forfait.getMontantPrimeMoy() < somme) {
                personneRoot.getOrCreateEnfant(IPCValeursPlanCalcul.CLE_DEPEN_PRIME_ASSURANCE_MALADIE_TOTAL).setValeur(forfait.getMontantPrimeMoy());
            }

        }
        return montantPrimeAssuranceMaladie;
    }

    /**
     * Méthode permettant de récupérer le forfait de prime moyenne d'assurance maladie pour la personne et la période donnée.
     *
     * @param personnePCAccordee
     * @return
     * @throws CalculException
     */
    private ForfaitPrimeMoyenneAssuranceMaladie getForfaitPrimeMoyenneAssuranceMaladie(PersonnePCAccordee personnePCAccordee) throws CalculException {
        Calendar calPeriode = Calendar.getInstance();
        calPeriode.setTime(dateDebut);
        Calendar calPersonne = JadeDateUtil.getGlobazCalendar(personnePCAccordee.getDateNaissance());
        if (calPersonne == null) {

            DroitMembreFamille dmfe;
            try {
                dmfe = PegasusImplServiceLocator.getDroitMembreFamilleService().read(
                        personnePCAccordee.getIdDroitPersonne());
            } catch (Exception e) {
                throw new CalculException("Failed to gather information for a calculBusinessException!", e);
            }
            throw new CalculBusinessException("pegasus.calcul.primeAssurance.dateNaissance.mandatory", dmfe
                    .getMembreFamille().getPrenom() + " " + dmfe.getMembreFamille().getNom());
        }
        int age = calPeriode.get(Calendar.YEAR) - calPersonne.get(Calendar.YEAR);
        ForfaitPrimeMoyenneAssuranceMaladie forfait;
        if (age < PeriodePCAccordee.AGE_JEUNE_ADULTE) {
            forfait = forfaitsPrimeMaladie.get(TypesForfait.FORFAIT_ENFANT);
        } else if (age < PeriodePCAccordee.AGE_ADULTE) {
            forfait = forfaitsPrimeMaladie.get(TypesForfait.FORFAIT_JEUNE_ADULTE);
        } else {
            forfait = forfaitsPrimeMaladie.get(TypesForfait.FORFAIT_ADULTE);
        }
        if (forfait == null) {
            DonneesPersonnelles dp;
            try {
                SimpleDroitMembreFamille dmf = PegasusImplServiceLocator.getSimpleDroitMembreFamilleService().read(
                        personnePCAccordee.getIdDroitPersonne());
                dp = PegasusImplServiceLocator.getDonneesPersonnellesService().read(dmf.getIdDonneesPersonnelles());
            } catch (Exception e) {
                throw new CalculException("Failed to gather information for a calculBusinessException!", e);
            }
            String nomLocalite = dp.getLocalite().getNumPostal();
            throw new CalculBusinessException("pegasus.calcul.primeAssurance.forfait.mandatory", nomLocalite,
                    strDateDebut, strDateFin);
        }
        return forfait;
    }

    private void calculeStrategiesFinales(List<PersonnePCAccordee> enfants, List<PersonnePCAccordee> parents,
                                          CalculContext context) throws CalculException {

        // traite chaque possibilité de calculs comparatifs
        int nbEnfants = enfants.size();

        // Si fratrie
        if ((Boolean) context.get(Attribut.IS_FRATRIE)) {
            List<PersonnePCAccordee> combinaisonPersonnes = new ArrayList<PersonnePCAccordee>(parents);

            for (int idxEnfant = 0; idxEnfant < nbEnfants; idxEnfant++) {
                combinaisonPersonnes.add(enfants.get(idxEnfant));
            }

            // lance le calcul pour la combinaison
            CalculComparatif cc = calculeCC(TypeCalculCC.CALCUL_CC_NON_SEPARE, combinaisonPersonnes, context);
            getCalculsComparatifs().add(cc);

        } else {
            for (int idxCC = 0; idxCC < Math.pow(2, nbEnfants); idxCC++) {

                List<PersonnePCAccordee> combinaisonPersonnes = new ArrayList<PersonnePCAccordee>(parents);

                for (int idxEnfant = 0; idxEnfant < nbEnfants; idxEnfant++) {
                    if ((idxCC & (int) Math.pow(2, idxEnfant)) > 0) {
                        combinaisonPersonnes.add(enfants.get(idxEnfant));
                    }
                }

                // lance le calcul pour la combinaison
                CalculComparatif cc = calculeCC(TypeCalculCC.CALCUL_CC_NON_SEPARE, combinaisonPersonnes, context);
                getCalculsComparatifs().add(cc);
            }
        }

    }

    private void calculeStrategiesFinalesAvecSeparation(List<PersonnePCAccordee> enfants,
                                                        List<PersonnePCAccordee> parents, CalculContext context) throws CalculException {

        List<PersonnePCAccordee> combinaisonPersonnesCommun = new ArrayList<PersonnePCAccordee>();
        combinaisonPersonnesCommun.addAll(parents);

        for (PersonnePCAccordee parent : parents) {

            // calcul seul

            List<PersonnePCAccordee> combinaisonPersonnesSepare = new ArrayList<PersonnePCAccordee>();
            combinaisonPersonnesSepare.add(parent);
            CalculComparatif ccSeul = calculeCC(TypeCalculCC.CALCUL_CC_SEPARE_SEUL, combinaisonPersonnesSepare, context);

            // calcul par combinaison d'enfants
            int nbEnfants = enfants.size();
            for (int idxCC = 0; idxCC < Math.pow(2, nbEnfants); idxCC++) {

                combinaisonPersonnesSepare = new ArrayList<>();
                combinaisonPersonnesSepare.add(parent);
                combinaisonPersonnesCommun = new ArrayList<>(parents);

                for (int idxEnfant = 0; idxEnfant < nbEnfants; idxEnfant++) {
                    if ((idxCC & (int) Math.pow(2, idxEnfant)) > 0) {
                        combinaisonPersonnesSepare.add(enfants.get(idxEnfant));
                        combinaisonPersonnesCommun.add(enfants.get(idxEnfant));
                    }
                }

                // lance le calcul pour la combinaison
                CalculComparatif ccSepare = calculeCC(TypeCalculCC.CALCUL_CC_SEPARE_AVEC_ENFANTS,
                        combinaisonPersonnesSepare, context);
                CalculComparatif ccCommun = calculeCC(TypeCalculCC.CALCUL_CC_SEPARE_COMMUN, combinaisonPersonnesCommun,
                        context, parent);

                // fusionne les 2 cc

                CalculComparatif ccFusionne = fusionneCC(ccSeul, ccSepare, ccCommun, context);

                getCalculsComparatifs().add(ccFusionne);

                // distingue les résultats du conjoint, tout en gardant une copie dans calculsComparatifs afin de
                // traiter les cc indistinctement
                if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(parent.getCsRoleFamille())) {
                    getCalculsComparatifsConjoint().add(ccFusionne);
                }

            }
        }

        // rassemble les cc par pair
        for (CalculComparatif cc : getCalculsComparatifs()) {
            if (cc.getCcConjoint() == null) {
                for (CalculComparatif ccAutre : getCalculsComparatifs()) {
                    if ((ccAutre != cc) && (ccAutre.getCcConjoint() == null)
                            && (cc.getPersonnes().equals(ccAutre.getPersonnes()))) {
                        cc.setCcConjoint(ccAutre);
                        ccAutre.setCcConjoint(cc);
                    }
                }
            }
        }

    }

    /**
     * execution des strategies de calcul de la dernière étape du calcul, impliquant les sommes des clés calculées
     *
     * @param context
     *            contexte du calcul
     * @param root
     *            racine de toutes les clés
     * @throws CalculException
     *             en cas d'erreur de calcul
     */
    private void CalculFinal(CalculContext context, TupleDonneeRapport root) throws CalculException {
        // calcul fortune

        for (StrategieCalculFinalisation strategie : StrategiesFinalisationFactory.getFactoryFortune().getStrategies()) {
            strategie.calcule(root, context, dateDebut);
        }

        // calcul revenus
        for (StrategieCalculFinalisation strategie : StrategiesFinalisationFactory.getFactoryRevenu().getStrategies()) {
            strategie.calcule(root, context, dateDebut);
        }

        // calcul depenses
        for (StrategieCalculFinalisation strategie : StrategiesFinalisationFactory.getFactoryDepense().getStrategies()) {
            strategie.calcule(root, context, dateDebut);
        }

        // calcul total
        for (StrategieCalculFinalisation strategie : StrategiesFinalisationFactory.getFactoryTotal().getStrategies()) {
            strategie.calcule(root, context, dateDebut);
        }

        // Nettoyage final
        for (StrategieCalculFinalisation strategie : StrategiesFinalisationFactory.getFactoryCleaning().getStrategies()) {
            strategie.calcule(root, context, dateDebut);
        }
    }

    private void CalculFinalCommun(CalculContext context, TupleDonneeRapport root) throws CalculException {
        // calcul fortune
        for (StrategieCalculFinalisation strategie : StrategiesFinalisationFactory.getFactoryFortune().getStrategies()) {
            strategie.calcule(root, context, dateDebut);
        }

        // calcul revenus
        for (StrategieCalculFinalisation strategie : StrategiesCoupleSepareFinalisationFactory.getFactoryRevenuCommun()
                .getStrategies()) {
            strategie.calcule(root, context, dateDebut);
        }

    }

    private void CalculFinalSepare(CalculContext context, TupleDonneeRapport root) throws CalculException {
        // calcul revenus
        for (StrategieCalculFinalisation strategie : StrategiesCoupleSepareFinalisationFactory
                .getFactoryAvecEnfantsRevenu().getStrategies()) {
            strategie.calcule(root, context, dateDebut);
        }

    }

    private void CalculFinalSeul(CalculContext context, TupleDonneeRapport root) throws CalculException {

        // calcul dépense
        for (StrategieCalculFinalisation strategie : StrategiesCoupleSepareFinalisationFactory.getFactorySeulDepense()
                .getStrategies()) {
            strategie.calcule(root, context, dateDebut);
        }
    }

    private List<CalculComparatif> rechercheReforme(Set<CalculComparatif> ccEgaux) {
        List<CalculComparatif> ccReforme = new ArrayList<>();
        for (CalculComparatif cc : ccEgaux) {
            if (cc.isReformePc()) {
                ccReforme.add(cc);
            }
        }
        return ccReforme;
    }

    private CalculComparatif chercheCCCadet(Set<CalculComparatif> ccEgaux) throws CalculException {
        int ageCCRetenu = -1;
        final String currentDate = JadeDateUtil.getGlobazFormattedDate(new Date());
        CalculComparatif ccRetenu = null;

        // parcours des cc
        for (CalculComparatif cc : ccEgaux) {

            // calculer age moyen du cc
            int ageCC = 0;

            if (!cc.getPersonnes().isEmpty()) {
                // calcul de l'age additionné des membres afin de comparer avec l'age total des membres du cc retenu
                for (PersonnePCAccordee personne : cc.getPersonnes()) {
                    int age = JadeDateUtil.getNbYearsBetween(personne.getDateNaissance(), currentDate);
                    ageCC += age;
                }
            } else {
                throw new CalculException("list of persons in cc should not be empty!");
            }

            // Si la somme est à zéro (Octroi partiel, ou refus) , on prend par défaut le plan avec tous les enfants
            // retenus, plus grande somme des
            // ages
            if (Float.parseFloat(cc.getMontantPCMensuel()) == 0.0f) {
                if ((ccRetenu == null) || (ageCC > ageCCRetenu)) {
                    ccRetenu = cc;
                    ageCCRetenu = ageCC;
                }
            }
            // Sinon on prend la plus petit somme des ages
            else {
                if ((ccRetenu == null) || (ageCC < ageCCRetenu)) {
                    ccRetenu = cc;
                    ageCCRetenu = ageCC;
                }
            }

        }

        return ccRetenu;
    }

    /**
     * Consolide les données en vue du calcul final
     *
     * @throws CalculException
     *             en cas d'erreur lors du calcul
     */
    public void consolideDonnees() throws CalculException {

        // preparation du contexte
        CalculContext context = CalculContext.getNewInstance();
        context.getValeurs().put(CalculContext.Attribut.DUREE_PERIODE, getNbJoursPeriode());

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateDebut);
        int nbJoursAnnee = cal.getActualMaximum(Calendar.DAY_OF_YEAR);
        context.getValeurs().put(CalculContext.Attribut.DUREE_ANNEE, nbJoursAnnee);

        for (String cleVarMet : PeriodePCAccordee.mappageVarMet.keySet()) {
            if (controlleurVariablesMetier.get(cleVarMet) != null) {
                context.getValeurs().put(PeriodePCAccordee.mappageVarMet.get(cleVarMet),
                        // Float.valueOf(this.variablesMetier.get(cleVarMet)));
                        controlleurVariablesMetier.get(cleVarMet));

            }
        }

        // Ajout des monnaies etrangeres
        context.put(Attribut.TAUX_DEVISES_ETRANGERES, getControlleurMonnaiesEtrangere());

        context.put(Attribut.DONNEES_HOMES, donneesHomes);

        context.put(Attribut.DATE_DEBUT_PERIODE, getStrDateDebut());

        if (calculReforme) {
            context.put(CalculContext.Attribut.REFORME, true);
            HashMap<String, String> map = new LinkedHashMap<>();
            String dateDebutPeriode = (String) context.get(Attribut.DATE_DEBUT_PERIODE);
            for (PersonnePCAccordee personne : personnes.values()) {
                if ((JadeDateUtil.getNbYearsBetween(personne.getDateNaissance(), dateDebutPeriode) >= 11)
                        && (IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(personne.getCsRoleFamille())
                        || ((IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(personne.getCsRoleFamille()) || IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(personne.getCsRoleFamille())) && isFratrie))) {
                    map.put(personne.getIdPersonne(), personne.getDateNaissance());
                }
            }
            context.put(CalculContext.Attribut.LIST_ENFANTS_SUP_11, map);
        }

        setIsHome(false);

        // execution des strategies de calcul

        for (PersonnePCAccordee personne : personnes.values()) {
            TupleDonneeRapport tupleRoot = personne.getRootDonneesConsolidees();

            boolean personneSansRente = isSansRente(personne.getDonneesBD());
            boolean personneAvecIJAI = hasIJAI(personne.getDonneesBD());

            for (CalculDonneesCC donnee : personne.getDonneesBD()) {

                boolean dealDonneeWithoutDessaisissementFotune = !donnee.getIsDessaisissementFortune();
                // Si dessaisissement de fortune, on traite, si dessaisisement de revenu avec, on ne le traite pas
                if (donnee.getIsDessaisissementFortune()) {
                    // traitement en cas de dessaisissement auto de fortune
                    StrategieCalcul strategie = StrategiesFactory.getStrategieDessaisissementFortune();
                    strategie.calcule(donnee, context, tupleRoot);
                } else if (donnee.getIsDessaisissementRevenu()) {
                    // traitement en cas de dessaisissement auto de revenu
                    StrategieCalcul strategie = StrategiesFactory.getStrategieDessaisissementRevenu();
                    strategie.calcule(donnee, context, tupleRoot);
                }

                if (dealDonneeWithoutDessaisissementFotune) {
                    if (calculReforme && personne.isConjoint() && (personneSansRente || personneAvecIJAI)) {
                        callStrategies(context, tupleRoot, donnee, PeriodePCAccordee.conjointFactories);
                    } else if (calculReforme && personne.isEnfant() && !this.isFratrie) {
                        callStrategies(context, tupleRoot, donnee, PeriodePCAccordee.enfantFactories);
                    } else {
                        callStrategies(context, tupleRoot, donnee, PeriodePCAccordee.factories);
                    }
                }

                // Strategies mixtes
                callStrategies(context, tupleRoot, donnee, PeriodePCAccordee.mixteFactories);

            }
            // ON set le homme pour la période --Ce qui va définir le type de séparation
            if (tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_NOMBRE_CHAMBRES) > 0) {
                setIsHome(true);
                // Idem pour la personne
                personne.setIsHome(true);
            }
            // set le home pour la personne, ce qui va déterminer le type de pc
            personne.setIsHome(tupleRoot.getValeurEnfant(IPCValeursPlanCalcul.CLE_INTER_NOMBRE_CHAMBRES) > 0);

            // set le type de rente du requérant venant du contexte afin de le récupérer et de pouvoir le redéfinir dans
            // le contexte des stratégies finales
            if (context.keyAlreadyExist(Attribut.TYPE_RENTE_REQUERANT)) {
                personne.setTypeRenteRequerant((String) context.get(Attribut.TYPE_RENTE_REQUERANT));
            }
        }

    }

    /**
     * Appel des différentes stratégies en fonction des factories de stratégies passées en paramètre.
     * @param context
     * @param tupleRoot
     * @param donnee
     * @param strategiesFactory
     * @throws CalculException
     */
    private void callStrategies(CalculContext context, TupleDonneeRapport tupleRoot, CalculDonneesCC donnee, StrategiesFactory[] strategiesFactory) throws CalculException {
        for (StrategiesFactory factory : strategiesFactory) {
            StrategieCalcul strategie = factory.getStrategie(donnee.getCsTypeDonneeFinanciere(),
                    getStrDateDebut());
            if (strategie != null) {
                strategie.calcule(donnee, context, tupleRoot);
            }
        }
    }

    /**
     * Méthode permettant de déterminer si la personne n'a pas de rente dans les données passées en paramètres.
     * @param donneesBD
     * @return vrai si la personnne n'a pas de rentes dans les données.
     */
    private boolean isSansRente(List<CalculDonneesCC> donneesBD) {
        return !donneesBD.stream().anyMatch(calculDonneesCC -> StringUtils.isNotEmpty(calculDonneesCC.getRenteAVSAICsType()) || StringUtils.isNotEmpty(calculDonneesCC.getAPIAVSCsType()) || StringUtils.isNotEmpty(calculDonneesCC.getIJAPGGenre()));
    }

    /**
     * Méthode permettant de déterminer si la personne a une IJAI dans les données passées en paramètres.
     * @param donneesBD
     * @return vrai si la personnne a une IJAI dans les données.
     */
    private boolean hasIJAI(List<CalculDonneesCC> donneesBD) {
        return donneesBD.stream().anyMatch(calculDonneesCC -> StringUtils.equals(IPCDroits.CS_IJAI, calculDonneesCC.getCsTypeDonneeFinanciere()));
    }

    /**
     * consolide et somme tous les tuples ayant la même clé entre les différentes personnes
     *
     * @param combinaisonPersonnes
     *            liste de personnes incluses dans le calcul
     * @return tuple racine de tous les tuples additionnés
     * @throws CalculException
     *             en cas d'erreur de calcul
     */
    private TupleDonneeRapport consolideTuples(List<PersonnePCAccordee> combinaisonPersonnes) throws CalculException {
        TupleDonneeRapport root = new TupleDonneeRapport("root");
        for (PersonnePCAccordee personne : combinaisonPersonnes) {
            TupleDonneeRapport personneRoot = personne.getRootDonneesConsolidees();
            boolean hasError = false;
            for (String key : personneRoot.getEnfants().keySet()) {
                if (personneRoot.getEnfants().get(key) == null) {
                    System.out.println("found null for tuple " + key);
                    hasError = true;
                }
            }
            if (hasError) {
                throw new CalculException("found null tuple(s)");
            }
            for (TupleDonneeRapport tuple : personneRoot.getEnfants().values()) {
                if (!IPCValeursPlanCalcul.CLE_REVEN_RENAUTRE_RENTE_ETRANGERE_TAUX_CHANGE.equals(tuple.getLabel())) {
                    sumTuple(root, tuple);
                } else {
                    mixTuple(root, tuple);
                }
            }
        }
        return root;
    }

    private boolean containsCcConjoint(Set<CalculComparatif> ccEgaux, ICalculComparatif cc) {
        for (CalculComparatif ccEgal : ccEgaux) {
            if (ccEgal.getCcConjoint() == cc) {
                return true;
            }
        }
        return false;
    }

    public void determineCCFavorable() throws CalculException {
        determineCCFavorable(getCalculsComparatifs());
    }

    public void determineCCFavorable(List<CalculComparatif> calculComparatifs) throws CalculException {

        switch (calculComparatifs.size()) {
            case 0:
                throw new CalculException("No calcul comparatif found!");
            case 1:
                calculComparatifs.get(0).setPlanRetenu(true);
                break;
            default:
                // ajout des cc dans les containers, TypePC, Listecc, instanciation
                Map<String, ArrayList<CalculComparatif>> listeCC = new HashMap<String, ArrayList<CalculComparatif>>();
                listeCC.put(IPCValeursPlanCalcul.STATUS_OCTROI, new ArrayList<CalculComparatif>());
                listeCC.put(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL, new ArrayList<CalculComparatif>());
                listeCC.put(IPCValeursPlanCalcul.STATUS_REFUS, new ArrayList<CalculComparatif>());

                // iteration sur les cc et depot dans les containers adequats
                for (CalculComparatif cc : calculComparatifs) {
                    // Ajout du cc dans la bonne liste
                    listeCC.get(cc.getEtatPC()).add(cc);
                }

                // Si on a des octrois, le cc retenu est un octroi
                if (listeCC.get(IPCValeursPlanCalcul.STATUS_OCTROI).size() > 0) {
                    setCCRetenuByContainer(listeCC.get(IPCValeursPlanCalcul.STATUS_OCTROI));
                }
                // Sinon le cc retenu est un octroi partiel...
                else if (listeCC.get(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL).size() > 0) {
                    setCCRetenuByContainer(listeCC.get(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL));
                } else// ou un refus
                {
                    setCCRetenuByContainer(listeCC.get(IPCValeursPlanCalcul.STATUS_REFUS));
                }

                break;
        }
    }

    private CalculComparatif finaliseCalculCC(List<PersonnePCAccordee> combinaisonPersonnesCommun,
                                              TupleDonneeRapport root) {
        // les données sont finalisées

        CalculComparatif cc = new CalculComparatif();

        cc.setPersonnes(combinaisonPersonnesCommun);

        cc.setMontants(root);

        return cc;
    }

    /**
     * Execute le calcul final de la PC Accordée, incluant de calculer pour toutes les combinaisons de personnes
     *
     * @param droit
     *            TODO
     *
     * @throws CalculException
     *             en cas d'erreur de calcul
     */
    public void finaliseCC(Droit droit) throws CalculException {

        List<PersonnePCAccordee> enfants = new ArrayList<PersonnePCAccordee>();
        List<PersonnePCAccordee> parents = new ArrayList<PersonnePCAccordee>();

        // determine liste enfants et parents
        for (PersonnePCAccordee personne : personnes.values()) {

            if (IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(personne.getCsRoleFamille())) {
                enfants.add(personne);
            } else {
                parents.add(personne);
            }
        }

        // initialise contexte
        CalculContext context = CalculContext.getNewInstance();

        context.put(Attribut.TYPE_RENTE_REQUERANT, getTypeRenteRequerant());

        context.put(Attribut.FAMILY_SIZE, personnes.size());

        // Si fratrie
        context.put(Attribut.IS_FRATRIE, droit.getDemande().getSimpleDemande().getIsFratrie());

        // ajout des variables metiers
        ajoutVariablesMetier(context);

        context.put(Attribut.DATE_DEBUT_PERIODE, strDateDebut);

        setTypeSeparationCC(parents.size());
        context.put(Attribut.TYPE_SEPARATION_CC, getTypeSeparationCC());

        if (calculReforme) {
            context.put(Attribut.REFORME, true);
        }
        context.put(CalculContext.Attribut.NB_PARENTS, parents.size());
        context.put(CalculContext.Attribut.NB_ENFANTS, enfants.size());
        int enfantSansRente = 0;

        // filtrage des enfants non considérables
        for (Iterator<PersonnePCAccordee> iterator = enfants.iterator(); iterator.hasNext(); ) {
            PersonnePCAccordee enfant = iterator.next();
            if (enfant.isSansRente()) {
                iterator.remove();
                enfantSansRente++;
            } else if (UtilFortune.isRefusChildFortune(enfant.getRootDonneesConsolidees(), context)) {
                iterator.remove();
            }

        }
        // nb personne dans le calcul (on retire les enfants sans rente pas ceux qui ne seront pas pris dans le calcul lors du comparatif)
        context.put(Attribut.NB_PERSONNES_CALCUL, personnes.size() - enfantSansRente);

        if (getTypeSeparationCC() == TypeSeparationCC.CALCUL_SEPARE_MALADIE) {
            // cas de couple séparé par la maladie
            calculeStrategiesFinalesAvecSeparation(enfants, parents, context);
        } else {
            // autres cas, sans séparation
            calculeStrategiesFinales(enfants, parents, context);
        }

    }

    /**
     * Recherche le type de rente du requérant.
     * Nécessaire car plan de calcul retenu non disponible
     *
     * @return le type de rente du requérant, un code système
     */
    private String getTypeRenteRequerant() {

        String typeRenteRequerant = null;

        for (PersonnePCAccordee personne : personnes.values()) {
            typeRenteRequerant = personne.getTypeRenteRequerant();

            if (null != typeRenteRequerant) {
                break;
            }

        }

        return typeRenteRequerant;
    }

    /**
     * Recherche si le conjoint à une rente principale afin de définir si on est dans le cas d'un couple à dom avec 2
     * rentes principales (requerant rentes principales obligatoire!)
     *
     * @return true si couple a dom2Rentes principales
     */
    private final Boolean findIsDom2RentesPrincipal() {
        for (PersonnePCAccordee personne : personnes.values()) {
            if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(personne.getCsRoleFamille())) {
                return (personne.getRootDonneesConsolidees().getValeurEnfant(
                        IPCValeursPlanCalcul.CLE_INTER_NB_RENTES_PRINCIPALES_COUPLE_A_DOM) > 0);
            }
        }
        return false;
    }

    /**
     * Execute les stratégies de fusion
     *
     * @param ccSeul
     *            resultat de calcul lorsque la personne est seule sans enfants
     * @param ccSepare
     *            resultat de calcul lorsque la personne est seule avec enfants
     * @param ccCommun
     *            resultat de calcul en couple avec enfants
     * @return resultats de calcul fusionnées et recalculés pour les sommes finales
     * @throws CalculException
     *             en cas d'erreur de calcul
     */
    private CalculComparatif fusionneCC(CalculComparatif ccSeul, CalculComparatif ccSepare, CalculComparatif ccCommun,
                                        CalculContext context) throws CalculException {

        CalculComparatif ccResultat = new CalculComparatif();

        // clone
        TupleDonneeRapport original = ccCommun.getMontants();
        TupleDonneeRapport root = original.getClone();

        // ccResultat.setMontants(root); //a enlever
        ccResultat.setPersonnes(ccCommun.getPersonnes());

        // calcul total
        for (StrategieCalculFusion strategie : StrategiesCoupleSepareFusionFactory.getFactoryFusion().getStrategies()) {
            strategie.calcule(ccCommun.getMontants(), ccSepare.getMontants(), ccSeul.getMontants(), root, context,
                    dateDebut);
        }

        ccResultat.setMontants(root);
        return ccResultat;
    }

    ;

    @Override
    public ICalculComparatif[] getCalculComparatifRetenus() {
        return getCCRetenu();
    }

    /**
     * @return the calculsComparatifs
     */
    public List<CalculComparatif> getCalculsComparatifs() {
        return calculReforme ? calculsComparatifsReforme : calculsComparatifs;
    }

    public List<CalculComparatif> getCalculsComparatifsConjoint() {
        return calculReforme ? calculsComparatifsConjointReforme : calculsComparatifsConjoint;
    }


    public CalculComparatif[] getCCRetenu() {
        // TODO return aussi cc retenu du conjoint
        CalculComparatif[] result = new CalculComparatif[2];

        for (CalculComparatif cc : getCalculsComparatifs()) {
            if (cc.isPlanRetenu()) {
                result[(getCalculsComparatifsConjoint().contains(cc) ? 1 : 0)] = cc;
            }
        }
        return result;
    }

    /**
     * @return the codeRente
     */
    public String getCodeRente() {
        return codeRente;
    }

    public Map<String, ControlleurMonnaieEtrangere> getControlleurMonnaiesEtrangere() {
        return controlleurMonnaiesEtrangere;
    }

    /**
     * @return the variablesMetier
     *
     *         public Map<String, String> getVariablesMetier() { return this.variablesMetier; }
     */
    public Map<String, ControlleurVariablesMetier> getControlleurVariablesMetier() {
        return controlleurVariablesMetier;
    }

    /**
     * @return the dateDebut
     */
    public Date getDateDebut() {
        return dateDebut;
    }

    /**
     * @return the dateFin
     */
    public Date getDateFin() {
        return dateFin;
    }

    public String getDateFinDemandeToClosePca() {
        return dateFinDemandeToClosePca;
    }

    public List<CalculDonneesHome> getDonneesHomes() {
        return donneesHomes;
    }

    /**
     * @return the forfaitsPrimeMaladie
     */
    public Map<ForfaitPrimeMoyenneAssuranceMaladie.TypesForfait, ForfaitPrimeMoyenneAssuranceMaladie> getForfaitsPrimeMaladie() {
        return forfaitsPrimeMaladie;
    }

    @Override
    public String getIdSimplePcAccordee() {
        return idSimplePcAccordee;
    }

    public boolean getIsHome() {
        return isHome;
    }

    public SimpleJoursAppoint getJoursAppointConjoint() {
        return joursAppointConjoint;
    }

    public SimpleJoursAppoint getJoursAppointRequerant() {
        return joursAppointRequerant;
    }

    /**
     * @return the legendesVariablesMetier
     */
    public Map<String, String> getLegendesVariablesMetier() {
        return legendesVariablesMetier;
    }

    public Map<String, Float> getListeTauxMonnaies() {
        return listeTauxMonnaies;
    }

    public float getMontantJournalierJoursAppoint() {
        return montantJournalierJoursAppoint;
    }

    public float getMontantJoursAppoint() {
        return montantJoursAppoint;
    }

    /**
     * Retourne le nombre de jours dans la période
     *
     * @return the nbJoursPeriode Le nombre de jours
     * @throws CalculException
     *             si les dates de début et de fin sont incorrect, ce qui ne devrait pas être possible à cette étape.
     */
    public int getNbJoursPeriode() throws CalculException {
        if (JadeStringUtil.isBlank(strDateFin)) {
            return -1;
        }

        int nbJoursPeriode;
        // calcule nombre de jours dans la période
        Calendar fin = JadeDateUtil.getGlobazCalendar(strDateFin);
        Calendar debut = JadeDateUtil.getGlobazCalendar(strDateDebut);
        nbJoursPeriode = (fin.get(Calendar.DAY_OF_YEAR) - debut.get(Calendar.DAY_OF_YEAR)) + 1;
        if (nbJoursPeriode < 1) {
            throw new CalculException("Period of PCAccordé is wrong. check that the start date (" + strDateDebut
                    + ") and end date (" + strDateFin + ") are correct, in the right order and in the same year.");
        }
        return nbJoursPeriode;
    }

    public int getNbrJoursAppoint() {
        return nbrJoursAppoint;
    }

    public PersonnePCAccordee getPersonneByCsRole(String csRole) {

        for (PersonnePCAccordee personne : personnes.values()) {
            if (csRole.equals(personne.getCsRoleFamille())) {
                return personne;
            }
        }
        return null;

    }

    ;

    /**
     * @return the personnes
     */
    public Map<String, PersonnePCAccordee> getPersonnes() {
        return personnes;
    }

    /**
     * @return the dateDebut
     */
    public String getStrDateDebut() {
        return strDateDebut;
    }

    /**
     * @return the dateFin
     */
    public String getStrDateFin() {
        return strDateFin;
    }

    public String getTypePc() throws CalculException {
        String typeRentePc = null;

        for (CalculComparatif cc : getCalculsComparatifs()) {
            // TODO gérer cas de couple séparé par maladie
            if (cc.isPlanRetenu()) {
                try {
                    if (cc.getMontants().getEnfants().containsKey(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT)) {
                        typeRentePc = cc.getMontants().getEnfants()
                                .get(IPCValeursPlanCalcul.CLE_INTER_TYPE_RENTE_REQUERANT).getLegende();
                    } else if (cc.getMontants().getEnfants().containsKey(IPCValeursPlanCalcul.CLE_REVEN_AUTREREV_IJAI)) {
                        typeRentePc = IPCRenteAvsAi.CS_TYPE_RENTE_IJAI;
                    }
                } catch (NullPointerException e) {
                    // Exeption, aucun calcul retenu
                    throw new CalculBusinessException("pegasus.calcul.persistance.typePC.typeRente.noPcRetenu",
                            strDateDebut, strDateFin);
                }
            }
        }

        String typePc = null;
        if (TypeRenteMap.listeCsRenteInvalidite.contains(typeRentePc)) {
            typePc = IPCPCAccordee.CS_TYPE_PC_INVALIDITE;
        } else if (TypeRenteMap.listeCsRenteSurvivant.contains(typeRentePc)) {
            typePc = IPCPCAccordee.CS_TYPE_PC_SURVIVANT;
        } else if (TypeRenteMap.listeCsRenteVieillesse.contains(typeRentePc)) {
            typePc = IPCPCAccordee.CS_TYPE_PC_VIELLESSE;
        } else {
            throw new CalculBusinessException("pegasus.calcul.persistance.typePC.typeRente.integrity", typeRentePc,
                    strDateDebut, strDateFin);
        }

        return typePc;
    }

    public TypeSeparationCC getTypeSeparationCC() {
        return typeSeparationCC;
    }

    public String getYearValidite() {
        return strDateDebut.substring(6);
    }

    public boolean isCalculRetro() {
        return isCalculRetro;
    }

    public boolean isLastPeriodForYear() {
        if (JadeStringUtil.isBlankOrZero(strDateFin)) {
            return false;
        }
        return strDateFin.substring(3, 5).equals("12");
    }

    private TupleDonneeRapport prepareCalculCC(List<PersonnePCAccordee> combinaisonPersonnesCommun,
                                               CalculContext context, PersonnePCAccordee personneEnCours) throws CalculException {
        calculeNombrePersonnes(combinaisonPersonnesCommun, context);
        //Prepation des varibles pour savoir si le couple a des rentes ou ij/AI
        prepareDonneeIJAI(combinaisonPersonnesCommun, context);


        float[] primesAssuranceMaladie = calculePrimeAssuranceMaladie(combinaisonPersonnesCommun);

        // calcul des elements unitaires/atomiques
        TupleDonneeRapport root = consolideTuples(combinaisonPersonnesCommun);

        // ajout primes maladies (requerant, total) dans le tuple
        root.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_REQUERANT,
                primesAssuranceMaladie[0]));
        root.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.CLE_TOTAL_PRIMEMAL_TOTAL,
                primesAssuranceMaladie[1]));

        if (isCalculReforme()) {
            // check prime assurance maladie
            controlePrimeAssuranceMaladie(combinaisonPersonnesCommun);

            // calcul de la PC minimale
            float pcMinale = calculPCMinale(combinaisonPersonnesCommun);
            root.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.PC_MINIMALE, pcMinale));
        }
        if (personneEnCours != null) {
            root.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.PERSONNE_EN_COURS, 0.0f, personneEnCours.getIdPersonne()));
            root.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.PERSONNE_EN_COURS_ISHOME, personneEnCours.getIsHome() ? 1.0f : 0.0f));
            root.addEnfantTuple(new TupleDonneeRapport(IPCValeursPlanCalcul.PERSONNE_EN_COURS_ISREQUERANT, personneEnCours.isRequerant() ? 1.0f : 0.0f));
        }

        return root;
    }

    private void prepareDonneeIJAI(List<PersonnePCAccordee> combinaisonPersonnes, CalculContext context) {
        for (PersonnePCAccordee personnePCAccordee : combinaisonPersonnes) {
            if (IPCDroits.CS_ROLE_FAMILLE_REQUERANT.equals(personnePCAccordee.getCsRoleFamille())) {
                if (personnePCAccordee.hasIJAI()) {
                    context.put(Attribut.REQUERANT_HAS_IJAI, true);
                }
            }
            if (IPCDroits.CS_ROLE_FAMILLE_CONJOINT.equals(personnePCAccordee.getCsRoleFamille())) {
                if (personnePCAccordee.hasIJAI()) {
                    context.put(Attribut.CONJOINT_HAS_IJAI, true);
                }
            }
        }
    }

    /**
     * Méthode permettant de contrôler que toutes les personnes dans le calcul possède une prime d'assurance maladie.
     * @param combinaisonPersonnesCommun
     * @return vrai si toutes les personnes ont une prime d'assurance maladie.
     */
    private void controlePrimeAssuranceMaladie(List<PersonnePCAccordee> combinaisonPersonnesCommun) throws CalculException {
        for (PersonnePCAccordee eachPersonne : combinaisonPersonnesCommun) {
            if (!eachPersonne.getRootDonneesConsolidees().containsValeurEnfant(IPCValeursPlanCalcul.CLE_DEPEN_PRIME_ASSURANCE_MALADIE_TOTAL)) {
                throw new CalculException("pegasus.primeassurancemaladie.reforme.mandatory", eachPersonne.getIdPersonne());
            }
        }
    }

    /**
     * Méthode permettant de calculer la PC minimale en fonction des personnes dans la PC accordée.
     *
     * @return la valeur de la PC minimale
     */
    private float calculPCMinale(List<PersonnePCAccordee> combinaisonPersonnes) throws CalculException {
        float pcMinimaleTotal = 0f;
        double pcMinimale;

        for (PersonnePCAccordee personnePCAccordee : combinaisonPersonnes) {
            ForfaitPrimeMoyenneAssuranceMaladie forfait = getForfaitPrimeMoyenneAssuranceMaladie(personnePCAccordee);
            pcMinimale = Math.max(forfait.getMontantPrimeMoy() * POURCENTAGE_PRIME_MOY_PC_MINIMAL, forfait.getMontantPrimeReductionMaxCanton());
            pcMinimaleTotal += pcMinimale;
        }
        return pcMinimaleTotal;
    }

    public void setCalculComparatifByDefaut(CalculComparatif cc) {
        getCalculsComparatifs().add(cc);
    }

    public void setCalculRetro(boolean isCalculRetro) {
        this.isCalculRetro = isCalculRetro;
    }

    private void setCCRetenuByContainer(ArrayList<CalculComparatif> containerCC) throws CalculException {
        CalculComparatif ccRetenu = null;
        float somme = -1f;
        Set<CalculComparatif> ccEgaux = new HashSet<CalculComparatif>();

        // Iteration sur les cc
        for (CalculComparatif cc : containerCC) {
            // Recup du montant de la pc mensuelle 0, ou plus
            float ccSomme = Float.parseFloat(cc.getMontantPCMensuel());
            // Si c'est un refus la somme utilisé est l'excedent annuel net
            if (cc.getEtatPC().equals(IPCValeursPlanCalcul.STATUS_REFUS)) {
                ccSomme = Float.parseFloat(cc.getExcedentAnnuel()) + Float.parseFloat(cc.getPrimeMoyenneAssMaladie());
            }
            // si somme > -1f, o et plus
            if ((ccRetenu == null) || (ccSomme > somme)) {
                ccRetenu = cc;
                somme = ccSomme;
                ccEgaux.clear();
                ccEgaux.add(cc);
            } else if ((ccSomme == somme) && !containsCcConjoint(ccEgaux, cc)) {
                ccEgaux.add(cc);
            }
        }

        // si plusieurs cc retenus ont le même montant, privilégié ceux de la réforme
        if (ccEgaux.size() > 1) {
            List<CalculComparatif> rechercheReforme = rechercheReforme(ccEgaux);
            if (!rechercheReforme.isEmpty()) {
                if (rechercheReforme.size() > 1) {
                    // si plusieurs cc retenus ont le même montant, choisir celui dont les enfants sont les plus jeunes
                    ccRetenu = chercheCCCadet(new HashSet(rechercheReforme));
                } else {
                    ccRetenu = rechercheReforme.get(0);
                }
            } else {
                // si plusieurs cc retenus ont le même montant, choisir celui dont les enfants sont les plus jeunes
                ccRetenu = chercheCCCadet(ccEgaux);
            }
        }

        // Si ccRetenu défini
        if (ccRetenu != null) {
            ccRetenu.setPlanRetenu(true);
            if (ccRetenu.getCcConjoint() != null) {
                ccRetenu.getCcConjoint().setPlanRetenu(true);
            }
        } else {
            throw new CalculException(
                    "Error happened while trying to find best calcul comparatif! None of them was choosen.");
        }

        // return ccRetenu;
    }

    /**
     * @param codeRente
     *            the codeRente to set
     */
    public void setCodeRente(String codeRente) {
        this.codeRente = codeRente;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
        strDateDebut = JadeDateUtil.getGlobazFormattedDate(dateDebut);
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
        strDateFin = JadeDateUtil.getGlobazFormattedDate(dateFin);
    }

    public void setDateFinDemandeToClosePca(String dateFinDemandeToClosePca) {
        this.dateFinDemandeToClosePca = dateFinDemandeToClosePca;
    }

    public void setIdSimplePcAccordee(String idSimplePcAccordee) {
        this.idSimplePcAccordee = idSimplePcAccordee;
    }

    public void setIsHome(boolean isHome) {
        this.isHome = isHome;
    }

    public void setJoursAppointConjoint(SimpleJoursAppoint joursAppointConjoint) {
        this.joursAppointConjoint = joursAppointConjoint;
    }

    public void setJoursAppointRequerant(SimpleJoursAppoint joursAppointRequerant) {
        this.joursAppointRequerant = joursAppointRequerant;
    }

    public void setListeTauxMonnaies(Map<String, Float> listeTauxMonnaies) {
        this.listeTauxMonnaies = listeTauxMonnaies;
    }

    public void setMontantJournalierJoursAppoint(float montantJournalierJoursAppoint) {
        this.montantJournalierJoursAppoint = montantJournalierJoursAppoint;
    }

    public void setMontantJoursAppoint(float montantJoursAppoint) {
        this.montantJoursAppoint = montantJoursAppoint;
    }

    public void setNbrJoursAppoint(int nbrJoursAppoint) {
        this.nbrJoursAppoint = nbrJoursAppoint;
    }

    /**
     * @param dateDebut
     *            the dateDebut to set
     */
    public void setStrDateDebut(String dateDebut) {
        strDateDebut = dateDebut;
        this.dateDebut = JadeDateUtil.getGlobazDate(dateDebut);
    }

    /**
     * @param dateFin
     *            the dateFin to set
     */
    public void setStrDateFin(String dateFin) {
        strDateFin = dateFin;
        this.dateFin = JadeDateUtil.getGlobazDate(dateFin);
    }

    public void setTypeSeparationCC(int nbParents) {

        if (isHome && (nbParents >= 2)) {
            typeSeparationCC = TypeSeparationCC.CALCUL_SEPARE_MALADIE;
        } else if (findIsDom2RentesPrincipal()) {
            typeSeparationCC = TypeSeparationCC.CALCUL_DOM2_PRINCIPALE;
        } else {
            typeSeparationCC = TypeSeparationCC.CALCUL_SANS_SEPARATION;
        }
    }

    /**
     * Fonction récursive qui somme 2 tuples ensemble, ainsi que leurs enfants. Si deux enfants ont un identifiant
     * commun, ils sont sommés
     *
     * @param base
     *            le tuple auquel sera ajouté les valeurs de l'autre tuple
     * @param tupleAjoute
     *            le tuple à ajouter
     */
    private void sumTuple(TupleDonneeRapport base, TupleDonneeRapport tupleAjoute) {
        final String key = tupleAjoute.getLabel();
        TupleDonneeRapport tuple;
        if (base.getEnfants().containsKey(key)) {
            tuple = base.getEnfants().get(key);

            tuple.addValeur(tupleAjoute);
        } else {
            tuple = new TupleDonneeRapport(tupleAjoute);
            base.getEnfants().put(key, tuple);
        }

        for (TupleDonneeRapport enfant : tupleAjoute.getEnfants().values()) {
            sumTuple(tuple, enfant);
        }
    }

    /**
     * Fonction récursive qui mix 2 tuples ensemble, ainsi que leurs enfants. Si deux enfants ont un identifiant
     * commun, seul le premier est gardé
     *
     * @param base
     *            le tuple auquel sera mixé les valeurs de l'autre tuple
     * @param tupleAjoute
     *            le tuple à mixer
     */
    private void mixTuple(TupleDonneeRapport base, TupleDonneeRapport tupleAjoute) {
        final String key = tupleAjoute.getLabel();
        TupleDonneeRapport tuple;
        if (base.getEnfants().containsKey(key)) {
            tuple = base.getEnfants().get(key);
        } else {
            tuple = new TupleDonneeRapport(tupleAjoute);
            base.getEnfants().put(key, tuple);
        }

        for (TupleDonneeRapport enfant : tupleAjoute.getEnfants().values()) {
            mixTuple(tuple, enfant);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n{");
        sb.append("periode=[").append(strDateDebut).append("-").append(strDateFin).append("];");
        sb.append("personnes=").append(personnes.values()).append(";");
        sb.append("varmet=").append(controlleurVariablesMetier).append(";");
        sb.append("}");
        return sb.toString();
    }

    public boolean isCalculReforme() {
        return calculReforme;
    }

    public void setCalculReforme(boolean calculReforme) {
        this.calculReforme = calculReforme;
    }

    public boolean isFratrie() {
        return isFratrie;
    }

    public void setFratrie(boolean fratrie) {
        isFratrie = fratrie;
    }

}
