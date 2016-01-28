package globaz.cygnus.utils;

import globaz.op.common.merge.IMergingContainer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author JPA
 * @since 11 juin 2010
 * @revision JJE 20 mai 2011
 */
public class RFXmlmlContainer implements IMergingContainer {

    private HashMap<String, Collection<String>> container = null;

    /**
     * Constructeur de HerculeContainer
     */
    public RFXmlmlContainer() {
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
        return (container.containsKey(theKey.toUpperCase()) ? container.get(theKey.toUpperCase())
                : new ArrayList<String>());
    }

    @Override
    public boolean isMultiRow(String theKey) {
        // if(theKey.startsWith("header")){
        // return false;
        // }else{
        return true;
        // }
    }

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
}
