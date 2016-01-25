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
     * lafam jusqu'à 16
     */
    public static final int LOI_LAFAM_16 = 1;
    /**
     * lafam de 16-25
     */
    public static final int LOI_LAFAM_16_25 = 2;
    /**
     * agricole jusqu'à 16
     */
    public static final int LOI_LFA_16 = 3;

    /**
     * agricole 16 - 25
     */
    public static final int LOI_LFA_16_25 = 4;
    /**
     * agricole ménage
     */
    public static final int LOI_LFA_MENAGE = 5;

}
