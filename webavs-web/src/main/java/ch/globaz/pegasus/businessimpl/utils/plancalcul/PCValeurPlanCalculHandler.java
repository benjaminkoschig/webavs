/**
 * 
 */
package ch.globaz.pegasus.businessimpl.utils.plancalcul;

import java.util.ArrayList;

/**
 * @author SCE
 * 
 *         8 nov. 2010
 */
public class PCValeurPlanCalculHandler {

    /* Code systeme de la valeur enfant */
    private String codeSysteme = null;// Cs de la valeur
    /* Colone dans laquelle afficher la valeur, par défaut colone 2 */
    private Integer colToAffiche = 2;
    /* classe css */
    private String cssClass = "";
    /* liste des membre de la categorie, si categorie */
    private ArrayList<PCValeurPlanCalculHandler> membresCategorie = null;
    /* valeur enfant avec signe */
    private String strValeur = null;

    /**
     * Constructeur
     * 
     * @param codeSysteme
     *            de l'enfant
     * @param valeur
     *            de l'enfant
     * @param signe
     *            de la valeur
     * @param cssClass
     *            de l'enfant
     */
    public PCValeurPlanCalculHandler(String codeSysteme, String valeur, String signe, String cssClass) {
        this.codeSysteme = codeSysteme;
        strValeur = signe + valeur;
        this.cssClass = cssClass;
    }

    /**
     * @return the codeSysteme
     */
    public String getCodeSysteme() {
        return codeSysteme;
    }

    /**
     * @return the colToAffiche
     */
    public Integer getColToAffiche() {
        return colToAffiche;
    }

    /**
     * @return the cssClass
     */
    public String getCssClass() {
        return cssClass;
    }

    /**
     * @return the membresCategorie
     */
    public ArrayList<PCValeurPlanCalculHandler> getMembresCategorie() {
        return membresCategorie;
    }

    /**
     * @return the strValeur
     */
    public String getStrValeur() {
        return strValeur;
    }

    /**
     * @param codeSysteme
     *            the codeSysteme to set
     */
    public void setCodeSysteme(String codeSysteme) {
        this.codeSysteme = codeSysteme;
    }

    /**
     * @param colToAffiche
     *            the colToAffiche to set
     */
    public void setColToAffiche(Integer colToAffiche) {
        this.colToAffiche = colToAffiche;
    }

    /**
     * @param cssClass
     *            the cssClass to set
     */
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }

    /**
     * @param membresCategorie
     *            the membresCategorie to set
     */
    public void setMembresCategorie(ArrayList<PCValeurPlanCalculHandler> membresCategorie) {
        this.membresCategorie = membresCategorie;
    }

    /**
     * @param strValeur
     *            the strValeur to set
     */
    public void setStrValeur(String strValeur) {
        this.strValeur = strValeur;
    }

}
