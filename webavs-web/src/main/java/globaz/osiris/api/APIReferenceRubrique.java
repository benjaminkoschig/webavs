/*
 * Cr�� le 7 sept. 05
 */
package globaz.osiris.api;

/**
 * @author sch date : 7 sept. 05
 */
public interface APIReferenceRubrique {

    String AF_FONDS_DE_COMPENSATION = "237036";
    String AF_LFA = "237051";
    String AF_LJA = "237052";
    String AF_PRESTATIONS = "237046";
    String AF_PRESTATIONS_A_RESTITUER = "237055";

    int AK_CODE_REFERENCE = 1;
    String AMENDE_POUR_DEFAUT_DE_PAIEMENT_AVS = "237049";
    String AMENDE_POUR_DEFAUT_DE_PAIEMENT_PS = "237050";
    String APG_ACM_COTISATIONS_AC = "237044";
    String APG_ACM_COTISATIONS_AVS = "237043";

    String APG_ACM_FONDS_DE_COMPENSATION = "237054";
    String APG_ACM_MONTANT_BRUT = "237041";
    String APG_ACM_NE_COMPENSATION_FNE = "237298";
    String APG_ACM_NE_COMPENSATION_MECP = "237288";
    String APG_ACM_NE_COMPENSATION_PP = "237293";

    String APG_ACM_NE_COTISATION_FNE = "237302";
    String APG_ACM_NE_FNE = "237297";
    String APG_ACM_NE_FOND_COMPENSATION_FNE = "237301";
    String APG_ACM_NE_FOND_COMPENSATION_MECP = "237291";
    String APG_ACM_NE_FOND_COMPENSATION_PP = "237296";
    String APG_ACM_NE_IMPOT_SOURCE_FNE = "237300";

    String APG_ACM_NE_IMPOT_SOURCE_MECP = "237290";
    String APG_ACM_NE_IMPOT_SOURCE_PP = "237295";
    String APG_ACM_NE_MECP = "237287";
    String APG_ACM_NE_PP = "237292";
    String APG_ACM_NE_RESTITUTION_FNE = "237299";
    String APG_ACM_NE_RESTITUTION_MECP = "237289";

    String APG_ACM_NE_RESTITUTION_PP = "237294";

    String APG_ACM_RESTITUTION = "237042";

    String APG_AVEC_AC_ASSURE = "237015";
    String APG_AVEC_AC_INDEPENDANT = "237495";

    String APG_SANS_AC_ASSURE = "237016";
    String APG_SANS_AC_INDEPENDANT = "237496";

    String APG_COTISATION_AC_ASSURE = "237020";
    String APG_COTISATION_AC_INDEPENDANT = "237500";

    String APG_COTISATION_AVS_ASSURE = "237019";
    String APG_COTISATION_AVS_INDEPENDANT = "237499";

    String APG_COTISATION_LFA = "237023";
    String APG_EMPLOYEUR_AVEC_AC = "237013";
    String APG_EMPLOYEUR_SANS_AC = "237014";

    String APG_FONDS_DE_COMPENSATION_ASSURE = "237021";
    String APG_FONDS_DE_COMPENSATION_EMPLOYEUR = "237501";
    String APG_FONDS_DE_COMPENSATION_INDEPENDANT = "237511";

    String APG_FRAIS_ADMINISTRATION = "237024";
    String APG_IMPOT_A_LA_SOURCE = "237022";
    String APG_IMPOT_A_LA_SOURCE_ACM = "237110";

    String APG_PRESTATION_A_RESTITUER_ASSURE = "237018";
    String APG_PRESTATION_A_RESTITUER_EMPLOYEUR = "237498";
    String APG_PRESTATION_A_RESTITUER_INDEPENDANT = "237508";

    String APG_SANS_COTISATIONS = "237017";

