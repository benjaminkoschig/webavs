/*
 * Créé le 8 sept. 05
 */
package globaz.ij.api.prestations;

/**
 * @author dvh
 */
public interface IIJPrestation {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // clonage
    public static final int CLONE_NORMAL = 0;
    public static final String CS_ANNULE = "52409005";
    public static final String CS_ATTENTE = "52409001";
    public static final String CS_DEFINITIF = "52409004";
    /** Etat_prestation */
    public static final String CS_GROUPE_ETAT_PRESTATION = "IJETATPRES";
    // type prestation
    public static final String CS_GROUPE_TYPE_PRESTATION = "IJTYPEPRES";

    public static final String CS_MIS_EN_LOT = "52409003";
    public static final String CS_NORMAL = "52419001";
    public static final String CS_RESTITUTION = "52419002";

    public static final String CS_VALIDE = "52409002";
    public static final String CS_GROUPE_MODE_CALCUL_FPI = "IJMODECFPI";
    public static final String JOUR_FPI = "30";

}
