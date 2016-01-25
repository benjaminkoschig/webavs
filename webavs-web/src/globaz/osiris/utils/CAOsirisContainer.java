/**
 * 
 */
package globaz.osiris.utils;

import globaz.op.common.merge.IMergingContainer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author jsi
 * 
 */
public class CAOsirisContainer implements IMergingContainer {

    private HashMap container = null;

    public CAOsirisContainer() {
        super();
        container = new HashMap();
    }

    @Override
    public boolean containsKey(String theKey) {
        return container.containsKey(theKey.toUpperCase());
    }

    @Override
    public Object getFieldValue(String theKey) {
        return container.containsKey(theKey.toUpperCase()) ? container.get(theKey.toUpperCase()) : "";
    }

    @Override
    public Collection getFieldValues(String theKey) {
        return (Collection) (container.containsKey(theKey.toUpperCase()) ? container.get(theKey.toUpperCase())
                : new ArrayList());
    }

    @Override
    public boolean isMultiRow(String theKey) {
        return container.containsKey(theKey.toUpperCase()) && container.get(theKey.toUpperCase()) instanceof Collection;
    }

    public void addValue(String key, Object value) {
        key = key.toUpperCase();
        Collection line = null;
        if (containsKey(key)) {
            line = getFieldValues(key);
        } else {
            line = new ArrayList();
            container.put(key, line);
        }
        line.add(value);
    }

    public void refreshValue(String key, Object value) {
        key = key.toUpperCase();
        if (containsKey(key)) {
            container.remove(key);
        }
        container.put(key, value);
    }

    public void addValueToTotal(String key, double value) {
        // df.format(new Double(content).doubleValue())
        key = key.toUpperCase();
        double temp = 0;
        if (containsKey(key)) {
            String v = container.get(key).toString().replace('\'', ' ');
            String vtrimed = removeSpaces(v);
            temp = new Double(vtrimed).doubleValue();
        }
        container.put(key, new Double(value + temp));

    }

    private String removeSpaces(String s) {
        StringTokenizer st = new StringTokenizer(s, " ", false);
        String t = "";
        while (st.hasMoreElements()) {
            t += st.nextElement();
        }
        return t;
    }

    public void addMap(Map m) {
        container.putAll(m);
    }
}
