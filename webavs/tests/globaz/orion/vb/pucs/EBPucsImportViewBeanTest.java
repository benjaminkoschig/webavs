package globaz.orion.vb.pucs;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.Test;

public class EBPucsImportViewBeanTest {

    public static <K, V extends List<E>, E> Map<K, List<E>> sortByValues(final Map<K, V> map) {
        Comparator<K> valueComparator = new Comparator<K>() {
            @Override
            public int compare(K k1, K k2) {
                int compare = map.get(k2).size() - (map.get(k1)).size();
                if (compare == 0) {
                    return -1;
                }
                return compare;
            }
        };
        Map<K, List<E>> sortedByValues = new TreeMap<K, List<E>>(valueComparator);
        sortedByValues.putAll(map);
        // for (Entry<K, V> entry : map.entrySet()) {
        // sortedByValues.put(entry.getKey(), entry.getValue());
        // }

        return sortedByValues;
    }

    @Test
    public void testSortByValuesWithMultipleValueDemo() throws Exception {
        Map<String, List<Integer>> map = new TreeMap<String, List<Integer>>();
        map.put("a", Arrays.asList(1));
        map.put("b", Arrays.asList(1, 2));
        map.put("c", Arrays.asList(1, 2, 3));
        map.put("d", Arrays.asList(1, 4));
        Map<String, List<Integer>> sortedMapByValue = sortByValues(map);

        assertEquals(4, sortedMapByValue.size());
        // assertEquals("[d, c, b, a]", Arrays.toString(sortedMapByValue.keySet().toArray()));
    }
}
