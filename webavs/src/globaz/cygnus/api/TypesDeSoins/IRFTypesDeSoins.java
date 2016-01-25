package globaz.cygnus.api.TypesDeSoins;

public interface IRFTypesDeSoins {

    // CONSTANTES POUR LES CS TYPES DE SOINS
    public static final String CS_COTISATIONS_PARITAIRES_01 = "66000050";
    public static final String CS_DEVIS_DENTAIRE_19 = "66000068";
    public static final String CS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_14 = "66000063";
    public static final String CS_FINANCEMENT_DES_SOINS_20 = "66000069";
    public static final String CS_FRAIS_DE_LIVRAISON_09 = "66000058";
    public static final String CS_FRAIS_DE_TRAITEMENT_DENTAIRE_15 = "66000064";
    public static final String CS_FRAIS_DE_TRANSPORT_16 = "66000065";
    public static final String CS_FRAIS_REFUSES_18 = "66000067";
    public static final String CS_FRANCHISE_ET_QUOTEPARTS_17 = "66000066";
    public static final String CS_LOCATION_DE_MOYENS_AUXILIAIRES_SUBSIDIAIREMENT_A_L_AI_08 = "66000057";
    public static final String CS_MAINTIEN_A_DOMICILE_13 = "66000062";
    public static final String CS_MOYENS_AUXILIAIRES_03 = "66000052";
    public static final String CS_MOYENS_AUXILIAIRES_05 = "66000054";
    public static final String CS_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI_11 = "66000060";
    public static final String CS_REGIME_ALIMENTAIRE_02 = "66000051";
    public static final String CS_REPARTITION_DES_MOYENS_AUXILIAIRES_06 = "66000055";
    public static final String CS_REPARTITION_DES_MOYENS_AUXILIAIRES_REMIS_EN_PRET_SUBSIDIAIREMENT_A_L_AI_07 = "66000056";
    public static final String CS_REPRISE_DE_LIT_ELECTRIQUE_10 = "66000059";
    public static final String CS_RETOUCHES_COUTEUSES_DE_CHAUSSURES_04 = "66000053";
    public static final String CS_STRUCTURE_ET_SEJOURS_12 = "66000061";

