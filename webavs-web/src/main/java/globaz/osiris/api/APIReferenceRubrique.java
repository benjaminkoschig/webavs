/*
 * Créé le 7 sept. 05
 */
package globaz.osiris.api;

/**
 * @author sch date : 7 sept. 05
 */
public interface APIReferenceRubrique {

    public final static String AF_FONDS_DE_COMPENSATION = "237036";
    public final static String AF_LFA = "237051";
    public final static String AF_LJA = "237052";
    public final static String AF_PRESTATIONS = "237046";
    public final static String AF_PRESTATIONS_A_RESTITUER = "237055";

    public final static int AK_CODE_REFERENCE = 1;
    public final static String AMENDE_POUR_DEFAUT_DE_PAIEMENT_AVS = "237049";
    public final static String AMENDE_POUR_DEFAUT_DE_PAIEMENT_PS = "237050";
    public final static String APG_ACM_COTISATIONS_AC = "237044";
    public final static String APG_ACM_COTISATIONS_AVS = "237043";

    public final static String APG_ACM_FONDS_DE_COMPENSATION = "237054";
    public final static String APG_ACM_MONTANT_BRUT = "237041";
    public final static String APG_ACM_NE_COMPENSATION_FNE = "237298";
    public final static String APG_ACM_NE_COMPENSATION_MECP = "237288";
    public final static String APG_ACM_NE_COMPENSATION_PP = "237293";

    public final static String APG_ACM_NE_COTISATION_FNE = "237302";
    public final static String APG_ACM_NE_FNE = "237297";
    public final static String APG_ACM_NE_FOND_COMPENSATION_FNE = "237301";
    public final static String APG_ACM_NE_FOND_COMPENSATION_MECP = "237291";
    public final static String APG_ACM_NE_FOND_COMPENSATION_PP = "237296";
    public final static String APG_ACM_NE_IMPOT_SOURCE_FNE = "237300";

    public final static String APG_ACM_NE_IMPOT_SOURCE_MECP = "237290";
    public final static String APG_ACM_NE_IMPOT_SOURCE_PP = "237295";
    public final static String APG_ACM_NE_MECP = "237287";
    public final static String APG_ACM_NE_PP = "237292";
    public final static String APG_ACM_NE_RESTITUTION_FNE = "237299";
    public final static String APG_ACM_NE_RESTITUTION_MECP = "237289";

    public final static String APG_ACM_NE_RESTITUTION_PP = "237294";

