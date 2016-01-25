/*
 * Crée le 25 octobre 2006
 */
package globaz.lyra.api;

/**
 * Classe pour les paramètres qui sont envoyés, elle permet de classer le tout correctement
 * 
 * @author hpe
 * 
 */
public class LYParametresInfos {

    // ~ Instance fields
    // ------------------------------------------------------------------------------------------------

    private String name = "";
    private String type = "";
    private String value = "";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    public LYParametresInfos() {
        super();
    }

    // ~ Methodes
    // ------------------------------------------------------------------------------------------------------

    /**
     * getter pour l'attribut name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * getter pour l'attribut type
     * 
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * getter pour l'attribut value
     * 
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * setter pour l'attribut name
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setter pour l'attribut type
     * 
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * setter pour l'attribut value
     * 
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

}
