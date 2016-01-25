/**
 * 
 */
package globaz.perseus.utils.plancalcul;

import globaz.jade.context.JadeThread;

/**
 * @author DDE
 * 
 */
public class PFLignePlanCalculHandler {

    private String cssClass = "";
    private String libelle = "&nbsp;";
    private PFValeurPlanCalculHandler valCol1 = null;
    private PFValeurPlanCalculHandler valCol2 = null;
    private PFValeurPlanCalculHandler valCol3 = null;

    public PFLignePlanCalculHandler() {
        valCol1 = new PFValeurPlanCalculHandler();
        valCol2 = new PFValeurPlanCalculHandler();
        valCol3 = new PFValeurPlanCalculHandler();
    }

    /**
     * @return the cssClass
     */
    public String getCssClass() {
        return cssClass;
    }

    /**
     * Retourne le libellé après l'avoir passé dans la transformations des labels
     * 
     * @return the libelle
     */
    public String getLibelle() {

        return JadeThread.getMessage(libelle);
    }

    /**
     * @return the valCol1
     */
    public PFValeurPlanCalculHandler getValCol1() {
        return valCol1;
    }

    /**
     * @return the valCol2
     */
    public PFValeurPlanCalculHandler getValCol2() {
        return valCol2;
    }

    /**
     * @return the valCol3
     */
    public PFValeurPlanCalculHandler getValCol3() {
        return valCol3;
    }

    /**
     * @param cssClass
     *            the cssClass to set
     */
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    /**
     * @param libelle
     *            the libelle to set
     */
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    /**
     * @param valCol1
     *            the valCol1 to set
     */
    public void setValCol1(PFValeurPlanCalculHandler valCol1) {
        this.valCol1 = valCol1;
    }

    /**
     * @param valCol1
     *            the valCol1 to set
     */
    public void setValCol1(String valCol1) {
        this.valCol1 = new PFValeurPlanCalculHandler(valCol1);
    }

    /**
     * Set d'une valeur formattée
     * 
     * @param valCol1
     * @param Souligné
     *            la valeur
     * @param Mettre
     *            en gras la valeur
     */
    public void setValCol1(String valCol1, Boolean souligne, Boolean gras) {
        this.valCol1 = new PFValeurPlanCalculHandler(valCol1, souligne, gras);
    }

    /**
     * @param valCol2
     *            the valCol2 to set
     */
    public void setValCol2(PFValeurPlanCalculHandler valCol2) {
        this.valCol2 = valCol2;
    }

    /**
     * @param valCol2
     *            the valCol2 to set
     */
    public void setValCol2(String valCol2) {
        this.valCol2 = new PFValeurPlanCalculHandler(valCol2);
    }

    /**
     * Set d'une valeur formattée
     * 
     * @param valCol1
     * @param Souligné
     *            la valeur
     * @param Mettre
     *            en gras la valeur
     */
    public void setValCol2(String valCol2, Boolean souligne, Boolean gras) {
        this.valCol2 = new PFValeurPlanCalculHandler(valCol2, souligne, gras);
    }

    /**
     * @param valCol3
     *            the valCol3 to set
     */
    public void setValCol3(PFValeurPlanCalculHandler valCol3) {
        this.valCol3 = valCol3;
    }

    /**
     * @param valCol3
     *            the valCol3 to set
     */
    public void setValCol3(String valCol3) {
        this.valCol3 = new PFValeurPlanCalculHandler(valCol3);
    }

    /**
     * Set d'une valeur formattée
     * 
     * @param valCol1
     * @param Souligné
     *            la valeur
     * @param Mettre
     *            en gras la valeur
     */
    public void setValCol3(String valCol3, Boolean souligne, Boolean gras) {
        this.valCol3 = new PFValeurPlanCalculHandler(valCol3, souligne, gras);
    }

}