    public final static String APG_ACM_RESTITUTION = "237042";
    public final static String APG_ASSURE_OU_INDEPENDANT_AVEC_AC = "237015";
    public final static String APG_ASSURE_OU_INDEPENDANT_SANS_AC = "237016";
    public final static String APG_COTISATION_AC = "237020";
    public final static String APG_COTISATION_AVS = "237019";
    public final static String APG_COTISATION_LFA = "237023";
    public final static String APG_EMPLOYEUR_AVEC_AC = "237013";
    public final static String APG_EMPLOYEUR_SANS_AC = "237014";
    public final static String APG_FONDS_DE_COMPENSATION = "237021";
    public final static String APG_FRAIS_ADMINISTRATION = "237024";
    public final static String APG_IMPOT_A_LA_SOURCE = "237022";
    public final static String APG_IMPOT_A_LA_SOURCE_ACM = "237110";
    public final static String APG_PRESTATION_A_RESTITUER = "237018";
    public final static String APG_SANS_COTISATIONS = "237017";
    public final static String API_AI = "237068";
    public final static String API_AI_EXTOURNE = "237080";
    public final static String API_AI_RETROACTIF = "237074";
    public final static String API_AVS = "237065";
    public final static String API_AVS_EXTOURNE = "237077";
    public final static String API_AVS_RETROACTIF = "237071";
    public final static String AUTRES_FRAIS_CONTENTIEUX = "237102";
    public final static String AUTRES_FRAIS_CONTENTIEUX_PARITAIRE = "237107";
    public final static String COMPENSATION_ALFA = "237056";
    public final static String COMPENSATION_APG_MAT = "237095";
    public final static String COMPENSATION_IJAI = "237096";
    public final static String COMPENSATION_IRRECOUVRABLE = "237094";
    public final static String COMPENSATION_LETTRAGE = "237090";
    public final static String COMPENSATION_LISSAGE = "237098";
    public final static String COMPENSATION_PCFAMILLES = "237150";
    public final static String COMPENSATION_RENTES = "237097";
    public final static String COMPENSATION_REPORT_DE_SOLDE = "237092";
    public final static String COMPENSATION_REPORT_DE_TAXE = "237093";
    public final static String COMPENSATION_TRANSFERT_DE_SOLDE = "237091";
    public final static String FRAIS_AVOCAT = "237101";
    public final static String FRAIS_AVOCAT_PARITAIRE = "237106";
    public final static String FRAIS_MAINLEVEE = "237100";
    public final static String FRAIS_MAINLEVEE_PARITAIRE = "237105";
    // Référence rubrique pour l'extrait de compte des documents AQUILA
    public final static String FRAIS_POURSUITES = "237099";
    public final static String FRAIS_POURSUITES_AMORTIS = "237103";
    public final static String FRAIS_POURSUITES_AMORTIS_PARITAIRE = "237108";
    public final static String FRAIS_POURSUITES_PARITAIRE = "237104";
    public final static String FRAIS_VARIABLES = "237047";
    public final static String IJAI_ASSURE_OU_INDEPENDANT = "237026";
    public final static String IJAI_COTISATIONS_AC = "237029";
    public final static String IJAI_COTISATIONS_AVS = "237028";
    public final static String IJAI_COTISATIONS_LFA = "237045";
    public final static String IJAI_EMPLOYEUR = "237025";
    public final static String IJAI_FONDS_DE_COMPENSATION = "237030";
    public final static String IJAI_FRAIS_ADMINISTRATION = "237032";
    public final static String IJAI_IMPOT_A_LA_SOURCE = "237031";
    public final static String IJAI_PRESTATIONS_A_RESTITUER = "237027";
    public final static String IMPOT_A_LA_SOURCE = "237081";
    public final static String IMPUTATION_DE_COTISATION_PERSONNELLE = "237059";
    public final static String IMPUTATION_DE_FAD_COTISATION_PERSONNELLE = "237089";
    public final static String INTERETS_MORATOIRES_AI = "237087";
    public final static String INTERETS_MORATOIRES_AVS = "237088";
    public final static String INTERETS_VARIABLES = "237048";
    public final static String MATERNITE_ACM_COTISATIONS_AC = "237040";
    public final static String MATERNITE_ACM_COTISATIONS_AVS = "237039";
    public final static String MATERNITE_ACM_FONDS_DE_COMPENSATION = "237053";
    public final static String MATERNITE_ACM_MONTANT_BRUT = "237037";
    public final static String MATERNITE_ACM_RESTITUTION = "237038";
    public final static String MATERNITE_ASSURE_OU_INDEPENDANT_AVEC_AC = "237003";
    public final static String MATERNITE_ASSURE_OU_INDEPENDANT_SANS_AC = "237004";
    public final static String MATERNITE_COTISATION_AC = "237008";
    public final static String MATERNITE_COTISATION_AVS = "237007";
    public final static String MATERNITE_COTISATION_LFA = "237011";
    public final static String MATERNITE_EMPLOYEUR_AVEC_AC = "237001";
    public final static String MATERNITE_EMPLOYEUR_SANS_AC = "237002";
    public final static String MATERNITE_FONDS_DE_COMPENSATION = "237009";
    public final static String MATERNITE_FRAIS_ADMINISTRATION = "237012";
    public final static String MATERNITE_IMPOT_A_LA_SOURCE = "237010";
    public final static String MATERNITE_IMPOT_A_LA_SOURCE_ACM = "237112";
    public final static String MATERNITE_IMPOT_A_LA_SOURCE_LAMAT_CANTONALE = "237111";
    public final static String MATERNITE_PRESTATION_A_RESTITUER = "237006";
    public final static String MATERNITE_SANS_COTISATIONS = "237005";
    public final static String MONTANT_MINIME = "237033";
    public final static String PC_AI = "237114";
    public final static String PC_AI_A_DOMICILE_ORDINAIRES_ESPECES = "237206";
    public final static String PC_AI_A_RESTITUER = "237118";
    public final static String PC_AI_ALLOCATIONS_DE_NOEL = "237284";
    public final static String PC_AI_EN_HOME_HORS_CANTON_SASH = "237208";
    public final static String PC_AI_EN_HOME_HORS_CANTON_SPAS = "237210";
    public final static String PC_AI_EN_HOME_SASH = "237207";
    public final static String PC_AI_EN_HOME_SPAS = "237209";
    public final static String PC_AI_EN_HOME = "237304";
    public final static String PC_AI_RESTITUTION_A_DOMICILE_ORDINAIRES_ESPECES = "237212";
    public final static String PC_AI_RESTITUTION_EN_HOME_HORS_CANTON_SASH = "237214";
    public final static String PC_AI_RESTITUTION_EN_HOME_HORS_CANTON_SPAS = "237216";
    public final static String PC_AI_RESTITUTION_EN_HOME_SASH = "237213";
    public final static String PC_AI_RESTITUTION_EN_HOME_SPAS = "237215";
    public final static String PC_AI_RESTITUTION_EN_HOME = "237306";

