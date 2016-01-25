package globaz.apg.enums;

public enum APTypeVersement {

    VERSEMENT_ASSURE(1),
    VERSEMENT_EMPLOYEUR(2),
    VERSEMENT_EMPLOYEUR_ET_ASSURE(3);

    private int codeTypeVersement;

    private APTypeVersement(int codeTypeVersement) {
        this.codeTypeVersement = codeTypeVersement;
    }

    public int getCodeTypeVersement() {
        return codeTypeVersement;
    }

    public String getCodeTypeVersementAsString() {
        return String.valueOf(codeTypeVersement);
    }
}
