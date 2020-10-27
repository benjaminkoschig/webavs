package globaz.naos.translation;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import globaz.framework.controller.FWController;
import globaz.globall.api.BISession;
import globaz.globall.db.BSession;
import globaz.globall.parameters.FWParametersSystemCode;

/**
 * Insérez la description du type ici. Date de création : (10.06.2002 10:42:09)
 * 
 * @author: Administrator
 */
public class CodeSystem {

    public static final String ADMINISTRATEUR = "19120054";
    // Type d'adresse
    public final static String ADRESSE_PROFESSIONNELLE = "19120045";
    public static final String AVEC_BENIFICIARE = "19150036";

    // Comptabilité Auxiliaire **************************************

    public static final String AVEC_PERSONNEL = "19120095";
    // Avis Mutation
    public final static String AVIS_MUTATION_CREATION = "817001";

    public final static String AVIS_MUTATION_FIN_AFFILIE = "817003";

    // Tiers ********************************************************

    public final static String AVIS_MUTATION_MODIFICATION = "817002";

    public static final String BENEFICIAIRE_ETAT_COMPTABILISE = "850004";
    // Etat pour les quittances des beneficiaires PC
    public final static String BENEFICIAIRE_ETAT_JOURNAL = "19200001";
    public static final String BENEFICIAIRE_ETAT_OUVERT = "850001";
    public static final String BENEFICIAIRE_ETAT_TRAITE = "850003";
    public static final String BENEFICIAIRE_ETAT_VALIDE = "850002";

    public static final String BENEFICIAIRE_PC = "19160027";
    public static final String BORDEREAU_MUTATION = "836017";

    public static final String BOUCLEMENT_ACOMPTE = "836018";
    public final static String BRANCHE_ECO_AGRICULTURE = "805001";

    public final static String BRANCHE_ECO_ETUDIANTS = "805044";

    // Uniquement pour RI et utilisé à la CCVD et à l'agence de Lausanne
    public final static String BRANCHE_ECO_NON_ACTIFS_INSTITUTION = "19150029";
    public final static String BRANCHE_ECO_PERS_SANS_ACT_LUCRA = "805047";
    // Branche Economique
    public final static String BRANCHE_ECO_PERS_VIVANT_PENSION = "805046";

    // Affiliation **************************************************

    public final static String BRANCHE_ECO_PERSONNEL_MAISON = "805038";
    // Catégories de taux
    public final static String CATEGORIE_TAUX_IND_TSE = "845004";

    public final static String CATEGORIE_TAUX_NON_ACTIF = "845003";
    public final static String CHAMPS_MOD_ADRESSE_COURRIER = "813026";

    public final static String CHAMPS_MOD_ADRESSE_DOMICILE = "813030";
    public final static String CHAMPS_MOD_ADRESSE_PAIEMENT = "813027";

    public final static String CHAMPS_MOD_ADRESSE_PROFES = "813031";
    public final static String CHAMPS_MOD_AGENCE_AVS = "813029";
    public final static String CHAMPS_MOD_ANNEE_DECIS_COTI = "813034";
    public final static String CHAMPS_MOD_ASSURANCE_COTI = "813021"; // événement

    public final static String CHAMPS_MOD_BRANCH_ECONO = "813006";
    // utilisé
    public final static String CHAMPS_MOD_CAISSE_AF_EXT = "813016"; // plus
    public final static String CHAMPS_MOD_CAISSE_AVS_EXT = "813015"; // plus
    // utilisé
    public final static String CHAMPS_MOD_CAISSE_LAA = "813017"; // plus utilisé
    public final static String CHAMPS_MOD_CAISSE_LPP = "813018"; // plus utilisé
    // pas
    // utilisé
    public final static String CHAMPS_MOD_CREATION_AFFILIE = "813022";
    public final static String CHAMPS_MOD_CREATION_CONTROL = "813032";
    public final static String CHAMPS_MOD_CREATION_COTI = "813023";
    public final static String CHAMPS_MOD_DATE_DEBUT = "813002";
    public final static String CHAMPS_MOD_DATE_DEBUT_COTI = "813019";
    public final static String CHAMPS_MOD_DATE_FIN = "813003";

    public final static String CHAMPS_MOD_DATE_FIN_COTI = "813020";
    public final static String CHAMPS_MOD_EXO_GENERAL = "813008";
    public final static String CHAMPS_MOD_HIST_TIER = "813025"; // plus utilisé
    public final static String CHAMPS_MOD_IRRECOU = "813009"; // événement pas
    public final static String CHAMPS_MOD_MASSE_ANNU_COTI = "813038";
    public final static String CHAMPS_MOD_MONT_ANNU_COTI = "813037";
    public final static String CHAMPS_MOD_MOTIF_FIN = "813035"; // pas généré
    public final static String CHAMPS_MOD_MOYEN_COMM = "813024";
    public final static String CHAMPS_MOD_NUM_AF_ANCIEN = "813036";
    // utilisé
    public final static String CHAMPS_MOD_NUM_AF_EXT = "813014"; // plus utilisé
    // Champs Modifie
    public final static String CHAMPS_MOD_NUM_AFFILIE = "813001";
    public final static String CHAMPS_MOD_NUM_AVS_EXT = "813013"; // plus
    public final static String CHAMPS_MOD_NUM_LAA = "813004"; // plus utilisé

