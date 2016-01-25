package globaz.corvus.api.avances;

public interface IREAvances {

    public final static String CS_DOMAINE_AVANCE_PC = "52859003";
    public final static String CS_DOMAINE_AVANCE_RENTE = "52859001";

    public final static String CS_DOMAINE_AVANCE_RFM = "52859002";

    public final static String CS_ETAT_1ER_ACOMPTE_ANNULE = "52853003";

    public final static String CS_ETAT_1ER_ACOMPTE_ATTENTE = "52853001";
    public final static String CS_ETAT_1ER_ACOMPTE_TERMINE = "52853002";

    public final static String CS_ETAT_ACOMPTE_ATTENTE = "52852001";

    public final static String CS_ETAT_ACOMPTE_EN_COURS = "52852002";

    public final static String CS_ETAT_ACOMPTE_TERMINE = "52852003";
    // REDOMAVANC - inforom 547
    public static final String CS_GROUPE_DOMAINE_AVANCE = "REDOMAVANC";
    // REETA1ACP ://Etat du 1er acompte pour avances
    public static final String CS_GROUPE_ETAT_1ER_ACOMPTE = "REETA1ACP";

    // REETAACPT ://Etat des acomptes pour avances
    public static final String CS_GROUPE_ETAT_ACOMPTES = "REETAACPT";

    // RETYPAVANC ://Type d avances
    public static final String CS_GROUPE_TYPE_AVANCE = "RETYPAVANC";
    public final static String CS_TYPE_ACOMPTES_MENSUEL = "52854001";
    public final static String CS_TYPE_ACOMPTES_UNIQUE = "52854002";
    public final static String CS_TYPE_LISTES = "52854003";

}
