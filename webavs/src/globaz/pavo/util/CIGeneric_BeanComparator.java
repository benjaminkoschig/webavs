package globaz.pavo.util;

import java.util.Comparator;

/**
 * @author fwi
 * 
 */
public class CIGeneric_BeanComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        CIGeneric_Bean bean1 = (CIGeneric_Bean) o1;
        CIGeneric_Bean bean2 = (CIGeneric_Bean) o2;

        return bean1.getSortKey().compareTo(bean2.getSortKey());
    }
}
