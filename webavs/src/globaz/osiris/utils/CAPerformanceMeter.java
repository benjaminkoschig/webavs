package globaz.osiris.utils;

import globaz.jade.log.JadeLogger;
import java.util.Vector;

/**
 * @author dda Utiliser pour le travail d'optimisation. 1. Initialiser un object de type PerformanceMeter. 2. Ajouté
 *         dans le code au point sensible des appelles de la méthode intermediary(). 3. Imprimer les résultats.
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
     * Ajoute une mesure à la liste des prises de temps.
     */
    private void addMeasure() {
        measures.addElement("" + System.currentTimeMillis());
    }

    /**
     * Soustrait à la mesure actuelle la mesure précédente.
     * 
     * @param index
     * @return
     */
    private long diffMeasure(int index, int minusIndex) {
        return Long.parseLong((String) measures.get(index)) - Long.parseLong((String) measures.get(minusIndex));
    }

    /**
     * Return la mesure intermédiaire formattée.
     * 
     * @param index
     * @return
     */
    private String getFormattedIntermediaryMeasure(int index) {
        return ("" + diffMeasure(index, index - 1)) + " ms.";
    }

    /**
     * Return la mesure de départ formattée.
     * 
     * @param index
     * @return
     */
    private String getFormattedStartMeasure() {
        return ((String) measures.get(0)) + " ms.";
    }

    /**
     * Ajoute un temps intermédiaire aux mesures.
     */
    public void intermediary() {
        addMeasure();
    }

    /**
     * Imprimer les résultats dans les logs.
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