    public final static String CHAMPS_MOD_NUM_LPP = "813012"; // plus utilisé
    public final static String CHAMPS_MOD_PERIODICITE = "813033";

    public final static String CHAMPS_MOD_PERSON_JURI = "813007";
    // pas
    // utilisé
    public final static String CHAMPS_MOD_PERSON_MAISON = "813011";
    // utilisé
    public final static String CHAMPS_MOD_PERSON_OCCAS = "813010"; // événement
    // TODO
    public final static String CHAMPS_MOD_SIEGE = "813041"; // pas généré TODO
    public final static String CHAMPS_MOD_TIER_AVS = "813042";
    public final static String CHAMPS_MOD_TIER_CANTON = "813040"; // pas généré
    public final static String CHAMPS_MOD_TIER_CONJOINT = "813028";

    // pas
    // utilisé
    public final static String CHAMPS_MOD_TIER_CONTRIB = "813044";
    public final static String CHAMPS_MOD_TIER_NOM = "813039";
    public final static String CHAMPS_MOD_TIER_ORIG = "813045";
    public final static String CHAMPS_MOD_TIER_TITRE = "813043"; // événement
    public final static String CHAMPS_MOD_TYPE_AFFILIE = "813005";
    // Genre Facturation
    public final static String CODE_FACTU_MONTANT_LIBRE = "846001";

    public static final String CONTROLE_EMPLOYEUR_AGENDE_MAN = "19300005";
    public static final String CONTROLE_EMPLOYEUR_AGENDE_MAN_PROCHAIN = "851017";
    public static final String CONTROLE_EMPLOYEUR_AGENDE_MAN_SUITE = "851016";
    public static final String CONTROLE_EMPLOYEUR_COLLABORATION = "19300003";
    public static final String CONTROLE_EMPLOYEUR_COLLABORATION_B = "851008";
    public static final String CONTROLE_EMPLOYEUR_COLLABORATION_C = "851009";

    public static final String CONTROLE_EMPLOYEUR_COLLABORATION_M = "851010";
    public static final String CONTROLE_EMPLOYEUR_COLLABORATION_TM = "851011";
    public static final String CONTROLE_EMPLOYEUR_CRITERES_ENTREPRISE = "19300004";
    public static final String CONTROLE_EMPLOYEUR_CRITERES_ENTREPRISE_AP = "851014";

    public static final String CONTROLE_EMPLOYEUR_CRITERES_ENTREPRISE_P = "851013";
    public static final String CONTROLE_EMPLOYEUR_CRITERES_ENTREPRISE_PP = "851012";

    public static final String CONTROLE_EMPLOYEUR_CRITERES_ENTREPRISE_TP = "851015";

    // Attribution des points pour le Contrôle employeur
    // derniere révision
    public static final String CONTROLE_EMPLOYEUR_DERNIERE_REVISION = "19300001";
    public static final String CONTROLE_EMPLOYEUR_DERNIERE_REVISION_PAS_DIFFERENCES = "851001";

    public static final String CONTROLE_EMPLOYEUR_DERNIERE_REVISION_PROBLEME_IMPORTANT = "851003";
    public static final String CONTROLE_EMPLOYEUR_DERNIERE_REVISION_PROBLEME_MINIME = "851002";
    public static final String CONTROLE_EMPLOYEUR_OBLIGATOIRE = "811001";
    public static final String CONTROLE_EMPLOYEUR_QUALITE_RH = "19300002";
    public static final String CONTROLE_EMPLOYEUR_QUALITE_RH_AB = "851005";
    public static final String CONTROLE_EMPLOYEUR_QUALITE_RH_M = "851006";
    public static final String CONTROLE_EMPLOYEUR_QUALITE_RH_TB = "851004";
    public static final String CONTROLE_EMPLOYEUR_QUALITE_RH_TM = "851007";
    public static final String CRITERE_SELECTION_DATE_ENREGISTREMENT = "853000";
    public static final String CRITERE_SELECTION_DEBUT_AFFILIATION = "853001";
    public static final String DECL_SAL_AMAT_GE = "19300001";
    public static final String DECL_SAL_MIXTE_DAN = "19170092";
    // Déclaration salaire, pré liste
    public static final String DECL_SAL_PRE_LISTE = "807001";
    public static final String DECL_SAL_PRE_MIXTE = "19170000";
    public static final String DECL_SAL_SANS_PRE = "807008";
    // Variables pour le catalogue de texte
    public static final String DOMAINE = "835001";
    public static final String DOMAINE_BENEF_PC = "835004";
    public static final String DOMAINE_CAT_AFF = "835001";
    public final static String DOMAINE_CAT_CAP_CGAS = "835006";
    public static final String DOMAINE_CAT_LAA = "835002";

