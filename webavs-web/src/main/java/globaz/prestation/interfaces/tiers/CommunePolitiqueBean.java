package globaz.prestation.interfaces.tiers;

public class CommunePolitiqueBean {

    private String code;
    private String nom;

    public CommunePolitiqueBean(String code, String nom) {
        super();
        this.code = code;
        this.nom = nom;
    }

    public String getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }
}
