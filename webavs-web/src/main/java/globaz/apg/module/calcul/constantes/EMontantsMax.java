package globaz.apg.module.calcul.constantes;

public enum EMontantsMax {
    
    COMCIABJUR("COMCIABJUR"),
    COMCIABBER("COMCIABBER"),
    COMCIABJUA("COMCIABJUA"),
    COMCIABBEA("COMCIABBEA");
    
    private String value;

    private EMontantsMax(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
