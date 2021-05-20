package globaz.osiris.db.ebill.enums;

public enum CAStatutEBillEnum {

    TRAITE_AUTOMATIQUEMENT("1", "EBILL_ENUM_TRAITE_AUTOMATIQUEMENT"),
    TRAITE_MANUELLEMENT("2", "EBILL_ENUM_TRAITE_MANUELLEMENT"),
    A_TRAITER("3", "EBILL_ENUM_A_TRAITER");

    public static final String NUMERO_STATUT_TRAITE_AUTOMATIQUEMENT = "1";
    public static final String NUMERO_STATUT_TRAITE_MANUELLEMENT = "2";
    public static final String NUMERO_STATUT_A_TRAITER = "3";
    private final String numeroStatut;
    private final String description;

    CAStatutEBillEnum(String numeroStatut, String description) {
        this.numeroStatut = numeroStatut;
        this.description = description;
    }

    public static CAStatutEBillEnum parValeur(final String numeroStatut) {
        switch (numeroStatut) {
            case NUMERO_STATUT_TRAITE_AUTOMATIQUEMENT:
                return TRAITE_AUTOMATIQUEMENT;
            case NUMERO_STATUT_TRAITE_MANUELLEMENT:
                return TRAITE_MANUELLEMENT;
            case NUMERO_STATUT_A_TRAITER:
                return A_TRAITER;
            default:
                return null;
        }
    }

    public String getNumeroStatut() {
        return numeroStatut;
    }

    public String getDescription() {
        return description;
    }
}
