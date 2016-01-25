package globaz.pegasus.utils.calculmoissuivant;

import java.util.Comparator;
import ch.globaz.pegasus.business.models.calcul.CalculMoisSuivant;

/**
 * Classe comparator permettant de comparer les positions pour l'affichage des données fianancières dans le flux html de
 * l'écran de préparation du calcul
 * 
 * @author sce
 * 
 */
public class CalculMoisSuivantComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        int positionO1 = ((CalculMoisSuivant) o1).getPosition();
        int positionO2 = ((CalculMoisSuivant) o2).getPosition();

        if (positionO1 > positionO2) {
            return 1;
        } else if (positionO1 == positionO2) {
            return 0;
        } else {
            return -1;
        }
    }

}
