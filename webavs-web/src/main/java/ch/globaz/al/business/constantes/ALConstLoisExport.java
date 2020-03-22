package ch.globaz.al.business.constantes;

/**
 * Interface des constantes pour les diff�rents lois subissant des r�gles vari�es pour les allocations exportables
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
     * lafam jusqu'� l'�ge de formation
     */
    public static final int LOI_LAFAM_FORMATION = 1;
    /**
     * lafam de l'�ge de formation - 25
     */
    public static final int LOI_LAFAM_FORMATION_25 = 2;
    /**
     * agricole jusqu'� l'�ge de fomation
     */
    public static final int LOI_LFA_FORMATION = 3;

    /**
     * agricole �ge de formation - 25
     */
    public static final int LOI_LFA_FORMATION_25 = 4;
    /**
     * agricole m�nage
     */
    public static final int LOI_LFA_MENAGE = 5;

}