    public static final String DOMAINE_CAT_LPP = "835003";
    public static final String DOMAINE_CONT_EMPL = "835005";
    public static final String DS_DAN = "807010";
    public static final String DS_ENVOI_PUCS = "19150066";
    // Déclaration de salaire
    // Déclaration de salaire envoyé électroniquement
    public final static String DS_ENVOIE_SELON_NORME = "807002";
    public final static String DS_SWISSDEC = "807011";
    // Etat Facturation
    public static final String ETAT_FACTURATION_COMPTABILISE = "902003";
    public static final String ETAT_TAXE_CO2 = "19200000";
    public static final String ETAT_TAXE_CO2_A_TRAITER = "848002";
    public static final String ETAT_TAXE_CO2_ABANDONNE = "848003";
    public static final String ETAT_TAXE_CO2_FACTURE = "848001";
    public static final String ETATS_RELEVE_COMPTABILISER = "827003";
    public static final String ETATS_RELEVE_FACTURER = "827002";
    // Etats des relevés
    public static final String ETATS_RELEVE_SAISIE = "827001";
    public static final String GEN_PARAM_ASS_AGE_MAX = "826004";
    public static final String GEN_PARAM_ASS_AGE_MIN = "826003";
    public static final String GEN_PARAM_ASS_CATEGORIE_AF = "826011";
    public static final String GEN_PARAM_ASS_CODE_CALC_AGE_MAX = "826006";
    public static final String GEN_PARAM_ASS_CODE_CALC_AGE_MIN = "826005";
    public static final String GEN_PARAM_ASS_COTISATION_MIN = "826013";
    public static final String GEN_PARAM_ASS_EXCLUSION_CAT = "826010";
    public static final String GEN_PARAM_ASS_FACT_EMPLOYE = "826007";
    // Genre Param Assurance
    public static final String GEN_PARAM_ASS_FRANCHISE = "826001";
    public static final String GEN_PARAM_ASS_FRANCHISE_MENSUEL = "826014";
    public static final String GEN_PARAM_ASS_PLAFOND = "826002";
    public static final String GEN_PARAM_ASS_PLANCHER = "826015";
    public static final String GEN_PARAM_ASS_MASSE_MINIMUM = "826016";
    public static final String GEN_PARAM_ASS_REDUCTION = "826008";
    public static final String GEN_PARAM_ASS_REMISE = "826009";
    public static final String GEN_PARAM_ASS_REVENU_MAX = "826012";
    public static final String GEN_VALEUR_ASS_MONTANT = "825003";
    // Genre Valeur Assurance
    public static final String GEN_VALEUR_ASS_TAUX = "825001";
    public static final String GEN_VALEUR_ASS_TAUX_VARIABLE = "825002";

    public final static String GENRE_ADMIN_AGENCE_COMMUNALE = "509031";
    // Genre Administration
    public final static String GENRE_ADMIN_CAISSE_COMP = "509001";
    public final static String GENRE_ADMIN_CAISSE_PROF = "509028";
    // Genre assurance
    public final static String GENRE_ASS_PARITAIRE = "801001";
    public final static String GENRE_ASS_PERSONNEL = "801002";
    public static final String GENRE_CAISSE_AF = "830002";
    // Genre Caisse
    public static final String GENRE_CAISSE_AVS = "830001";
    public static final String GENRE_CAISSE_LAA = "830004";
    public static final String GENRE_CAISSE_LPP = "830003";
    public static final String ID_DOCUMENT_NOUVELLE_AFFILIATION_INDEPENDANT = "85400000000";
    public static final String ID_DOCUMENT_NOUVELLE_AFFILIATION_NONACTIF = "85500000000";
    public static final String ID_DOCUMENT_QUITTANCE_CAS_EXISTANT = "85800000001";
    public static final String ID_DOCUMENT_QUITTANCE_NOUVEAU_CAS = "85900000001";
    public static final String ID_DOCUMENT_QUITTANCE_REMBOURSEMENT = "85930000001";
    // Facturation **************************************************
    public static final String ID_DOCUMENT_QUITTANCE_RETOURNER = "85920000001";
    public static final String ID_DOCUMENT_QUITTANCE_VERSO = "85910000001";
    public static final String ID_DOCUMENT_RADIATION_INDEPENDANT = "85600000000";
    public static final String ID_DOCUMENT_RADIATION_NONACTIF = "85700000000";
    // Intérêts moratoires
    public final static String INTERET_MORATOIRE_AUTOMATIQUE = "229001";

    // Interets des Relevés
    public static final String INTERETS_RELEVE_AUTO = "828001";
    public final static String LANGUE_ALLEMAND = "503002";
    public final static String LANGUE_ANGLAIS = "503003";

    // Langue
    public final static String LANGUE_FRANCAIS = "503001";
    public final static String LANGUE_ITALIEN = "503004";
    public final static String LANGUE_ROMANCHE = "503005";

    public static final String LETTRE_BIENVENUE = "836016";
    // Mode de Recouvrement
    public static final String MODE_RECOUV_AUTOMATIQUE = "907005";
    public final static String MOIS_AOUT = "837008";
    public final static String MOIS_AVRIL = "837004";
    public final static String MOIS_DECEMBRE = "837012";
    public final static String MOIS_FEVRIER = "837002";
    // Mois
    public final static String MOIS_JANVIER = "837001";
    public final static String MOIS_JUILLET = "837007";
    public final static String MOIS_JUIN = "837006";
    public final static String MOIS_MAI = "837005";
    public final static String MOIS_MARS = "837003";
    public final static String MOIS_NOVEMBRE = "837011";
    public final static String MOIS_OCTOBRE = "837010";
    public final static String MOIS_SEPTEMBRE = "837009";
    public final static String MOTIF_AFFIL_LISTES_ECOLES = "816015";
    public final static String MOTIF_AFFIL_NON_ACTIF_AL_ETRANGER = "816005";
    // Motif Affiliation
    public final static String MOTIF_AFFIL_NOUVELLE_AFFILIATION = "816001";

