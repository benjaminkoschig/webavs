package globaz.apg.enums;

public enum APCategorieEntreprise {

    MAGASIN_MARCHE("52026001"),
    RESTAURANT("52026002"),
    BAR("52026003"),
    DIVERTISSEMENT("52026004"),
    SERVICE("52026005"),
    AUTRES("52026006");

    private String codeSystem;

    private APCategorieEntreprise(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

}
