package ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul;

import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.globaz.pegasus.business.constantes.IPCVariableMetier;
import ch.globaz.pegasus.business.models.calcul.CalculVariableMetier;

public class VariableMetierBuilder implements ICalculDonneesHorsDroitBuilder {

    private final static List<String> champsVariableMetier = Arrays.asList(new String[]{
            IPCVariableMetier.CS_2091_DPC,
            IPCVariableMetier.CS_FORFAIT_FRAIS_CHAUFFAGE,
            IPCVariableMetier.CS_FORFAIT_CHARGES,
            IPCVariableMetier.CS_DEDUCTION_FORAITAIRE_FORTUNE_COUPLES,
            IPCVariableMetier.CS_DEDUCTION_FORAITAIRE_FORTUNE_CELIBATAIRES,
            IPCVariableMetier.CS_DEDUCTION_FORAITAIRE_FORTUNE_ENFANTS,
            IPCVariableMetier.CS_DEDUCTION_FORAITAIRE_IMMOBILIER_ASSURE,
            IPCVariableMetier.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE,
            IPCVariableMetier.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME,
            IPCVariableMetier.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON,
            IPCVariableMetier.CS_FRACTIONS_FORTUNE_VIEILLESSE_ENFANT, IPCVariableMetier.MONTANT_MINIMALE_PC,
            IPCVariableMetier.CS_FRACTIONS_FORTUNE_VIEILLESSE_HOME,
            IPCVariableMetier.CS_FRACTIONS_FORTUNE_VIEILLESSE_MAISON,
            IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_SEPARE_VIEILLESSE_HOME,
            IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_SEPARE_NON_VIEILLESSE_HOME,
            IPCVariableMetier.CS_FRANCHISE_REVENUS_PRIVILEGIERS_CELIBATAIRES,
            IPCVariableMetier.CS_FRANCHISE_REVENUS_PRIVILEGIERS_FAMILLE,
            IPCVariableMetier.CS_FRACTIONS_FRAIS_ENTRETIEN_IMMEUBLE,
            IPCVariableMetier.CS_FRACTIONS_FRAIS_ENTRETIEN_IMMEUBLE_MOINS_10_ANS,
            IPCVariableMetier.CS_FRACTIONS_FRAIS_ENTRETIEN_IMMEUBLE_PLUS_20_ANS_PRINCIPALE,
            IPCVariableMetier.CS_FRACTIONS_FRAIS_ENTRETIEN_IMMEUBLE_PLUS_20_ANS_ANNEXE,
            IPCVariableMetier.CS_FRACTIONS_FRAIS_ENTRETIEN_IMMEUBLE_MOINS_20_ANS_ANNEXE,
            IPCVariableMetier.CS_BESOINS_VITAUX_2_ENFANTS, IPCVariableMetier.CS_BESOINS_VITAUX_4_ENFANTS,
            IPCVariableMetier.CS_BESOINS_VITAUX_5_ENFANTS, IPCVariableMetier.CS_BESOINS_VITAUX_CELIBATAIRES,
            IPCVariableMetier.CS_BESOINS_VITAUX_COUPLES, IPCVariableMetier.CS_ARGENT_POCHE_MEDICALISE,
            IPCVariableMetier.CS_ARGENT_POCHE_NON_MEDICALISE, IPCVariableMetier.CS_ARGENT_POCHE_HOME_AVS_ANNUEL,
            IPCVariableMetier.CS_ARGENT_POCHE_HOME_AI_ANNUEL,
            IPCVariableMetier.CS_CATEGORIE_ARGENT_POCHE_LVPC_EMS_NOMMED_AGE_AVANCE,
            IPCVariableMetier.CS_CATEGORIE_ARGENT_POCHE_LVPC_EMS_NONMED_PSY,
            IPCVariableMetier.CS_CATEGORIE_ARGENT_POCHE_LVPC_ESE_HANDICAP_PHYSIQUE,
            IPCVariableMetier.CS_FRACTION_REVENUS_PRIVILEGIES,
            IPCVariableMetier.CS_TAUX_PENSION_NON_RECONNUE,
            IPCVariableMetier.DEPENSE_LOYER_PLAFOND_CELIBATAIRE, IPCVariableMetier.DEPENSE_LOYER_PLAFOND_COUPLE,
            IPCVariableMetier.MENSUALISATION_IJ_CHOMAGE, IPCVariableMetier.DEPENSE_LOYER_PLAFOND_FAUTEUIL_ROULANT,
            IPCVariableMetier.CS_REFORME_DEPENSE_LOYER_PLAFOND_FAUTEUIL_ROULANT,
            IPCVariableMetier.CS_REFORME_TAUX_REVENUS_NON_RENTIER,
            IPCVariableMetier.CS_REFORME_TAUX_REVENUS_IJAI,
            IPCVariableMetier.CS_REFORME_FRACTION_REVENUS_PRIVILEGIES_ENFANT,
            IPCVariableMetier.CS_PLAFOND_ANNUEL_EMS, IPCVariableMetier.CS_FORFAIT_REVENU_NATURE_TENUE_MENAGE,
            IPCVariableMetier.CS_DEDUCTION_FORAITAIRE_IMMOBILIER_ASSURE_HOME_API,
            IPCVariableMetier.CS_AMORTISSEMENT_ANNUEL_DESSAISISSEMENT_FORTUNE,
            IPCVariableMetier.CS_TAUX_IMPUTATION_SOUSLOCATIONS_FRAIS_ACUISITION, IPCVariableMetier.CS_ALLOCATION_NOEL,
            IPCVariableMetier.CS_TAUX_IMPUTATION_LOYER_FRAIS_ACQUISITION, IPCVariableMetier.CS_PLAFOND_ANNUEL_HOME,
            IPCVariableMetier.CS_MONTANT_TYPE_CHAMBRE_EPS,
            IPCVariableMetier.CS_MONTANT_TYPE_CHAMBRE_SPEN,
            IPCVariableMetier.CS_PLAFOND_ANNUEL_INSTITUTION, IPCVariableMetier.CS_PLAFOND_ANNUEL_LITS_ATTENTE,
            IPCVariableMetier.TAUX_IMPUTATIONS_VALEUR_LOCATIVE_BRUT,
            IPCVariableMetier.TAUX_IMPUTATIONS_VALEUR_LOCATIVE_BRUT_M10,
            IPCVariableMetier.TAUX_IMPUTATIONS_LOYER_EFFECTIF,
            IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_HOME,
            IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_RESIDENT,
            IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_MOITIE,
            IPCVariableMetier.CS_REFORME_SEUIL_FORTUNE_SEUL,
            IPCVariableMetier.CS_REFORME_SEUIL_FORTUNE_COUPLE,
            IPCVariableMetier.CS_REFORME_SEUIL_FORTUNE_ENFANT,
            IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_1,
            IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_2,
            IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_3,
            IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_4,
            IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_MOINS_11_ENFANTS_5,
            IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_2,
            IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_4,
            IPCVariableMetier.CS_REFORME_BESOINS_VITAUX_AGE_PLUS_OU_EGAL_11_ENFANTS_5,
            IPCVariableMetier.CS_REFORME_DEDUCTION_FORAITAIRE_FORTUNE_CELIBATAIRES,
            IPCVariableMetier.CS_REFORME_DEDUCTION_FORAITAIRE_FORTUNE_COUPLES,
            IPCVariableMetier.CS_REFORME_FORFAIT_FRAIS_CHAUFFAGE,
            IPCVariableMetier.CS_REFORME_FORFAIT_CHARGES});
    private final static List<String> champsVariableMetierAvecFraction = Arrays.asList(new String[]{
            IPCVariableMetier.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE,
            IPCVariableMetier.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_HOME,
            IPCVariableMetier.CS_FRACTIONS_FORTUNE_NON_VIEILLESSE_MAISON,
            IPCVariableMetier.CS_FRACTIONS_FORTUNE_VIEILLESSE_ENFANT,
            IPCVariableMetier.CS_FRACTIONS_FORTUNE_VIEILLESSE_HOME,
            IPCVariableMetier.CS_FRACTIONS_FORTUNE_VIEILLESSE_MAISON,
            IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_SEPARE_VIEILLESSE_HOME,
            IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_SEPARE_NON_VIEILLESSE_HOME,
            IPCVariableMetier.CS_FRACTION_REVENUS_PRIVILEGIES, IPCVariableMetier.CS_FRACTIONS_FRAIS_ENTRETIEN_IMMEUBLE,
            IPCVariableMetier.CS_FRACTIONS_FRAIS_ENTRETIEN_IMMEUBLE_MOINS_10_ANS,
            IPCVariableMetier.TAUX_IMPUTATIONS_VALEUR_LOCATIVE_BRUT,
            IPCVariableMetier.TAUX_IMPUTATIONS_VALEUR_LOCATIVE_BRUT_M10,
            IPCVariableMetier.TAUX_IMPUTATIONS_LOYER_EFFECTIF,
            IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_HOME,
            IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_RESIDENT,
            IPCVariableMetier.CS_REFORME_FRACTIONS_FORTUNE_MOITIE,
            IPCVariableMetier.CS_REFORME_FRACTION_REVENUS_PRIVILEGIES_ENFANT
    });
    private final static List<String> champsVariableMetierAvecTaux = Arrays.asList(new String[]{
            IPCVariableMetier.CS_REFORME_TAUX_REVENUS_NON_RENTIER,
            IPCVariableMetier.CS_REFORME_TAUX_REVENUS_IJAI
    });

