package globaz.hermes.api;

import globaz.globall.api.BIEntity;
import globaz.globall.api.BITransaction;
import java.util.Map;

/**
 * Interface d'API pour les annonces <br>
 * Exemple : <br>
 * <code>
 * put(CODE_APPLICATION, _codeApplication); <br>
 * put(CODE_ENREGISTREMENT, _enregistrement); <br>
 * put(MOTIF_ANNONCE, _motif); <br>
 * </code>
 * 
 * @author ado
 */
public interface IHEAnnoncesViewBean extends BIEntity {
    public final static String ANNEE_COTISATIONS = "118061";
    public static String ANNEE_D_INSCRIPTION_AAAA = "118132";
    // /////////
    public static String ANNEE_OUVERTURE_CI = "118083";
    public final static String ANNEES_COTISATIONS_AAAA = "118065";
    /** L'ayant droit (ARC 11 04) */
    public final static String AYANT_DROIT = "118023";
    public final static String BRANCHE_ECONOMIQUE = "118064";
    public final static String CHIFFRE_CLE_EXTOURNES = "118055";
    public final static String CHIFFRE_CLE_GENRE_COTISATIONS = "118056";
    public static String CHIFFRE_CLEF_PARTICULIER = "118096";
    public final static String CI_ADDITIONNEL = "118080"; // Champs
    public final static String CODE_1_OU_2 = "118052";
    public final static String CODE_A_D_S = "118063";
    // ///////////// Liste des champs //////////////////////////
    /** Le code application */
    public final static String CODE_APPLICATION = "118001";
    /** Code de traitement (retour de la centrale) */
    public final static String CODE_DE_TRAITEMENT = "118038";
    // ////////////////////
    public final static String CODE_DE_TRAITEMENT_2 = "118102";
    public final static String CODE_DE_TRAITEMENT_3 = "118106";
    public final static String CODE_DE_TRAITEMENT_4 = "118110";
    public final static String CODE_DE_TRAITEMENT_5 = "118114";
    public final static String CODE_DE_TRAITEMENT_6 = "118118";
    public final static String CODE_DE_TRAITEMENT_7 = "118122";
    // ////////////////////
    public final static String CODE_DE_TRAITEMENT_8 = "118126";
    /** Le code enregistrement */
    public final static String CODE_ENREGISTREMENT = "118002";
    public final static String CODE_SPECIAL = "118058";
    // /////
    public final static String CODE_VALEUR_CHAMP_15 = "118077"; // Champs
    // ////////////////////
    public static String CODE_VALEUR_CHAMP_8 = "118130";
    public static String CODE_VALEUR_DU_CHAMP_10 = "118157";
    public static String CODE_VALEUR_DU_CHAMP_12 = "118145";
    public static String CODE_VALEUR_DU_CHAMP_14 = "118158";
    public static String CODE_VALEUR_DU_CHAMP_15 = "118148";
    public static String CODE_VALEUR_DU_CHAMP_18 = "118151";
    public static String CODE_VALEUR_DU_CHAMP_21 = "118154";
    public static String CODE_VALEUR_DU_CHAMP_22 = "118160";
    public static String CODE_VALEUR_DU_CHAMP_26 = "118161";
    public static String CODE_VALEUR_DU_CHAMP_9 = "118143";
    public final static String CS_11_ANNONCE_ARC = "111001";
    public final static String CS_20_ACCUSE_RECEPTION_ARC = "111002";
    public final static String CS_21_AUTORISATION_OUVERTURE_CI = "111003";
    public final static String CS_22_RCI_OU_ORDRE_SPLITTING = "111006";
    public final static String CS_23_CI_COMPLEMENT = "111004";
    public final static String CS_24_COMMUNICATION_ETAT_REGISTRE_ASSURES_CENTRALE = "111008";
    public final static String CS_25_CONFIRMATION_RCI_OU_ORDRE_SPLITTING = "111005";
    public final static String CS_29_REVOCATION_RCI_OU_ORDRE_SPLITTING = "111007";
    public final static String CS_38_EXTRAIT_CI_DONNEES = "111011";
    public final static String CS_39_EXTRAIT_CI_CONTROLE = "111012";
    public final static String CS_41_ANNONCE_CAS_CALCULES_SELON_ANCIEN_DROIT = "111027";
    public final static String CS_42_ANNONCE_CAS_CALCULES_SELION_ANCIEN_DROIT = "111028";
    public final static String CS_43_ANNONCE_CAS_CALCULES_SELION_ANCIEN_DROIT = "111029";
    public final static String CS_44_ANNONCE_CAS_CALCULES_SELON_NOUVEAU_DROIT = "111030";