    String APG_COMPCIAB_JU_PARITAIRE_PARTICIPATION = "237520";
    String APG_COMPCIAB_JU_PARITAIRE_MONTANT_BRUT = "237521";
    String APG_COMPCIAB_JU_PARITAIRE_MONTANT_RESTITUTION = "237522";
    String APG_COMPCIAB_JU_PERSONNEL_COTISATIONS_AC = "237523";
    String APG_COMPCIAB_JU_PERSONNEL_COTISATIONS_AVS = "237524";
    String APG_COMPCIAB_JU_PERSONNEL_PARTICIPATION = "237525";
    String APG_COMPCIAB_JU_PERSONNEL_MONTANT_BRUT = "237526";
    String APG_COMPCIAB_JU_PERSONNEL_MONTANT_RESTITUTION = "237527";
    String APG_COMPCIAB_BE_PARITAIRE_PARTICIPATION = "237528";
    String APG_COMPCIAB_BE_PARITAIRE_MONTANT_BRUT = "237529";
    String APG_COMPCIAB_BE_PARITAIRE_MONTANT_RESTITUTION = "237530";
    String APG_COMPCIAB_BE_PERSONNEL_COTISATIONS_AC = "237531";
    String APG_COMPCIAB_BE_PERSONNEL_COTISATIONS_AVS = "237532";
    String APG_COMPCIAB_BE_PERSONNEL_PARTICIPATION = "237533";
    String APG_COMPCIAB_BE_PERSONNEL_MONTANT_BRUT = "237534";
    String APG_COMPCIAB_BE_PERSONNEL_MONTANT_RESTITUTION = "237535";
    String APG_COMPCIAB_JU_PARITAIRE_IMPOT_SOURCE = "237536";
    String APG_COMPCIAB_JU_PERSONNEL_IMPOT_SOURCE = "237537";
    String APG_COMPCIAB_BE_PARITAIRE_IMPOT_SOURCE = "237538";
    String APG_COMPCIAB_BE_PERSONNEL_IMPOT_SOURCE = "237539";

    // *** R�f�rence prestation MATCIAB ******************
    String MATCIAB_JU_PARITAIRE_PARTICIPATION = "237560";
    String MATCIAB_JU_PARITAIRE_MONTANT_BRUT = "237561";
    String MATCIAB_JU_PARITAIRE_MONTANT_RESTITUTION = "237562";
    String MATCIAB_JU_PERSONNEL_COTISATIONS_AC = "237563";
    String MATCIAB_JU_PERSONNEL_COTISATIONS_AVS = "237564";
    String MATCIAB_JU_PERSONNEL_PARTICIPATION = "237565";
    String MATCIAB_JU_PERSONNEL_MONTANT_BRUT = "237566";
    String MATCIAB_JU_PERSONNEL_MONTANT_RESTITUTION = "237567";
    String MATCIAB_BE_PARITAIRE_PARTICIPATION = "237568";
    String MATCIAB_BE_PARITAIRE_MONTANT_BRUT = "237569";
    String MATCIAB_BE_PARITAIRE_MONTANT_RESTITUTION = "237570";
    String MATCIAB_BE_PERSONNEL_COTISATIONS_AC = "237571";
    String MATCIAB_BE_PERSONNEL_COTISATIONS_AVS = "237572";
    String MATCIAB_BE_PERSONNEL_PARTICIPATION = "237573";
    String MATCIAB_BE_PERSONNEL_MONTANT_BRUT = "237574";
    String MATCIAB_BE_PERSONNEL_MONTANT_RESTITUTION = "237575";
    // **************************************************


    String API_AI = "237068";
    String API_AI_EXTOURNE = "237080";
    String API_AI_RETROACTIF = "237074";
    String API_AVS = "237065";
    String API_AVS_EXTOURNE = "237077";
    String API_AVS_RETROACTIF = "237071";
    String AUTRES_FRAIS_CONTENTIEUX = "237102";
    String AUTRES_FRAIS_CONTENTIEUX_PARITAIRE = "237107";
    String COMPENSATION_ALFA = "237056";
    String COMPENSATION_APG_MAT = "237095";
    String COMPENSATION_IJAI = "237096";
    String COMPENSATION_IRRECOUVRABLE = "237094";
    String COMPENSATION_LETTRAGE = "237090";
    String COMPENSATION_LISSAGE = "237098";
    String COMPENSATION_PCFAMILLES = "237150";
    String COMPENSATION_RENTES = "237097";
    String COMPENSATION_REPORT_DE_SOLDE = "237092";
    String COMPENSATION_REPORT_DE_TAXE = "237093";
    String COMPENSATION_TRANSFERT_DE_SOLDE = "237091";
    String FRAIS_AVOCAT = "237101";
    String FRAIS_AVOCAT_PARITAIRE = "237106";
    String FRAIS_MAINLEVEE = "237100";
    String FRAIS_MAINLEVEE_PARITAIRE = "237105";
    // R�f�rence rubrique pour l'extrait de compte des documents AQUILA
    String FRAIS_POURSUITES = "237099";
    String FRAIS_POURSUITES_AMORTIS = "237103";
    String FRAIS_POURSUITES_AMORTIS_PARITAIRE = "237108";
    String FRAIS_POURSUITES_PARITAIRE = "237104";
    String FRAIS_VARIABLES = "237047";
    String IJAI_ASSURE_OU_INDEPENDANT = "237026";
    String IJAI_COTISATIONS_AC = "237029";
    String IJAI_COTISATIONS_AVS = "237028";
    String IJAI_COTISATIONS_LFA = "237045";
    String IJAI_EMPLOYEUR = "237025";
    String IJAI_FONDS_DE_COMPENSATION = "237030";
    String IJAI_FRAIS_ADMINISTRATION = "237032";
    String IJAI_IMPOT_A_LA_SOURCE = "237031";
    String IJAI_PRESTATIONS_A_RESTITUER = "237027";
    String IMPOT_A_LA_SOURCE = "237081";
    String IMPUTATION_DE_COTISATION_PERSONNELLE = "237059";
    String IMPUTATION_DE_FAD_COTISATION_PERSONNELLE = "237089";
    String INTERETS_MORATOIRES_AI = "237087";
    String INTERETS_MORATOIRES_AVS = "237088";
    String INTERETS_VARIABLES = "237048";
    String MATERNITE_ACM_COTISATIONS_AC = "237040";
    String MATERNITE_ACM_COTISATIONS_AVS = "237039";
    String MATERNITE_ACM_FONDS_DE_COMPENSATION = "237053";
    String MATERNITE_ACM_MONTANT_BRUT = "237037";
    String MATERNITE_ACM_RESTITUTION = "237038";