    private VariableMetier tauxOFAS = null;

    public VariableMetier getTauxOFAS() {
        return tauxOFAS;
    }

    @Override
    public ArrayList<VariableMetier> loadFor(JadeAbstractSearchModel varMetSearch) {

        // LIste des variables métier
        Map<String, VariableMetier> listeVarMet = new HashMap<String, VariableMetier>();

        for (globaz.jade.persistence.model.JadeAbstractModel absDonnee : varMetSearch.getSearchResults()) {

            CalculVariableMetier donnee = (CalculVariableMetier) absDonnee;
            final String strDateDebut = "01." + donnee.getDateDebut();
            Date vmDateDebut = JadeDateUtil.getGlobazDate(strDateDebut);
            String csTypeVariableMetier = donnee.getCsTypeVariableMetier();

            if (VariableMetierBuilder.champsVariableMetier.contains(csTypeVariableMetier)) {
                // if (IPCVariableMetier.CS_2091_DPC.equals(donnee.getCsTypeVariableMetier())) {
                // // ajoute dans la liste locale des taux OFAS
                // montantsTauxOFAS.put(Integer.parseInt(donnee.getDateDebut().substring(3)), donnee.getValue());
                // tauxOFAS.getVariablesMetiers().put(vmDateDebut.getTime(), donnee.getValue());
                // }
                // Si la liste ne contient pas la variblesemétier, on instancie un nouvel objet variable metier
                if (!listeVarMet.containsKey(csTypeVariableMetier)) {
                    VariableMetier varMetier = new VariableMetier();
                    varMetier.setCsTypeVariableMetier(csTypeVariableMetier);
                    varMetier.getVariablesMetiers().put(vmDateDebut.getTime(), donnee.getValue());
                    listeVarMet.put(csTypeVariableMetier, varMetier);
                } else {
                    // La variable metier a deja ete crée on ajoute une autre periode
                    VariableMetier varMetier = listeVarMet.get(csTypeVariableMetier);
                    varMetier.getVariablesMetiers().put(vmDateDebut.getTime(), donnee.getValue());
                }
                // on set les legendes des variables métiers avec fraction le necessitant
                if (VariableMetierBuilder.champsVariableMetierAvecFraction.contains(csTypeVariableMetier)) {
                    VariableMetier varMetier = listeVarMet.get(csTypeVariableMetier);
                    varMetier.getLegendesVariablesMetiers().put(vmDateDebut.getTime(),
                            String.format("%s/%s", donnee.getFractionNumerateur(), donnee.getFractionDenominateur()));
                }
                // on ajout les légendes des variables métiers avec taux
                if (VariableMetierBuilder.champsVariableMetierAvecTaux.contains(csTypeVariableMetier)) {
                    VariableMetier varMetier = listeVarMet.get(csTypeVariableMetier);
                    int taux = Math.round(100 * Float.parseFloat(donnee.getTaux()));
                    varMetier.getLegendesVariablesMetiers().put(vmDateDebut.getTime(), taux + "%");
                }
            }
        }

        tauxOFAS = listeVarMet.get(IPCVariableMetier.CS_2091_DPC);
        return new ArrayList<VariableMetier>(listeVarMet.values());
    }

    public void setTauxOFAS(VariableMetier tauxOFAS) {
        this.tauxOFAS = tauxOFAS;
    }

}
