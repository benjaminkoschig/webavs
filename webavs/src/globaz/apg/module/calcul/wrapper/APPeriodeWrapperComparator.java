package globaz.apg.module.calcul.wrapper;

import java.util.Comparator;

/**
 * Descpription Compararateur pour les listes d'objets APPeriodeWrapper La comparaison se fait sur la date de début.
 * 
 * @author scr Date de création 18 mai 05
 */
public class APPeriodeWrapperComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if (!(o1.getClass().equals(o2.getClass()))) {
            throw new ClassCastException();
        }
        APPeriodeWrapper c1 = (APPeriodeWrapper) o1;
        APPeriodeWrapper c2 = (APPeriodeWrapper) o2;
        return c1.getDateDebut().toAMJ().compareTo(c2.getDateFin().toAMJ());
    }
}