    public final static String CS_45_ANNONCE_CAS_CALCULES_SELON_NOUVEAU_DROIT = "111031";
    public final static String CS_46_ANNONCE_CAS_CALCULES_SELON_NOUVEAU_DROIT = "111032";
    public final static String CS_50_REPONSE_CENTRALE_AUX_CAISSES = "111033";
    public final static String CS_51_ANNONCE_ETAT_RENTES_CENTRALE_AUX_CAISSES = "111034";
    public final static String CS_52_CAS_DECES_ANNONCES_CENTRALE_AUX_CAISSES = "111036";
    public final static String CS_53_ANNONCE_CAS_CALCULES_SELON_NOUVEAU_DROIT = "111035";
    public final static String CS_72_DEC_COMM_MES_INDIV = "111041";
    public final static String CS_73_DEMANDES = "111042";
    public final static String CS_74_PRON_RENTES_OU_ALLOC = "111043";
    public final static String CS_75_REFUS = "111044";
    public final static String CS_81_APG_ANCIEN_DROIT = "111045";
    public final static String CS_85_IJAI_ANCIEN = "111047";
    public final static String CS_8A_DONNEES_COMPTABLES_A_CENTRACOMPTE_EXPLOITATION = "111022";
    public final static String CS_8B_BILAN_ET_COMPTE_ADMINISTRATION = "111023";
    public final static String CS_8C_RECAPITULATION_MENSUELCOMPTABILITE_AFFILIES = "111024";
    public final static String CS_8D_RECAPITULATION_MENSUELRENTES = "111025";
    public final static String CS_8E_BALANCE_MOUVEMENTS_ANUELLES = "111026";
    public final static String CS_8F_APG_NOUVEAU_DROIT = "111046";
    public final static String CS_8G_IJAI_NOUVEAU = "111048";
    public final static String CS_A_L_EMPLOYEUR = "112402";
    /** CS Statut */
    public final static String CS_A_TRAITER = "117002";
    public static String CS_AGE_DEBUT_INVALIDITE_AYANT_DROIT = "119041";
    public static String CS_AGE_DEBUT_INVALIDITE_EPOUSE = "119042";
    public final static String CS_AIDE_CAPITAL = "119000";
    public final static String CS_ALLOC_ASSISTANCE_JOUR = "118851";
    public final static String CS_ALLOC_FRAIS_GARDE = "118857";
    public final static String CS_ALLOC_FRAIS_GARDE_MONTANT = "118858";
    public final static String CS_ALLOC_ISOLEE_FRAIS_GARDE = "118860";
    public final static String CS_ALLOC_MENAGE = "118848";
    public final static String CS_ALLOC_PERSONNE_SEULE = "118847";
    public final static String CS_ALLOCATION_DE_BASE = "118855";
    public final static String CS_ALLOCATION_EXPLOITATION = "118850";
    public final static String CS_ALLOCATION_IMPOTENT = "119002";
    public static String CS_ANC_BONIFI_TACHES_EDUC_MOYENNES_FRANCS = "119076";
    public static String CS_ANC_REDUCTION_POUR_ANTICIPATION = "119078";
    public static String CS_ANC_REVENU_ANNUEL_MOYEN_SANS_BONIFI_TACHES_EDUC_FRANCS = "119075";
    public static String CS_ANCIEN_CAS_SPECIAL_1_CODE = "119070";
    public static String CS_ANCIEN_CAS_SPECIAL_2_CODE = "119071";
    public static String CS_ANCIEN_CAS_SPECIAL_3_CODE = "119072";
    public static String CS_ANCIEN_CAS_SPECIAL_4_CODE = "119073";
    public static String CS_ANCIEN_CAS_SPECIAL_5_CODE = "119074";
    public static String CS_ANCIEN_MONTANT_MENSUEL = "119069";
    public static String CS_ANCIEN_REVENU_ANN_MOY_DET = "119067";
    public static String CS_ANCIEN_SUPPLEMENT_AJOURNEMENT = "119077";
    public static String CS_ANCIENNE_RO_REMPLACEE = "119068";
    public static String CS_ANNEE_COTISATIONS_DE_LA_CLASSE_AGE = "119027";
    public static String CS_ANNEE_DE_NIVEAU = "119021";
    public final static String CS_ANNEE_ET_MOIS_DU_DEBUT_DU_DROIT_A_LA_NOUV_PREST_AAAAMM = "118828";
    public final static String CS_ANNEE_ET_MOIS_DU_DEBUT_DU_DROIT_A_LA_PREM_PREST_AAAAMM = "118829";
    public final static String CS_ANNONCE_RECTIFICATIF_POUR_PAIEMENT_RETROACTIF = "112203";
    public final static String CS_ANNONCE_RECTIFICATIF_POUR_RESTITUTION = "112204";
    public final static String CS_ASSURANCE = "118818";
    public static String CS_AUCUN = "112601";
    public static String CS_BONIFI_TACHES_EDUC_MOYENNES_FRANCS = "119052";
    public final static String CS_CANTON_ETAT_DOMICILE = "118864";
    public final static String CS_CARTE_RECT_POUR_PAIEMENT_RETROACTIF = "112003";
    public final static String CS_CARTE_RECT_POUR_RESTITUTION = "112004";
    public static String CS_CAS_SPECIAL_1_CODE = "119045";
    public static String CS_CAS_SPECIAL_2_CODE = "119046";
    public static String CS_CAS_SPECIAL_3_CODE = "119047";
    public static String CS_CAS_SPECIAL_4_CODE = "119048";
    public static String CS_CAS_SPECIAL_5_CODE = "119049";
    public static String CS_CATEGORIE_EMPLOYEUR = "115002";
    // ******************************************************************************************************/
    public static String CS_CATEGORIE_INDEPENDANT = "115001";
    public static String CS_CATEGORIE_RENTIER = "115003";
    // HEETCIV ://Contenu de l''annonce
    public final static String CS_CELIBATAIRE = "112301";
    public static String CS_CHIFFRE_CLE_GENRE_PRESTATION = "118940";
    public final static String CS_CODE_ALLOCATION_INITIATION_TRAVAIL_MAXIMALE_PAR_JOUR = "118966";
    public final static String CS_CODE_BRANCHE = "118942";
    public final static String CS_CODE_CAPACITE_TRAVAIL_DEBUT_MESURE = "118949";
    public final static String CS_CODE_CAPACITE_TRAVAIL_FIN_MESURE = "118951";
    public final static String CS_CODE_COMPTEUR_NOMBRE_ABSENCES_1 = "118971";
    public final static String CS_CODE_COMPTEUR_NOMBRE_ABSENCES_2 = "118972";
    public final static String CS_CODE_COMPTEUR_NOMBRE_ABSENCES_3 = "118973";
    public final static String CS_CODE_COMPTEUR_NOMBRE_ABSENCES_4 = "118974";
    public final static String CS_CODE_COMPTEUR_NOMBRE_ABSENCES_5 = "118975";
    public final static String CS_CODE_COMPTEUR_SOMME_JOURS_ABSENCE_1 = "118976";
    public final static String CS_CODE_COMPTEUR_SOMME_JOURS_ABSENCE_2 = "118977";
    public final static String CS_CODE_COMPTEUR_SOMME_JOURS_ABSENCE_3 = "118978";
    public final static String CS_CODE_COMPTEUR_SOMME_JOURS_ABSENCE_4 = "118979";
    public final static String CS_CODE_COMPTEUR_SOMME_JOURS_ABSENCE_5 = "118980";
    public final static String CS_CODE_DATE_DEBUT_MESURE = "118956";
    public final static String CS_CODE_DATE_DEBUT_PREVUE_MESURE = "118953";
    public final static String CS_CODE_DATE_ENGAGEMENT_EFFECTIVE = "118967";
    public final static String CS_CODE_DATE_FIN_MESURE = "118957";
    public final static String CS_CODE_DATE_FIN_PREVUE_MESURE = "118954";
    public final static String CS_CODE_DATE_INTERRUPTION_ACTIVITE_LUCRATIVE_AAAAMMJJ = "118948";
    public final static String CS_CODE_DATE_PERTE_EMPLOI = "118968";
    public final static String CS_CODE_DE_L_ATTEINTE_FONCTIONNELLE = "118809";
    public final static String CS_CODE_DE_L_INFIRMITE = "118808";
    public final static String CS_CODE_DE_LA_PRESTATION = "118810";
    public static String CS_CODE_DE_MUTATION = "119018";
    public final static String CS_CODE_DE_REFUS_1 = "118830";
    public final static String CS_CODE_DE_REFUS_2 = "118831";
    public final static String CS_CODE_DE_REFUS_3 = "118832";
    public final static String CS_CODE_DE_REFUS_4 = "118833";
    public final static String CS_CODE_DE_REFUS_5 = "118834";
    public static String CS_CODE_DE_REVENUS_SPLITTES = "119054";
    public final static String CS_CODE_DE_REVISION = "118823";
    public final static String CS_CODE_DE_VALIDITE_DE_LA_PRESTATION = "118816";
    public final static String CS_CODE_DUREE_MESURES = "118981";
    public final static String CS_CODE_FONCTION = "118943";
    public final static String CS_CODE_FORM_PLUS_ELEVEE_ACC = "118945";
    public final static String CS_CODE_INDEMNITE_JOURNALIERE = "118955";
    public static String CS_CODE_INFIRMITE = "119058";
    public final static String CS_CODE_INFORMATION_MOMENT_EMBAUCHE = "118959";
    public final static String CS_CODE_MOTIF_FIN_ALLOCATION_INITIATION_TRAVAIL = "118964";
    public final static String CS_CODE_MOTIF_FIN_MESURE_REINSERTION = "118958";
    public final static String CS_CODE_MOTIF_PERTE_EMPLOI = "118969";
    public final static String CS_CODE_MOTIF_TAUX_OCCUPATION = "118961";
    public final static String CS_CODE_NOMBRE_JOURS_ABSENCE_DECOMPTE_CONTRIBUTION_EMPLOYEURS = "118970";
    public final static String CS_CODE_NOMBRE_JOURS_CONTRIBUTIONS = "118965";
    public final static String CS_CODE_NOMBRE_JOURS_PRENDRE_COMPTE_FACTURATION_MESURE_OCTROYEE = "118962";
    public final static String CS_CODE_PRESENCE_DEBUT_MESURE = "118950";
    public final static String CS_CODE_PRESENCE_FIN_MESURE = "118952";
    public final static String CS_CODE_PRESTATION_LAI_8A = "119080";
    public final static String CS_CODE_PRIX_PAR_JOUR = "118946";
    public final static String CS_CODE_PROF_EXERCEE = "118944";
    public final static String CS_CODE_RENTE_EXCLUE = "118963";
    public static String CS_CODE_SURVIVANT_INVALIDE = "119063";
    public final static String CS_CODE_TAUX_OCCUPATION = "118960";
    public final static String CS_CODE_TYPE_DE_DECISION = "118947";
    public final static String CS_CODE_VALEUR_CHAMP_33 = "118892";
    public final static String CS_CODE_VALEUR_CHAMP23 = "118854";
    public final static String CS_CODE_VALEUR_CHAMP28 = "118859";
    public final static String CS_CODE_VALEUR_DU_CHAMP_24_PERIODE1 = "118908";
    public final static String CS_CODE_VALEUR_DU_CHAMP_24_PERIODE2 = "118918";
    public final static String CS_CODE_VALEUR_DU_CHAMP_24_PERIODE3 = "118928";
    public static String CS_CODEINFIRMITE_AYANT_DROIT = "119037";
    public static String CS_CODEINFIRMITE_EPOUSE = "119038";
    public static String CS_COMMENTAIRE = "118941";
    public final static String CS_CONTENU_DE_L_ANNONCE = "118861";
    public final static String CS_COUT_ENTRAINEMENT_ENDURANCE = "118982";
    public final static String CS_COUT_ENTRAINEMENT_PROGRESSIF = "118983";
    public final static String CS_COUT_MR_ENTREPRISE = "118985";
    public final static String CS_COUT_REST_STANDARD = "118984";
    public final static String CS_COUT_TOTAL = "118991";
    public final static String CS_COUT_TOTAL_AIT_CEPH = "118990";
    public final static String CS_COUT_TOTAL_ALLOCATIONS_INITIATION_TRAVAIL = "118988";
    public final static String CS_COUT_TOTAL_CONTR_EMPL_PERSONNE_HANDICAPEE = "118989";
    public final static String CS_COUT_TOTAL_INDEMNITE_JOURNALIERE = "118987";
    public final static String CS_COUT_TOTAL_MR = "118986";
    public final static String CS_DATE_DE_LA_DECISION_AAAAMMJJ = "118803";
    public final static String CS_DATE_DE_LA_DEMANDE_AAAAMMJJ = "118804";
    public static String CS_DATE_DEBUT_ANTICIPATION_MMAA = "119062";
    public final static String CS_DATE_DECES = "118935";
    public final static String CS_DATE_DU_PRONONCE_AAAAMMJJ = "118822";
    public static String CS_DATE_REVOCATION_AJOURNEMENT_MMAA = "119030";
    public final static String CS_DEBUT_DROIT_ALLOCATION = "118871";
    public static String CS_DEBUT_DU_DROIT_MMAA = "119010";
    // // NEW VKU
    public final static String CS_DECISION_PRINCIPE = "118992";
    public final static String CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE1 = "118901";
    public final static String CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE2 = "118911";
    public final static String CS_DEDUCTION_NOURRITURE_LOGEMENT_PERIODE3 = "118921";
    public final static String CS_DEGRE_D_IMPOTENCE = "118825";
    public final static String CS_DEGRE_D_INVALIDITE = "118824";
    public final static String CS_DEGRE_IMPOTENCE_ANCIEN = "119084";
    public static String CS_DEGRE_INVALIDITE = "119059";
    public final static String CS_DEGRE_INVALIDITE_ANCIEN = "119083";
    public static String CS_DEGREINVALIDITE_AYANT_DROIT = "119035";
    public static String CS_DEGREINVALIDITE_EPOUSE = "119036";
    // Contenu de l''annonce
    public final static String CS_DEMANDE_D_ALLOCATION = "112201";
    // HEMODPAI ://Mode de paiement
    public final static String CS_DIRECT_AYANT_DROIT = "112401";
    public final static String CS_DIVORCEE = "112304";
    public static String CS_DOMICILE = "118934";
    public final static String CS_DOMICILE_DE_LA_PERSONNE_ASSUREE = "118805";
    public final static String CS_DROIT_PRESTATION_POUR_ENFANT_PERIODE_1 = "118933";
    public final static String CS_DROIT_PRESTATION_POUR_ENFANT_PERIODE_2 = "118936";
    public final static String CS_DROIT_PRESTATION_POUR_ENFANT_PERIODE_3 = "118940";
    public final static String CS_DROITS_ACQUIS_4_RéVISION_AI = "118900";
    public final static String CS_DUPLICATA = "112002";
    public final static String CS_DUPLICATA_CONTENU = "112202";
    public static String CS_DUREE_AJOURNEMENT_AMM = "119028";
    public static String CS_DUREE_COTISATIONS_CHOIX_ECHELLE_RENTES_AV_1973_AAMM = "119024";
    public static String CS_DUREE_COTISATIONS_CHOIX_ECHELLE_RENTES_DES_1973_AAMM = "119025";
    public static String CS_DUREE_COTISATIONS_MANQUANTES_POUR_LES_ANNEES_1948_72 = "119026";
    public static String CS_DUREE_COTISATIONS_MANQUANTES_POUR_LES_ANNEES_1973_78 = "119050";
    public static String CS_DUREE_COTISATIONS_POUR_DETERMINER_REVENU_ANNUEL_MOYEN_AAMM = "119020";
    public static String CS_ECHELLE_DE_RENTES = "119023";
    public final static String CS_EMPLOYEUR_CENTRE_READAPTATION = "112702";
    // ///////////////////////////////////////
    // ///////////////////////////////////////
    /** CS Statut */
    public final static String CS_EN_ATTENTE = "117001";
    public final static String CS_ETAT_CIVIL_AYANT_DROIT = "118865";
    public final static String CS_EVALUATION_DE_L_INVALIDITé = "118826";
    public final static String CS_FIN_DROIT_ALLOCATION = "118872";
    public static String CS_FIN_DU_DROIT_MMAA = "119016";
    public final static String CS_FORMATION_PROFESSIONNELLE_INITIALE = "118997";
    public final static String CS_FORMULAIRE = "118819";
    public static String CS_FRACTION_DE_LA_RENTE = "119066";
    public final static String CS_GARANTIE_AA = "118898";
    public final static String CS_GARANTIE_AA_PERIODE_1 = "118879";
    public final static String CS_GARANTIE_AA_PERIODE_2 = "118884";
    public final static String CS_GARANTIE_AA_PERIODE_3 = "118889";
    public final static String CS_GARANTIE_DROIT_ACQUIS_5_EME_REV_PERIODE_1 = "118934";
    public final static String CS_GARANTIE_DROIT_ACQUIS_5_EME_REV_PERIODE_2 = "118937";
    public final static String CS_GARANTIE_DROIT_ACQUIS_5_EME_REV_PERIODE_3 = "118941";
    public final static String CS_GARANTIE_INDEMNITE_JOURNALIERE_AI = "118867";
    public final static String CS_GENRE_ACTIVITE_AVANT_SERVICE = "118845";
    public final static String CS_GENRE_ACTIVITE_AVANT_SERVICE_OU_ACCOUCH = "118866";
    public static String CS_GENRE_ANNONCE = "119064";
    public final static String CS_GENRE_DE_CARTE = "118836";
    public final static String CS_GENRE_DE_COTISATION = "118827";
    public final static String CS_GENRE_DE_READAPTATION = "118897";
    public final static String CS_GENRE_DE_READAPTATION_1_A_8_PERIODE_1 = "118878";
    public final static String CS_GENRE_DE_READAPTATION_1_A_8_PERIODE_2 = "118883";
    public final static String CS_GENRE_DE_READAPTATION_1_A_8_PERIODE_3 = "118888";
    public final static String CS_GENRE_DE_SERVICE = "118837";
    public final static String CS_GENRE_DE_SERVICE_PRESTATION = "118862";
    public static String CS_GENRE_DROIT_API = "119043";
    public final static String CS_GENRE_INDEMNITE_JOURNALIERE = "118895";
    /** Statut groupe */
    public final static String CS_GROUPE_STATUT = "HESTATUT";
    public final static String CS_INDEMNITE_JOURNALIERE_REDUITE = "118899";
    public final static String CS_INDEMNITE_JOURNALIERE_REDUITE_PERIODE_1 = "118880";
    public final static String CS_INDEMNITE_JOURNALIERE_REDUITE_PERIODE_2 = "118885";
    public final static String CS_INDEMNITE_JOURNALIERE_REDUITE_PERIODE_3 = "118890";
    public final static String CS_INDEMNITES_AC = "118821";
    public final static String CS_INSTITUTION_COMMUNICATION = "118993";
    public static String CS_LIEU_ORIGINE = "118933";
    public static String CS_LIMITES_DE_REVENU = "119031";
    public final static String CS_MARIE = "112302";
    public static String CS_MENSUALITE_PRESTATION_FRANCS = "119014";
    public static String CS_MENSUALITE_RENTE_ORDINAIRE_REMPLACEE_FRANCS = "119015";
    public final static String CS_MESURES_INTEGRATION = "118995";
    public final static String CS_MESURES_INTERVENTION_PRECOCE = "118994";
    public static String CS_MINIMUM_GARANTIT = "119032";
    public final static String CS_MODE_PAIEMENT = "118869";
    // ******************** Code système pour les données du régime des APG
    // ********************************/
    public final static String CS_MOIS_COMPTABLE_ET_ANNEE = "118835";
    public static String CS_MOIS_DU_RAPPORT = "119017";
    public final static String CS_MONTANT_ALLOC_ASSISTANCE_PERIODE_2 = "118938";
    public final static String CS_MONTANT_ALLOC_ASSISTANCE_PERIODE_3 = "118942";
    public final static String CS_MONTANT_ALLOC_INIT_TRAVAIL_PERIODE_2 = "118939";
    public final static String CS_MONTANT_ALLOC_INIT_TRAVAIL_PERIODE_3 = "118943";
    // Récap rentes: Envoi des ARC 8D
    public static String CS_MONTANT_FFFFFFFFFFCC_1 = "119004";
    public static String CS_MONTANT_FFFFFFFFFFCC_2 = "119005";
    public static String CS_MONTANT_FFFFFFFFFFCC_3 = "119006";
    public static String CS_MONTANT_FFFFFFFFFFCC_4 = "119007";
    public static String CS_MONTANT_FFFFFFFFFFCC_5 = "119008";
    public final static String CS_MONTANT_MAXIMUM_EN_FRS_FFFFFFFCC = "118815";
    public final static String CS_MOTIFS_INTERRUPTION_PERIODE1 = "118905";
    public final static String CS_MOTIFS_INTERRUPTION_PERIODE2 = "118915";
    public final static String CS_MOTIFS_INTERRUPTION_PERIODE3 = "118925";
    public final static String CS_MOYENS_AUXILIAIRES = "119001";
    public static String CS_NATIONALITE = "119065";
    public static String CS_NB_ANNEE_ANTICIPATION = "119060";
    public static String CS_NB_ANNEES_BONIFI_TACHES_ASSISTANCE_AADD = "119056";
    public static String CS_NB_ANNEES_BONIFI_TACHES_EDUC = "119053";
    public static String CS_NB_ANNEES_BONIFI_TACHES_EDUCATIVES_AADD = "119055";
    public static String CS_NB_ANNEES_BONIFI_TACHES_TRANSITOIREs_AD = "119057";
    public final static String CS_NIF = "118812";
    public final static String CS_NO_DECISION_AI_COMMUNICATION_PERIODE_1 = "118935";
    public final static String CS_NOMBRE_DE_JOURS_DE_SERVICE = "118844";
    public final static String CS_NOMBRE_DE_JOURS_PERIODE_1 = "118876";
    public final static String CS_NOMBRE_DE_JOURS_PERIODE_2 = "118881";
    public final static String CS_NOMBRE_DE_JOURS_PERIODE_3 = "118886";
    public final static String CS_NOMBRE_DE_JOURS_PERIODE1 = "118902";
    public final static String CS_NOMBRE_DE_JOURS_PERIODE2 = "118912";
    public final static String CS_NOMBRE_DE_JOURS_PERIODE3 = "118922";
    public final static String CS_NOMBRE_ENFANTS = "118849";
    public final static String CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P1 = "118904";
    public final static String CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P2 = "118914";
    public final static String CS_NOMBRE_JOURS_INTERR_READAPT_AVEC_IJ_P3 = "118924";
    public static String CS_NOUVEAU_NUMERO_ASSURE_AYANT_DROIT_PRESTATION = "119013";
    public final static String CS_NUM_AGENCE_PAIE_PRESTATION = "118937";
    public final static String CS_NUM_ASSURE_AYANT_DROIT_PRESTATION = "118938";
    public final static String CS_NUM_CAISSE_PAIE_PRESTATION = "118936";
    public final static String CS_NUMERO_ASSURE = "118839";
    public final static String CS_NUMERO_ASSURE_13_POSITIONS = "118932";
    public final static String CS_NUMERO_ASSURE_A_ONZE_CHIFFRES = "118802";
    public final static String CS_NUMERO_ASSURE_AYANT_DROIT_CONJOINT = "118931";
    public final static String CS_NUMERO_ASSURE_PERE_ENFANT = "118870";
    public final static String CS_NUMERO_ASSURE_PERSONNE_REQUERANTE = "118863";
    public final static String CS_NUMERO_DE_COMPTE = "118838";
    public final static String CS_NUMERO_DE_CONTROLE = "118840";
    // /** ARC AI **/
    public final static String CS_NUMERO_DE_DECISION_AAAANNNNNNP = "118801";
    public final static String CS_NUMERO_DE_PRESTATION_OU_DU_FOURNISSEUR_DE_PREST = "118806";
    public final static String CS_OFFICE_AI = "118800";
    public static String CS_OFFICEAI_COMPETENT_AYANT_DROIT = "119033";
    public static String CS_OFFICEAI_COMPETENT_EPOUSE = "119034";
    public final static String CS_ORIENTATION_PROFESSIONNELLE = "118996";
    /** CS Statut */
    public final static String CS_ORPHELIN = "117005";
    public final static String CS_PERIODE_A_JJMMAA = "118894";

