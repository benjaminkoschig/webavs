/*
 * Créé le 2 mai 05
 * 
 * Description :
 */
package globaz.prestation.clone.xml.data;

import java.util.List;

/**
 * @author scr
 * 
 *         Descpription
 */
public interface IPRXmlElement {
    public void addXmlElement(IPRXmlElement element);

    public List getXmlElements();
}
