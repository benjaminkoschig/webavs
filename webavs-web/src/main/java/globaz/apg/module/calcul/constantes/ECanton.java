package globaz.apg.module.calcul.constantes;

public enum ECanton {
    
    BE("BE"),
    JU("JU");
    
    private String value;

    private ECanton(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