    // Rubrique PC_AVS :
    public final static String PC_AVS = "237113";
    public final static String PC_AVS_A_DOMICILE_ORDINAIRES_ESPECES = "237217";
    public final static String PC_AVS_A_RESTITUER = "237117";
    public final static String PC_AVS_ALLOCATIONS_DE_NOEL = "237205";
    public final static String PC_AVS_EN_HOME_HORS_CANTON_SASH = "237219";
    public final static String PC_AVS_EN_HOME_HORS_CANTON_SPAS = "237221";
    public final static String PC_AVS_EN_HOME_SASH = "237218";
    public final static String PC_AVS_EN_HOME_SPAS = "237220";
    public final static String PC_AVS_EN_HOME = "237303";
    public final static String PC_AVS_RESTITUTION_A_DOMICILE_ORDINAIRES_ESPECES = "237222";
    public final static String PC_AVS_RESTITUTION_EN_HOME_HORS_CANTON_SASH = "237224";
    public final static String PC_AVS_RESTITUTION_EN_HOME_HORS_CANTON_SPAS = "237226";
    public final static String PC_AVS_RESTITUTION_EN_HOME_SASH = "237223";
    public final static String PC_AVS_RESTITUTION_EN_HOME_SPAS = "237225";
    public final static String PC_AVS_RESTITUTION_EN_HOME = "237305";

    // Clé alternée
    public final static String PC_COMPTE_COURANT_BLOCAGE = "237233";
    public final static String PCF_A_RESTITUER = "237142";
    public final static String PCF_FRAIS_GARDE = "237140";
    public final static String PCF_FRAIS_GARDE_A_RESTITUER = "237143";
    public final static String PCF_IMPOT_SOURCE = "237149";
    public final static String PCF_MESURE_COACHING = "237153";
    public final static String PCF_MESURE_COACHING_A_RESTITUER = "237154";
    public final static String PCF_PRESTATION = "237139";
    public final static String PCF_RFM = "237141";
    public final static String PCF_RFM_A_RESTITUER = "237144";
    public final static String PRESTATION_AI_A_RESTITUER = "237085";
    public final static String PRESTATION_API_AI_A_RESTITUER = "237086";
    public final static String PRESTATION_API_AVS_A_RESTITUER = "237084";
    public final static String PRESTATION_AVS_A_RESTITUER = "237083";
    public final static String PRESTATION_LAMATGE_ADOPTION = "237061";
    public final static String PRESTATION_LAMATGE_NAISSANCE = "237060";
    public final static String PRESTATION_LAMATGE_RESTITUTION = "237062";
    public final static String REDUCTION_DE_COTISATION_PERSONNELLE = "237058";
    public final static String REGULARISATION_CCP = "237082";
    public final static String REMISE_AI_DSAS = "237229";
    public final static String REMISE_AVS_DSAS = "237230";
    public final static String REMISE_DE_COTISATION_PERSONNELLE = "237057";
    public final static String RENTE_EXTRAORDINAIRE_AI = "237067";
    public final static String RENTE_EXTRAORDINAIRE_AI_EXTOURNE = "237079";
    public final static String RENTE_EXTRAORDINAIRE_AI_RETROACTIF = "237073";
    public final static String RENTE_EXTRAORDINAIRE_AVS_EXTOURNE = "237076";
    public final static String RENTE_EXTRAORDINAIRE_AVS_RETROACTIF = "237070";
    public final static String RENTE_EXTRAORDINAIREAVS = "237064";
    public final static String RENTE_ORDINAIRE_AI = "237066";
    public final static String RENTE_ORDINAIRE_AI_EXTOURNE = "237078";
    public final static String RENTE_ORDINAIRE_AI_RETROACTIF = "237072";
    public final static String RENTE_ORDINAIRE_AVS = "237063";
    public final static String RENTE_ORDINAIRE_AVS_EXTOURNE = "237075";
    public final static String RENTE_ORDINAIRE_AVS_RETROACTIF = "237069";
    public final static String RENTE_PONT_PRESTATION = "237145";
    public final static String RENTE_PONT_RESTIT = "237147";
    public final static String RENTE_PONT_RFM = "237146";
    public final static String RENTE_PONT_RFM_RESTIT = "237148";
    public final static String RENTES_FONDS_DE_COMPENSATION = "237035";
    public final static String RENTES_PONT_RESTIT = "237153";
    public final static String RENTES_PONT_RFM_RESTIT = "237154";

