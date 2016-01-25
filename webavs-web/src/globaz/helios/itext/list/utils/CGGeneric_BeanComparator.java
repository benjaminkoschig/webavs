package globaz.helios.itext.list.utils;

import java.util.Comparator;

/**
 * @author fwi
 * 
 */
public class CGGeneric_BeanComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        CGGeneric_Bean bean1 = (CGGeneric_Bean) o1;
        CGGeneric_Bean bean2 = (CGGeneric_Bean) o2;

        return bean1.getSortKey().compareTo(bean2.getSortKey());
    }
}
