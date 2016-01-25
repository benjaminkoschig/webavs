package ch.globaz.al.business.constantes;

/**
 * Constantes utilisée pour le remplissage de la table de correspondance d'ids de l'importation
 * 
 * @author jts
 * 
 */
public interface ALConstCorrespondanceIdentifiants {
    /** Nom de la table allocataire dans Alfa-gest */
    public static final String TABLE_ALLOCATAIRE_ALFAGEST = "JAFPALL";
    /** Nom de la table allocataire dans WebAF */
    public static final String TABLE_ALLOCATAIRE_WEBAF = "ALALLOC";

    /** Nom de la table droit dans Alfa-gest */
    public static final String TABLE_DROIT_ALFAGEST = "JAFPDRO";
    /** Nom de la table droit dans WebAF */
    public static final String TABLE_DROIT_WEBAF = "ALDROIT";

    /** Nom de la table enfant dans Alfa-gest */
    public static final String TABLE_ENFANT_ALFAGEST = "JAFPENAF";
    /** Nom de la table enfant dans WebAF */
    public static final String TABLE_ENFANT_WEBAF = "ALENFANT";

    /** Nom de la table tiers dans Alfa-gest */
    public static final String TABLE_TIERS_ALFAGEST = "JAFPPHYS";
    /** Nom de la table tiers dans WebAF */
    public static final String TABLE_TIERS_WEBAF = "TITIERP";
}