    public final static String RFM_AI = "237116";// CCJU

    public final static String RFM_AI_A_RESTITUER = "237120";// CCJU
    public final static String RFM_AI_A_RESTITUER_AIDE_AU_MENAGE = "237120";
    public final static String RFM_AI_A_RESTITUER_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE = "237120";
    public final static String RFM_AI_A_RESTITUER_COTISATIONS_PARITAIRES = "237120";
    public final static String RFM_AI_A_RESTITUER_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL = "237120";
    public final static String RFM_AI_A_RESTITUER_FRAIS_DE_PENSION_COURT_SEJOUR = "237120";
    public final static String RFM_AI_A_RESTITUER_FRAIS_DE_TRANSPORT = "237120";
    public final static String RFM_AI_A_RESTITUER_FRQP = "237120";
    public final static String RFM_AI_A_RESTITUER_MOYENS_AUXILIAIRES = "237120";
    public final static String RFM_AI_A_RESTITUER_PARTICIPATION_COURT_SEJOUR = "237120";
    public final static String RFM_AI_A_RESTITUER_PENSION_HOME_DE_JOUR = "237120";
    public final static String RFM_AI_A_RESTITUER_REGIME = "237120";
    public final static String RFM_AI_A_RESTITUER_TRAITMENT_DENTAIRE = "237120";
    public final static String RFM_AI_A_RESTITUER_UNITE_ACCUEIL_TEMPORAIRE = "237120";

    public final static String RFM_AI_AIDE_AU_MENAGE = "237184";
    public final static String RFM_AI_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE = "237193";
    public final static String RFM_AI_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SASH = "237278";
    public final static String RFM_AI_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SPAS = "237253";
    public final static String RFM_AI_AIDE_AU_MENAGE_SASH = "237274";
    public final static String RFM_AI_AIDE_AU_MENAGE_SPAS = "237249";

    public final static String RFM_AI_COTISATIONS_PARITAIRES = "237190";
    public final static String RFM_AI_COTISATIONS_PARITAIRES_SASH = "237286";
    public final static String RFM_AI_COTISATIONS_PARITAIRES_SPAS = "237286";

    public final static String RFM_AI_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL = "237188";
    public final static String RFM_AI_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SASH = "237276";
    public final static String RFM_AI_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SPAS = "237251";

    public final static String RFM_AI_FINANCEMENT_DES_SOINS = "237122";// CCJU
    public final static String RFM_AI_FINANCEMENT_DES_SOINS_IMPORTATION_AUTO = "237151";// CCJU

    public final static String RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR = "237201";
    public final static String RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR_SASH = "237282";
    public final static String RFM_AI_FRAIS_DE_PENSION_COURT_SEJOUR_SPAS = "237257";

    public final static String RFM_AI_FRAIS_DE_TRANSPORT = "237186";
    public final static String RFM_AI_FRAIS_DE_TRANSPORT_SASH = "237275";
    public final static String RFM_AI_FRAIS_DE_TRANSPORT_SPAS = "237250";

