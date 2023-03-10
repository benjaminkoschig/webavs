package ch.globaz.perseus.business.constantes;

import java.util.ArrayList;

public enum CSTypeSoin {
    ACCOMPAGNEMENT_SOCIAL("55032281", "55031063"),
    AIDE_AU_MENAGE_PAR_AIDE_PRIVEE("55032277", "55031062"),
    AIDE_AU_MENAGE_PAR_CONTRAT_DE_TRAVAIL("55032278", "55031062"),
    AIDE_AU_MENAGE_PAR_SPITEX("55032276", "55031062"),
    AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE("55032280", "55031062"),
    AIDE_AU_MENAGE_PAR_UNE_ORGANISATION("55032279", "55031062"),
    ANIMATION("55032284", "55031063"),
    APPAREIL_ACOUSTIQUE("55032211", "55031054"),
    APPAREIL_ACOUSTIQUE_55("55032219", "55031055"),
    APPAREIL_ORTHOPHONIQUE("55032225", "55031055"),
    APPAREIL_ORTHOPHONIQUE_54("55032216", "55031054"),
    APPAREIL_RESPIRATOIRE("55032260", "55031060"),
    APPAREIL_RESPIRATOIRE_56("55032240", "55031056"),
    AU_LIEU_DU_TRAITEMENT_MEDICAL("55032295", "55031065"),
    BARRIERES("55032253", "55031058"),
    CANNE_LONGUE_D_AVEUGLE("55032265", "55031060"),
    CANNE_LONGUE_D_AVEUGLE_56("55032245", "55031056"),
    CHAISE_POUR_COXARTHROSE("55032264", "55031060"),
    CHAISE_POUR_COXARTHROSE_56("55032244", "55031056"),
    CHAISES_PERCEES("55032250", "55031057"),
    CHAISES_PERCEES_52("55032204", "55031052"),
    CHAISES_PERCEES_55("55032228", "55031055"),
    CHAUSSURES_ORTHOPEDIQUES("55032221", "55031055"),
    CHAUSSURES_ORTHOPEDIQUES_54("55032212", "55031054"),
    CHIEN_GUIDE_POUR_AVEUGLE("55032266", "55031060"),
    CORSET_ORTHOPEDIQUE("55032230", "55031055"),
    CORSET_ORTHOPEDIQUE_52("55032206", "55031052"),
    COTISATIONS_AVSAF_PARITAIRES("55032200", "55031050"),
    COTISATIONS_LAA("55032201", "55031050"),
    COTISATIONS_LPP("55032202", "55031050"),
    COTISATIONS_PARITAIRES("55031050", ""),
    COURT_SEJOUR("55032274", "55031061"),
    CURE_THERMALE("55032270", "55031061"),
    DANS_UN_ATELIER_PROTEGE_OU_AUTRE_LIEU_DE_FORMATION("55032296", "55031065"),
    DANS_UN_UAT_OU_ACCUEIL_DE_JOUR("55032297", "55031065"),
    DEVIS_DENTAIRE("55031068", ""),
    DEVIS_DENTAIRE_68("55032301", "55031068"),
    DISPOSITIF_AUTOMATIQUE_COMMANDE_DU_TELEPHONE("55032239", "55031056"),
    DISPOSITIF_AUTOMATIQUE_COMMANDE_TELEPHONE("55032259", "55031060"),
    ELEVATEUR_POUR_MALADE("55032263", "55031060"),
    ELEVATEUR_POUR_MALADE_56("55032243", "55031056"),
    ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL("55031063", ""),
    ENCADREMENT_SECURITAIRE("55032283", "55031063"),
    ENCADREMENT_SOCIO_EDUCATIF("55032282", "55031063"),
    EPITHESES_DE_L_OEIL("55032226", "55031055"),
    EPITHESES_DE_L_OEIL_54("55032217", "55031054"),
    EXPERTISE_DENTISTE_CONSEIL("55032293", "55031064"),
    FAUTEUIL_ROULANT_PAS_DANS_HOME("55032222", "55031055"),
    FAUTEUIL_ROULANT_PAS_DANS_HOME_54("55032213", "55031054"),
    FRAIS_ATTESTATION_EXPERTISE_EXAMEN("55032303", "55031064"),
    FRAIS_D_ENDOPROTHESES("55032209", "55031052"),
    FRAIS_DE_LIVRAISON("55031058", ""),
    FRAIS_DE_STAGE("55032272", "55031061"),
    FRAIS_DE_TRAITEMENT_DENTAIRE("55031064", ""),
    FRAIS_DE_TRANSPORT("55031065", ""),
    FRAIS_REFUSES("55031067", ""),
    FRAIS_REFUSES_67("55032300", "55031067"),
    FRANCHISE_ET_QUOTEPARTS("55031066", ""),
    FRANCHISE_ET_QUOTEPARTS_66("55032299", "55031066"),
    INHALATEUR("55032261", "55031060"),
    INHALATEUR_56("55032241", "55031056"),
    INSTALLATION_SANITAIRE_AUTOMATIQUE_COMPLEMENTAIRE("55032262", "55031060"),
    INSTALLATION_SANITAIRE_AUTOMATQIUE_COMPLEMENTAIRE("55032242", "55031056"),
    LABORATOIRE("55032294", "55031064"),
    LIT_ELECTRIQUE("55032251", "55031058"),
    LIT_ELECTRIQUE_55("55032233", "55031055"),
    LIT_ELECTRIQUE_56("55032249", "55031057"),
    LOCATION_DE_MOYENS_AUXILIAIRES_SUBSIDIAIREMENT_A_L_AI("55031057", ""),
    LOMBOSTAT_ORTHOPEDIQUE("55032229", "55031055"),
    LOMBOSTAT_ORTHOPEDIQUE_52("55032205", "55031052"),
    LONG_SEJOUR_DE_COURTE_DUREE("55032275", "55031061"),
    LUNETTES_VERRES_DE_CONTACT("55032232", "55031055"),
    LUNETTES_VERRES_DE_CONTACT_52("55032208", "55031052"),
    LUNNETTES_LOUPE("55032224", "55031055"),
    LUNNETTES_LOUPE_54("55032215", "55031054"),
    MACHINE_A_ECRIRE_AUTOMATIQUE("55032256", "55031060"),
    MACHINE_A_ECRIRE_AUTOMATIQUE_56("55032236", "55031056"),
    MACHINE_A_ECRIRE_ELECTRIQUE("55032255", "55031060"),
    MACHINE_A_ECRIRE_ELECTRIQUE_56("55032235", "55031056"),
    MACHINE_A_ECRIRE_EN_BRAILLE("55032267", "55031060"),
    MACHINE_A_ECRIRE_EN_BRAILLE_56("55032247", "55031056"),
    MAGNETOPHONE_POUR_AVEUGLE("55032268", "55031060"),
    MAGNETOPHONE_POUR_AVEUGLE_56("55032248", "55031056"),
    MAGNETOPHONE_POUR_PARALYSE("55032257", "55031060"),
    MAGNETOPHONE_POUR_PARALYSE_56("55032237", "55031056"),
    MAINTIEN_A_DOMICILE("55031062", ""),
    MEDICAMENTS("55032285", "55031064"),
    MINERVE("55032207", "55031052"),
    MINERVE_ORTHESE_DU_TRONC("55032231", "55031055"),
    MOYENS_AUXILIAIRES("55031052", ""),
    MOYENS_AUXILIAIRES_OMAV("55031054", ""),
    MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI("55031060", ""),
    PENSION_HOME_DE_JOUR("55032269", "55031061"),
    PERRUQUE("55032223", "55031055"),
    PERRUQUE_54("55032214", "55031054"),
    PILES_ACOUSTIQUES("55032302", "55031052"),
    PILES_POUR_APPAREIL_ACOUSTIQUE("55032220", "55031055"),
    POTENCE("55032252", "55031058"),
    POTENCE_55("55032234", "55031055"),
    PROTHESE_DENTAIRE("55032287", "55031064"),
    PROTHESE_DENTAIRE_PMU("55032290", "55031064"),
    PROTHESE_FACIALE_EPITHESES("55032227", "55031055"),
    PROTHESE_FACIALE_EPITHESES_54("55032218", "55031054"),
    REGIME_ALIMENTAIRE("55031051", ""),
    REGIME_ALIMENTAIRE_51("55032203", "55031051"),
    REGIME_ALIMENTAIRE_DIABETIQUE("55032304", "55031051"),
    REPARATION_DE_PROTHESE_DENTAIRE("55032288", "55031064"),
    REPARATION_PROTHESE_DENTAIRE_PMU("55032291", "55031064"),
    REPARTITION_DES_MOYENS_AUXILIAIRES("55031055", ""),
    REPARTITION_DES_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI("55031056", ""),
    REPRISE_DE_LIT_ELECTRIQUE("55031059", ""),
    REPRISE_DE_LIT_ELECTRIQUE_59("55032254", "55031059"),
    RETOUCHES_COUTEUSES_DE_CHAUSSURES("55031053", ""),
    RETOUCHES_COUTEUSES_DE_CHAUSSURES_53("55032210", "55031053"),
    SEJOUR_DE_CONVALESCENCE("55032271", "55031061"),
    SOINS_VETERINAIRES_CHIEN_GUIDE_POUR_AVEUGLE("55032246", "55031056"),
    STRUCTURE_ET_SEJOURS("55031061", ""),
    TOURNEUR_DE_PAGE("55032258", "55031060"),
    TOURNEUR_DE_PAGE_56("55032238", "55031056"),
    TRAITEMENT_DENTAIRE("55032286", "55031064"),
    TRAITEMENT_DENTAIRE_PMU("55032289", "55031064"),
    TRAITEMENT_ORTHODONTIQUE_PMU("55032292", "55031064"),
    UNITE_D_ACCUEIL_TEMPORAIRE("55032273", "55031061"),
    VISITE_CHEZ_LES_PARENTS_ENFANT_EN_EMS("55032298", "55031065");

    /**
     * Permet de retrouver un ?l?ment d'?numeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code syst?me
     * @return Le type d'enum correspondant
     */
    public static CSTypeSoin getEnumFromCodeSystem(String codeSystem) {
        CSTypeSoin[] allValues = CSTypeSoin.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeSoin vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    public static ArrayList<CSTypeSoin> getSousTypesFromCodeSystem(String codeSystem) {
        CSTypeSoin[] allValues = CSTypeSoin.values();
        ArrayList<CSTypeSoin> sousTypes = new ArrayList<CSTypeSoin>();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeSoin vm = allValues[i];
            if (vm.codeSystemParent.equals(codeSystem)) {
                sousTypes.add(vm);
            }
        }
        return sousTypes;
    }

    private String codeSystem;
    private String codeSystemParent;

    private CSTypeSoin(String codeSystem, String codeSystemParent) {
        this.codeSystem = codeSystem;
        this.codeSystemParent = codeSystemParent;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public String getCodeSystemParent() {
        return codeSystemParent;
    }

}
