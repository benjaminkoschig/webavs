package ch.globaz.pegasus.business.constantes;

public enum EPCCodeAmal {

    CODE_A("A"),
    CODE_C("C"),
    CODE_D("D"),
    CODE_F("F"),
    CODE_H("H"),
    CODE_I("I"),
    CODE_J("J"),
    CODE_K("K"),
    CODE_UNDEFINED("U"),
    CODE_STANDARD("X"),
    CODE_ADAPTATION("Z");

    private String property;

    EPCCodeAmal(String prop) {
        property = prop;
    }

    public String getProperty() {
        return property;
    }
}
