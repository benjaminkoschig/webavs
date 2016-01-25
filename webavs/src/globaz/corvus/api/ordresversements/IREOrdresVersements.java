package globaz.corvus.api.ordresversements;

public interface IREOrdresVersements {

    // Type des ordres de versement
    public final static String CS_GROUPE_TYPE_ORDRE_VERSEMENT = "RETYPOVE";

    public final static String CS_TYPE_ALLOCATION_NOEL = "52842012";
    public final static String CS_TYPE_ASSURANCE_SOCIALE = "52842002";
    // Le bénéficiaire principal de la demande
    public final static String CS_TYPE_BENEFICIAIRE_PRINCIPAL = "52842004";

    // Restitution d'une rente accordée présente dans la période de la nouvelle
    // décision
    public final static String CS_TYPE_DETTE = "52842011";

    public final static String CS_TYPE_DETTE_RENTE_AVANCES = "52842008";

    // Compensation, cad dette dans la CA
    public final static String CS_TYPE_DETTE_RENTE_DECISION = "52842005";
    public final static String CS_TYPE_DETTE_RENTE_PRST_BLOQUE = "52842009";
    public final static String CS_TYPE_DETTE_RENTE_RESTITUTION = "52842007";
    public final static String CS_TYPE_DETTE_RENTE_RETOUR = "52842006";
    // IS
    public final static String CS_TYPE_IMPOT_SOURCE = "52842003";
    public final static String CS_TYPE_INTERET_MORATOIRE = "52842010";

    public final static String CS_TYPE_JOURS_APPOINT = "52842013";

    // Créanciers
    public final static String CS_TYPE_TIERS = "52842001";
}