    String MATERNITE_AVEC_AC_ASSURE = "237003";
    String MATERNITE_AVEC_AC_INDEPENDANT = "237473";

    String MATERNITE_SANS_AC_ASSURE = "237004";
    String MATERNITE_SANS_AC_INDEPENDANT = "237474";

    String MATERNITE_COTISATION_AC_ASSURE = "237008";
    String MATERNITE_COTISATION_AC_INDEPENDANT = "237478";

    String MATERNITE_COTISATION_AVS_ASSURE = "237007";
    String MATERNITE_COTISATION_AVS_INDEPENDANT = "237477";

    String MATERNITE_COTISATION_LFA = "237011";
    String MATERNITE_EMPLOYEUR_AVEC_AC = "237001";
    String MATERNITE_EMPLOYEUR_SANS_AC = "237002";

    String MATERNITE_FONDS_DE_COMPENSATION_ASSURE = "237009";
    String MATERNITE_FONDS_DE_COMPENSATION_EMPLOYEUR = "237479";
    String MATERNITE_FONDS_DE_COMPENSATION_INDEPENDANT = "237489";

    String MATERNITE_FRAIS_ADMINISTRATION = "237012";

    String MATERNITE_IMPOT_A_LA_SOURCE_ASSURE = "237010";
    String MATERNITE_IMPOT_A_LA_SOURCE_INDEPENDANT = "237490";

    String MATERNITE_IMPOT_A_LA_SOURCE_ACM = "237112";

    String MATERNITE_IMPOT_A_LA_SOURCE_LAMAT_CANTONALE_ASSURE = "237111";
    String MATERNITE_IMPOT_A_LA_SOURCE_LAMAT_CANTONALE_INDEPENDANT = "237591";

    String MATERNITE_PRESTATION_A_RESTITUER_ASSURE = "237006";
    String MATERNITE_PRESTATION_A_RESTITUER_EMPLOYEUR = "237476";
    String MATERNITE_PRESTATION_A_RESTITUER_INDEPENDANT = "237486";


