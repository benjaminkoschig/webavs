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

    // Etat_Prestation_APG
    public static final String CS_ETAT_PRESTATION_ANNULE = "52006006";
    public static final String CS_ETAT_PRESTATION_CONTROLE = "52006003";
    public static final String CS_ETAT_PRESTATION_DEFINITIF = "52006005";
    public static final String CS_ETAT_PRESTATION_MIS_LOT = "52006004";
    public static final String CS_ETAT_PRESTATION_OUVERT = "52006001";
    public static final String CS_ETAT_PRESTATION_VALIDE = "52006002";

    // Genre_Prestation_APG
    @Deprecated
    public static final String CS_GENRE_ACM_ALPHA = "52015002";
    @Deprecated
    public static final String CS_GENRE_ACM2_ALPHA = "52015005";
    @Deprecated
    public static final String CS_GENRE_LAMAT = "52015003";
    @Deprecated
    public static final String CS_GENRE_STANDARD = "52015001";

    // Groupe_Prestation_APG
    public static final String CS_GROUPE_ETAT_PRESTATION_APG = "APETATPRST";
    public static final String CS_GROUPE_GENRE_PRESTATIONS = "APGENRPRES";
    public static final String CS_GROUPE_TYPE_PRESTATION_APG = "APTYPPREST";

    // Type_Prestation_APG
    public static final String CS_TYPE_ANNULATION = "52005002";
    public static final String CS_TYPE_NORMAL = "52005001";

    public static final String FIELDNAME_DATEDEBUT_PRESTATION = "VHDDEB";

    public String getDateDebut();

    public String getIdDroit();

    public String getIdPrestation();

}