    // Motif de Fin
    public final static String MOTIF_FIN_AUCUN = "0";
    public final static String MOTIF_FIN_CESSATION_ACTIVITE = "803001";
    public final static String MOTIF_FIN_CHANGEMENT_CAISSE = "803002";
    public final static String MOTIF_FIN_DIV = "803003";
    public final static String MOTIF_FIN_AFFILIE_ERREUR = "803004";
    public final static String MOTIF_FIN_CHANGEMENT_NOM = "803005";
    public final static String MOTIF_FIN_DECES = "803006";
    public final static String MOTIF_FIN_RETRAITE = "803008";
    public final static String MOTIF_FIN_EPOUSE_PARTIELLEMENT_ACTIVE = "803009";
    public final static String MOTIF_FIN_PARTIELLEMENT_ACTIVE = "803010";
    public final static String MOTIF_FIN_EPOUSE_ACTIVE_SUP_50PURCENT = "803011";
    public final static String MOTIF_FIN_ACTIF_SUP_50PURCENT = "803012";
    public final static String MOTIF_FIN_A_ETRANGER = "803013";
    public final static String MOTIF_FIN_CAISSE_COMPETENTE = "803023";
    public final static String MOTIF_FIN_SORTIE = "803024";
    public final static String MOTIF_FIN_SORTIE_FUSION = "803025";
    public final static String MOTIF_FIN_REORGANISATION = "803026";
    public final static String MOTIF_FIN_DISSOLUTION = "803029";
    public final static String MOTIF_FIN_LIQUIDATION = "803030";
    public final static String MOTIF_FIN_FIN_COUV_ASSURANCE = "803035";
    public final static String MOTIF_FIN_FIN_ADHESION = "803036";
    public final static String MOTIF_FIN_EXCEPTION = "803037";
    public final static String MOTIF_FIN_SANS_PERS = "803038";
    public final static String MOTIF_FIN_MASSE = "803039";
    public final static String MOTIF_FIN_DEPART_HORS_CANTON = "803040";
    public final static String MOTIF_FIN_FAILLITE = "19130017";
    public final static String Transfert_dans_une_autre_commune = "803041";
    public final static String Remise_de_l_exploitation = "803042";
    public final static String MOTIF_FIN_EXCLUSION_LTN = "803043";

    // Nature Rubrique
    public final static String NAT_RUBRIQUE_COTI_AVEC_MASSE = "200002";
    public final static String NAT_RUBRIQUE_COTI_SANS_MASSE = "200003";
    public final static String PARTIC_AFFILIE_1_SOMMATION = "818003";

    public final static String PARTIC_AFFILIE_2_SOMMATION = "818004";
    public final static String PARTIC_AFFILIE_2_SOMMATION_TAXATION = "818005";
    public final static String PARTIC_AFFILIE_ACTI_ACCESS = "818002";
    public final static String PARTIC_AFFILIE_ADMINISTRATEUR = "19120054";
    public final static String PARTIC_AFFILIE_BAREME_DEGRESSIF = "818026";
    public final static String PARTIC_AFFILIE_CODE_BLOCAGE_DECFINAL = "818023";
    public final static String PARTIC_AFFILIE_COT_PERS_AUTRE_AGENCE = "818024";

    public final static String PARTIC_AFFILIE_DEPARTEMENT = "818009";
    public final static String PARTIC_AFFILIE_DEROG_ACOMTEMP = "818012";
    public final static String PARTIC_AFFILIE_EXEMPT_AMENDE_DEC_STRUCT = "818025";

    public final static String PARTIC_AFFILIE_EXEMPT_COTI = "818010";
    public final static String PARTIC_AFFILIE_EXEMPTION_CO2 = "818021";
    public final static String PARTIC_AFFILIE_FICHE_PARTIELLE = "818022";
    public final static String PARTIC_AFFILIE_FORCER_TSE = "818028";
    public final static String PARTIC_AFFILIE_HOSPICE_GENERAL = "818020";
    public final static String PARTIC_AFFILIE_INSCRIT_RC = "19120060";
    public final static String PARTIC_AFFILIE_NON_ACTIF_ASSISSTE = "818014";
    public final static String PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE = "818015";
    public final static String PARTIC_AFFILIE_NON_ACTIF_BENEFICIAIRE_FAMILLE = "818027";
    public final static String PARTIC_AFFILIE_OPPOSITION = "818007";
    public final static String PARTIC_AFFILIE_PERIODE_AFFILIATION = "818017";
    public final static String PARTIC_AFFILIE_PERSONNEL_MAISON = "818018";
    public final static String PARTIC_AFFILIE_PP_PAR = "19120103";
    public final static String PARTIC_AFFILIE_PP_PERS = "19120104";
    public final static String PARTIC_AFFILIE_RECOURS = "818008";
    public final static String PARTIC_AFFILIE_REFUGIE = "818013";
    public final static String PARTIC_AFFILIE_RMCAS = "818019";

