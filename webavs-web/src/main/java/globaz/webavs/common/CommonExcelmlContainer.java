package globaz.webavs.common;

import globaz.op.common.merge.IMergingContainer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MMO
 * @since 31.05.2010
 */
public class CommonExcelmlContainer implements IMergingContainer {

    private Map<String, Collection<String>> container = null;

    public CommonExcelmlContainer() {
        container = new HashMap<String, Collection<String>>();
    }

    @Override
    public boolean containsKey(String theKey) {
        theKey = theKey.toUpperCase();

        return container.containsKey(theKey);
    }

    @Override
    public Object getFieldValue(String theKey) {
        return getFieldValues(theKey);
    }

    @Override
    public Collection<String> getFieldValues(String theKey) {
        theKey = theKey.toUpperCase();

        return container.containsKey(theKey) ? container.get(theKey) : null;
    }

    @Override
    public boolean isMultiRow(String theKey) {
        return getFieldValues(theKey) == null ? false : (getFieldValues(theKey).size() > 1);
    }

    /**
     * Permet d'ajouter une valeur � la Collection mapp�e par la cl� pass�e en param�tres Si la cl� n'exite pas dans le
     * container une nouvelle ArrayList est mapp�e sous la cl�
     * 
     * @param columnName
     *            La cl� � tester
     * @param value
     *            La nouvelle valeur � ajouter � la Collection de valeurs mapp�e par la cl�
     */
    public void put(String columnName, String value) {
        columnName = columnName.toUpperCase();
        Collection<String> line = null;

        if (containsKey(columnName)) {
            line = getFieldValues(columnName);
        } else {
            line = new ArrayList<String>();
            container.put(columnName, line);
        }

        line.add(value);
    }

    public int size() {
        return container.size();
    }

    public boolean isEmpty() {
        return container.isEmpty();
    }
}
