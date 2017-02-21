package ch.globaz.pegasus.business.domaine.parametre.variableMetier;

import ch.globaz.common.domaine.CodeSystemEnum;
import ch.globaz.common.domaine.CodeSystemEnumUtils;

public enum VariableMetierType implements CodeSystemEnum<VariableMetierType> {
    ARGENT_POCHE_MEDICALISE("64008001"),
    ARGENT_POCHE_NON_MEDICALISE("64008002"),
    BESOINS_VITAUX_2_ENFANTS("64008003"),
    BESOINS_VITAUX_4_ENFANTS("64008004"),
    BESOINS_VITAUX_5_ENFANTS("64008005"),
    BESOINS_VITAUX_COUPLES("64008006"),
    BESOINS_VITAUX_CELIBATAIRES("64008007"),
    DEDUCTION_FORFAITAIRE_FORTUNE_COUPLES("64008008"),
    DEDUCTION_FORFAITAIRE_FORTUNE_CELIBATAIRES("64008009"),
    DEDUCTION_FORFAITAIRE_FORTUNE_ENFANTS("64008010"),
    DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE("64008011"),
    FORFAIT_CHARGES("64008012"),
    FORFAIT_FRAIS_CHAUFFAGE("64008013"),
    FRAIS_ENTRETIEN_IMMEUBLE("64008014"),
    FRANCHISE_CAISSE_MALADIE("64008015"),
    FRANCHISE_REVENUS_PRIVILEGIERS_FAMILLE("64008016"),
    FRANCHISE_REVENUS_PRIVILEGIERS_CELIBATAIRES("64008017"),
    MONTANT_MAXIMUM_PC_HOME("64008018"),
    MONTANT_MAXIMUM_PC_MAISON("64008019"),
    // MONTANT_MINIMAL_PC("64008020"),
    FRACTION_REVENUS_PRIVILEGIES("64008021"),
    FRACTIONS_FORTUNE_NON_VIEILLESSE("64008022"),
    FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME("64008049"),
    FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON("64008050"),
    FRACTIONS_FORTUNE_VIEILLESSE_ENFANT("64008023"),
    FRACTIONS_FORTUNE_VIEILLESSE_HOME("64008024"),
    FRACTIONS_FORTUNE_VIEILLESSE_MAISON("64008025"),
    REGIME_ALIMENTAIRE("64008029"),
    TAUX_OFAS("64008030"),
    LOYER_MAXIMUM_PERSONNES_SEULES("64008031"),
    LOYER_MAXIMUM_AUTRES("64008032"),
    MONTANT_MINIMAL_PC("64008033"),
    AMORTISSEMENT_ANNUEL("64008034"),
    TAUX_PENSION_NON_RECONNUE("64008035"),
    PLAFOND_DEPENSE_FAUTEUIL_ROULANT("64008036"),
    FORFAIT_REVENU_NATURE_TENUE_MENAGE("64008037"),
    MONTANT_ALLOCATIONS_NOEL("64008038"),
    DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE_API_HOME("64008039"),
    MENSUALISATION_IJ_CHOMAGE_NBRE_JOURS("64008040"),
    TAUX_SOUS_LOCATIONS_LOYER_FRAIS_ACQUISITION("64008041"),
    TAUX_IMPUTATION_REVENUS_SOUS_LOCATION_FRAIS("64008042"),
    MONTANT_ESE_HANDICAP_PHYSIQUE("64008043"),
    MONTANT_EMS_NON_MEDICALISE_PSYHIATRIQUE("64008044"),
    MONTANT_EMS_NON_MEDICALISE_AGE_AVANCEES("64008045"),
    ARGENT_POCHE_HOME_AVS_ANNUEL("64008046"),
    EPS_ETABLISSEMENT_MEDSOC("64008057"),
    ARGENT_POCHE_HOME_AI_ANNUEL("64008047");

    private String value;

    VariableMetierType(String value) {
        this.value = value;
    }

    public static VariableMetierType fromValue(String value) {
        return CodeSystemEnumUtils.valueOfById(value, VariableMetierType.class);
    }

    @Override
    public String getValue() {
        return value;
    }

}
