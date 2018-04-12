package ch.globaz.orion.ws.enums;

public enum SexeSalarie {
    HOMME(316000),
    FEMME(316001),
    UNDEFINED(0);

    private Integer codeSysteme;

    private SexeSalarie(Integer codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    public static SexeSalarie valueOf(Integer codeSysteme) {
        if (codeSysteme != null) {
            for (SexeSalarie sexe : SexeSalarie.values()) {
                if (sexe.getCodeSysteme().equals(codeSysteme)) {
                    return sexe;
                }
            }
        }
        throw new IllegalArgumentException("the value " + codeSysteme + " is not valid for "
                + SexeSalarie.class.getName());
    }

    public Integer getCodeSysteme() {
        return codeSysteme;
    }
}