    public final static String CS_PERIODE_A_JJMMAA_PERIODE1 = "118910";
    public final static String CS_PERIODE_A_JJMMAA_PERIODE2 = "118920";
    public final static String CS_PERIODE_A_JJMMAA_PERIODE3 = "118930";
    public final static String CS_PERIODE_DE_JJMMAA = "118893";
    public final static String CS_PERIODE_DE_JJMMAA_PERIODE1 = "118909";
    public final static String CS_PERIODE_DE_JJMMAA_PERIODE2 = "118919";
    public final static String CS_PERIODE_DE_JJMMAA_PERIODE3 = "118929";
    public final static String CS_PERIODE_DE_SERVICE_A_JJMMAA = "118843";
    public final static String CS_PERIODE_DE_SERVICE_DE_JJMM = "118842";
    public final static String CS_PERIODE_DE_SERVICE_DE_JJMMAA = "118873";
    // Versement de l''indemnité journalière
    public final static String CS_PERSONNE_ASSUREE = "112701";
    public final static String CS_PETITE_INDEMNITE_JOURNALIERE_AI = "118875";
    public final static String CS_PREM_NUM_ASSURE_COMPL = "118939";
    public static String CS_PREMIER_NUMERO_ASSURE_COMPLEMENTAIRE = "119011";
    public final static String CS_PREMIERE_DEMANDE = "118820";
    public static String CS_PREST_DEJA_DIMINUE = "112602";
    public final static String CS_PRESTATION_SELON_TARIF = "118811";
    /** CS Statut */
    public final static String CS_PROBLEME = "117004";
    // HEGENCART ://Genre de carte
    public final static String CS_QUESTIONNAIRE = "112001";
    public final static String CS_RECLASSEMENT = "118998";
    public final static String CS_RECRUE = "118841";
    public static String CS_REDUCTION = "119044";
    public static String CS_REDUCTION_ANTICIPATION_FRANCS = "119061";
    // 9 ème révision (ancien droit)
    // enregistrement 01
    public static String CS_REFUGIE = "119009";
    public final static String CS_RENTE = "119003";
    public final static String CS_REPARTITION_EMPLOYEUR_ASSURE = "112403";
    public final static String CS_REPARTITION_ENTRE_EMPLOYEUR_PERSONNE_ASSUREE = "112703";
    // enregistrement 02
    public static String CS_REVENU_ANNUEL_MOYEN_DETERMINANT_EN_FRANCS = "119019";
    public static String CS_REVENU_ANNUEL_MOYEN_SANS_BONIFI_TACHES_EDUC_FRANCS = "119051";

