package ch.globaz.al.business.constantes;

/**
 * Interface de constantes utilis�e pour la GED
 * 
 * @author PTA
 * 
 */
public interface ALConstGed {

    /**
     * properties dans les diff�rents domaines pour l'affichage de la ged de l'affilie
     */
    public static final String AL_PROPERTY_AFFICHAGE_DOSSIER_GED_AFFIL = "ged.service.name";
    /**
     * properties dans les diff�rents domaines pour l'affichage de la ged de l'allocataire
     */
    public static final String AL_PROPERTY_AFFICHAGE_DOSSIER_GED_ALLOC = "domaine.nomService.ged";
    /** Valeur du param�tre ParentDatabaseCode pour affiliation */
    public final static String PARENT_DB_CODE_AFFIL = "AFFIL";
    /** Nom du param�tre CODE_BASE_DOCUMENTS_PARENTE */
    public final static String PROP_NAME_CODE_BASE_DOCUMENTS_PARENTE = "CODE_BASE_DOCUMENTS_PARENTE";
    /** Nom du param�tre CODE_DOSSIER_CLASSEMENT_PARENT */
    public final static String PROP_NAME_CODE_DOSSIER_CLASSEMENT_PARENT = "CODE_DOSSIER_CLASSEMENT_PARENT";
    /** Nom du param�tre CODE_LIEN_DOSSIER_PARENT */
    public final static String PROP_NAME_CODE_LIEN_DOSSIER_PARENT = "CODE_LIEN_DOSSIER_PARENT";
    /** Nom du param�tre CODE_STATUT_DOSSIER */
    public final static String PROP_NAME_CODE_STATUT_DOSSIER = "CODE_STATUT_DOSSIER";
    /** Nom du param�tre DATE_OUVERTURE_DOSSIER */
    public final static String PROP_NAME_DATE_OUVERTURE_DOSSIER = "DATE_OUVERTURE_DOSSIER";
    /** Nom du param�tre NUMERO_AFFILIE */
    public final static String PROP_NAME_NUMERO_AFFILIE = "NUMERO_AFFILIE";
    /** Nom du param�tre NUMERO_AVS */
    public final static String PROP_NAME_NUMERO_AVS = "NUMERO_AVS";
    /** Nom du param�tre TYPE_DOSSIER */
    public final static String PROP_NAME_TYPE_DOSSIER = "TYPE_DOSSIER";

    /** Nom du param�tre TYPE_SOUS_DOSSIER */
    public final static String PROP_NAME_TYPE_SOUS_DOSSIER = "TYPE_SOUS_DOSSIER";
    /** Valeur du param�tre VAL_TYPE_DOSSIER_ALLOC */
    public final static String PROP_VAL_TYPE_DOSSIER_ALLOC = "VAL_TYPE_DOSSIER_ALLOC";
    /** Nom du service allocataire */
    public final static String SERVICE_NAME_ALLOC = "allocataire";
    /** Statut de dossier "1" */
    public static final String STATUT_DOSSIER_1 = "1";

    /** Statut de dossier "A" */
    public static final String STATUT_DOSSIER_A = "A";
    /** Statut de dossier "W" */
    public static final String STATUT_DOSSIER_W = "W";
    /** Cat�gorie pour une activit� collaborateur agricole ou agriculteur */
    public static final String VAL_CAT_AGR = "AGR";
    /** Cat�gorie pour activit� ind�pendant */
    public static final String VAL_CAT_IND = "IND";
    /** Cat�gorie pour activit� non-actif */
    public static final String VAL_CAT_NA = "NAC";
    /**
     * Cat�gorie pour statut dossier autre que IS et activit� autre que non-actif, collaborateur agricole, agriculteur,
     * travailleur agricole
     */
    public static final String VAL_CAT_NG = "SAL";
    /** Cat�gorie si le statut du dossier � statut IS */
    public static final String VAL_CAT_NGB = "SAB";
    /** Cat�gorie pour une activit� travailleur agricole */
    public static final String VAL_CAT_TA = "TAG";
    /**
     * type de sous dossier pour une activit� collaborateur agricole ou agriculteur
     */
    public final static String VAL_TYPE_SOUS_DOSSIER_AGR = "AGR";
    /**
     * type de sous dossier pour une activit� ind�pendant
     */
    public final static String VAL_TYPE_SOUS_DOSSIER_IND = "IND";
    /**
     * type de sous dossier pour activit� non-actif
     */
    public final static String VAL_TYPE_SOUS_DOSSIER_NA = "NA";

    /**
     * type de sous dossier pour statut dossier autre que IS et activit� autre que non-actif, collaborateur agricole,
     * agriculteur, travailleur agricole
     */
    public final static String VAL_TYPE_SOUS_DOSSIER_NG = "NG";

    /**
     * type de sous dossier si le statut du dossier � statut IS
     */
    public final static String VAL_TYPE_SOUS_DOSSIER_NGB = "NGB";
    /**
     * type de sous dossier pour une activit� travailleur agricole
     */
    public final static String VAL_TYPE_SOUS_DOSSIER_TA = "TA";

}