    public final static String PARTIC_AFFILIE_SANS_COMM_FISC = "818006";
    public final static String PARTIC_AFFILIE_SANS_PERSONNEL = "818011";
    // Particularité Affiliation
    public final static String PARTIC_AFFILIE_SURSIS = "818001";
    public final static String PARTIC_AFFILIE_CLOTURE_RECAP_MANUELLE = "818030";
    public final static String PARTIC_PERIO_CONTR_EMPLOYEUR = "818016";

    // public final static String PERIODICITE_SEMESTRIELLE = "802003";
    public final static String PERIODICITE_ANNUELLE = "802004";
    public final static String PERIODICITE_ANNUELLE_30_JUIN = "802005";
    public final static String PERIODICITE_ANNUELLE_30_SEPT = "802007";
    public final static String PERIODICITE_ANNUELLE_31_MARS = "802006";
    // Périodicité
    public final static String PERIODICITE_MENSUELLE = "802001";
    public final static String PERIODICITE_TRIMESTRIELLE = "802002";
    public static final String PERS_JURIDIQUE_CODE_A = "A";
    public static final String PERS_JURIDIQUE_CODE_B = "B";

    // Personne Juridique
    public final static String PERS_JURIDIQUE_NA = "806018";
    public static final String PERS_JURIDIQUE_RAISON_INDIVIDUELLE = "806001";
    public static final String PERS_JURIDIQUE_SA = "806004";
    public static final String PERS_JURIDIQUE_SOCIETE_IMMOBILIERE = "19120061";
    public static final String PREVISION_ACOMPTE = "836019";
    public static final String RAPPEL_SUIVI_RELEVES = "836003";

    // Role
    public final static String ROLE_AFFILIE = "517002";

    public final static String ROLE_AFFILIE_PARITAIRE = "517039";
    public final static String ROLE_AFFILIE_PERSONNEL = "517040";

    private final static String SESSION_CONTROLLER_NAME = "objController";
    public final static String SEXE_FEMME = "516002";
    // Sexe
    public final static String SEXE_HOMME = "516001";
    public static final String SOMMATION_SUIVI_RELEVES = "836004";
    // Status Suivi
    public final static String STATUS_SUIVI_AFFILIATION = "815005";

    public static final String TYPE_ADHESION_AFFILIATION = "19140016";
    public static final String TYPE_ADHESION_AGENCE = "19140015";
    public static final String TYPE_ADHESION_ASSOCIATION = "824002";
    // Type Adhesion
    public static final String TYPE_ADHESION_CAISSE = "824001";
    public static final String TYPE_ADHESION_CAISSE_PRINCIPALE = "824003";
    public static final String TYPE_ADHESION_EMPLOYEUR = "824005";

    public static final String TYPE_ADHESION_PERSONNELLE = "824006";

    public final static String TYPE_AFFILI_BENEF_AF = "19150036";

    public final static String TYPE_AFFILI_CAP_EMPLOYEUR = "804014";

    public final static String TYPE_AFFILI_CAP_INDEPENDANT = "804015";

    // types de catalogues babel

    public final static String TYPE_AFFILI_CGAS_EMPLOYEUR = "804016";
    public final static String TYPE_AFFILI_CGAS_INDEPENDANT = "804017";
    public final static String TYPE_AFFILI_EMPLOY = "804002";

    public final static String TYPE_AFFILI_EMPLOY_D_F = "804012";
    public final static String TYPE_AFFILI_FICHIER_CENT = "804009";
    // Type Affiliation
    public final static String TYPE_AFFILI_INDEP = "804001";
    public final static String TYPE_AFFILI_INDEP_EMPLOY = "804005";

    public final static String TYPE_AFFILI_LTN = "804010";
    public final static String TYPE_AFFILI_NON_ACTIF = "804004";