    // CONSTANTES POUR LES CS SOUS TYPES DE SOINS
    public static final String st_1_COTISATIONS_AVSAF_PARITAIRES = "66000200";
    public static final String st_1_COTISATIONS_LAA = "66000201";
    public static final String st_1_COTISATIONS_LPP = "66000202";
    public static final String st_10_REPRISE_DE_LIT_ELECTRIQUE = "66000254";
    public static final String st_11_APPAREIL_RESPIRATOIRE = "66000260";
    public static final String st_11_CANNE_LONGUE_D_AVEUGLE = "66000265";
    public static final String st_11_CHAISE_POUR_COXARTHROSE = "66000264";
    public static final String st_11_CHIEN_GUIDE_POUR_AVEUGLE = "66000266";
    public static final String st_11_DISPOSITIF_AUTOMATIQUE_COMMANDE_TELEPHONE = "66000259";
    public static final String st_11_ELEVATEUR_POUR_MALADE = "66000263";
    public static final String st_11_INHALATEUR = "66000261";
    public static final String st_11_INSTALLATION_SANITAIRE_AUTOMATIQUE_COMPLEMENTAIRE = "66000262";
    public static final String st_11_MACHINE_A_ECRIRE_AUTOMATIQUE = "66000256";
    public static final String st_11_MACHINE_A_ECRIRE_ELECTRIQUE = "66000255";
    public static final String st_11_MACHINE_A_ECRIRE_EN_BRAILLE = "66000267";
    public static final String st_11_MAGNETOPHONE_POUR_AVEUGLE = "66000268";
    public static final String st_11_MAGNETOPHONE_POUR_PARALYSE = "66000257";
    public static final String st_11_TOURNEUR_DE_PAGE = "66000258";
    public static final String st_12_COURT_SEJOUR = "66000274";
    public static final String st_12_CURE_THERMALE = "66000270";
    public static final String st_12_FRAIS_DE_STAGE = "66000272";
    public static final String st_12_LONG_SEJOUR = "66000275";
    public static final String st_12_PENSION_HOME_DE_JOUR_14OMPC = "66000269";
    public static final String st_12_SEJOUR_DE_CONVALESCENCE = "66000271";
    public static final String st_12_SUPPLEMENT_FRAIS_DE_PENSION = "66000447";
    public static final String st_12_UNITE_D_ACCUEIL_TEMPORAIRE = "66000273";
    public static final String st_13_AIDE_AU_MENAGE_AVANCES = "66000448";
    public static final String st_13_AIDE_AU_MENAGE_PAR_AIDE_PRIVEE = "66000277";
    public static final String st_13_AIDE_AU_MENAGE_PAR_CONTRAT_DE_TRAVAIL = "66000278";
    public static final String st_13_AIDE_AU_MENAGE_PAR_SPITEX_OMSV_CMS = "66000276";
    public static final String st_13_AIDE_AU_MENAGE_PAR_UN_MEMBRE_DE_LA_FAMILLE_13bOMPC = "66000280";
    public static final String st_13_AIDE_AU_MENAGE_PAR_UNE_ORGANISATION_OSAD = "66000279";
    public static final String st_14_ACCOMPAGNEMENT_SOCIAL = "66000281";
    public static final String st_14_ANIMATION = "66000284";
    public static final String st_14_ENCADREMENT_SECURITAIRE = "66000283";
    public static final String st_14_ENCADREMENT_SOCIO_EDUCATIF = "66000282";
    public static final String st_15_EXPERTISE_DENTISTE_CONSEIL = "66000293";
    public static final String st_15_LABORATOIRE = "66000294";
    public static final String st_15_MEDICAMENTS = "66000285";
    public static final String st_15_PROTHESE_DENTAIRE = "66000287";
    public static final String st_15_PROTHESE_DENTAIRE_PMU = "66000290";
    public static final String st_15_REPARATION_DE_PROTHESE_DENTAIRE = "66000288";
    public static final String st_15_REPARATION_PROTHESE_DENTAIRE_PMU = "66000291";
    public static final String st_15_TRAITEMENT_DENTAIRE = "66000286";
    public static final String st_15_TRAITEMENT_DENTAIRE_PMU = "66000289";
    public static final String st_15_TRAITEMENT_ORTHODONTIQUE_PMU = "66000292";
    public static final String st_16_AMBULANCE = "66000306";
    public static final String st_16_AU_LIEU_DU_TRAITEMENT_MEDICAL = "66000295";
    public static final String st_16_DANS_UN_ATELIER_PROTEGE_OU_AUTRE_LIEU_DE_FORMATION_REHABILITATION = "66000296";
    public static final String st_16_DANS_UN_UAT_OU_ACCUEIL_DE_JOUR = "66000297";
    public static final String st_16_VISITE_CHEZ_LES_PARENTS_ENFANT_EN_EMS = "66000298";
    public static final String st_16_Taxi = "66000449";
    public static final String st_17_FRANCHISE_ET_QUOTEPARTS = "66000299";
    public static final String st_18_FRAIS_REFUSES = "66000300";
    public static final String st_19_DEVIS_DENTAIRE = "66000301";
    public static final String st_2_REGIME_ALIMENTAIRE = "66000203";
    public static final String st_2_REGIME_ALIMENTAIRE_DIABETIQUE = "66000304";
    public static final String st_20_FINANCEMENT_DES_SOINS = "66000305";
    public static final String st_3_CHAISES_PERCEES = "66000204";
    public static final String st_3_CORSET_ORTHOPEDIQUE = "66000206";
    public static final String st_3_FRAIS_D_ENDOPROTHESES = "66000209";
    public static final String st_3_LOMBOSTAT_ORTHOPEDIQUE = "66000205";
    public static final String st_3_LUNETTES_VERRES_DE_CONTACT = "66000208";
    public static final String st_3_MINERVE = "66000207";
    public static final String st_4_RETOUCHES_COUTEUSES_DE_CHAUSSURES = "66000210";
    public static final String st_5_APPAREIL_ACOUSTIQUE = "66000211";
    public static final String st_5_APPAREIL_ORTHOPHONIQUE = "66000216";
    public static final String st_5_CHAUSSURES_ORTHOPEDIQUES = "66000212";
    public static final String st_5_EPITHESES_DE_L_OEIL = "66000217";
    public static final String st_5_FAUTEUIL_ROULANT_PAS_DANS_HOME = "66000213";
    public static final String st_5_LUNNETTES_LOUPE = "66000215";
    public static final String st_5_PERRUQUE = "66000214";
    public static final String st_5_PROTHESE_FACIALE_EPITHESES = "66000218";
    public static final String st_6_APPAREIL_ACOUSTIQUE = "66000219";
    public static final String st_6_APPAREIL_ORTHOPHONIQUE = "66000225";
    public static final String st_6_CHAISES_PERCEES = "66000228";
    public static final String st_6_CHAUSSURES_ORTHOPEDIQUE = "66000221";
    public static final String st_6_CORSET_ORTHOPEDIQUE = "66000230";
    public static final String st_6_EPITHESES_DE_L_OEIL = "66000226";
    public static final String st_6_FAUTEUIL_ROULANT_PAS_DANS_HOME = "66000222";
    public static final String st_6_LIT_ELECTRIQUE = "66000233";
    public static final String st_6_LOMBOSTAT_ORTHOPEDIQUE = "66000229";
    public static final String st_6_LUNETTES_VERRES_DE_CONTACT = "66000232";
    public static final String st_6_LUNNETTES_LOUPE = "66000224";
    public static final String st_6_MINERVE_ORTHESE_DU_TRONC = "66000231";
    public static final String st_6_PERRUQUE = "66000223";
    public static final String st_6_PILES_POUR_APPAREIL_ACOUSTIQUE = "66000220";
    public static final String st_6_POTENCE = "66000234";
    public static final String st_6_PROTHESE_FACIALE_EPITHESES = "66000227";
    public static final String st_7_APPAREIL_RESPIRATOIRE = "66000240";
    public static final String st_7_CANNE_LONGUE_D_AVEUGLE = "66000245";
    public static final String st_7_CHAISE_POUR_COXARTHROSE = "66000244";
    public static final String st_7_DISPOSITIF_AUTOMATIQUE_COMMANDE_DU_TELEPHONE = "66000239";
    public static final String st_7_ELEVATEUR_POUR_MALADE = "66000243";
    public static final String st_7_INHALATEUR = "66000241";
    public static final String st_7_INSTALLATION_SANITAIRE_AUTOMATQIUE_COMPLEMENTAIRE = "66000242";
    public static final String st_7_MACHINE_A_ECRIRE_AUTOMATIQUE = "66000236";
    public static final String st_7_MACHINE_A_ECRIRE_ELECTRIQUE = "66000235";
    public static final String st_7_MACHINE_A_ECRIRE_EN_BRAILLE = "66000247";
    public static final String st_7_MAGNETOPHONE_POUR_AVEUGLE = "66000248";
    public static final String st_7_MAGNETOPHONE_POUR_PARALYSE = "66000237";
    public static final String st_7_SOINS_VETERINAIRES_CHIEN_GUIDE_POUR_AVEUGLE = "66000246";
    public static final String st_7_TOURNEUR_DE_PAGE = "66000238";
    public static final String st_8_CHAISES_PERCEES = "66000250";
    public static final String st_8_LIT_ELECTRIQUE = "66000249";
    public static final String st_9_BARRIERES = "66000253";
    public static final String st_9_LIT_ELECTRIQUE = "66000251";
    public static final String st_9_POTENCE = "66000252";

}
