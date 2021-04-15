package globaz.osiris.db.ebill.enums;

public enum CAFichierTraitementStatutEBillEnum {

    TRAITE(1, "EBILL_ENUM_TRAITE"),
    A_TRAITE(2, "EBILL_ENUM_A_TRAITER");

    private int index;
    private String description;

    CAFichierTraitementStatutEBillEnum(int index, String description) {
        this.index = index;
        this.description = description;
    }

    public static CAFichierTraitementStatutEBillEnum fromIndex(String anIndex) {
        for (CAFichierTraitementStatutEBillEnum statut : CAFichierTraitementStatutEBillEnum.values()) {
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
