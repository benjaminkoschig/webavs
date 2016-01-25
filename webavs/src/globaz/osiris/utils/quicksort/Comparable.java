package globaz.osiris.utils.quicksort;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * @author user To change this generated comment edit the template variable "typecomment":
 *         Window>Preferences>Java>Templates. To enable and disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class Comparable implements Comparator, Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int indexCompare = 0;

    /**
     * Constructor for Comparable.
     */
    public Comparable(int index) {
        indexCompare = index;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(Object, Object)
     */
    @Override
    public int compare(Object o1, Object o2) {
        if ((o1 == null) && (o2 == null)) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        String s1 = (String) ((List) o1).get(indexCompare);
        String s2 = (String) ((List) o2).get(indexCompare);
        return s1.compareTo(s2);
    }
}