    public final static String TYPE_AFFILI_NON_SOUMIS = "804013";
    public final static String TYPE_AFFILI_PROVIS = "804007";
    public final static String TYPE_AFFILI_SELON_ART_1A = "804006";
    public final static String TYPE_AFFILI_TSE = "804008";
    public final static String TYPE_AFFILI_TSE_VOLONTAIRE = "804011";
    public final static String TYPE_ASS_ACCUEIL_ENFANT = "812038";
    public final static String TYPE_ASS_AFI = "812010";
    public final static String TYPE_ASS_AUTRES = "812004";
    public final static String TYPE_ASS_CAP_10 = "812028";
    // Type assurance
    public final static String TYPE_ASS_CAP_15 = "812036";
    public final static String TYPE_ASS_CAP_16 = "812037";
    public final static String TYPE_ASS_CAP_17 = "812040";
    public final static String TYPE_ASS_CAP_20 = "812030";
    public final static String TYPE_ASS_CAP_30 = "812031";
    public final static String TYPE_ASS_CAP_31 = "812032";
    public final static String TYPE_ASS_CAP_40 = "812033";
    public final static String TYPE_ASS_CAP_41 = "812034";
    public final static String TYPE_ASS_CAP_42 = "812041";
    public final static String TYPE_ASS_CAP_50 = "812035";
    public final static String TYPE_ASS_CAP_60 = "812042";
    public final static String TYPE_ASS_CGAS = "812029";
    public final static String TYPE_ASS_COTISATION_AC = "812006";
    public final static String TYPE_ASS_COTISATION_AC2 = "812007";
    public final static String TYPE_ASS_COTISATION_AF = "812002";
    public final static String TYPE_ASS_COTISATION_AVS_AI = "812001";
    public final static String TYPE_ASS_COTISATION_FEDERATIVE = "812023";
    public final static String TYPE_ASS_COTISATION_PERIODE = "812022";
    public final static String TYPE_ASS_CPS_AUTRE = "812016";
    public final static String TYPE_ASS_CPS_GENERAL = "812015";
    public final static String TYPE_ASS_FFPP = "812009";
    public final static String TYPE_ASS_FFPP_MASSE = "812027";
    public final static String TYPE_ASS_FRAIS_ADMIN = "812003";
    public final static String TYPE_ASS_FRAIS_ADMIN_MAJ = "812043";
    public final static String TYPE_ASS_IMPOT_SOURCE = "812018";
    public static final String TYPE_ASS_LAA = "812008";
    public final static String TYPE_ASS_LAE = "812026";
    public final static String TYPE_ASS_MANUELLE = "812021";
    public final static String TYPE_ASS_MATERNITE = "812005";
    public final static String TYPE_ASS_PC_FAMILLE = "812024";
    public final static String TYPE_ASS_PREVHOR = "812020";
    public final static String TYPE_ASS_REDCOTI_DOUBLE_AFF = "812013";
    public final static String TYPE_ASS_REDCOTI_DSE = "812014";
    public final static String TYPE_ASS_REDPFA_DOUBLE_AFF = "812011";
    public final static String TYPE_ASS_REDPFA_DSE = "812012";
    public final static String TYPE_ASS_REDPFA_DSE_VARIABLE = "812039";
    public final static String TYPE_ASS_TAXE_AMENDE = "812017";
    public final static String TYPE_ASS_TAXE_CO2 = "812019";
    // Type Association
    public static final String TYPE_ASSOCIATION_ASSOCIE = "834001";
    public static final String TYPE_ASSOCIATION_COMMENDIT = "834002";
    public static final String TYPE_CALCUL_COTISATION = "833003";
    public static final String TYPE_CALCUL_MASSE = "833002";
    // Type Calcul
    public static final String TYPE_CALCUL_STANDARD = "833001";
    public static final String TYPE_CAT_ANNONCE = "836001";
    public static final String TYPE_CAT_ANNONCE_LAA = "836014";
    public static final String TYPE_CAT_ANNONCE_LAA_TSE = "836022";
    public static final String TYPE_CAT_ANNONCE_LPP = "836010";

    public static final String TYPE_CAT_ATTESTATION_AFFILIATION = "836006";

    public static final String TYPE_CAT_ATTESTATION_IND_EMPL = "836034";

    public static final String TYPE_CAT_ATTESTATION_IP = "836015";
    public static final String TYPE_CAT_ATTESTATION_PERSONNELLE = "836036";
    public static final String TYPE_CAT_CONFIRMATION = "836002";

    public final static String TYPE_CAT_DECISION_CAP = "836037";

    public final static String TYPE_CAT_DECISION_CGAS = "836038";
    public static final String TYPE_CAT_FICHE_CARTOTHEQUE = "836005";
    public static final String TYPE_CAT_QUEST_LAA = "836011";
    public static final String TYPE_CAT_QUEST_LPP = "836007";

    public static final String TYPE_CAT_RAPPEL_LAA = "836012";

    public static final String TYPE_CAT_RAPPEL_LPP = "836008";
    public static final String TYPE_CAT_SOMMATION_LAA = "836013";
    public static final String TYPE_CAT_SOMMATION_LPP = "836009";
    public static final String TYPE_CAT_CONTROLE_LPP = "836400";
    // Type Afact
    public static final String TYPE_FACT_FACT_STANDARD = "904001";
    public static final String TYPE_LETTRE_DECISION = "836031";
    public static final String TYPE_LETTRE_LIBRE = "836030";
    public static final String TYPE_LETTRE_PROCHAIN_CONTROLE = "836029";
    public static final String TYPE_LETTRE_REDISTRIBUTION_TAXECO2 = "836033";

    public static final String TYPE_LIEN_ASSOCIE = "819007";
    public static final String TYPE_LIEN_DOMICILE_CHEZ = "819004";
    public final static String TYPE_LIEN_DOMICILE_CHEZ_INV = "820024";
    // Type de lien d'affilié
    public static final String TYPE_LIEN_PERSONNEL_DECLARE = "819001";
    // Type de lien d'affilié inverse
    public final static String TYPE_LIEN_PERSONNEL_DECLARE_INV = "820021";
    public static final String TYPE_LIEN_PERSONNEL_MAISON = "819005";

    public final static String TYPE_LIEN_PERSONNEL_MAISON_INV = "820025";
    public static final String TYPE_LIEN_REGIE = "819003";
    public final static String TYPE_LIEN_REGIE_INV = "820023";
    public static final String TYPE_LIEN_SUCCURSALE = "819006";

