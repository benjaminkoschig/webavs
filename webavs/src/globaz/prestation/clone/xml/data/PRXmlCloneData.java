/*
 * Créé le 28 avr. 05
 * 
 * Description :
 */
package globaz.prestation.clone.xml.data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author scr
 * 
 *         Descpription
 */
public class PRXmlCloneData {

    Map data = null;

    public PRXmlCloneData() {
        data = new HashMap();
    }

    public void add(PRXmlCloneElement cloneElement) {
        data.put(cloneElement.getId(), cloneElement);
    }

    public boolean contains(String id) {
        return data.containsKey(id);
    }

    public PRXmlCloneElement getXmlCloneElement(String id) {
        if (data.containsKey(id)) {
            return (PRXmlCloneElement) data.get(id);
        } else {
            return null;
        }
    }

}
