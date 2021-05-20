package globaz.osiris.db.ebill.enums;

public enum CAInscriptionTypeEBillEnum {

    INSCRIPTION("1", "EBILL_ENUM_INSCRIPTION"),
    INSCRIPTION_DIRECTE("2", "EBILL_ENUM_INSCRIPTION_DIRECTE"),
    RESILIATION("3", "EBILL_ENUM_RESILIATION");

    public static final String NUMERO_TYPE_INSCRIPTION = "1";
    public static final String NUMERO_TYPE_INSCRIPTION_DIRECTE = "2";
    public static final String NUMERO_TYPE_RESILIATION = "3";
    private final String numeroType;
    private final String description;

    CAInscriptionTypeEBillEnum(String numeroType, String description) {
        this.numeroType = numeroType;
        this.description = description;
    }

    public static CAInscriptionTypeEBillEnum parValeur(final String numeroType) {
        switch (numeroType) {
            case NUMERO_TYPE_INSCRIPTION:
                return INSCRIPTION;
            case NUMERO_TYPE_INSCRIPTION_DIRECTE:
                return INSCRIPTION_DIRECTE;
            case NUMERO_TYPE_RESILIATION:
                return RESILIATION;
            default:
                return null;
        }
    }

    public boolean estInscription() {
        return this == INSCRIPTION;
    }

    public boolean estInscriptionDirecte() {
        return this == INSCRIPTION_DIRECTE;
    }

    public boolean estResiliation() {
        return this == RESILIATION;
    }

    public String getNumeroType() {
        return numeroType;
    }

    public String getDescription() {
        return description;
    }
}
