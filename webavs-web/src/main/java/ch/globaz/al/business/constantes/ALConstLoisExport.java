package ch.globaz.al.business.constantes;

/**
 * Interface des constantes pour les différents lois subissant des règles variées pour les allocations exportables
 * 
 * @author GMO
 */
public interface ALConstLoisExport {

    /** Constante AELE */
    public static final String AELE = "AELE";

    /**
     * lafam
     */
    public static final int LAFAM = 10;
    /**
     * agricole
     */
    public static final int LFA = 11;
    /**
     * lafam jusqu'à l'âge 16 ans
     */
    public static final int LOI_LAFAM_FIN_ENFANT = 1;
    /**
     * lafam de l'âge de formation - 25
     */
    public static final int LOI_LAFAM_FORMATION_25 = 2;
    /**
     * agricole jusqu'à 16 ans
     */
    public static final int LOI_LFA_FIN_ENFANT = 3;

    /**
     * agricole âge de formation - 25
     */
    public static final int LOI_LFA_FORMATION_25 = 4;
    /**
     * agricole ménage
     */
    public static final int LOI_LFA_MENAGE = 5;

}