    // R�f�rence droit PANDEMIE
    String PANDEMIE_INDEMN_GARDE_ENFANTS_POUR_SALARIE = "237401";
    String PANDEMIE_INDEMN_GARDE_ENFANTS_POUR_INDEPENDANT = "237402";
    String PANDEMIE_INDEMN_QUARANTAINE_POUR_SALARIE = "237403";
    String PANDEMIE_INDEMN_QUARANTAINE_POUR_INDEPENDANT = "237404";
    String PANDEMIE_INDEMN_FERMETURE_FORCEE = "237405";
    String PANDEMIE_INDEMN_INTERDICTION_MANIFESTATION = "237406";
    String PANDEMIE_INTERETS_REMUNERATOIRES_PANDEMIE = "237407";
    String PANDEMIE_INDEMN_FRAIS_GESTION_PANDEMIE = "237408";
    String PANDEMIE_IMPOTS_A_LA_SOURCE = "237409";
    String PANDEMIE_COT_AC_SALARIE = "237410";
    String PANDEMIE_RESTIT_GARDE_ENFANTS_POUR_SALARIE = "237411";
    String PANDEMIE_RESTIT_GARDE_ENFANTS_POUR_INDEPENDANT = "237412";
    String PANDEMIE_RESTIT_QUARANTAINE_POUR_SALARIE = "237413";
    String PANDEMIE_RESTIT_QUARANTAINE_POUR_INDEPENDANT = "237414";
    String PANDEMIE_RESTIT_FERMETURE_FORCEE = "237415";
    String PANDEMIE_RESTIT_INTERDICTION_MANIFESTATION = "237416";
    String PANDEMIE_COT_AVS_ASSURE = "237417";
    String PANDEMIE_COT_AVS_INDEPENDANT = "237418";
    String PANDEMIE_COT_AC_INDEPENDANT = "237419";
    String PANDEMIE_FONDS_COMPENSATIONS_EMPLOYEUR = "237420";
    String PANDEMIE_FONDS_COMPENSATIONS_INDEPENDANT = "237421";
    String PANDEMIE_FONDS_COMPENSATIONS_ASSURE = "237422";
    String PANDEMIE_COMPENSATIONS = "237423";
    String PANDEMIE_COTISATIONS_LFA = "237424";
    String PANDEMIE_FRAIS_ADMINISTRATION = "237425";
    String PANDEMIE_CAS_RIGUEUR_10k_90k = "237426";
    String PANDEMIE_INDEMN_GARDE_ENFANTS_HANDICAP_POUR_SALARIE = "237427";
    String PANDEMIE_INDEMN_GARDE_ENFANTS_HANDICAP_POUR_INDEPENDANT = "237428";
    String PANDEMIE_RESTIT_CAS_RIGUEUR_10k_90k = "237429";
    String PANDEMIE_RESTIT_GARDE_ENFANTS_HANDICAP_POUR_SALARIE = "237430";
    String PANDEMIE_RESTIT_GARDE_ENFANTS_HANCICAP_POUR_INDEPENDANT = "237431";
    String PANDEMIE_INDEMN_SALARIE_EVENEMENTIEL = "237432";
    String PANDEMIE_RESTIT_SALARIE_EVENEMENTIEL = "237433";

    // R�f�rences rubriques 2e vague.
    String PANDEMIE_INDEPANDANT_FERMETURE = "237434";
    String PANDEMIE_RESTIT_INDEPANDANT_FERMETURE = "237435";
    String PANDEMIE_DIRIGEANT_SALARIE_FERMETURE = "237436";
    String PANDEMIE_RESTIT_DIRIGEANT_SALARIE_FERMETURE = "237437";
    String PANDEMIE_INDEPENDANT_MANIF_ANNULEE = "237438";
    String PANDEMIE_RESTIT_INDEPENDANT_MANIF_ANNULEE = "237439";
    String PANDEMIE_DIRIGEANT_SALARIE_MANIF_ANNULEE = "237440";
    String PANDEMIE_RESTIT_DIRIGEANT_SALARIE_MANIF_ANNULEE = "237441";
    String PANDEMIE_INDEPENDANT_LIMITATION_ACTIVITE = "237442";
    String PANDEMIE_RESTIT_INDEPENDANT_LIMITATION_ACTIVITE = "237443";
    String PANDEMIE_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE = "237444";
    String PANDEMIE_RESTIT_DIRIGEANT_SALARIE_LIMITATION_ACTIVITE = "237445";
    String PANDEMIE_INDEMN_GARDE_PARENTALE_17_09_20_SALARIE = "237446";
    String PANDEMIE_RESTIT_GARDE_PARENTALE_17_09_20_SALARIE = "237447";
    String PANDEMIE_INDEMN_GARDE_PARENTALE_17_09_20_INDEPENDANT = "237448";
    String PANDEMIE_RESTIT_GARDE_PARENTALE_17_09_20_INDEPENDANT = "237449";
    String PANDEMIE_INDEMN_QUARANTAINE_17_09_20_SALARIE = "237450";
    String PANDEMIE_RESTIT_QUARANTAINE_17_09_20_SALARIE = "237451";
    String PANDEMIE_INDEMN_QUARANTAINE_17_09_20_INDEPENDANT = "237452";
    String PANDEMIE_RESTIT_QUARANTAINE_17_09_20_INDEPENDANT = "237453";
    String PANDEMIE_INDEMN_GARDE_PARENTALE_HANDICAP_17_09_20_SALARIE = "237454";
    String PANDEMIE_RESTIT_GARDE_PARENTALE_HANDICAP_17_09_20_SALARIE = "237455";
    String PANDEMIE_INDEMN_GARDE_PARENTALE_HANDICAP_17_09_20_INDEPENDANT = "237456";
    String PANDEMIE_RESTIT_GARDE_PARENTALE_HANDICAP_17_09_20_INDEPENDANT = "237457";
    String PANDEMIE_INDEMN_PERSONNE_VULNERABLE_SALARIE = "237458";
    String PANDEMIE_RESTIT_PERSONNE_VULNERABLE_SALARIE = "237459";
    String PANDEMIE_INDEMN_PERSONNE_VULNERABLE_INDEPENDANT = "237460";
    String PANDEMIE_RESTIT_PERSONNE_VULNERABLE_INDEPENDANT = "237461";

