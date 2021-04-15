package globaz.osiris.db.ebill.enums;

public enum CAFichierInscriptionStatutEBillEnum {

    TRAITE(1, "EBILL_ENUM_TRAITE_SANS_ERREUR"),
    TRAITE_ERREUR(2, "EBILL_ENUM_TRAITE_AVEC_ERREUR"),
    NON_TRAITE(3, "EBILL_ENUM_NON_TRAITE");

    private int index;
    private String description;

    CAFichierInscriptionStatutEBillEnum(int index, String description) {
        this.index = index;
        this.description = description;
    }

    public static CAFichierInscriptionStatutEBillEnum fromIndex(String anIndex) {
        for (CAFichierInscriptionStatutEBillEnum statut : CAFichierInscriptionStatutEBillEnum.values()) {
            if (statut.index == Integer.parseInt(anIndex)) {
                return statut;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public String getDescription() {
        return description;
    }

}
