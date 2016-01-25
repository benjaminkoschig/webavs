package globaz.osiris.utils;

import globaz.jade.log.JadeLogger;
import java.util.Vector;

/**
 * @author dda Utiliser pour le travail d'optimisation. 1. Initialiser un object de type PerformanceMeter. 2. Ajout�
 *         dans le code au point sensible des appelles de la m�thode intermediary(). 3. Imprimer les r�sultats.
 */
public class CAPerformanceMeter {

    private String measureName = "";
    private Vector measures = new Vector();

    /**
     * Construtcteur.
     * 
     * @param measureName
     *            Le nom de la mesure en cours.
     */
    public CAPerformanceMeter(String measureName) {
        this.measureName = measureName;
        addMeasure();
    }

    /**
     * Ajoute une mesure � la liste des prises de temps.
     */
    private void addMeasure() {
        measures.addElement("" + System.currentTimeMillis());
    }

    /**
     * Soustrait � la mesure actuelle la mesure pr�c�dente.
     * 
     * @param index
     * @return
     */
    private long diffMeasure(int index, int minusIndex) {
        return Long.parseLong((String) measures.get(index)) - Long.parseLong((String) measures.get(minusIndex));
    }

    /**
     * Return la mesure interm�diaire formatt�e.
     * 
     * @param index
     * @return
     */
    private String getFormattedIntermediaryMeasure(int index) {
        return ("" + diffMeasure(index, index - 1)) + " ms.";
    }

    /**
     * Return la mesure de d�part formatt�e.
     * 
     * @param index
     * @return
     */
    private String getFormattedStartMeasure() {
        return ((String) measures.get(0)) + " ms.";
    }

    /**
     * Ajoute un temps interm�diaire aux mesures.
     */
    public void intermediary() {
        addMeasure();
    }

    /**
     * Imprimer les r�sultats dans les logs.
     */
    public void print() {
        for (int i = 0; i < measures.size(); i++) {
            if (i == 0) {
                JadeLogger.info(this, measureName + " Start time : " + getFormattedStartMeasure());
            } else if (i == (measures.size() - 1)) {
                JadeLogger.info(this, measureName + " Total time : " + getFormattedStartMeasure());
            } else {
                JadeLogger.info(this, measureName + " Intermediate[" + i + "] : " + getFormattedIntermediaryMeasure(i));
            }
        }
    }
}