    public final static String TYPE_LIEN_SUCCURSALE_INV = "820026";
    public static final String TYPE_LIEN_TAXE_SOUS = "819002";
    public final static String TYPE_LIEN_TAXE_SOUS_INV = "820022";
    // Type Module
    public static final String TYPE_MODULE_RELEVE = "905015";
    public static final String TYPE_RELEVE_BOUCLEMENT_ACOMPTE = "829007";
    public static final String TYPE_RELEVE_COMPLEMENT = "829005";

    public static final String TYPE_RELEVE_CONTROL_EMPL = "829003";
    public static final String TYPE_RELEVE_DECOMP_FINAL = "829002";
    public static final String TYPE_RELEVE_DECOMP_FINAL_COMPTA = "829008";
    // Type de relevés
    public static final String TYPE_RELEVE_PERIODIQUE = "829001";
    public static final String TYPE_RELEVE_RECTIF = "829004";

    public static final String TYPE_RELEVE_SALAIRE_DIFFERES = "829009";
    public static final String TYPE_RELEVE_TAXATION_OFFICE = "829006";
    public static final String TYPE_TAUX_CAISSE = "844005";
    public static final String TYPE_TAUX_CANTON = "844006";
    public static final String TYPE_TAUX_CTRL_EMP = "844008";

    public static final String TYPE_TAUX_DECLARATION = "844003";
    // Type de taux
    public static final String TYPE_TAUX_DEFAUT = "844001";
    public static final String TYPE_TAUX_FORCE = "844002";

    public static final String TYPE_TAUX_GROUPE = "844004";

    public static final String TYPE_TAUX_MOYEN = "844007";
    // Type tiers
    public final static String TYPE_TIERS_TIERS = "500006";

    public static final String PROVENANCE_IDE_FICHIER_OFS = "864001";
    public static final String PROVENANCE_IDE_RESERVE1 = "864002";
    public static final String PROVENANCE_IDE_RESERVE2 = "864003";
    public static final String PROVENANCE_IDE_RESERVE3 = "864004";
    public static final String PROVENANCE_IDE_RESERVE4 = "864005";

    // Statut IDE
    public static final String STATUT_IDE_DEFINITIF = "855001";
    public static final String STATUT_IDE_PROVISOIRE = "855002";
    public static final String STATUT_IDE_MUTATION = "855003";
    public static final String STATUT_IDE_REACTIVATION = "855004";
    public static final String STATUT_IDE_DEFINITIF_RADIE = "855005";
    public static final String STATUT_IDE_RADIE = "855006";
    public static final String STATUT_IDE_ANNULE = "855007";

    public static final String CATEGORIE_ANNONCE_IDE_ENVOI = "856001";
    public static final String CATEGORIE_ANNONCE_IDE_RECEPTION = "856002";

    public static final String TYPE_ANNONCE_IDE_DESENREGISTREMENT_ACTIF = "857001";
    public static final String TYPE_ANNONCE_IDE_DESENREGISTREMENT_PASSIF = "857002";
    public static final String TYPE_ANNONCE_IDE_ENREGISTREMENT_ACTIF = "857003";
    public static final String TYPE_ANNONCE_IDE_ENREGISTREMENT_PASSIF = "857004";
    public static final String TYPE_ANNONCE_IDE_REACTIVATION = "857005";
    public static final String TYPE_ANNONCE_IDE_MUTATION_CONFIRMEE = "857006";
    public static final String TYPE_ANNONCE_IDE_MUTATION_REJETEE = "857007";
    public static final String TYPE_ANNONCE_IDE_CREATION_CONFIRMEE = "857008";
    public static final String TYPE_ANNONCE_IDE_ANNULEE = "857009";
    public static final String TYPE_ANNONCE_IDE_CREATION = "857010";
    public static final String TYPE_ANNONCE_IDE_MUTATION = "857011";
    public static final String TYPE_ANNONCE_IDE_CREATION_INFO_ABO = "857012";
    public static final String TYPE_ANNONCE_IDE_MUTATION_INFO_ABO = "857013";
    public static final String TYPE_ANNONCE_IDE_RADIATION = "857014";
    public static final String TYPE_ANNONCE_IDE_FOSC = "857015";
    public static final String TYPE_ANNONCE_IDE_FAILLITE = "857016";

    public static final String ETAT_ANNONCE_IDE_ENREGISTRE = "858001";
    public static final String ETAT_ANNONCE_IDE_SUSPENDU = "858002";
    public static final String ETAT_ANNONCE_IDE_TRAITE = "858003";
    public static final String ETAT_ANNONCE_IDE_ERREUR = "858004";
    public static final String ETAT_ANNONCE_IDE_ATTENTE = "858005";

    // LPP

    public static final String CS_SEUIL_LPP = "10800042";

    public static String getCode(BSession session, String code) throws Exception {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getCodeUtilisateur();
    }

    public static String getCode(HttpSession session, String code) throws Exception {

        BISession bSession = CodeSystem.getSession(session);
        return CodeSystem.getCode((BSession) bSession, code);
    }

    public static final String getCodeMois(int noMois) {
        return String.valueOf(837000 + noMois);
    }

    public static final String getCodeMois(String noMois) {
        return CodeSystem.getCodeMois(Integer.parseInt(noMois));
    }

