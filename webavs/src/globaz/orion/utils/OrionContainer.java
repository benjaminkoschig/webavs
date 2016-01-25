package globaz.orion.utils;

import globaz.op.common.merge.IMergingContainer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class OrionContainer implements IMergingContainer {

    // Attributs
    private HashMap container = null;

    // Constructeurs
    public OrionContainer() {
        super();
        container = new HashMap();
    }

    // Méthodes
    @Override
    public boolean containsKey(String theKey) {
        return container.containsKey(theKey);
    }

    @Override
    public Object getFieldValue(String theKey) {
        return container.containsKey(theKey.toUpperCase()) ? container.get(theKey.toUpperCase()) : new ArrayList();
    }

    @Override
    public Collection getFieldValues(String theKey) {
        return (Collection) (container.containsKey(theKey.toUpperCase()) ? container.get(theKey.toUpperCase())
                : new ArrayList());
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
        Collection line = null;
        if (containsKey(columnName)) {
            line = getFieldValues(columnName);
        } else {
            line = new ArrayList();
            container.put(columnName, line);
        }
        line.add(value);
    }

}