    public final static String CS_REVENU_JOURN_DET_NON_PLAFONNE_FFFFCC = "118896";
    public final static String CS_REVENU_JOURNALIER_MOYEN = "118846";
    public final static String CS_REVENU_LUCRATIF = "119082";

    public final static String CS_REVENU_SANS_INVALIDITE_PAR_ANNEE = "119081";
    public static String CS_REVENUS_PRIS_EN_COMPTE = "119022";
    public static String CS_SECOND_NUMERO_ASSURE_COMPLEMENTAIRE = "119012";

    public final static String CS_SERVICE_PLACEMENT = "118999";
    public final static String CS_STATUT_DE_LA_DECISION = "118817";
    public static String CS_SUPPLEMENT_AJOURNEMENT_FRANCS = "119029";
    // ******************** Code système pour les données du régime des IJAI
    // ********************************/
    // champs pour ARC IJAI
    public final static String CS_SUPPLEMENT_DE_READAPTATION = "118874";
    public static String CS_SURVENANCE_EVENEMENT_ASSURE_AYANT_DROIT_MMAA = "119039";

    public static String CS_SURVENANCE_EVENEMENT_ASSURE_EPOUSE_MMAA = "119040";
    public final static String CS_TAUX_JOUR_ALLOC_BASE = "118856";
    public final static String CS_TAUX_JOURNALIER = "118852";
    public final static String CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE_1 = "118877";
    public final static String CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE_2 = "118882";
    public final static String CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE_3 = "118887";
    public final static String CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE1 = "118903";
    public final static String CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE2 = "118913";
    public final static String CS_TAUX_JOURNALIER_MONTANT_FFFCC_PERIODE3 = "118923";
    /** CS Statut */
    public final static String CS_TERMINE = "117003";

