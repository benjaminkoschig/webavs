package ch.globaz.amal.business.constantes;

public enum PaymentCategoryEnum {
    PAIEMENT_DEBITEUR("1"),
    RP_RETROACTIVE("2"),
    ANNULATION("3");

    private String value;

    public String getValue() {
        return value;
    }

    private PaymentCategoryEnum(String value) {
        this.value = value;
    }

    public static boolean isPaiementDebiteur(String value) {
        return PAIEMENT_DEBITEUR.getValue().equals(value);
    }

    public static boolean isRPRetro(String value) {
        return RP_RETROACTIVE.getValue().equals(value);
    }

    public static boolean isAnnulation(String value) {
        return ANNULATION.getValue().equals(value);
    }
}