    public final static String RFM_AI_FRQP = "237180";
    public final static String RFM_AI_FRQP_SASH = "237272";
    public final static String RFM_AI_FRQP_SPAS = "237247";

    public final static String RFM_AI_MOYENS_AUXILIAIRES = "237124";// CCJU
    public final static String RFM_AI_MOYENS_AUXILIAIRES_SASH = "237277";
    public final static String RFM_AI_MOYENS_AUXILIAIRES_SPAS = "237252";

    public final static String RFM_AI_PARTICIPATION_COURT_SEJOUR = "237199";
    public final static String RFM_AI_PARTICIPATION_COURT_SEJOUR_SASH = "237281";
    public final static String RFM_AI_PARTICIPATION_COURT_SEJOUR_SPAS = "237256";

    public final static String RFM_AI_PENSION_HOME_DE_JOUR = "237197";
    public final static String RFM_AI_PENSION_HOME_DE_JOUR_SASH = "237280";
    public final static String RFM_AI_PENSION_HOME_DE_JOUR_SPAS = "237255";

    public final static String RFM_AI_REGIME = "237203";
    public final static String RFM_AI_REGIME_SASH = "237283";
    public final static String RFM_AI_REGIME_SPAS = "237258";

    public final static String RFM_AI_SASH = "237128";
    public final static String RFM_AI_SPAS = "237126";

    public final static String RFM_AI_TRAITEMENT_DENTAIRE = "237182";
    public final static String RFM_AI_TRAITEMENT_DENTAIRE_SASH = "237273";
    public final static String RFM_AI_TRAITEMENT_DENTAIRE_SPAS = "237248";

    public final static String RFM_AI_UNITE_ACCUEIL_TEMPORAIRE = "237195";
    public final static String RFM_AI_UNITE_ACCUEIL_TEMPORAIRE_SASH = "237279";
    public final static String RFM_AI_UNITE_ACCUEIL_TEMPORAIRE_SPAS = "237254";

    public final static String RFM_AVANCE_SAS = "237232";// CCJU
    public final static String RFM_AVS = "237115";// CCJU
    public final static String RFM_AVS_A_RESTITUER = "237119";// CCJU
    public final static String RFM_AVS_A_RESTITUER_AIDE_AU_MENAGE = "237119";
    public final static String RFM_AVS_A_RESTITUER_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE = "237119";
    public final static String RFM_AVS_A_RESTITUER_COTISATIONS_PARITAIRES = "237119";
    public final static String RFM_AVS_A_RESTITUER_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL = "237119";
    public final static String RFM_AVS_A_RESTITUER_FRAIS_DE_PENSION_COURT_SEJOUR = "237119";
    public final static String RFM_AVS_A_RESTITUER_FRAIS_DE_TRANSPORT = "237119";
    public final static String RFM_AVS_A_RESTITUER_FRQP = "237119";
    public final static String RFM_AVS_A_RESTITUER_MOYENS_AUXILIAIRES = "237119";
    public final static String RFM_AVS_A_RESTITUER_PARTICIPATION_COURT_SEJOUR = "237119";
    public final static String RFM_AVS_A_RESTITUER_PENSION_HOME_DE_JOUR = "237119";
    public final static String RFM_AVS_A_RESTITUER_REGIME = "237119";
    public final static String RFM_AVS_A_RESTITUER_TRAITMENT_DENTAIRE = "237119";
    public final static String RFM_AVS_A_RESTITUER_UNITE_ACCUEIL_TEMPORAIRE = "237119";

    public final static String RFM_AVS_AIDE_AU_MENAGE = "237159";
    public final static String RFM_AVS_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE = "237168";
    public final static String RFM_AVS_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SASH = "237266";
    public final static String RFM_AVS_AIDE_AU_MENAGE_PAR_MEMBRE_FAMILLE_SPAS = "237241";
    public final static String RFM_AVS_AIDE_AU_MENAGE_SASH = "237261";
    public final static String RFM_AVS_AIDE_AU_MENAGE_SPAS = "237237";

