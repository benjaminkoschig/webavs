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
public class PRXmlLinkedClassElement implements IPRXmlElement {

    public final static String MANAGER_NOT_APPLICABLE = "na";
    public final static String RELATION_TYPE_1_TO_1 = "1_TO_1";
    public final static String RELATION_TYPE_1_TO_N = "1_TO_N";

    private String className = null;
    private String forIdParent = null;
    private String idParent = null;
    private List linkedClass = null;
    private String managerClassName = null;
    private String relationType = null;

    /*
     * (non-Javadoc)
     * 
     * @seeglobaz.prestation.clone.xml.data.IPRXmlElement#addXmlElement(globaz. prestation.clone.xml.data.IPRXmlElement)
     */
    @Override
    public void addXmlElement(IPRXmlElement element) {
        if (linkedClass == null) {
            linkedClass = new ArrayList();
        }
        linkedClass.add(element);
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
    public String getForIdParent() {
        return forIdParent;
    }

    /**
     * @return
     */
    public String getIdParent() {
        return idParent;
    }

    /**
     * @return
     */
    public String getManagerClassName() {
        return managerClassName;
    }

    /**
     * @return
     */
    public String getRelationType() {
        return relationType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.prestation.clone.xml.data.IPRXmlElement#getXmlElements()
     */
    @Override
    public List getXmlElements() {
        return linkedClass;
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
    public void setForIdParent(String string) {
        forIdParent = string;
    }

    /**
     * @param string
     */
    public void setIdParent(String string) {
        idParent = string;
    }

    /**
     * @param string
     */
    public void setManagerClassName(String string) {
        managerClassName = string;
    }

    /**
     * @param i
     */
    public void setRelationType(String s) {
        relationType = s;
    }

}
