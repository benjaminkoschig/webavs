/*
 * Créé le 27 oct. 05
 */
package globaz.externe;

/**
 * @author dvh
 */
public interface IPRConstantesExternes {

    // APG
    public static final String ATTESTATION_FISCALE_APG = "5001PAP";
    // IJAI
    public static final String ATTESTATION_FISCALE_IJ = "5011PIJ";
    public static final String ATTESTATIONS_NON_RECUES_IJ = "5016PIJ";
    // Branche économique agricole
    public static final String BE_AGRICOLE = "805001";
    public static final String CONTROLE_PRESTATIONS_LOT_APG = "5008PAP";
    public static final String CONTROLE_PRESTATIONS_LOT_IJ = "5014PIJ";
    public static final String DECISION_IJAI = "5019PIJ";
    public static final String DECISION_MATERNITE = "5003PAP";
    public static final String DECISION_MOYENS_DE_DROIT = "5042PIJ";
    public static final String DECISION_REFUS_APG = "5038PAP";
    public static final String DECOMPTE_APG_ACM = "5005PAP";
    public static final String DECOMPTE_APG_NORMAL = "5004PAP";
    public static final String DECOMPTE_APG_VENTILATION = "5039PAP";
    public static final String DECOMPTE_IJ = "5012PIJ";
    public static final String DECOMPTE_MAT_ACM = "5007PAP";
    public static final String DECOMPTE_MAT_LAMAT = "5040PAP";
    public static final String DECOMPTE_MAT_NORMAL = "5006PAP";
    public static final String DECOMPTE_APG_NORMAL_ACMNE = "5044PAP";
    public static final String DECOMPTE_PANDEMIE_NORMAL = "5045PAP";
    public static final String DECOMPTE_MAT_VENTILATION = "5041PAP";
    public static final String ECHEANCES_18_25_ANS_IJ = "5017PIJ";
    public static final String ECHEANCES_REVISION_CAS_IJ = "5021PIJ";
    public static final String FIELDNAME_TABLE_AVS_NUM_AVS = "HXNAVS";
    public static final String FIELDNAME_TABLE_PERIODE_DATEDEBUT = "WHDDEB";
    public static final String FIELDNAME_TABLE_PERIODE_DATEFIN = "WHDFIN";
    public static final String FIELDNAME_TABLE_PERSONNE_DATEDECES = "HPDDEC";
    public static final String FIELDNAME_TABLE_PERSONNE_DATENAISSANCE = "HPDNAI";
    public static final String FIELDNAME_TABLE_PERSONNE_NATIONALITE = "HNIPAY";
    public static final String FIELDNAME_TABLE_PERSONNE_SEXE = "HPTSEX";
    public static final String FIELDNAME_TABLE_TIERS_ID_TIERS_TI = "HTITIE";
    public static final String FIELDNAME_TABLE_TIERS_NOM = "HTLDE1";
    public static final String FIELDNAME_TABLE_TIERS_NOM_FOR_SEARCH = "HTLDU1";
    public static final String FIELDNAME_TABLE_TIERS_PRENOM = "HTLDE2";
    public static final String FIELDNAME_TABLE_TIERS_PRENOM_FOR_SEARCH = "HTLDU2";
    public static final String FORMULAIRE_BASE_INDEMNI_IJ = "5013PIJ";
    public static final String LETTRE_ACCOMPAGNEMENT_APG = "5037PAP";
    public static final String LETTRE_ACCOMPAGNEMENT_IJ = "5020PIJ";
    public static final String LISTE_PRESTATIONS_APG = "5022PAP";
    public static final String LISTE_PRESTATIONS_CONTROLEES_APG = "5002PAP";
    public static final String LISTE_PRESTATIONS_IJ = "5023PIJ";
    public static final String LISTE_STATISTIQUES_OFAS_APG = "5010PAP";
    public static final String OSIRIS_CS_CODE_ISO_MONNAIE_CHF = "215001";
    /** Rôle d'un tiers */
    public final static String OSIRIS_IDEXATION_GED_ROLE_NON_AFFILIE = "517037";
    /*
     * N° de référence infoRom pour les documents PC
     */
    public static final String PC_REF_INFOROM_COMMUNICATION_ADAPTATION = "6508PPC";
    public static final String PC_REF_INFOROM_LAPRAMS_SPAS = "6509PPC";
    public static final String PC_REF_INFOROM_LAPRAMS_SASH = "6510PPC";
    public static final String PC_REF_INFOROM_COMMUNICATION_OCC = "6501PPC";
    public static final String PC_REF_INFOROM_DECISION_APRES_CALCUL = "6500PPC";
    public static final String PC_REF_INFOROM_DECISION_TRANSFERT_DOSSIER = "6503PPC";
    public static final String PC_REF_INFOROM_DECISION_TRANSFERT_DOSSIER_VD = "6504PPC";
    public static final String PC_REF_INFOROM_DEMANDE_ATTESTATION_BILLAG = "6505PPC";
    public static final String PC_REF_INFOROM_TRANSFERT_DOSSIER_NON_OCTROYES = "6502PPC";
    public static final String PC_REF_INFOROM_TRANSFERT_RENTE_CAISSE_ACCEPTANT = "6506PPC";
    public static final String PC_REF_INFOROM_TRANSFERT_RENTE_CAISSE_REFUSANT = "6507PPC";
    /*
     * Domaine application des PC Familles (utilisé pour la mise en GED des documents)
     */
    public static final String PCF_ATTESTATION_FISCALE = "6000PCF";
    public static final String PCF_ATTESTATION_FISCALE_RP = "6018PCF";
    public static final String PCF_DECISION_OCTROI_AJOUT_AIDES_CATEGORIELLES = "6001PCF";
    public static final String PCF_DECISION_OCTROI_ANNONCE_CHANGEMENT = "6002PCF";
    public static final String PCF_DECISION_OCTROI_HORS_RI = "6003PCF";
    public static final String PCF_DECISION_OCTROI_PARTIEL = "6004PCF";
    public static final String PCF_DECISION_OCTROI_PARTIEL_MODIF_ECONOMIQUE = "6005PCF";
    public static final String PCF_DECISION_OCTROI_RI_AVEC_PROJET = "6006PCF";
    public static final String PCF_DECISION_OCTROI_RI_SANS_PROJET = "6007PCF";
    public static final String PCF_DECISION_PROJET = "6008PCF";
    public static final String PCF_DECISION_REFUS = "6009PCF";
    public static final String PCF_DECISION_RENONCIATION = "6010PCF";
    public static final String PCF_DECISION_SUPPRESSION = "6011PCF";
    public static final String PCF_DECISION_SUPPRESSION_VOLONTAIRE = "6012PCF";
    public static final String PCF_DECISION_TRAITEMENT_MASSE_CAISSE_PRINCIPALE = "6019PCF";
    public static final String PCF_DECISION_TRAITEMENT_MASSE_CAISSE_SECONDAIRE = "6020PCF";
    public static final String PCF_FACTURE_IMPRIMER_DECISION_FACTURE = "6013PCF";
    public static final String PCF_FACTURE_IMPRIMER_REFUS_FACTURE = "6015PCF";
    public static final String PCF_FACTURE_REFUS_DEMANDE = "6014PCF";
    public static final String PCF_LETTRE_EN_TETE = "6016PCF";
    public static final String PCF_REVISION_DOSSIER = "6017PCF";
    public static final String PCF_DECISION_NON_ENTREE_EN_MATIERE = "6021PCF";

