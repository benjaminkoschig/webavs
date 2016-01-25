package ch.globaz.al.business.constantes;

/**
 * Diverses constantes utiles pour la génération de protocoles
 * 
 * @author jts
 * 
 */
public interface ALConstProtocoles {

    /** permet de définir le type de protocole généré par un traitement */
    public enum TypeProtocole {
        CSV,
        INFO,
        PDF
    }

    public static final String IMPRIMER_PROTOCOLE_CSV = "2";
    public static final String IMPRIMER_PROTOCOLE_CSV_PDF = "1";
    public static final String IMPRIMER_PROTOCOLE_PDF = "0";

    /**
     * Info affilié (ex : numéro d'affilié)
     */
    public static final String INFO_AFFILIE = "info_affilie";

    public static final String INFO_DATE_DEBUT_VALIDITE = "info_date_debut_validite";

    public static final String INFO_DATE_IMPRESSION = "info_date_impression";

    /**
     * Date et heure
     */
    public static final String INFO_DATEHEURE = "info_dateheure";
    /**
     * Date d'échéance
     */
    public static final String INFO_ECHEANCE = "info_echeance";
    public static final String INFO_EMAIL = "info_email";
    public static final String INFO_USER_EMAIL = "info_user_email";
    public static final String INFO_GESTION_TEXTE_LIBRE = "info_gestion_texte_libre";
    public static final String INFO_INSERTION_GED = "info_insertion_ged";

    /**
     * Numéro de passage
     */
    public static final String INFO_PASSAGE = "info_passage";
    /**
     * Période traitée
     */
    public static final String INFO_PERIODE = "info_periode";
    /**
     * Nom du processus
     */
    public static final String INFO_PROCESSUS = "info_processus";

    public static final String INFO_TEXTE_LIBRE = "info_texte_libre";

    /**
     * Nom du traitement
     */
    public static final String INFO_TRAITEMENT = "info_traitement";

    /**
     * Nom de l'utilisateur
     */
    public static final String INFO_UTILISATEUR = "info_utilisateur";

    /**
     * nom du tableau des erreurs
     */
    public static final String TABLE_ERRORS = "tableErreurs";

    /**
     * nom du tableau des avertissements
     */
    public static final String TABLE_WARNINGS = "tableAvertissements";

    /**
     * nouveau champs D0113
     */
    public static final String INFO_TRI_IMPRESSION = "info_tri_impression";
    public static final String INFO_GESTION_COPIE = "info_gestion_copie";

    /**
     * critère de decisions par lot
     */
    public static final String CRITERE_AFFILIE = "critere_inNumeroAffilie";
    public static final String CRITERE_ACTIVITE = "critere_inActivites";
    public static final String CRITERE_STATUT = "critere_inStatut";
    public static final String CRITERE_TARIF = "critere_inTarif";
    public static final String CRITERE_DROIT = "critere_inTypeDroit";
    public static final String CRITERE_ETAT = "critere_etat";
    public static final String CRITERE_VALID_FIN_GREAT = "critere_dateFinValiditeGREAT";
    public static final String CRITERE_VALID_FIN_LESS = "critere_dateFinValiditeLESS";
    public static final String CRITERE_VALID_GREAT = "critere_dateValiditeGREAT";
    public static final String CRITERE_VALID_LESS = "critere_dateValiditeLESS";
    public static final String CRITERE_LISTEDOSSIER = "critere_listeDossier";

}