    /**
     * Retourne le libellé d'un code système.
     * 
     * @param session
     * @param code
     * @return
     * @throws Exception
     */
    public static String getLibelle(BSession session, String code) throws Exception {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCurrentCodeUtilisateur().getLibelle();
    }

    public static String getLibelle(BSession session, String code, String idLangue) throws Exception {
        FWParametersSystemCode cs = new FWParametersSystemCode();
        cs.setSession(session);
        cs.getCode(code);
        return cs.getCodeUtilisateur(idLangue).getLibelle();
    }

    /**
     * Retourne le libellé d'un code système.
     * 
     * @param session
     * @param code
     * @return
     * @throws Exception
     */
    public static String getLibelle(HttpSession session, String code) throws Exception {

        BISession bSession = CodeSystem.getSession(session);
        return CodeSystem.getLibelle((BSession) bSession, code);
    }

    public static String getLibelleIso(BSession session, String code, String isoLangue) throws Exception {
        if ("IT".equals(isoLangue) || "it".equals(isoLangue)) {
            return CodeSystem.getLibelle(session, code, "I");
        } else if ("DE".equals(isoLangue) || "de".equals(isoLangue)) {
            return CodeSystem.getLibelle(session, code, "D");
        } else {
            return CodeSystem.getLibelle(session, code, "F");
        }
    }

    public static List<String> getListTypeAssuranceCAP() {

        List<String> listTypeAssuranceCAP = new ArrayList<String>();

        listTypeAssuranceCAP.add(CodeSystem.TYPE_ASS_CAP_10);
        listTypeAssuranceCAP.add(CodeSystem.TYPE_ASS_CAP_15);
        listTypeAssuranceCAP.add(CodeSystem.TYPE_ASS_CAP_16);
        listTypeAssuranceCAP.add(CodeSystem.TYPE_ASS_CAP_17);
        listTypeAssuranceCAP.add(CodeSystem.TYPE_ASS_CAP_20);
        listTypeAssuranceCAP.add(CodeSystem.TYPE_ASS_CAP_30);
        listTypeAssuranceCAP.add(CodeSystem.TYPE_ASS_CAP_31);
        listTypeAssuranceCAP.add(CodeSystem.TYPE_ASS_CAP_40);
        listTypeAssuranceCAP.add(CodeSystem.TYPE_ASS_CAP_41);
        listTypeAssuranceCAP.add(CodeSystem.TYPE_ASS_CAP_42);
        listTypeAssuranceCAP.add(CodeSystem.TYPE_ASS_CAP_50);
        listTypeAssuranceCAP.add(CodeSystem.TYPE_ASS_CAP_60);

        return listTypeAssuranceCAP;

    }

    /**
     * Extrait la Session de HttpSession.
     * 
     * @param httpSession
     * @return la BISession
     * @throws Exception
     */
    public static BISession getSession(HttpSession httpSession) throws Exception {

        BISession session = null;
        FWController controller = (FWController) httpSession.getAttribute(CodeSystem.SESSION_CONTROLLER_NAME);
        if (controller != null) {
            session = controller.getSession();
        } else {
            throw new Exception("Aucune session connectée");
        }
        return session;
    };

    public static boolean isTypeAssuranceCAPWithEnfant(String theTypeAssurance) {
        return CodeSystem.TYPE_ASS_CAP_10.equalsIgnoreCase(theTypeAssurance)
                || CodeSystem.TYPE_ASS_CAP_20.equalsIgnoreCase(theTypeAssurance);
    };

    /**
     * Constructeur de CodeSystem.
     */
    public CodeSystem() {
        super();
    }

    /*
     * Retourne une liste d'option sans valeur "vide", pour un Type de "Code System" donné.
     * 
     * @param type le type de Code System
     * 
     * @param selectedOption le code System a selectionner
     * 
     * @param session la session HTTP actuelle
     * 
     * @return la liste d'option
     */
    /*
     * public static String getOption(HttpSession session, String code, String type) { return getOption((BSession)
     * ((FWController) session.getAttribute("objController")).getSession(), code, type); }
     */

    /*
     * Retourne une liste d'option sans valeur "vide", pour un Type de code System donné.
     * 
     * @param type le type de Code System
     * 
     * @param selectedOption le code System a selectionner
     * 
     * @param bsession la session actuelle (provenant du viewBean de la page jsp)
     * 
     * @return la liste d'option
     */
    /*
     * public static String getOption (BSession bsession, String code, String type) {
     * 
     * StringBuffer options = new StringBuffer(); try { FWParametersSystemCodeManager scManager =
     * FWTranslation.getSystemCodeListSup(type, bsession, null);
     * 
     * for (int i = 0 ; i < scManager.size(); i ++) { FWParametersSystemCode codeSystem =
     * (FWParametersSystemCode)scManager.getEntity(i);
     * 
     * if (! JAUtil.isStringEmpty(codeSystem.getIdCode())) { options.append("<option "); if
     * (codeSystem.getIdCode().equals(code)) { options.append("selected "); } options.append("value=\"" +
     * codeSystem.getIdCode() + "\">"); options.append(CodeSystem.getLibelle(bsession, codeSystem.getIdCode()));
     * options.append("</option>"); } } } catch (Exception ex) { JadeLogger.error(null, ex); } return
     * options.toString(); }
     */
}
