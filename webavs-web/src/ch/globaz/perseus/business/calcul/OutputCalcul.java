/**
 * 
 */
package ch.globaz.perseus.business.calcul;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * @author DDE
 * 
 */
public class OutputCalcul implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Hashtable<OutputData, Float> calcul = null;
    private Hashtable<String, String> calculXml = null;

    public OutputCalcul() {
        calcul = new Hashtable<OutputData, Float>();
        calculXml = new Hashtable<String, String>();
    }

    /**
     * Ajoute une donnée calculée (Float)
     * 
     * @param type
     * @param valeur
     */
    public void addDonnee(OutputData type, Float valeur) {
        calcul.put(type, valeur);
        calculXml.put(type.toString(), valeur.toString());
    }

    /**
     * Ajoute une donnée d'information (String)
     * 
     * @param type
     * @param valeur
     */
    public void addDonnee(OutputData type, String valeur) {
        calculXml.put(type.toString(), valeur);
    }

    /**
     * @return the calcul
     */
    public Hashtable<OutputData, Float> getCalcul() {
        return calcul;
    }

    /**
     * Retourne les données sous un format XML permettant de le stocker dans un blob sans problème de comptabilité entre
     * les versions
     * 
     * @return the calculXml
     */
    public Hashtable<String, String> getCalculXml() {
        return calculXml;
    }

    /**
     * Retourne une donnée calculée
     * 
     * @param type
     * @return La donnée en Float ou 0
     */
    public Float getDonnee(OutputData type) {
        if (calcul.containsKey(type)) {
            return calcul.get(type);
        } else {
            return new Float(0);
        }
    }

    /**
     * Retourne une donnée calculée convertie en string ou une donnée information
     * 
     * @param type
     * @return La donnée en string ou 0
     */
    public String getDonneeString(OutputData type) {
        if (calculXml.containsKey(type.toString())) {
            return calculXml.get(type.toString());
        } else {
            return "0";
        }
    }

    /**
     * Permet de reconstruire la structure sur la base des données XML
     * 
     * @param calculXml
     *            the calculXml to set
     */
    public void setCalculXml(Hashtable<String, String> calculXml) {
        this.calculXml = calculXml;

        calcul = new Hashtable<OutputData, Float>();
        for (Enumeration<String> e = calculXml.keys(); e.hasMoreElements();) {
            String key = e.nextElement();
            OutputData keyEnum = OutputData.valueOf(key);
            try {
                calcul.put(keyEnum, Float.parseFloat(calculXml.get(key)));
            } catch (NumberFormatException er) {
                // On ne fait rien, il s'agit simplement d'une donnée info,
                // elle ne sera pas dans le calcul
            }
        }
    }

}
