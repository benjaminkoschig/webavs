package ch.globaz.pegasus.businessimpl.utils.calcul;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;

/**
 * 
 * @author ECO
 * 
 */
public class CalculContext {

    public enum Attribut {
        CS_2091_DPC,
        CS_AMORTISSEMENT_ANNUEL_DESSAISISSEMENT_FORTUNE,
        CS_ARGENT_POCHE_MEDICALISE,
        CS_ARGENT_POCHE_NON_MEDICALISE,
        CS_ARGENT_POCHE_HOME_AVS_ANNUEL,
        CS_ARGENT_POCHE_HOME_AI_ANNUEL,

        CS_BESOINS_VITAUX_2_ENFANTS,
        CS_BESOINS_VITAUX_4_ENFANTS,
        CS_BESOINS_VITAUX_5_ENFANTS,
        CS_BESOINS_VITAUX_CELIBATAIRES,
        CS_BESOINS_VITAUX_COUPLES,
        CS_DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE,
        CS_DEDUCTION_FORFAITAIRE_IMMOBILIER_ASSURE_HOME_API,
        CS_DEDUCTION_FORTUNE_CELIBATAIRE,
        CS_DEDUCTION_FORTUNE_COUPLE,
        CS_DEDUCTION_FORTUNE_ENFANT,
        CS_FORFAIT_CHARGES,
        CS_FORFAIT_FRAIS_CHAUFFAGE,
        CS_FORFAIT_REVENU_NATURE_TENUE_MENAGE,
        CS_FRACTION_REVENUS_PRIVILEGIES,
        CS_FRACTION_REVENUS_PRIVILEGIES_LEGENDE,
        CS_FRACTIONS_FORTUNE_NON_VIEILLESSE,
        CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_LEGENDE,
        CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME,
        CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME_LEGENDE,
        CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON,
        CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON_LEGENDE,
        CS_FRACTIONS_FORTUNE_VIEILLESSE_ENFANT,
        CS_FRACTIONS_FORTUNE_VIEILLESSE_ENFANT_LEGENDE,
        CS_FRACTIONS_FORTUNE_VIEILLESSE_HOME,
        CS_FRACTIONS_FORTUNE_VIEILLESSE_HOME_LEGENDE,
        CS_FRACTIONS_FORTUNE_VIEILLESSE_MAISON,
        CS_FRACTIONS_FORTUNE_VIEILLESSE_MAISON_LEGENDE,
        CS_FRANCHISE_REVENUS_PRIVILEGIERS_CELIBATAIRES,
        CS_FRANCHISE_REVENUS_PRIVILEGIERS_FAMILLE,
        CS_TAUX_IMPUTATION_LOYER_FRAIS_ACQUISITION,
        CS_TAUX_IMPUTATION_SOUSLOCATIONS_FRAIS_ACUISITION,
        CS_PLAFOND_ANNUEL_EMS,
        CS_PLAFOND_ANNUEL_INSTITUTION,
        CS_PLAFOND_ANNUEL_LITS_ATTENTE,
        DATE_DEBUT_PERIODE,
        DEPENSE_LOYER_PLAFOND_CELIBATAIRE,
        DEPENSE_LOYER_PLAFOND_COUPLE,
        DEPENSE_LOYER_PLAFOND_FAUTEUIL_ROULANT,
        DONNEES_HOMES,
        DUREE_ANNEE,
        DUREE_PERIODE,
        HAS_BIEN_IMMO_PRINCIPAL,
        HAS_HABITAT_CHEZ_PROPRIETAIRE,
        IS_FRATRIE,
        LOYER_POIDS_PRORATA_ENFANT,
        MENSUALISATION_IJ_CHOMAGE,
        MONTANT_MINIMALE_PC,
        NB_ENFANTS,
        NB_PARENTS,
        NB_PERSONNES,
        TAUX_DEVISES_ETRANGERES,
        TAUX_PENSION_NON_RECONNUE,
        TYPE_SEPARATION_CC,
        CS_CATEGORIE_ARGENT_POCHE_LVPC_ESE_HANDICAP_PHYSIQUE,
        CS_CATEGORIE_ARGENT_POCHE_LVPC_EMS_NONMED_PSY,
        CS_CATEGORIE_ARGENT_POCHE_LVPC_EMS_NOMMED_AGE_AVANCE,
        FRAIS_ENTRETIEN_IMMEUBLE,

        FRAIS_ENTRETIEN_IMMEUBLE_MOINS_10_ANS,
        TYPE_RENTE_REQUERANT,
        MONTANT_TYPE_CHAMBRE_EPS,
        TAUX_BIEN_IMMO_FRACTION_VALEUR_LOCATIVE_BRUTE,
        TAUX_BIEN_IMMO_FRACTION_VALEUR_LOCATIVE_BRUTE_M10,
        TAUX_BIEN_IMMO_FRACTION_LOYER_EFFECTIF;
    }

    static CalculContext getNewInstance() {
        return new CalculContext();
    }

    private final Map<Attribut, Object> attributs = new HashMap<Attribut, Object>(Attribut.values().length);
    private Set<CasMetier> casMetier = new HashSet<CasMetier>();

    private CalculContext() {
    }

    public boolean contains(Attribut key) {
        return attributs.containsKey(key);
    }

    public Object get(Attribut key) throws CalculException {
        if (attributs.containsKey(key)) {
            return attributs.get(key);
        } else {
            throw new CalculException("Couldn't find any value for the given attribute : " + key);
        }
    }

    public boolean keyAlreadyExist(Attribut key) {
        return attributs.containsKey(key);
    }

    public Set<CasMetier> getCasMetier() {
        return casMetier;
    }

    /**
     * @return the valeurs
     */
    public Map<Attribut, Object> getValeurs() {
        return attributs;
    }

    public void put(Attribut key, Object valeur) {
        attributs.put(key, valeur);
    }

    public void setCasMetier(Set<CasMetier> casMetier) {
        this.casMetier = casMetier;
    }

}