    String MATERNITE_SANS_COTISATIONS = "237005";
    String MONTANT_MINIME = "237033";
    String PC_AI = "237114";
    String PC_AI_A_DOMICILE_ORDINAIRES_ESPECES = "237206";
    String PC_AI_A_RESTITUER = "237118";
    String PC_AI_ALLOCATIONS_DE_NOEL = "237284";
    String PC_AI_EN_HOME_HORS_CANTON_SASH = "237208";
    String PC_AI_EN_HOME_HORS_CANTON_SPAS = "237210";
    String PC_AI_EN_HOME_SASH = "237207";
    String PC_AI_EN_HOME_SPAS = "237209";
    String PC_AI_EN_HOME = "237304";
    String PC_AI_RESTITUTION_A_DOMICILE_ORDINAIRES_ESPECES = "237212";
    String PC_AI_RESTITUTION_EN_HOME_HORS_CANTON_SASH = "237214";
    String PC_AI_RESTITUTION_EN_HOME_HORS_CANTON_SPAS = "237216";
    String PC_AI_RESTITUTION_EN_HOME_SASH = "237213";
    String PC_AI_RESTITUTION_EN_HOME_SPAS = "237215";
    String PC_AI_RESTITUTION_EN_HOME = "237306";
    String PC_AI_PART_CANTONALE = "237593";

    String PC_AVS_EN_HOME_EPS = "237309";
    String PC_AVS_EN_HOME_HORS_CANTON_EPS = "237310";
    String PC_AI_EN_HOME_EPS = "237311";
    String PC_AI_EN_HOME_HORS_CANTON_EPS = "237312";
    String PC_AVS_RESTITUTION_EN_HOME_HORS_CANTON_EPS = "237314";
    String PC_AVS_RESTITUTION_EN_HOME_EPS = "237313";
    String PC_AI_RESTITUTION_EN_HOME_HORS_CANTON_EPS = "237316";
    String PC_AI_RESTITUTION_EN_HOME_EPS = "237315";

    String COMPENSATION_LISSAGE_HORS_AVS = "237317";

    // Rubrique PC_AVS :
    String PC_AVS = "237113";
    String PC_AVS_A_DOMICILE_ORDINAIRES_ESPECES = "237217";
    String PC_AVS_A_RESTITUER = "237117";
    String PC_AVS_ALLOCATIONS_DE_NOEL = "237205";
    String PC_AVS_EN_HOME_HORS_CANTON_SASH = "237219";
    String PC_AVS_EN_HOME_HORS_CANTON_SPAS = "237221";
    String PC_AVS_EN_HOME_SASH = "237218";
    String PC_AVS_EN_HOME_SPAS = "237220";
    String PC_AVS_EN_HOME = "237303";
    String PC_AVS_RESTITUTION_A_DOMICILE_ORDINAIRES_ESPECES = "237222";
    String PC_AVS_RESTITUTION_EN_HOME_HORS_CANTON_SASH = "237224";
    String PC_AVS_RESTITUTION_EN_HOME_HORS_CANTON_SPAS = "237226";
    String PC_AVS_RESTITUTION_EN_HOME_SASH = "237223";
    String PC_AVS_RESTITUTION_EN_HOME_SPAS = "237225";
    String PC_AVS_RESTITUTION_EN_HOME = "237305";
    String PC_AVS_PART_CANTONALE = "237592";

    // Cl� altern�e
    String PC_COMPTE_COURANT_BLOCAGE = "237233";
    String PCF_A_RESTITUER = "237142";
    String PCF_FRAIS_GARDE = "237140";
    String PCF_FRAIS_GARDE_A_RESTITUER = "237143";
    String PCF_IMPOT_SOURCE = "237149";
    String PCF_MESURE_COACHING = "237153";
    String PCF_MESURE_COACHING_A_RESTITUER = "237154";
    String PCF_PRESTATION = "237139";
    String PCF_RFM = "237141";
    String PCF_RFM_A_RESTITUER = "237144";
    String PRESTATION_AI_A_RESTITUER = "237085";
    String PRESTATION_API_AI_A_RESTITUER = "237086";
    String PRESTATION_API_AVS_A_RESTITUER = "237084";
    String PRESTATION_AVS_A_RESTITUER = "237083";

