package ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul;

import java.util.Calendar;
import java.util.Map;
import java.util.TreeMap;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;

/**
 * Classe encapsulant une variable métier, avec tous l'historique de ses valeurs dans le temps
 * 
 * @author sce
 * 
 */
public class VariableMetier {

    @Override
    public String toString() {
        return "VariableMetier [csTypeVariableMetier=" + csTypeVariableMetier + ", legendesVariablesMetiers="
                + legendesVariablesMetiers + ", variablesMetiers=" + variablesMetiers + "]";
    }

    private String csTypeVariableMetier = null;

    private TreeMap<Long, String> legendesVariablesMetiers = new TreeMap<Long, String>();

    /**
     * Liste historique des valeurs de la variable métier. Chaque valeur est valide à partir de la date qui est dans la
     * clé, et ce jusqu'à la date de la clé suivante. Il est important que ce soit un TreeMap pour garantir l'ordre
     * chronologique des clés.
     */
    private TreeMap<Long, String> variablesMetiers = new TreeMap<Long, String>();

    public String getCsTypeVariableMetier() {
        return csTypeVariableMetier;
    }

    public Map<Long, String> getLegendesVariablesMetiers() {
        return legendesVariablesMetiers;
    }

    private long getTimeStampForYearAndMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        return cal.getTimeInMillis();

    }

    public String getValeurByDate(int month, int year) throws CalculException {

        // month = "01." + month;

        // if (!JadeDateUtil.isGlobazDate(month)) {
        // throw new CalculException("The date format passed is wrong!");
        // }
        // ;

        // String month = month.split(".")[0];
        // String year = month.split(".")[1];

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);

        long timeStamp = cal.getTimeInMillis();

        return (variablesMetiers.get(Long.valueOf(timeStamp)));
    }

    /**
     * Retourne la valeur d'une variable métier depuis une date. Ce qui implique que la date de la variable doit être
     * plus petite ou égale à la date recherchée (comprise dans la période)
     * 
     * @param month
     * @param year
     * @return
     * @throws CalculException
     */
    public String getValeurDepuisDate(int month, int year) throws CalculException {

        String returnValue = null;
        long timestamp = getTimeStampForYearAndMonth(year, month);

        for (Long timeStampVariable : variablesMetiers.keySet()) {
            if (timestamp >= timeStampVariable) {
                returnValue = variablesMetiers.get(timeStampVariable);
                // hack prob bd, si valeur de 0, null, on force, on doit avoir une valeur ici
                if (returnValue == null) {
                    returnValue = "0";
                }
            }
        }
        // cas ou on ne trouve rien, le time stamp est toujours inférieur aux variables métiers!
        if (returnValue == null) {
            throw new CalculException(
                    "The variable metiers <"
                            + csTypeVariableMetier
                            + "> cannot be find with the method getValeurDepuisDate. That seems the timestamp is always before than the variablemetiers's timestamp");
        }
        return returnValue;

    }

    public TreeMap<Long, String> getVariablesMetiers() {
        return variablesMetiers;
    }

    public void setCsTypeVariableMetier(String csTypeVariableMetier) {
        this.csTypeVariableMetier = csTypeVariableMetier;
    }

    public void setLegendesVariablesMetiers(Map<Long, String> legendesVariablesMetiers) {
        this.legendesVariablesMetiers = new TreeMap(legendesVariablesMetiers);
    }

    public void setVariablesMetiers(Map<Long, String> variablesMetiers) {
        this.variablesMetiers = new TreeMap(variablesMetiers);
    }
}
