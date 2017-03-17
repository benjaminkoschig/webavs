package globaz.corvus.api.lots;

public interface IRELot {

    public static final String CS_ETAT_LOT_EN_TRAITEMENT = "52834005";
    public static final String CS_ETAT_LOT_ERREUR = "52834004";
    public static final String CS_ETAT_LOT_OUVERT = "52834002";
    public static final String CS_ETAT_LOT_PARTIEL = "52834003";
    public static final String CS_ETAT_LOT_VALIDE = "52834001";
    // Etat du Lot
    public static final String CS_GROUPE_ETAT_LOT = "REETALOT";

    // RELOTOWN ://Proprietaire du lot
    public static final String CS_GROUPE_LOT_OWNER = "RELOTOWN";

    // Type de Lot
    public static final String CS_GROUPE_TYPE_LOT = "RETYPLOT";
    public static final String CS_LOT_OWNER_PC = "52858003";

    public static final String CS_LOT_OWNER_RENTES = "52858001";
    public static final String CS_LOT_OWNER_RFM = "52858002";
    public static final String CS_TYP_LOT_DEBLOCAGE_RA = "52833004";
    public static final String CS_TYP_LOT_DECISION = "52833001";
    public static final String CS_TYP_LOT_DECISION_RESTITUTION = "52833006";

    public static final String CS_TYP_LOT_DIMINUTION = "52833003";
    public static final String CS_TYP_LOT_MENSUEL = "52833002";
    public static final String CS_TYP_LOT_PMT_AVANCES = "52833005";

}
