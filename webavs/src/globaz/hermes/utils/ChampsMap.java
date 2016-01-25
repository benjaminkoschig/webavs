package globaz.hermes.utils;

import java.io.Serializable;
import java.util.Vector;

/**
 * Insérez la description du type ici. Date de création : (10.01.2003 11:14:27)
 * 
 * @author: Administrator
 */
public class ChampsMap extends UnsortedMap implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Vector champLongueur = new Vector();
    private Vector parametragesAnnonce = new Vector();

    /**
     * Commentaire relatif au constructeur ChampMap.
     */
    public ChampsMap() {
        super();
    }

    public boolean containsKey(Object o) {
        return keys.contains(o);
    }

    public boolean containsValue(Object o) {
        return values.contains(o);
    }

    public int getLongueur(Object o) {
        int index = keys.indexOf(o);
        // if (((String)
        // o).equals(globaz.hermes.db.gestion.HEAnnoncesViewBean.DATE_CLOTURE_MMAA))
        if (globaz.hermes.db.gestion.HEAnnoncesViewBean.isDate_MMAA((String) o)) {
            return ((Integer) champLongueur.elementAt(index)).intValue() + 1;
        } else {
            return ((Integer) champLongueur.elementAt(index)).intValue();
        }
    }

    public String getParamAnnonce(int i) {
        return (String) parametragesAnnonce.elementAt(i);
    }

    /**
     * Insérez la description de la méthode ici. Date de création : (07.05.2003 13:45:33)
     * 
     * @return java.util.Vector
     */
    public String[] getParametrageAnnonce() {
        String[] retour = new String[parametragesAnnonce.size()];
        for (int i = 0; i < parametragesAnnonce.size(); i++) {
            retour[i] = (String) parametragesAnnonce.elementAt(i);
        }
        return retour;
    }

    public String keyAt(int i) {
        return (String) keys.elementAt(i);
    }

    public int paramAnnonceSize() {
        return parametragesAnnonce.size();
    }

    public void put(Object key, Object value, int longueur) {
        put(key, value);
        setLongueur(longueur);
    }

    public void putParamAnnonce(String s) {
        if (parametragesAnnonce.indexOf(s) == -1) {
            parametragesAnnonce.addElement(s);
        }
    }

    public boolean remove(Object key) {
        return keys.remove(key);
    }

    public void replace(String key, String value) {
        int keyIndex = keys.indexOf(key);
        if (keyIndex != -1) {
            values.setElementAt(value, keyIndex);
        }
    }

    public void setLongueur(int longueur) {
        champLongueur.addElement(new Integer(longueur));
    }

    @Override
    public String toString() {
        String retour = "";
        for (int i = 0; i < keys.size(); i++) {
            retour += "{" + keys.elementAt(i) + "," + valueAt(i) + "," + getLongueur(keys.elementAt(i)) + "}\n";
        }
        return retour;
    }

    public String valueAt(int i) {
        return (String) values.elementAt(i);
    }

    public String valueAt(Object key) {
        return (String) values.elementAt(keys.indexOf(key));
    }
}