    public final static String CS_TOTAL_ALLOC_POUR_PERTE_GAIN = "118868";
    public final static String CS_TOTAL_APG = "118853";
    public final static String CS_TOTAL_INDEMN_JOURN_AI_MONTANT_FFFFFCC_P1 = "118907";
    public final static String CS_TOTAL_INDEMN_JOURN_AI_MONTANT_FFFFFCC_P2 = "118917";
    public final static String CS_TOTAL_INDEMN_JOURN_AI_MONTANT_FFFFFCC_P3 = "118927";
    public final static String CS_TOTAL_INDEMNITE_JOURNALIERE_AI_MONTANT_FFFFFCC = "118891";
    public final static String CS_TOTAL_INDEMNITES_ANNONCEES = "119091";
    public final static String CS_VALABLE_DES_LE_AAAAMMJJ = "118813";
    public final static String CS_VALABLE_JUSQU_AU_AAAAMMJJ = "118814";
    public final static String CS_VERSEMENT_INDEMNITE_JOURNALIERE_PERIODE1 = "118906";
    public final static String CS_VERSEMENT_INDEMNITE_JOURNALIERE_PERIODE2 = "118916";
    public final static String CS_VERSEMENT_INDEMNITE_JOURNALIERE_PERIODE3 = "118926";
    public final static String CS_VEUF_VEUVE = "112303";
    public final static String CS_VISA_DU_COLLABORATEUR_DE_L_OFFICE_AI = "118807";
    // /** Valeur maximale de la date de cloture sous format bbAA*/
    public final static int DATE_CLOTURE_AA_MAX = 99;
    /** Date cloture (ARC 11 04) */
    public final static String DATE_CLOTURE_MMAA = "118025";
    /** Valeur maximale de la date de cloture sous format MMAA */
    public final static int DATE_CLOTURE_MMAA_MAX = 1299;
    public static String DATE_CLOTURE_OU_ORDRE_SPLITTING_MMAA = "118084";
    /** Date début, 1er domicile MMAA (ARC 11 04) */
    public final static String DATE_DEBUT_1ER_DOMICILE_MMAA = "118028";
    /** Date debut 2ème domicile MMAA (ARC 11 04) */
    public final static String DATE_DEBUT_2EME_DOMICILE_MMAA = "118030";
    /** Date début 3ème domicile MMAA (ARC 11 04) */
    public final static String DATE_DEBUT_3EME_DOMICILE_MMAA = "118032";
    /** Date début 4ème domicile MMAA (ARC 11 04) */
    public final static String DATE_DEBUT_4EME_DOMICILE_MMAA = "118034";
    /** Date fin, 1er domicile MMAA (ARC 11 04) */
    public final static String DATE_FIN_1ER_DOMICILE_MMAA = "118029";
    /** Date fin 2ème domicile MMAA (ARC 11 04) */
    public final static String DATE_FIN_2EME_DOMICILE_MMAA = "118031";
    /** Date fin 3ème domicile MMAA (ARC 11 04) */
    public final static String DATE_FIN_3EME_DOMICILE_MMAA = "118033";
    /** Date fin 4ème domicile MMAA (ARC 11 04) */
    public final static String DATE_FIN_4EME_DOMICILE_MMAA = "118035";
    /** Date dans le cas d'un ARC 25 01-99 */
    public final static String DATE_MMAA_OU_ZERO_AVANT_01071972 = "118045";
    public final static String DATE_MMAA_OU_ZERO_AVANT_01071972_2 = "118103";
    public final static String DATE_MMAA_OU_ZERO_AVANT_01071972_3 = "118107";
    public final static String DATE_MMAA_OU_ZERO_AVANT_01071972_4 = "118111";
    public final static String DATE_MMAA_OU_ZERO_AVANT_01071972_5 = "118115";
    public final static String DATE_MMAA_OU_ZERO_AVANT_01071972_6 = "118119";
    public final static String DATE_MMAA_OU_ZERO_AVANT_01071972_7 = "118123";
    public final static String DATE_MMAA_OU_ZERO_AVANT_01071972_8 = "118127";
    public static String DATE_NAISSANCE_1_JJMMAA = "118100";
    /** Date de naissance 2 (ARC 11 02) */
    public final static String DATE_NAISSANCE_2_JJMMAAAA = "118017";
    /** Date de naissance 3 (ARC 11 03) */
    public final static String DATE_NAISSANCE_3_JJMMAAAA = "118021";
    /** Date de naissance en JJMMAAAA */
    public final static String DATE_NAISSANCE_JJMMAAAA = "118010";
    public static String DATE_ORDRE_JJMMAA = "118094";
    /** Date ordre (ARC 11 04) */
    public final static String DATE_ORDRE_JJMMAAAA = "118026";
    public final static String DATE_TRANSMISSION = "118082"; // Champs
    /**
     * Domicile en 1 pour la suisse, 2 pas de domicile en Suisse, 3 pour l'Allemagne, la Finlande et la Norvège (ARC 11
     * 04)
     */
    public final static String DOMICILE_EN_SUISSE_CODE_INFORMATION = "118027";
    public final static String DUREE_COTISATIONS_DEBUT = "118059";
    public final static String DUREE_COTISATIONS_FIN = "118060";
    /** L'état nominatif */
    public final static String ETAT_NOMINATIF = "118008";
    // ///////////////////////////////////////
    public static String ETAT_NOMINATIF_1 = "118098";
    /** Etat nominatif 2 (ARC 11 02) */
    public final static String ETAT_NOMINATIF_2 = "118016";

