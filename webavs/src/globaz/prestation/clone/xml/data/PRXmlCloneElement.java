/*
 * Créé le 28 avr. 05
 * 
 * Description :
 */
package globaz.prestation.clone.xml.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author scr
 * 
 *         Descpription
 */
public class PRXmlCloneElement implements IPRXmlElement {

    private String className = null;
    private String id = null;
    private List linkedClassList = null;

    public PRXmlCloneElement() {
        linkedClassList = new ArrayList();
    }

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.prestation.clone.xml.data.IPRXmlElement#addXmlElement(globaz. prestation.clone.xml.data.IPRXmlElement)
     */
    @Override
    public void addXmlElement(IPRXmlElement element) {
        linkedClassList.add(element);

    }

    /**
     * @return
     */
    public String getClassName() {
        return className;
    }

    /**
     * @return
     */
    public String getId() {
        return id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.clone.xml.data.IPRXmlElement#getXmlElements()
     */
    @Override
    public List getXmlElements() {

        return linkedClassList;
    }

    /**
     * @param string
     */
    public void setClassName(String string) {
        className = string;
    }

    /**
     * @param string
     */
    public void setId(String string) {
        id = string;
    }

}
