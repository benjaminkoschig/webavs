package globaz.apg.api.droits;

/**
 * Descpription
 *
 * @author scr Date de création 23 mai 05
 */
public interface IAPDroitLAPG {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    String[] DROITS_MODIFIABLES = {IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE,IAPDroitLAPG.CS_ETAT_DROIT_ERREUR,IAPDroitLAPG.CS_ETAT_DROIT_VALIDE, IAPDroitLAPG.CS_ETAT_DROIT_ATTENTE_REPONSE};
    // Code système des états des droits APG

    /** DOCUMENT ME! */
    String CS_ALLOCATION_DE_MATERNITE = "52001012";

    /** DOCUMENT ME! */
    String CS_ARMEE_SERVICE_NORMAL = "52001001";

    String CS_CINQ_MOIS_ACTIVITE = "52022003";

    /** DOCUMENT ME! */
    String CS_COURS_MONITEURS_JEUNES_TIREURS = "52001011";

    /** le droit est en attente */
    String CS_ETAT_DROIT_ATTENTE = "52003001";

    /** le droit est définitif */
    String CS_ETAT_DROIT_DEFINITIF = "52003002";

    // Codes système des genre de service

    /** le droit est partiel */
   String CS_ETAT_DROIT_PARTIEL = "52003003";
   String CS_ETAT_DROIT_TRANSFERE = "52003004";
   String CS_ETAT_DROIT_REFUSE = "52003005";
   String CS_ETAT_DROIT_ERREUR = "52003006";
   String CS_ETAT_DROIT_VALIDE = "52003007";
   String CS_ETAT_DROIT_ATTENTE_REPONSE = "52003008";

    /** DOCUMENT ME! */
    String CS_FORMATION_DE_BASE = "52001007";

    /** DOCUMENT ME! */
    String CS_FORMATION_DE_CADRE_JEUNESSE_SPORTS = "52001008";

    /** Nom du groupe des codes système des états des droit APG */
   String CS_GROUPE_ETAT_DROIT_APG = "APETADROIT";

    /** Nom du groupe des codes système des genres de service APG */
    String CS_GROUPE_GENRE_SERVICE_APG = "APGENSERVI";

    // Motif_refus_dossier
    /** Nom du groupe des codes systèmes des motifs de refus d'un dossier */
    String CS_GROUPE_MOTIFS_REFUS = "APMOREDO";

    // Motif_transfer_dossier
    /** Nom du groupe des codes système des motifs de transfer de dossiers */
    String CS_GROUPE_MOTIFS_TRANSFER = "APMOTRDO";

    // Informations_complementaires
    /** Nom du groupe des codes système des types d'informations complémentaires */
    String CS_GROUPE_TYPE_INFO_COMPL = "APINFCMP";

    String CS_NEUF_MOIS_ASSUJETTISSEMENT = "52022002";

    String CS_PERCEPTION_COTISATIONS_AVS = "52017003";

    String CS_PROTECTION_CIVILE_CADRE_SPECIALISTE = "52001013";

    String CS_PROTECTION_CIVILE_COMMANDANT = "52001014";

    /** DOCUMENT ME! */
    String CS_PROTECTION_CIVILE_SERVICE_NORMAL = "52001006";

    /** DOCUMENT ME! */
    String CS_RECRUTEMENT = "52001004";

    String CS_REFUS_DOSSIER = "52016002";

    /** le droit est refusé */
   String CS_REFUSE = "52003005";

   String CS_SANS_ACTIVITE_ACCOUCHEMENT = "52022004";

   String CS_SANS_ACTIVITE_LUCRATIVE = "52022001";

    /** DOCUMENT ME! */
    String CS_SERVICE_AVANCEMENT = "52001003";

    /** DOCUMENT ME! */
    String CS_SERVICE_CIVIL_AVEC_TAUX_RECRUES = "52001010";

    /** DOCUMENT ME! */
    String CS_SERVICE_INTERRUPTION_AVANT_ECOLE_SOUS_OFF = "52001015";

    /** DOCUMENT ME! */
    String CS_SERVICE_INTERRUPTION_PENDANT_SERVICE_AVANCEMENT = "52001016";

    /** DOCUMENT ME! */
   String CS_SERVICE_CIVIL_SERVICE_NORMAL = "52001009";

    /** DOCUMENT ME! */
    String CS_SERVICE_EN_QUALITE_DE_RECRUE = "52001002";

    /** DOCUMENT ME! */
    String CS_SOF_EN_SERVICE_LONG = "52001005";

    /** le droit est transféré */
    String CS_TANSFERE = "52003004";

    String CS_TRANSFER_DOSSIER = "52016001";

    String CS_VERSEMENT_RENTE_AI = "52017002";

    String CS_VERSEMENT_RENTE_AVS = "52017001";
    /**
     * Constante pour FERCIAB
     */
    String CS_DEMENAGEMENT_CIAB = "52001050";
    String CS_NAISSANCE_CIAB = "52001051";
    String CS_MARIAGE_LPART_CIAB = "52001052";
    String CS_DECES_CIAB = "52001053";
    String CS_JOURNEES_DIVERSES_CIAB = "52001054";
    String CS_CONGE_JEUNESSE_CIAB = "52001055";
    String CS_SERVICE_ETRANGER_CIAB = "52001056";
    String CS_DECES_DEMI_JOUR_CIAB = "52001057";

    /** Genres service en ce qui concerne la pandémie */
   String CS_GARDE_PARENTALE = "52001040";
   String CS_QUARANTAINE = "52001041";
   String CS_INDEPENDANT_PANDEMIE = "52001042";
   String CS_INDEPENDANT_PERTE_GAINS = "52001043";
   String CS_GARDE_PARENTALE_HANDICAP = "52001044";
   String CS_INDEPENDANT_MANIF_ANNULEE = "52001045";

}