    public static final String PRONONCES_2_ANS_PLUS_IJ = "5018PIJ";
    /**
     * properties dans les différents domaines pour l'affichage de la ged
     */
    public static final String PROPERTY_AFFICHAGE_DOSSIER_GED = "domaine.nomService.ged";
    /**
     * properties common pour l'affichage de la ged lié aux affiliation
     */
    public static final String PROPERTY_AFFICHAGE_DOSSIER_GED_COTI = "ged.service.name";
    public static final String RECAP_MENSUELLE_ANNONCES_APG = "5009PAP";
    public static final String RECAP_MENSUELLE_ANNONCES_IJ = "5015PIJ";

    public static final String RENTES_LISTE_AVANCE = "document.REListeAvanceExcel";
    /*
     * RFM Numérotation des documents pour Inforom
     */
    public static final String RFM_BORDEREAU = "7003PRF";
    public static final String RFM_DECISION_DE_RESTITUTION = "7002PRF";
    public static final String RFM_DECISION_MENSUELLE_REGIME = "7001PRF";
    public static final String RFM_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_OCTROI = "7043PRF";
    public static final String RFM_DECISION_MENSUELLE_REGIME_AVEC_EXCEDENT_REFUS = "7042PRF";
    public static final String RFM_DECISION_PONCTUELLE = "7000PRF";
    public static final String RFM_LETTRE_EN_TETE = "7004PRF";