    String PRESTATION_LAMATGE_ADOPTION_ASSURE = "237061";
    String PRESTATION_LAMATGE_ADOPTION_EMPLOYEUR = "237541";
    String PRESTATION_LAMATGE_ADOPTION_INDEPENDANT = "237551";

    String PRESTATION_LAMATGE_NAISSANCE_ASSURE = "237060";
    String PRESTATION_LAMATGE_NAISSANCE_EMPLOYEUR = "237540";
    String PRESTATION_LAMATGE_NAISSANCE_INDEPENDANT = "237550";

    String PRESTATION_LAMATGE_RESTITUTION_ASSURE = "237062";
    String PRESTATION_LAMATGE_RESTITUTION_EMPLOYEUR = "237542";
    String PRESTATION_LAMATGE_RESTITUTION_INDEPENDANT = "237552";

    String REDUCTION_DE_COTISATION_PERSONNELLE = "237058";
    String REGULARISATION_CCP = "237082";
    String REMISE_AI_DSAS = "237229";
    String REMISE_AVS_DSAS = "237230";
    String REMISE_DE_COTISATION_PERSONNELLE = "237057";
    String RENTE_EXTRAORDINAIRE_AI = "237067";
    String RENTE_EXTRAORDINAIRE_AI_EXTOURNE = "237079";
    String RENTE_EXTRAORDINAIRE_AI_RETROACTIF = "237073";
    String RENTE_EXTRAORDINAIRE_AVS_EXTOURNE = "237076";
    String RENTE_EXTRAORDINAIRE_AVS_RETROACTIF = "237070";
    String RENTE_EXTRAORDINAIREAVS = "237064";
    String RENTE_ORDINAIRE_AI = "237066";
    String RENTE_ORDINAIRE_AI_EXTOURNE = "237078";
    String RENTE_ORDINAIRE_AI_RETROACTIF = "237072";
    String RENTE_ORDINAIRE_AVS = "237063";
    String RENTE_ORDINAIRE_AVS_EXTOURNE = "237075";
    String RENTE_ORDINAIRE_AVS_RETROACTIF = "237069";
    String RENTE_PONT_PRESTATION = "237145";
    String RENTE_PONT_RESTIT = "237147";
    String RENTE_PONT_RFM = "237146";
    String RENTE_PONT_RFM_RESTIT = "237148";
    String RENTES_FONDS_DE_COMPENSATION = "237035";
    String RENTES_PONT_RESTIT = "237153";
    String RENTES_PONT_RFM_RESTIT = "237154";

    String RFM_AI = "237116";// CCJU
    String RFM_AI_HOME = "237308";// CCVS

    String RFM_AI_A_RESTITUER = "237120";// CCJU
    String RFM_AI_A_RESTITUER_AIDE_AU_MENAGE = "237120";
    String RFM_AI_A_RESTITUER_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE = "237120";
    String RFM_AI_A_RESTITUER_COTISATIONS_PARITAIRES = "237120";
    String RFM_AI_A_RESTITUER_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL = "237120";
    String RFM_AI_A_RESTITUER_FRAIS_DE_PENSION_COURT_SEJOUR = "237120";
    String RFM_AI_A_RESTITUER_FRAIS_DE_TRANSPORT = "237120";
    String RFM_AI_A_RESTITUER_FRQP = "237120";
    String RFM_AI_A_RESTITUER_MOYENS_AUXILIAIRES = "237120";
    String RFM_AI_A_RESTITUER_PARTICIPATION_COURT_SEJOUR = "237120";
    String RFM_AI_A_RESTITUER_PENSION_HOME_DE_JOUR = "237120";
    String RFM_AI_A_RESTITUER_REGIME = "237120";
    String RFM_AI_A_RESTITUER_TRAITMENT_DENTAIRE = "237120";
    String RFM_AI_A_RESTITUER_UNITE_ACCUEIL_TEMPORAIRE = "237120";

    String RFM_AI_AIDE_AU_MENAGE = "237184";
    String RFM_AI_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE = "237193";
    String RFM_AI_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SASH = "237278";
    String RFM_AI_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SPAS = "237253";
    String RFM_AI_AIDE_AU_MENAGE_SASH = "237274";
    String RFM_AI_AIDE_AU_MENAGE_SPAS = "237249";