    /** Etat nominatif 3 (ARC 11 03) */
    public final static String ETAT_NOMINATIF_3 = "118020";
    /** L'état d'origine */
    public final static String ETAT_ORIGINE = "118011";
    public static String ETAT_ORIGINE_1 = "118101";
    /** Etat d'origin 2 (ARC 11 02) */
    public final static String ETAT_ORIGINE_2 = "118018";
    /** Etat d'origine (ARC 11 03) */
    public final static String ETAT_ORIGINE_3 = "118022";
    public static String EXERCICE_COMPTABLE_AAAA = "118134";
    /** Date de début du splitting (ARC 11 05) */
    public final static String LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_01 = "118037";
    // /////////
    public final static String LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_02 = "118066";
    public final static String LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_03 = "118067";
    public final static String LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_04 = "118068";
    public final static String LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_05 = "118069";
    public final static String LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_06 = "118070";
    public final static String LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_07 = "118071";
    public final static String LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_08 = "118072";

    public final static String LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_09 = "118073";
    public final static String LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_10 = "118074";

    public final static String LISTE_ANNEES_DEBUT_FIN_CHIFFRECLEF_AAAA_11 = "118075";
    public static String MONTANT_1 = "118144";
    public static String MONTANT_2 = "118146";
    public static String MONTANT_3 = "118149";
    public static String MONTANT_4 = "118152";
    public static String MONTANT_5 = "118155";
    public static String MONTANT_AVOIR_1 = "118137";
    public static String MONTANT_AVOIR_2 = "118141";
    public static String MONTANT_CREANCIER_1 = "118171";
    public static String MONTANT_CREANCIER_2 = "118173";
    public static String MONTANT_DEBITEUR_1 = "118170";
    public static String MONTANT_DEBITEUR_2 = "118172";
    public static String MONTANT_DOIT_1 = "118136";
    public static String MONTANT_DOIT_2 = "118140";
    public static String MONTANT_SOLDE_1 = "118138";
    public static String MONTANT_SOLDE_2 = "118142";
    /** Le motif de l'annonce */
    public final static String MOTIF_ANNONCE = "118012";
    //
    public static String MOTIF_RCI_OU_ORDRE_SPLITTING = "118097";
    public final static String NOMBRE_INSCRIPTIONS_CI = "118079"; // Champs
    public final static String NUMERO_AFILLIE = "118054";
    /** Le numéro de l'agence */
    public final static String NUMERO_AGENCE = "118004";
    public final static String NUMERO_AGENCE_2 = "118105";
    public final static String NUMERO_AGENCE_3 = "118109";
    public final static String NUMERO_AGENCE_4 = "118113";
    public final static String NUMERO_AGENCE_5 = "118117";
    public final static String NUMERO_AGENCE_6 = "118121";
    public final static String NUMERO_AGENCE_7 = "118125";
    public final static String NUMERO_AGENCE_8 = "118129";
    public final static String NUMERO_AGENCE_CI = "118051";
    public final static String NUMERO_AGENCE_COMMETTANTE = "118047";
    public static String NUMERO_AGENCE_FIXANT_RENTE = "118086";
    /** Le numéro de l'annonce */
    public final static String NUMERO_ANNONCE = "118006";
    /** Le numéro de l'assuré */
    public final static String NUMERO_ASSURE = "118007";
    /** Le numéro de l'assuré (cas d'un ARC 11 02) */
    public final static String NUMERO_ASSURE_1 = "118013";
    /** Numéro d'assuré 2 (ARC 11 02) */
    public final static String NUMERO_ASSURE_2 = "118015";
    /** Numéro assuré 3 (ARC 11 03) */
    public final static String NUMERO_ASSURE_3 = "118019";
    public static String NUMERO_ASSURE_A_COMPLETER = "118088";

