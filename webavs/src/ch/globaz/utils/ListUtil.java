package ch.globaz.utils;

import java.util.HashSet;
import java.util.Set;

public class ListUtil {
    /**
     * Retourne true si la collection contient des doublons
     * 
     * @param all
     * @return
     */
    public static <T> boolean hasDuplicate(Iterable<T> all) {
        Set<T> set = new HashSet<T>();
        // Set#add returns false if the set does not change, which
        // indicates that a duplicate element has been added.
        for (T each : all) {
            if (!set.add(each)) {
                return true;
            }
        }
        return false;
    }
}
