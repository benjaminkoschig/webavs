/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.plancalcul;

/**
 * @author SCE
 * 
 *         9 nov. 2010
 */
public class PCLignePlanCalculHandler {

    private String csCode = null;

    private String cssClass = "";

    private String legende = null;

    private String libelle = null;

    private PCValeurPlanCalculHandler valCol1 = null;

    private PCValeurPlanCalculHandler valCol2 = null;

    private PCValeurPlanCalculHandler valCol3 = null;

    /**
     * Constructeur des lignes a afficher dans la page JSP. Constructeur réduit sans sytle css
     * 
     * @param libelle
     * @param valCol1
     * @param valCol2
     * @param valCol3
     */
    public PCLignePlanCalculHandler(String csCode, String legende, PCValeurPlanCalculHandler valCol1,
            PCValeurPlanCalculHandler valCol2, PCValeurPlanCalculHandler valCol3) {
        super();
        this.csCode = csCode;
        this.valCol1 = valCol1;
        this.valCol2 = valCol2;
        this.valCol3 = valCol3;
        this.legende = legende;
    }

    /**
     * Constructeur des lignes a afficher dans la page JSP.Avec cssClass.
     * 
     * @param libelle
     * @param valCol1
     * @param valCol2
     * @param valCol3
     */
    public PCLignePlanCalculHandler(String csCode, String legende, String cssClass, PCValeurPlanCalculHandler valCol1,
            PCValeurPlanCalculHandler valCol2, PCValeurPlanCalculHandler valCol3) {
        super();
        this.csCode = csCode;
        this.valCol1 = valCol1;
        this.valCol2 = valCol2;
        this.valCol3 = valCol3;
        this.legende = legende;
        this.cssClass = cssClass;
    }

    /**
     * @return the csCode
     */
    public String getCsCode() {
        return csCode;
    }

    /**
     * @return the cssClass
     */
    public String getCssClass() {
        return cssClass;
    }

    /**
     * @return the legende
     */
    public String getLegende() {
        return legende;
    }

    /**
     * @return the libelle
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * @return the valCol1
     */
    public PCValeurPlanCalculHandler getValCol1() {
        return valCol1;
    }

    /**
     * @return the valCol2
     */
    public PCValeurPlanCalculHandler getValCol2() {
        return valCol2;
    }

    /**
     * @return the valCol3
     */
    public PCValeurPlanCalculHandler getValCol3() {
        return valCol3;
    }

    /**
     * @param csCode
     *            the csCode to set
     */
    public void setCsCode(String csCode) {
        this.csCode = csCode;
    }

    /**
     * @param cssClass
     *            the cssClass to set
     */
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    /**
     * @param legende
     *            the legende to set
     */
    public void setLegende(String legende) {
        this.legende = legende;
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
    public void setValCol1(PCValeurPlanCalculHandler valCol1) {
        this.valCol1 = valCol1;
    }

    /**
     * @param valCol2
     *            the valCol2 to set
     */
    public void setValCol2(PCValeurPlanCalculHandler valCol2) {
        this.valCol2 = valCol2;
    }

    /**
     * @param valCol3
     *            the valCol3 to set
     */
    public void setValCol3(PCValeurPlanCalculHandler valCol3) {
        this.valCol3 = valCol3;
    }

}
