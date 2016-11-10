package ch.globaz.orion.business.domaine.pucs;

public enum DeclarationSalaireProvenance {
    SWISS_DEC("4", "PUCS_TYPE_SWISS_DEC"),
    DAN("2", "PUCS_TYPE_DAN"),
    PUCS("1", "PUCS_TYPE_PUCS"),
    PROVENANCE_PUCS_CCJU("3", "PUCS_TYPE_PUCS"),
    UNDEFINDED("", "");

    private String value;
    private String label;

    private DeclarationSalaireProvenance(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isSwissDec() {
        return SWISS_DEC.equals(this);
    }

    public boolean isDan() {
        return DAN.equals(this);
    }

    public boolean isPucs() {
        return PUCS.equals(this);
    }

    public boolean isFromEbusiness() {
        return isDan() || isPucs();
    }

    public boolean isUndefinded() {
        return UNDEFINDED.equals(this);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static DeclarationSalaireProvenance fromValue(String value) {
        return fromValue(value, false);
    }

    public static DeclarationSalaireProvenance fromValueWithOutException(String value) {
        return fromValue(value, true);
    }

    private static DeclarationSalaireProvenance fromValue(String value, boolean withOutException) {
        if (SWISS_DEC.getValue().equals(value)) {
            return SWISS_DEC;
        } else if (DAN.getValue().equals(value)) {
            return DAN;
        } else if (PUCS.getValue().equals(value)) {
            return PUCS;
        } else if (!withOutException) {
            throw new IllegalArgumentException("This value: " + value + " is not definded for this enum: "
                    + DeclarationSalaireProvenance.class.getName());
        } else {
            return UNDEFINDED;
        }
    }
}
