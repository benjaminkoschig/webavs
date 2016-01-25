package globaz.apg.module.calcul.wrapper;

import java.util.Comparator;

/**
 * Descpription Compararateur pour les listes d'objets APPrestationWrapper La comparaison se fait sur la date de début
 * de la prestation calculée.
 * 
 * @author scr Date de création 18 mai 05
 */
public class APPrestationWrapperComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if (!(o1.getClass().equals(o2.getClass()))) {
            throw new ClassCastException();
        }
        APPrestationWrapper c1 = (APPrestationWrapper) o1;
        APPrestationWrapper c2 = (APPrestationWrapper) o2;
        return c1.getPeriodeBaseCalcul().getDateDebut().toAMJ()
                .compareTo(c2.getPeriodeBaseCalcul().getDateDebut().toAMJ());
    }
}
