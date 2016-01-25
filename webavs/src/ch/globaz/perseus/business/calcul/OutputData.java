/**
 * 
 */
package ch.globaz.perseus.business.calcul;

import java.io.Serializable;

/**
 * @author DDE
 * 
 */
public enum OutputData implements Serializable {
    DEPENSES_BESOINS_VITAUX("besoinsVitaux"),
    DEPENSES_CHARGES_ANNUELLES("chargesAnnuelles"),
    DEPENSES_CHARGES_ANNUELLES_MODIF("chargesAnnuellesModif"),
    DEPENSES_COTISATION_NON_ACTIF("cotisationNonActif"),
    DEPENSES_FRAIS_ENTRETIENS_IMMEUBLE("fraisEntretiensImmeuble"),
    DEPENSES_FRAIS_ENTRETIENS_IMMEUBLE_MODIF("fraisEntretiensImmeubleModif"),
    DEPENSES_FRAIS_IMMEUBLE("fraisImmeuble"),
    DEPENSES_FRAIS_IMMEUBLE_MODIF("fraisImmeubleModif"),
    DEPENSES_FRAIS_OBTENTION_REVENU_CONJOINT("fraisObtentionRevenuConjoint"),
    DEPENSES_FRAIS_OBTENTION_REVENU_CONJOINT_MODIF("fraisObtentionRevenuConjointModif"),
    DEPENSES_FRAIS_OBTENTION_REVENU_ENFANTS("fraisObtentionRevenuEnfants"),
    DEPENSES_FRAIS_OBTENTION_REVENU_ENFANTS_DETAIL_XML("fraisObtentionRevenuEnfantsDetailXml"),
    DEPENSES_FRAIS_OBTENTION_REVENU_REQUERANT("fraisObtentionRevenuRequerant"),
    DEPENSES_FRAIS_OBTENTION_REVENU_REQUERANT_MODIF("fraisObtentionRevenuRequerantModif"),
    DEPENSES_FRAIS_REPAS_CONJOINT("fraisRepasConjoint"),
    DEPENSES_FRAIS_REPAS_CONJOINT_MODIF("fraisRepasConjointModif"),
    DEPENSES_FRAIS_REPAS_REQUERANT("fraisRepasRequerant"),
    DEPENSES_FRAIS_REPAS_REQUERANT_MODIF("fraisRepasRequerantModif"),
    DEPENSES_FRAIS_TRANSPORT_CONJOINT("fraisTransportConjoint"),
    DEPENSES_FRAIS_TRANSPORT_CONJOINT_MODIF_TAXATEUR("fraisTransportConjointModifTaxateur"),
    DEPENSES_FRAIS_TRANSPORT_REQUERANT("fraisTransportRequerant"),
    DEPENSES_FRAIS_TRANSPORT_REQUERANT_MODIF_TAXATEUR("fraisTransportRequerantModifTaxateur"),
    DEPENSES_FRAIS_VETEMENTS_CONJOINT("fraisVetementsConjoint"),
    DEPENSES_FRAIS_VETEMENTS_REQUERANT("fraisVetementsRequerant"),
    DEPENSES_INTERETS_HYPOTHECAIRES("interetsHypothecaires"),
    DEPENSES_LOYER_ANNUEL("loyerAnnuel"),
    DEPENSES_LOYER_ANNUEL_MODIF("loyerAnnuelModif"),
    DEPENSES_PENSION_ALIMENTAIRE_VERSEE("pensionAlimentaireVersee"),
    DEPENSES_RECONNUES("depensesReconnues"),
    DEPENSES_VALEUR_LOCATIVE("valeurLocative"),
    DETTE_AUTRES_DETTES("autresDettes"),
    DETTE_DEDUCTION_LEGALE("deductionLegale"),
    DETTE_HYPOTHECAIRES("dettesHypothecaires"),
    ENTETE_CONJOINT_INFOS("enteteConjointInfos"),
    ENTETE_ENFANTS_INFOS("enteteEnfantsInfos"),
    ENTETE_LOCALITE("enteteLocalite"),
    ENTETE_PERIODE("entetePeriode"),
    ENTETE_REQUERANT_INFOS("enteteRequerantInfos"),
    ENTETE_REQUERANT_NSS("enteteRequerantNss"),
    EXCEDANT_REVENU("excedantRevenu"),
    FORTUNE_AUTRE_BIEN("autreBien"),
    FORTUNE_AUTRES_IMMEUBLES("autresImmeubles"),
    FORTUNE_BIENS_ETRANGERS("biensEtrangers"),
    FORTUNE_CESSION_CONJOINT("cessionConjoint"),
    FORTUNE_CESSION_DEDUC_CONJOINT("cessionDeducConjoint"),
    FORTUNE_CESSION_DEDUC_REQUERANT("cessionDeducRequerant"),
    FORTUNE_CESSION_MODIF_CONJOINT("cessionModifConjoint"),
    FORTUNE_CESSION_MODIF_REQUERANT("cessionModifRequerant"),
    FORTUNE_CESSION_REQUERANT("cessionRequerant"),
    FORTUNE_ENFANTS("fortuneEnfants"),
    FORTUNE_HOIRIE("hoirie"),
    FORTUNE_IMMEUBLE_HABITE("immeubleHabite"),
    FORTUNE_IMMEUBLE_HABITE_DEDUC("immeubleHabiteDeduc"),
    FORTUNE_IMMEUBLE_HABITE_MODIF("immeubleHabiteModif"),
    FORTUNE_IMMOBILIERE("fortuneImmobiliere"),
    FORTUNE_LIQUIDITE("liquidite"),
    FORTUNE_MOBILIERE("fortuneMobiliere"),
    FORTUNE_NETTE("fortuneNette"),
    FORTUNE_RACHAT_ASSURANCE_VIE("rachatAssuranceVie"),
    MESURE_CHARGES_LOYER("mesureChargesLoyer"),
    MESURE_COACHING("mesureCoaching"),
    MESURE_ENCOURAGEMENT("mesureEncouragement"),
    PRESTATION_ANNUELLE("prestationAnnuelle"),
    PRESTATION_ANNUELLE_MODIF("prestationAnnuelleModif"),
    PRESTATION_MENSUELLE("prestationMensuelle"),
    REVENUS_ACTIVITE("revenuActivite"),
    REVENUS_ACTIVITE_MODIF("revenuActiviteModif"),
    REVENUS_AIDE_FORMATION("aideFormation"),
    REVENUS_AIDES_LOGEMENT("aidesLogement"),
    REVENUS_ALLOCATION_CANTONALE_MATERNITE("allocationCantonaleMaternite"),
    REVENUS_ALLOCATIONS_AMINH("allocationsAMINH"),
    REVENUS_ALLOCATIONS_FAMILIALES("allocationsFamiliales"),
    REVENUS_AUTRES_CREANCES("autresCreances"),
    REVENUS_AUTRES_RENTES("autresRentes"),
    REVENUS_AUTRES_REVENUS("autresRevenus"),
    REVENUS_BRUT_IMPOT_SOURCE("revenuBrutImpotSource"),
    REVENUS_CONTRAT_ENTRETIENS_VIAGER("contratEntretiensViager"),
    REVENUS_DEDUCTION_FRANCHISE("revenuDeductionFranchise"),
    REVENUS_DETERMINANT("revenuDeterminant"),
    REVENUS_DROIT_HABITATION("droitHabitation"),
    REVENUS_ENFANTS("revenusEnfants"),
    REVENUS_ENFANTS_DETAIL_XML("revenusEnfantsDetailXml"),
    REVENUS_FORTUNE_NETTE_MODIF("fortuneNetteModif"),
    REVENUS_HYPOTHETIQUE("revenuHypothetique"),
    REVENUS_HYPOTHETIQUE_MODIF("revenuHypothetiqueModif"),
    REVENUS_INDEMNITE_JOURNALIERE_ACCIDENTS("indemniteJournaliereAccidents"),
    REVENUS_INDEMNITE_JOURNALIERE_AI("indemniteJournaliereAI"),
    REVENUS_INDEMNITE_JOURNALIERE_APG("indemniteJournaliereaAPG"),
    REVENUS_INDEMNITE_JOURNALIERE_CHOMAGE("indemniteJournaliereChomage"),
    REVENUS_INDEMNITE_JOURNALIERE_MALADIE("indemniteJournaliereMaladie"),
    REVENUS_INDEMNITE_JOURNALIERE_MILITAIRE("indemniteJournaliereMilitaire"),
    REVENUS_INDEMNITES_JOURNALIERES("indemnitesJournalieres"),
    REVENUS_INDEPENDANT_CONJOINT("revenuIndependantConjoint"),
    REVENUS_INDEPENDANT_REQUERANT("revenuIndependantRequerant"),
    REVENUS_INTERET_FORTUNE("interetFortune"),
    REVENUS_INTERET_FORTUNE_DESSAISIE("interetFortuneDessaisie"),
    REVENUS_LOYERS_ET_FERMAGES("loyersEtFermages"),
    REVENUS_PENSION_ALIMENTAIRE("pensionAlimentaire"),
    REVENUS_PRESTATIONS_RECUEES("prestationsRecuees"),
    REVENUS_RENDEMENT_FORTUNE_IMMOBILIERE("rendementFortuneImmobiliere"),
    REVENUS_RENDEMENT_FORTUNE_MOBILIERE("rendementFortuneMobiliere"),
    REVENUS_SALAIRE_NATURE_CONJOINT("salaireNatureConjoint"),
    REVENUS_SALAIRE_NATURE_REQUERANT("salaireNatureRequerant"),
    REVENUS_SALAIRE_NET_CONJOINT("salaireNetConjoint"),
    REVENUS_SALAIRE_NET_REQUERANT("salaireNetRequerant"),
    REVENUS_SOUS_LOCATION("sousLocation"),
    REVENUS_SUCCESSION_NON_PARTAGEE("successionNonPartagee"),
    REVENUS_TAUX_FORTUNE("tauxFortune"),
    REVENUS_TOTAL_RENTES("totalRentes"),
    REVENUS_VALEUR_LOCATIVE_PROPRE_IMMEUBLE("valeurLocativePropreImmeuble"),
    REVENUS_VALEUR_USUFRUIT("valeurUsufruit");

    private String codeChamp;

    private OutputData(String codeChamp) {
        this.codeChamp = codeChamp;
    }

    /**
     * @return the codeChamp
     */
    public String getCodeChamp() {
        return codeChamp;
    }

}
