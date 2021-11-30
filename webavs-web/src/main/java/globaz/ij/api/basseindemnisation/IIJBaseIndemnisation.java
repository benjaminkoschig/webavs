package globaz.ij.api.basseindemnisation;

/**
 * <h1>Descpription.</h1>
 * 
 * <p>
 * Les codes systemes associes � une base d'indemnisation.
 * </p>
 * 
 * <p>
 * Il y a plus d'informations sur le cycle de vie d'une base dans la classe
 * {@link globaz.ij.regles.IJBaseIndemnisationRegles IJBaseIndemnisationRegles}.
 * </p>
 * 
 * @see globaz.ij.regles.IJBaseIndemnisationRegles
 * @author scr
 */
public interface IIJBaseIndemnisation {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /** lors d'un clonage, un fils de la base clon�e sera cr��. */
    public static final int CLONE_FILS = 11110002;
    /** lors d'un clonage, une toute nouvelle base sera cr��e. */
    public static final int CLONE_NORMAL = 11110001;

    // Etat base indemnisation
    public static final String CS_GROUPE_ETAT = "IJETABASIN";
    public static final String CS_OUVERT = "52412001";
    public static final String CS_VALIDE = "52412002";
    public static final String CS_COMMUNIQUE = "52412003";
    public static final String CS_ANNULE = "52412004";

    // Type_base_indemnisation
    public static final String CS_GROUPE_TYPE = "IJTYPBASIN";
    public static final String CS_NORMAL = "52414001";
    public static final String CS_CORRECTION = "52414002";
    public static final String CS_DUPLICATA = "52414003";

    public static final String IJ_CALENDAR_INTERNE = "1";
    public static final String IJ_CALENDAR_EXTERNE = "2";
    public static final String IJ_CALENDAR_INCAPACITE_PARTIELLE = "3";
    public static final String IJ_CALENDAR_NON_ATTESTE = ".";

    public static final String PREFIX_JOUR_CALENDRIER = "CAL_JOUR_";

    // Ancien Motifs d'interruption "IJMOTIFINT"
    public static final String CS_MAL_ACC_SANS_RAP_READAPT = "52411001";
    public static final String CS_MAL_ACC_EN_RAP_READAPT = "52411002";
    public static final String CS_GROSSESSE = "52411003";
    public static final String CS_ACCOUCHEMENT = "52411004";
    public static final String CS_AUTRES_MOTIFS = "52411005";

    // Nouveau Motifs d'interruption
    public static final String CS_GROUPE_MOTIF_INTERRUPTION = "IJMOTIFIN2";
    public static final String CS_MALADIE = "52436001";
    public static final String CS_ACCIDENT = "52436002";
    public static final String CS_MATERNITE = "52436003";
    public static final String CS_PROCHE_AIDANT = "52436004";
    public static final String CS_AUTRES = "52436005";
}
