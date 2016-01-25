package globaz.hercule.utils;

import globaz.op.common.merge.IMergingContainer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JPA
 * @since 11 juin 2010
 * @revision SCO 2 déc. 2010
 */
public class HerculeContainer implements IMergingContainer {

    private Map<String, Collection<String>> container = null;

    /**
     * Constructeur de HerculeContainer
     */
    public HerculeContainer() {
        super();
        container = new HashMap<String, Collection<String>>();
    }

    @Override
    public boolean containsKey(String theKey) {
        return container.containsKey(theKey);
    }

    @Override
    public Object getFieldValue(String theKey) {
        return container.containsKey(theKey.toUpperCase()) ? container.get(theKey.toUpperCase())
                : new ArrayList<String>();
    }

    @Override
    public Collection<String> getFieldValues(String theKey) {
        return container.containsKey(theKey.toUpperCase()) ? container.get(theKey.toUpperCase())
                : new ArrayList<String>();
    }

    @Override
    public boolean isMultiRow(String theKey) {
        return true;
    }

    public void put(String columnName, String value) {
        String name = columnName.toUpperCase();
        Collection<String> line = null;

        if (containsKey(name)) {
            line = getFieldValues(name);
        } else {
            line = new ArrayList<String>();
            container.put(name, line);
        }

        line.add(value);
    }

    public boolean isEmpty() {
        return container.isEmpty();
    }
}
