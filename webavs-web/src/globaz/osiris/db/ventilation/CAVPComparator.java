package globaz.osiris.db.ventilation;

import globaz.jade.client.util.JadeStringUtil;
import java.io.Serializable;
import java.util.Comparator;

public class CAVPComparator implements Comparator, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Object o1, Object o2) {
        String rubrique1 = ((CAVPDetailRubriqueSection) o1).getOrdre();
        String rubrique2 = ((CAVPDetailRubriqueSection) o2).getOrdre();

        if (JadeStringUtil.isBlank(rubrique1) && !JadeStringUtil.isBlank(rubrique2)) {
            return -1;
        }

        if (!JadeStringUtil.isBlank(rubrique1) && JadeStringUtil.isBlank(rubrique2)) {
            return 1;
        }

        if (JadeStringUtil.isBlank(rubrique1) && JadeStringUtil.isBlank(rubrique2)) {
            /*
             * Integer rub1 = new Integer("0"); Integer rub2 = new Integer("0"); int compare = rub1.compareTo(rub2); if
             * (compare == 0) { return -1; } else { return compare; }
             */
            return -1;
        }

        if (!JadeStringUtil.isBlank(rubrique1) && !JadeStringUtil.isBlank(rubrique2)) {
            Integer rub1 = new Integer(rubrique1);
            Integer rub2 = new Integer(rubrique2);
            int compare = rub1.compareTo(rub2);
            if (compare == 0) {
                compare = rub1.compareTo(rub2);
                if (compare == 0) {
                    return -1;
                } else {
                    return compare;
                }
            } else {
                return compare;
            }
        }
        return -1;
    }

}
