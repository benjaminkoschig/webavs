package globaz.corvus.api.basescalcul;

public interface IREPrestationDue {

    public final static String CS_ETAT_ACTIF = "52827002";
    public final static String CS_ETAT_ATTENTE = "52827001";
    public final static String CS_ETAT_TRAITE = "52827003";
    // REETAPD ://Etat de la prestation due
    public final static String CS_GROUPE_ETAT = "REETAPD";

    // RETYPPD ://Type de la prestation due
    public final static String CS_GROUPE_TYPE = "RETYPPD";
    // Type de paiement de la prestation due
    // RETYPMPD ://Type de paiement
    public final static String CS_GROUPE_TYPE_DE_PMT = "RETYPMPD";
    public final static String CS_TYPE_DE_PMT_PMT_MENS = "52829001";

    public final static String CS_TYPE_DE_PMT_RENCHERISSEMENT = "52829002";
    public final static String CS_TYPE_MNT_TOT = "52828002";
    public final static String CS_TYPE_PMT_MENS = "52828001";

}
