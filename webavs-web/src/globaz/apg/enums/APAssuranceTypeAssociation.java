package globaz.apg.enums;

public enum APAssuranceTypeAssociation {

    FNE(52024003, "FNE", "Fédération Neuchâteloise des Entrepreneurs"),
    MECP(52024001, "MECP", "Menuiserie, Ebénistes, Charpentiers et Parqueteurs"),
    NONE(0, "None", "None"),

    PP(52024002, "PP", "Plâtriers et Peintres");

    private int codesystem;
    private String description = "";
    private String nomTypeAssociation = "";

    private APAssuranceTypeAssociation(int codesystem, String nomTypeAssociation, String description) {
        this.codesystem = codesystem;
        this.nomTypeAssociation = nomTypeAssociation;
        this.description = description;
    }

    public boolean equals(String codeSystem) {

        return String.valueOf(getCodesystem()).equalsIgnoreCase(codeSystem);

    }

    public int getCodesystem() {
        return codesystem;
    }

    public String getCodesystemToString() {
        return String.valueOf(codesystem);
    }

    public String getDescription() {
        return description;
    }

    public String getNomTypeAssociation() {
        return nomTypeAssociation;
    }

    public boolean isCodeSystemEqual(String codeSystem) {
        return String.valueOf(getCodesystem()).equalsIgnoreCase(codeSystem);
    }

    public void setCodesystem(int codesystem) {
        this.codesystem = codesystem;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNomTypeAssociation(String nomTypeAssociation) {
        this.nomTypeAssociation = nomTypeAssociation;
    }

}
