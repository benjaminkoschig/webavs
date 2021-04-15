package globaz.osiris.db.ebill.enums;

public enum CATraitementEtatEBillEnum {

    TRAITE("1", "EBILL_ENUM_TRAITE"),
    EN_ERREUR("2", "EBILL_ENUM_ERREUR"),
    REJECTED_OR_PENDING("3", "EBILL_ENUM_REJETEE");

    public static final String NUMERO_ETAT_TRAITE = "1";
    public static final String NUMERO_ETAT_EN_ERREUR = "2";
    public static final String NUMERO_ETAT_REJECTED_OR_PENDING = "3";
    private final String numeroEtat;
    private final String description;

    CATraitementEtatEBillEnum(String numeroEtat, String description) {
        this.numeroEtat = numeroEtat;
        this.description = description;
    }

    public static CATraitementEtatEBillEnum parValeur(final String numeroStatut) {
        switch (numeroStatut) {
            case NUMERO_ETAT_TRAITE:
                return TRAITE;
            case NUMERO_ETAT_EN_ERREUR:
                return EN_ERREUR;
            case NUMERO_ETAT_REJECTED_OR_PENDING:
                return REJECTED_OR_PENDING;
            default:
                return null;
//                throw new Exception("etatEBill.inexistant");
        }
    }

    public String getNumeroEtat() {
        return numeroEtat;
    }

    public String getDescription() {
        return description;
    }
}
