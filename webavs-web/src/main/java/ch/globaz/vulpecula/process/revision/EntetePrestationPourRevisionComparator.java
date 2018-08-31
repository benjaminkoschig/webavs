/**
 * 
 */
package ch.globaz.vulpecula.process.revision;

import java.io.Serializable;
import java.util.Comparator;
import ch.globaz.vulpecula.business.models.is.EntetePrestationComplexModel;
import ch.globaz.vulpecula.domain.models.common.Date;

/**
 * Tri des postes de travails :
 * 1. Par groupe actif (1) / inactif (-1)
 * 2. Par chronologie
 * 
 * 
 */
public class EntetePrestationPourRevisionComparator implements Comparator<EntetePrestationComplexModel>, Serializable {

    @Override
    public int compare(final EntetePrestationComplexModel e1, final EntetePrestationComplexModel e2) {
        Date date1 = new Date(e1.getPeriodeDe());
        Date date2 = new Date(e2.getPeriodeDe());

        if (date1.after(date2)) {
            return 1;
        } else {
            return -1;
        }

    }
}
