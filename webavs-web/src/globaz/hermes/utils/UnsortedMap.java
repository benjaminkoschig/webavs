package globaz.hermes.utils;

import java.util.Enumeration;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (10.01.2003 10:42:44)
 * 
 * @author: Administrator
 */
public class UnsortedMap {
    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2003 10:47:36)
     * 
     * @param args
     *            java.lang.String[]
     */
    public static void main(String[] args) {
        UnsortedMap map = new UnsortedMap();
        map.put("1", "numero 1");
        map.put("2", "numero 2");
        map.put("3", "numero 3");
        System.out.println(map);
        System.out.println(map.get("2"));
        //
        Vector v = new Vector();
        v.add("1");
        v.add("1");
        v.add("1");
        v.add("2");
        System.out.println(v);
    }

    protected Vector keys;

    protected Vector values;

    /**
     * Commentaire relatif au constructeur UnsortedMap.
     */
    public UnsortedMap() {
        super();
        keys = new Vector();
        values = new Vector();
    }

    public Enumeration elements() {
        return values.elements();
    }

    public Object get(Object key) {
        return values.elementAt(keys.indexOf(key));
    }

    public Enumeration keys() {
        return keys.elements();
    }

    public void put(Object key, Object value) {
        keys.addElement(key);
        values.addElement(value);
    }

    public int size() {
        return keys.size();
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (10.01.2003 10:43:10)
     * 
     * @return java.lang.String
     */
    @Override
    public String toString() {
        String display = "";
        for (Enumeration e = keys(); e.hasMoreElements();) {
            Object o = e.nextElement();
            display += "{" + o + "=" + get(o) + "} \n";
        }
        return display;
    }
}
