/**
 * 
 */
package ch.globaz.vulpecula.domain.comparators;

import java.io.Serializable;
import java.util.Comparator;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.decompte.DecompteSalaire;

/**
 * Tri des postes de travails :
 * 1. Par groupe actif (1) / inactif (-1)
 * 2. Par chronologie
 * 
 * 
 */
public class DecompteSalairePourRevisionComparator implements Comparator<DecompteSalaire>, Serializable {

    @Override
    public int compare(final DecompteSalaire d1, final DecompteSalaire d2) {
        Date date1 = d1.getPeriodeDebut();
        Date date2 = d2.getPeriodeDebut();

        if (date1.after(date2)) {
            return 1;
        } else {
            return -1;
        }

    }
}
