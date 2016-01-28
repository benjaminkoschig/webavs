package globaz.osiris.utils.quicksort;

/**
 * Insérez la description du type ici. Date de création : (15.07.2003 13:56:01)
 * 
 * @author: Administrator
 */
public class ComparableVector extends java.util.Vector {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public int compareTo(Object obj2, int index) {

        ComparableVector obj3 = (ComparableVector) obj2;

        if (obj3 == null) {
            return 0;
        }

        String str1 = (String) elementAt(index);
        String str2 = (String) obj3.elementAt(index);

        return (str1.compareTo(str2));

    }
}
