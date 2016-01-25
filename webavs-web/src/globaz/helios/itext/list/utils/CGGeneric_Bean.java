/**
 * 
 */
package globaz.helios.itext.list.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jsi
 * 
 */
public class CGGeneric_Bean {

    protected List listCol = new ArrayList(100);
    protected List listColME = new ArrayList(100);

    /** Une clé optionnelle utile au tri */
    private Comparable sortKey = null;

    public CGGeneric_Bean() {
    }

    /**
     * @return the listCol
     */
    public Object getCol(int index) {
        return listCol.get(index);
    }

    public Object getColME(int index) {
        return listColME.get(index);
    }

    /**
     * @param type
     * @param index
     * @param value
     */
    public void setCol(String type, int index, String value) {
        if (type.equals("me")) {
            while (listColME.size() < index) {
                listColME.add(null);
            }
            listColME.add(value);
        } else {
            while (listCol.size() < index) {
                listCol.add(null);
            }
            listCol.add(index, value);
        }
    }

    public int getColSize(String type) {
        if (type.equals("me")) {
            return listColME.size();
        } else {
            return listCol.size();
        }

    }

    public Comparable getSortKey() {
        return sortKey;
    }

    public void setSortKey(Comparable sortKey) {
        this.sortKey = sortKey;
    }
}