    public static String NUMERO_ASSURE_ANTERIEUR = "118089";
    public static String NUMERO_ASSURE_AVANT_1_7_1972 = "118090";
    public final static String NUMERO_ASSURE_AVANT_DROIT_OU_PARTENAIRE = "118049";
    /** Numéro d'assuré de l'ayant droit (ARC 11 04) */
    public final static String NUMERO_ASSURE_AYANT_DROIT = "118024";
    public static String NUMERO_ASSURE_COMPLETE = "118087";
    /**
     * Numéro d'assuré du conjoint dans le cas d'un splitting en cas de divorce (ARC 11 05)
     */
    public final static String NUMERO_ASSURE_CONJOINT_SPLITTING_DIVORCE = "118036";
    public static String NUMERO_ASSURE_PARTENAIRE = "118091";
    /** Le numéro de la caisse */
    public final static String NUMERO_CAISSE = "118003";
    public final static String NUMERO_CAISSE__CI = "118050";
    public final static String NUMERO_CAISSE_2 = "118104";
    public final static String NUMERO_CAISSE_3 = "118108";
    public final static String NUMERO_CAISSE_4 = "118112";
    public final static String NUMERO_CAISSE_5 = "118116";
    public final static String NUMERO_CAISSE_6 = "118120";
    public final static String NUMERO_CAISSE_7 = "118124";
    public final static String NUMERO_CAISSE_8 = "118128";
    public final static String NUMERO_CAISSE_COMMETTANTE = "118046";
    public static String NUMERO_CAISSE_FIXANT_RENTE = "118085";
    /** Erreur dans l'annonce envoyée (retour de la centrale) */
    public final static String NUMERO_CHAMP_1_INCORRECT = "118042";
    /** Erreur dans l'annonce envoyée (retour de la centrale) */
    public final static String NUMERO_CHAMP_2_INCORRECT = "118043";
    /** Erreur dans l'annonce envoyée (retour de la centrale) */
    public final static String NUMERO_CHAMP_3_INCORRECT = "118044";
    public static String NUMERO_DE_COMPTE_1 = "118135";
    public static String NUMERO_DE_COMPTE_2 = "118139";
    public static String NUMERO_DE_COMPTE_3 = "118147";
    public static String NUMERO_DE_COMPTE_4 = "118150";
    public static String NUMERO_DE_COMPTE_5 = "118153";
    public static String NUMERO_DE_RUBRIQUE = "118156";
    public static String NUMERO_DE_RUBRIQUE_2 = "118162";
    public static String NUMERO_DE_RUBRIQUE_3 = "118163";
    public static String NUMERO_DE_RUBRIQUE_4 = "118164";
    public static String NUMERO_DE_RUBRIQUE_5 = "118165";
    public static String NUMERO_POSTAL_EMPLOYEUR = "118093";
    /** Observation de la centrale (retour de la centrale) */
    public final static String OBSERVATION_1_CENTRALE = "118039";
    /** Observation de la centrale (retour de la centrale) */
    public final static String OBSERVATION_2_CENTRALE = "118040";
    /** Observation de la centrale (retour de la centrale) */
    public final static String OBSERVATION_3_CENTRALE = "118041";
    public final static String PART_BONIFICATIONS_ASSISTANCES = "118057";
    // /////////
    public static String PARTIE_INFORMATION = "118092";

    public final static String PC_GENRE_DE_PRESTATION = "119090";
    public final static String PC_NUMERO_AGENCE_PC = "119086";
    public final static String PC_NUMERO_AGENCE_QUI_VERSE_LA_PRESTATION = "119089";
    public final static String PC_NUMERO_CAISSE_QUI_VERSE_LA_PRESTATION = "119088";
    public final static String PC_NUMERO_OFFICE_PC = "119085";
    public final static String PC_REFERENCE_INTERNE_OFFICE_PC = "119087";

    public static String PERIODE_COMPTABLE = "118133";
    /** Le référence interne de la caisse */
    public final static String REFERENCE_INTERNE_CAISSE = "118005";
    public final static String REFERENCE_INTERNE_CAISSE_COMMETTANTE = "118048";
    public static String REPORT_DES_REVENUS_INSCRITS_JUSQUICI = "118131";
    /** Des blancs réservés ou pour compléter... */
    public final static String RESERVE_A_BLANC = "118014";
    public static String RESERVE_A_BLANC_3 = "118166";
    public static String RESERVE_A_BLANC_4 = "118167";
    public static String RESERVE_A_BLANC_5 = "118168";
    public static String RESERVE_A_BLANC_6 = "118169";
    // /////
    public final static String RESERVE_BLANC_2 = "118076";
    public static String RESERVE_ZERO_2 = "118095";
    public final static String RESERVE_ZEROS = "118053";
    public final static String REVENU = "118062";
    /** Le sexe */
    public final static String SEXE = "118009";
    public static String SEXE_1 = "118099";

