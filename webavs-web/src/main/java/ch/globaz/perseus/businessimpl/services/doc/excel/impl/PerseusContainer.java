package ch.globaz.perseus.businessimpl.services.doc.excel.impl;

import globaz.op.common.merge.IMergingContainer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author DMA Reprise de hercule
 * @since 21 juin 2011
 */
public class PerseusContainer implements IMergingContainer {

    private HashMap<String, Collection<String>> container = null;

    /**
     * Constructeur de PegasusContainer
     */
    public PerseusContainer() {
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

    public int indexOf(String theColumn, String theKey) {
        ArrayList<String> list = new ArrayList<String>(getFieldValues(theColumn));
        return list.indexOf(theKey);
    }

    @Override
    public boolean isMultiRow(String theKey) {
        // TODO Auto-generated method stub
        return true;
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
