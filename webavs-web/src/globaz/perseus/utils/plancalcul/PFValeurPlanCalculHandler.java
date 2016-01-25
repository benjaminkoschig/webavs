/**
 * 
 */
package globaz.perseus.utils.plancalcul;

/**
 * @author DDE
 * 
 */
public class PFValeurPlanCalculHandler {

    private String cssClass;
    private String valeur;

    /**
     * Constructeur avec donn�e vide
     */
    public PFValeurPlanCalculHandler() {
        valeur = "";
        cssClass = "";
    }

    /**
     * Constructeur permetant d'ajouter une valeur non format�e
     * 
     * @param valeur
     */
    public PFValeurPlanCalculHandler(String valeur) {
        this();
        this.valeur = valeur;
    }

    /**
     * Constructeur permetant de formatter la donn�e
     * 
     * @param valeur
     * @param soulign�
     *            la valeur
     * @param mettre
     *            en gras la valeur
     */
    public PFValeurPlanCalculHandler(String valeur, Boolean souligne, Boolean gras) {
        this(valeur);
        if (souligne) {
            cssClass += " souligne";
        }
        if (gras) {
            cssClass += " total";
        }
    }

    /**
     * @return the cssClass
     */
    public String getCssClass() {
        return cssClass;
    }

    /**
     * @return the valeur
     */
    public String getValeur() {
        return valeur;
    }

    /**
     * @param cssClass
     *            the cssClass to set
     */
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    /**
     * @param valeur
     *            the valeur to set
     */
    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

}