    public final static String SPLITTING_CAS_DIVORCE = "118081"; // Champs
    public final static String TOTAL_REVENUS = "118078"; // Champs
    public final static String WANT_CHECK_CI_OUVERT_FALSE = "false";
    public final static String WANT_CHECK_CI_OUVERT_TRUE = "true";
    public final static String WANT_CHECK_NUM_AFF_FALSE = "false";
    public final static String WANT_CHECK_NUM_AFF_TRUE = "true";

    // //////////////////////

    // *****************************************************************************************************/
    /**
     * Ajoute l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si l'ajout a échoué
     */
    public void add(BITransaction transaction) throws java.lang.Exception;

    /**
     * Efface la map de valeurs
     */
    public void clear();

    /**
     * Supprime l'enregistrement de la BD
     * 
     * @exception java.lang.Exception
     *                si la suppression a échouée
     */
    public void delete(BITransaction transaction) throws java.lang.Exception;

    /**
     * Retourne le champ enregistrement de l'annonce en cours
     */
    public String getChampEnregistrementFromAttr() throws Exception;

    /**
     * permet d'obtenir la date d'engagement saisit lors de la création de l'arc
     * 
     * @return la date d'engagement ou null si il n'y a pas de date d'engagement associée
     */
    public String getDateEngagement() throws java.lang.Exception;

    /**
     * Renvoie la valeur du champ
     * 
     * @param FIELD
     *            la variable statique désignant le champ
     * @return la valeur du champ
     */
    public String getField(String FIELD) throws Exception;

    /**
     * Renvoie la clef primaire de l'annonce dans la bdd
     * 
     * @return la valeur de la propriété idAnnonce
     */
    public String getIdAnnonce();

    /**
     * Renvoie la clef primaire du lot pour cette annonce<br>
     * Une annonce appartient forcément à un lot<br>
     * Si aucun lot, alors c'est le lot du jour
     * 
     * @return la valeur de la propriété idLot
     */
    public String getIdLot();

    /**
     * permet d'obtenir le numéro d'affilié saisit lors de la création de l'arc
     * 
     * @return le numéro d'affilé ou null si il n'y a pas de numéro d'affilié associé
     */
    public String getNumeroAffilie() throws java.lang.Exception;

    /**
     * permet d'obtenir le numéro d'employé
     * 
     * @return le numéro d'employé ou null
     */
    public String getNumeroEmploye() throws java.lang.Exception;

    /**
     * permet d'obtenir le numéro de succursale
     * 
     * @return le numéro de succursale ou null
     */
    public String getNumeroSuccursale() throws java.lang.Exception;

    /**
     * Renvoie la référence unique
     * 
     * @return la valeur de la propriété refUnique
     */
    public String getRefUnique();

    /**
     * Renvoie le statut
     * 
     * @return la valeur du statut
     */
    public String getStatut();

    public String getUtilisateur();

    /**
     * Insère la valeur d'un champ<br>
     * ex : <code>put(CODE_APPLICATION,"11");</code>
     * 
     * @param le
     *            champ à remplir
     * @param la
     *            valeur du champ
     */
    public void put(String idField, String value);

    /**
     * Insère une Map de valeurs<br>
     * La clef est la variable statique désignant le champ<br>
     * La valeur est la valeur du champ<br>
     * 
     * @param valueMap
     *            l'objet de type Map contenant les clefs et les valeurs
     */
    public void putAll(Map valueMap);

    /**
     * Charge l'enregistrement depuis la BD
     * 
     * @exception java.lang.Exception
     *                si le chargement a échoué
     */
    public void retrieve(BITransaction transaction) throws java.lang.Exception;

    public void setArchivage(Boolean bool);

    public void setCategorie(String cat);

    /**
     * définit la date d'engagement d'un arc
     * 
     * @param date
     *            date d'engagement sous format JJ.MM.AAAA
     */
    public void setDateEngagement(String date);

    /**
     * Définit la valeur de la propriété idAnnonce c'est la clef primaire de l'annonce
     * 
     * @param newIdAnnonce
     *            newIdAnnonce
     */
    public void setIdAnnonce(String newIdAnnonce);

    /**
     * Définit la valeur de la propriété idLot clef étrangère permettant d'identifier le lot auquel appartient cette
     * annonce
     * 
     * @param newIdLot
     *            newIdLot
     */
    public void setIdLot(String newIdLot);

    /**
     * Définit la valeur de la propriété idProgramme Le programme qui fait cet ajout (HELIOS, PAVO...)
     * 
     * @param newIdProgramme
     *            newIdProgramme
     */
    public void setIdProgramme(String newIdProgramme);

    public void setLangueCorrespondance(String langueCorrespondance);

    public void setNumeroAffilie(String numeroAffilie);

    /**
     * définit le numéro de l'employé
     * 
     * @param numeroEmploye
     */
    public void setNumeroEmploye(String numeroEmploye);

    /**
     * définit le numéro de succursale
     * 
     * @param numeroSuccursale
     */
    public void setNumeroSuccursale(String numeroSuccursale);

    /*
     * Définit la priorité d'envoi du lot
     */
    public void setPriorite(String pty);

    /**
     * Définit la valeur de la propriété refUnique clef permettant d'identifier la série auquelle appartient cette
     * annonce
     * 
     * @param newRef
     *            newRef
     */
    public void setRefUnique(String newRef);

    /**
     * Définit la valeur de la propriété statut mettre à CS_EN_ATTENTE si on attend des retours sinon mettre à
     * CS_TERMINE
     * 
     * @param statut
     */
    public void setStatut(String statut);

    /**
     * Définit le type du lot en envoi ou en réception
     * 
     * @param newIdAnnonce
     *            newIdAnnonce
     */
    public void setTypeLot(String typeLot);

    /**
     * Définit la valeur de la propriété utilisateur L'utilisateur qui créé cette annonce (lisible depuis l'objet
     * BSession)
     * 
     * @param newUtilisateur
     *            newUtilisateur
     */
    public void setUtilisateur(String newUtilisateur);

    /**
     * Met à jour l'enregistrement dans la BD
     * 
     * @exception java.lang.Exception
     *                si la mise à jour a échouée
     */
    public void update(BITransaction transaction) throws java.lang.Exception;

    /**
     * permet de déactiver le contrôle du ci ouvert lors de la création d'un arc
     * 
     * @param valeur
     *            false si l'on ne veut pas contrôler que le ci est ouvert avant de créer l'arc
     */
    public void wantCheckCiOuvert(String valeur);

    /**
     * permet de déactiver le contrôle du numéro affilié lors de la création d'un arc
     * 
     * @param valeur
     *            false si l'on ne veut pas contrôler que le numéro affilié avant de créer l'arc
     */
    public void wantCheckNumAffilie(String valeur);

}
