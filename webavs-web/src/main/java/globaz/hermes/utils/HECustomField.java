package globaz.hermes.utils;

import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (29.01.2003 13:37:43)
 * 
 * @author: Administrator
 */
public class HECustomField {
    private String field;
    private Vector values;

    public HECustomField(String _field) {
        field = new String(_field);
        values = new Vector();
    }

    public void addValue(String value) {
        values.add(value);
    }

    public String getField() {
        return field;
    }

    @Override
    public String toString() {
        return field;
    }
}
