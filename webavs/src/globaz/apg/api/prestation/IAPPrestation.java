/*
 * Créé le 11 mai 05
 * 
 * Description :
 */
package globaz.apg.api.prestation;

/**
 * DOCUMENT ME!
 * 
 * @author scr Descpription
 */
public interface IAPPrestation {

    // Type_Prestation_APG

    public final static String CS_ETAT_PRESTATION_ANNULE = "52006006";

    public final static String CS_ETAT_PRESTATION_CONTROLE = "52006003";

    public final static String CS_ETAT_PRESTATION_DEFINITIF = "52006005";

    public final static String CS_ETAT_PRESTATION_MIS_LOT = "52006004";
    public final static String CS_ETAT_PRESTATION_OUVERT = "52006001";

    public final static String CS_ETAT_PRESTATION_VALIDE = "52006002";

    // Prestation de type ACM, pour les caisses horlogères, uniquement
    @Deprecated
    public final static String CS_GENRE_ACM_ALPHA = "52015002";

    // Prestation de type LAMAT, pour la loi sur l'ass. maternite genevoise
    @Deprecated
    public final static String CS_GENRE_LAMAT = "52015003";

    // Prestation standard
    @Deprecated
    public final static String CS_GENRE_STANDARD = "52015001";

    // Etat_Prestation_APG
    /** Nom du groupe des états de la prestation APG */
    public static final String CS_GROUPE_ETAT_PRESTATION_APG = "APETATPRST";

    // Genre_Prestation
    public static final String CS_GROUPE_GENRE_PRESTATIONS = "APGENRPRES";

    /** Nom du groupe du type de prestation APG */
    public static final String CS_GROUPE_TYPE_PRESTATION_APG = "APTYPPREST";

    public final static String CS_TYPE_ANNULATION = "52005002";

    public final static String CS_TYPE_NORMAL = "52005001";

    // Champ de la table des prestations
    public final static String FIELDNAME_DATEDEBUT_PRESTATION = "VHDDEB";

    public String getDateDebut();

    public String getIdDroit();

    public String getIdPrestation();

}