    public final static String RFM_AVS_COTISATIONS_PARITAIRES = "237165";
    public final static String RFM_AVS_COTISATIONS_PARITAIRES_SASH = "237264";
    public final static String RFM_AVS_COTISATIONS_PARITAIRES_SPAS = "237264";

    public final static String RFM_AVS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL = "237163";
    public final static String RFM_AVS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SASH = "237263";
    public final static String RFM_AVS_ENCADREMENT_ET_ACCOMPAGNEMENT_SOCIAL_SPAS = "237239";

    public final static String RFM_AVS_FINANCEMENT_DES_SOINS = "237121";// CCJU
    public final static String RFM_AVS_FINANCEMENT_DES_SOINS_IMPORTATION_AUTO = "237152";// CCJU

    public final static String RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR = "237176";
    public final static String RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR_SASH = "237270";
    public final static String RFM_AVS_FRAIS_DE_PENSION_COURT_SEJOUR_SPAS = "237245";

    public final static String RFM_AVS_FRAIS_DE_TRANSPORT = "237161";
    public final static String RFM_AVS_FRAIS_DE_TRANSPORT_SASH = "237262";
    public final static String RFM_AVS_FRAIS_DE_TRANSPORT_SPAS = "237238";

    public final static String RFM_AVS_FRQP = "237155";
    public final static String RFM_AVS_FRQP_SASH = "237259";
    public final static String RFM_AVS_FRQP_SPAS = "237235";

    public final static String RFM_AVS_MOYENS_AUXILIAIRES = "237123";// CCJU
    public final static String RFM_AVS_MOYENS_AUXILIAIRES_SASH = "237265";
    public final static String RFM_AVS_MOYENS_AUXILIAIRES_SPAS = "237240";

    public final static String RFM_AVS_PARTICIPATION_COURT_SEJOUR = "237174";
    public final static String RFM_AVS_PARTICIPATION_COURT_SEJOUR_SASH = "237269";
    public final static String RFM_AVS_PARTICIPATION_COURT_SEJOUR_SPAS = "237244";

    public final static String RFM_AVS_PENSION_HOME_DE_JOUR = "237172";
    public final static String RFM_AVS_PENSION_HOME_DE_JOUR_SASH = "237268";
    public final static String RFM_AVS_PENSION_HOME_DE_JOUR_SPAS = "237243";

    public final static String RFM_AVS_REGIME = "237178";
    public final static String RFM_AVS_REGIME_SASH = "237271";
    public final static String RFM_AVS_REGIME_SPAS = "237246";

    public final static String RFM_AVS_SASH = "237127";
    public final static String RFM_AVS_SPAS = "237125";

    public final static String RFM_AVS_TRAITEMENT_DENTAIRE = "237157";
    public final static String RFM_AVS_TRAITEMENT_DENTAIRE_SASH = "237260";
    public final static String RFM_AVS_TRAITEMENT_DENTAIRE_SPAS = "237236";

    public final static String RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE = "237170";
    public final static String RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE_SASH = "237267";
    public final static String RFM_AVS_UNITE_ACCUEIL_TEMPORAIRE_SPAS = "237242";

    public final static String RUBRIQUE_DE_LISSAGE = "237034";

    public final static String TAXE_SOMMATION_BULLETIN_NEUTRE = "237109";

    /**
     * Cette méthode retourne la rubrique chargée en fonction du code de référence En cas de problème, cette méthode
     * retourne null
     * 
     * @param idCodeReference
     * @return APIRubrique
     */
    public APIRubrique getRubriqueByCodeReference(String idCodeReference);

    /**
     * Définit la valeur de la propriété alternateKey, permet de faire un retrieve sans connaître l'idRefRubrique<br>
     * Clé alterné numéro 1 : idCodeReference<br>
     * avant de faire un retrieve, il faut utiliser les méthodes suivantes:<br>
     * setAlternateKey(AK_CODE_REFERENCE)<br>
     * setIdCodeReference(String idCodeReference)<br>
     * <br>
     * 
     * @param altKey
     *            altKey indique le type de clé alternée à utiliser
     */
    void setAlternateKey(int alternateKey);

    /**
     * set l'id du code de référence (code système)
     * 
     * @param String
     *            newIdCodeReference
     */
    public void setIdCodeReference(String newIdCodeReference);

}
