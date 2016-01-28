package globaz.aquila.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Pascal Lovy, 15-dec-2004
 */
public class CompositeComparator implements Comparator, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** Liste des comparateurs */
    private Comparator[] cList = null;

    /**
     * Initialise le comparateur
     * 
     * @param comparatorList
     *            Liste des comparateurs pris en charge
     */
    public CompositeComparator(Comparator[] comparatorList) {
        if (comparatorList == null) {
            throw new NullPointerException();
        }
        cList = comparatorList;
    }

    /**
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Object o1, Object o2) {
        int result = 0;
        // Teste tous les comparateurs jusqu'a ce qu'il y aie une différence
        for (int i = 0; (i < cList.length) && (result == 0); i++) {
            result = cList[i].compare(o1, o2);
        }
        return result;
    }

}