    String RFM_AI_COTISATIONS_PARITAIRES = "237190";
    String RFM_AI_COTISATIONS_PARITAIRES_SASH = "237286";
    String RFM_AI_COTISATIONS_PARITAIRES_SPAS = "237286";

    String RFM_AI_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL = "237188";
    String RFM_AI_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SASH = "237276";
    String RFM_AI_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SPAS = "237251";

    String RFM_AI_FINANCEMENT_DES_SOINS = "237122";// CCJU
    String RFM_AI_FINANCEMENT_DES_SOINS_IMPORTATION_AUTO = "237151";// CCJU

    String RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR = "237201";
    String RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR_SASH = "237282";
    String RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR_SPAS = "237257";

    String RFM_AI_FRAIS_DE_TRANSPORT = "237186";
    String RFM_AI_FRAIS_DE_TRANSPORT_SASH = "237275";
    String RFM_AI_FRAIS_DE_TRANSPORT_SPAS = "237250";

    String RFM_AI_FRQP = "237180";
    String RFM_AI_FRQP_SASH = "237272";
    String RFM_AI_FRQP_SPAS = "237247";

    String RFM_AI_MOYENS_AUXILIAIRES = "237124";// CCJU
    String RFM_AI_MOYENS_AUXILIAIRES_SASH = "237277";
    String RFM_AI_MOYENS_AUXILIAIRES_SPAS = "237252";

    String RFM_AI_PARTICIPATION_COURT_SEJOUR = "237199";
    String RFM_AI_PARTICIPATION_COURT_SEJOUR_SASH = "237281";
    String RFM_AI_PARTICIPATION_COURT_SEJOUR_SPAS = "237256";

    String RFM_AI_PENSION_HOME_DE_JOUR = "237197";
    String RFM_AI_PENSION_HOME_DE_JOUR_SASH = "237280";
    String RFM_AI_PENSION_HOME_DE_JOUR_SPAS = "237255";

    String RFM_AI_REGIME = "237203";
    String RFM_AI_REGIME_SASH = "237283";
    String RFM_AI_REGIME_SPAS = "237258";

    String RFM_AI_SASH = "237128";
    String RFM_AI_SPAS = "237126";

    String RFM_AI_TRAITEMENT_DENTAIRE = "237182";
    String RFM_AI_TRAITEMENT_DENTAIRE_SASH = "237273";
    String RFM_AI_TRAITEMENT_DENTAIRE_SPAS = "237248";

    String RFM_AI_UNITE_ACCUEIL_TEMPORAIRE = "237195";
    String RFM_AI_UNITE_ACCUEIL_TEMPORAIRE_SASH = "237279";
    String RFM_AI_UNITE_ACCUEIL_TEMPORAIRE_SPAS = "237254";

    String RFM_AVANCE_SAS = "237232";// CCJU
    String RFM_AVS = "237115";// CCJU
    String RFM_AVS_HOME = "237307";// CCVS
    String RFM_AVS_DOMICILE = "237135";// CCVS
    String RFM_AI_DOMICILE = "237136";// CCVS

    String RFM_AVS_A_RESTITUER = "237119";// CCJU
    String RFM_AVS_A_RESTITUER_AIDE_AU_MENAGE = "237119";
    String RFM_AVS_A_RESTITUER_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE = "237119";
    String RFM_AVS_A_RESTITUER_COTISATIONS_PARITAIRES = "237119";
    String RFM_AVS_A_RESTITUER_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL = "237119";
    String RFM_AVS_A_RESTITUER_FRAIS_DE_PENSION_COURT_SEJOUR = "237119";
    String RFM_AVS_A_RESTITUER_FRAIS_DE_TRANSPORT = "237119";
    String RFM_AVS_A_RESTITUER_FRQP = "237119";
    String RFM_AVS_A_RESTITUER_MOYENS_AUXILIAIRES = "237119";
    String RFM_AVS_A_RESTITUER_PARTICIPATION_COURT_SEJOUR = "237119";
    String RFM_AVS_A_RESTITUER_PENSION_HOME_DE_JOUR = "237119";
    String RFM_AVS_A_RESTITUER_REGIME = "237119";
    String RFM_AVS_A_RESTITUER_TRAITMENT_DENTAIRE = "237119";
    String RFM_AVS_A_RESTITUER_UNITE_ACCUEIL_TEMPORAIRE = "237119";

