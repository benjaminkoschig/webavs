package ch.globaz.amal.business.constantes;

public enum TypesOfLossEnum {
    ACTE_DE_DEFAUT_BIEN("1", "42003821"),
    ACTE_DE_DEFAUT_BIEN_FAILLITE("2", "42003822"),
    TITRE_EQUIVALENT("3", "42003823");

    private String value;
    private String cs;

    public String getValue() {
        return value;
    }

    public String getCs() {
        return cs;
    }

    private TypesOfLossEnum(String value, String cs) {
        this.value = value;
        this.cs = cs;
    }
}