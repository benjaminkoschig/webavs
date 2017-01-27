package globaz.prestation.interfaces.tiers;

public class CommunePolitiqueBean {

    private String code;
    private String nom;
    private int codeInt;

    public CommunePolitiqueBean(String code, String nom, Integer codeInt) {
        super();
        this.code = code;
        this.nom = nom;
        this.codeInt = codeInt;
    }

    public String getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

    public boolean isNotFound() {
        return codeInt == 1;
    }

    public boolean isTooMany() {
        return codeInt == 2;
    }
}