    String RFM_AVS_AIDE_AU_MENAGE = "237159";
    String RFM_AVS_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE = "237168";
    String RFM_AVS_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SASH = "237266";
    String RFM_AVS_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SPAS = "237241";
    String RFM_AVS_AIDE_AU_MENAGE_SASH = "237261";
    String RFM_AVS_AIDE_AU_MENAGE_SPAS = "237237";

    String RFM_AVS_COTISATIONS_PARITAIRES = "237165";
    String RFM_AVS_COTISATIONS_PARITAIRES_SASH = "237264";
    String RFM_AVS_COTISATIONS_PARITAIRES_SPAS = "237264";

    String RFM_AVS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL = "237163";
    String RFM_AVS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SASH = "237263";
    String RFM_AVS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SPAS = "237239";

    String RFM_AVS_FINANCEMENT_DES_SOINS = "237121";// CCJU
    String RFM_AVS_FINANCEMENT_DES_SOINS_IMPORTATION_AUTO = "237152";// CCJU

    String RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR = "237176";
    String RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR_SASH = "237270";
    String RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR_SPAS = "237245";

    String RFM_AVS_FRAIS_DE_TRANSPORT = "237161";
    String RFM_AVS_FRAIS_DE_TRANSPORT_SASH = "237262";
    String RFM_AVS_FRAIS_DE_TRANSPORT_SPAS = "237238";

    String RFM_AVS_FRQP = "237155";
    String RFM_AVS_FRQP_SASH = "237259";
    String RFM_AVS_FRQP_SPAS = "237235";

    String RFM_AVS_MOYENS_AUXILIAIRES = "237123";// CCJU
    String RFM_AVS_MOYENS_AUXILIAIRES_SASH = "237265";
    String RFM_AVS_MOYENS_AUXILIAIRES_SPAS = "237240";

    String RFM_AVS_PARTICIPATION_COURT_SEJOUR = "237174";
    String RFM_AVS_PARTICIPATION_COURT_SEJOUR_SASH = "237269";
    String RFM_AVS_PARTICIPATION_COURT_SEJOUR_SPAS = "237244";

    String RFM_AVS_PENSION_HOME_DE_JOUR = "237172";
    String RFM_AVS_PENSION_HOME_DE_JOUR_SASH = "237268";
    String RFM_AVS_PENSION_HOME_DE_JOUR_SPAS = "237243";

    String RFM_AVS_REGIME = "237178";
    String RFM_AVS_REGIME_SASH = "237271";
    String RFM_AVS_REGIME_SPAS = "237246";

    String RFM_AVS_SASH = "237127";
    String RFM_AVS_SPAS = "237125";

    String RFM_AVS_TRAITEMENT_DENTAIRE = "237157";
    String RFM_AVS_TRAITEMENT_DENTAIRE_SASH = "237260";
    String RFM_AVS_TRAITEMENT_DENTAIRE_SPAS = "237236";

    String RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE = "237170";
    String RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE_SASH = "237267";
    String RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE_SPAS = "237242";

    // R�f�rences des rubriques "int�r�ts moratoires" utilis�s dans l'importation automatique des contentieux via ELP.
    String CONTENTIEUX_INTERET_MORATOIRE_PARITAIRE = "237600";
    String CONTENTIEUX_INTERET_MORATOIRE_PERSONNEL = "237601";

    String RUBRIQUE_DE_LISSAGE = "237034";

    String TAXE_SOMMATION_BULLETIN_NEUTRE = "237109";

    /**
     * Cette m�thode retourne la rubrique charg�e en fonction du code de r�f�rence En cas de probl�me, cette m�thode
     * retourne null
     *
     * @param idCodeReference
     * @return APIRubrique
     */
    public APIRubrique getRubriqueByCodeReference(String idCodeReference);

    /**
     * D�finit la valeur de la propri�t� alternateKey, permet de faire un retrieve sans conna�tre l'idRefRubrique<br>
     * Cl� altern� num�ro 1 : idCodeReference<br>
     * avant de faire un retrieve, il faut utiliser les m�thodes suivantes:<br>
     * setAlternateKey(AK_CODE_REFERENCE)<br>
     * setIdCodeReference(String idCodeReference)<br>
     * <br>
     *
     * @param altKey altKey indique le type de cl� altern�e � utiliser
     */
    void setAlternateKey(int alternateKey);

    /**
     * set l'id du code de r�f�rence (code syst�me)
     *
     * @param String newIdCodeReference
     */
    public void setIdCodeReference(String newIdCodeReference);

}