    public static final String RFM_LETTRE_TYPE_AIDE_AU_MENAGE_PRIVE_DEMANDE_EVALUATION_DES_BESOINS_AU_CMS = "7039PRF";
    public static final String RFM_LETTRE_TYPE_AIDE_AU_MENAGE_PRIVE_DEMANDE_REEVALUATION_DES_BESOINS_AU_CMS = "7040PRF";
    public static final String RFM_LETTRE_TYPE_AIDE_AU_MENAGE_PRIVE_RAPPEL_DEMANDE_EVALUATION_DES_BESOINS_AU_CMS = "7041PRF";
    public static final String RFM_LETTRE_TYPE_DENTISTE_BIS_DEMANDE_DOCUMENTS_MANQUANTS_AU_DENTISTE = "7031PRF";
    public static final String RFM_LETTRE_TYPE_DENTISTE_DEMANDE_DOCUMENTS_MANQUANTS_ET_QMD_AU_DENTISTE = "7032PRF";
    public static final String RFM_LETTRE_TYPE_DENTISTE_DEMANDE_PRECISION_TRAITEMENT_ORTHODONTIQUE_ET_QMD_AU_DENTISTE = "7037PRF";
    public static final String RFM_LETTRE_TYPE_DENTISTE_ENVOIS_DEVIS_AU_MEDECIN_CONSEIL = "7028PRF";
    public static final String RFM_LETTRE_TYPE_DENTISTE_ENVOIS_DEVIS_TRAITEMENT_ORTHODONTIQUE_AU_MEDECIN_CONSEIL = "7038PRF";
    public static final String RFM_LETTRE_TYPE_DENTISTE_ENVOIS_DOSSIER_COMPLET_AU_MEDECIN_CONSEIL = "7029PRF";
    public static final String RFM_LETTRE_TYPE_DENTISTE_ENVOIS_FACTURE_AU_MEDECIN_CONSEIL = "7027PRF";
    public static final String RFM_LETTRE_TYPE_DENTISTE_ENVOIS_FACTURE_MEDECIN_CONSEIL_DE_LA_FACTURE_TRAITEMENT_ETRANGER = "7035PRF";
    public static final String RFM_LETTRE_TYPE_DENTISTE_ENVOIS_RAPPEL_AU_MEDECIN_CONSEIL = "7030PRF";
    public static final String RFM_LETTRE_TYPE_DENTISTE_LETTRE_ASSURE_SUITE_AU_DEVIS_ETRANGER = "7036PRF";
    public static final String RFM_LETTRE_TYPE_DENTISTE_LETTRE_ASSURE_SUITE_REPONSE_MEDECIN_CONSEIL_DEVIS = "7034PRF";
    public static final String RFM_LETTRE_TYPE_DENTISTE_RAPPEL_DEMANDE_DOCUMENTS_MANQUANTS_ET_QMD_AU_DENTISTE = "7033PRF";
    public static final String RFM_LETTRE_TYPE_LIT_ELECTRIQUE_BON_DE_LIVRAISON = "7022PRF";
    public static final String RFM_LETTRE_TYPE_LIT_ELECTRIQUE_QUESTIONNAIRE_CCJU = "7021PRF";
    public static final String RFM_LETTRE_TYPE_MOYENS_AUXILIAIRES_ANNEXE_BON_ACCUSE_DE_RECEPTION_BENEFICIARE_PC = "7024PRF";

    public static final String RFM_LETTRE_TYPE_MOYENS_AUXILIAIRES_BON_ACHAT_POUR_REMISE_EN_PRET = "7025PRF";
    public static final String RFM_LETTRE_TYPE_MOYENS_AUXILIAIRES_BON_REMISE_EN_PRET = "7023PRF";

    public static final String RFM_LETTRE_TYPE_RESTITUTION_SUITE_A_UN_NOUVEAU_CALCUL_RETROACTIF = "7019PRF";
    public static final String RFM_LETTRE_TYPE_RESTITUTION_SUITE_AU_DECES_DU_BENEFICIAIRE = "7020PRF";
    public static final String RFM_LISTE_DECOMPTE = "7046PRF";
    public static final String RFM_LISTE_RECAP_PAIEMENT = "7045PRF";
    public static final String RFM_LISTE_STATISTIQUES_PAR_MONTANT = "7047PRF";
    public static final String RFM_LISTE_STATISTIQUES_PAR_NOMBRE_DE_CAS = "7044PRF";

    public static final String TABLE_AVS = "TIPAVSP";
    public static final String TABLE_PERIOD = "SFPERIOD";
    public static final String TABLE_PERIOD_FIELD_IDMEMBREFAMILLE = "WHIDMF";
    public static final String TABLE_PERSONNE = "TIPERSP";
    public static final String TABLE_TIERS = "TITIERP";
    public static final String TIERS_CS_ADMINISTRATION_GENRE_AFFAIRES_MILITAIRES = "52890001";
    public static final String TIERS_CS_DOMAINE_ALLOCATION_DE_NOEL = "64800010";

    public static final String TIERS_CS_DOMAINE_APPLICATION_APG = "519002";

    public static final String TIERS_CS_DOMAINE_APPLICATION_DEFAULT = "519004";
    public static final String TIERS_CS_DOMAINE_APPLICATION_IJAI = "519009";
    public static final String TIERS_CS_DOMAINE_APPLICATION_RENTE = "519006";
    public static final String TIERS_CS_DOMAINE_MATERNITE = "519003";
    public static final String TIERS_CS_TYPE_ADRESSE_COURRIER = "508001";
}
