package ch.globaz.eavs.model;

import globaz.jade.log.JadeLogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import ch.globaz.eavs.exception.EAVSInvalidXmlFormatException;
import ch.globaz.eavs.model.eahviv2011000101.v3.Message;
import ch.globaz.eavs.model.eahviv2011000102.common.AbstractContent;
import ch.globaz.eavs.utils.EAVSUtils;

public abstract class EAVSAbstractModel {
    protected static final String MINORVERSION = "minorVersion=\"4\"";
    public static final String SCHEMAINSTANCE = "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";
    private EAVSNameSpaceListener listener;
    private StringBuffer nameSpacesForHeader;

    public EAVSAbstractModel() {
        super();
    }

    public void addChildren(EAVSAbstractModel aModel) throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Method m = null;
        try {
            m = this.getClass().getDeclaredMethod("set" + EAVSUtils.getNomMethode(aModel.getClass()),
                    new Class[] { EAVSAbstractModel.class });
        } catch (NoSuchMethodException e) {
            // no setXXX(), so we try to get an addXXX()
            try {
                m = this.getClass().getDeclaredMethod("add" + EAVSUtils.getNomMethode(aModel.getClass()),
                        new Class[] { EAVSAbstractModel.class });
            } catch (NoSuchMethodException e1) {
                throw new NoSuchMethodException("no add/set[" + EAVSUtils.getNomMethode(aModel.getClass())
                        + "] for class " + this.getClass());
            }
        }
        assert m != null : "method is null in " + this.getClass().getName() + ".addChildren()";
        m.invoke(this, new Object[] { aModel });
        JadeLogger.trace(this, aModel.getClass().toString() + " added to " + this.getClass().toString());
    }

    public StringBuffer asXml() throws EAVSInvalidXmlFormatException {
        listener.onAddNode(this);
        StringBuffer result = new StringBuffer();
        if (generateNameSpace()) { // ceci est une classe "root" -> on ajoute la
            // définition XML
            result.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
        }
        result.append("<" + getTargetNameSpace() + ":" + getNomClasse());
        StringBuffer monTag = new StringBuffer();
        if (EAVSNonFinalNode.class.isAssignableFrom(this.getClass())) {
            EAVSNonFinalNode thisFinal = (EAVSNonFinalNode) this;
            List children = thisFinal.getChildren();
            if (children != null) {
                Iterator i = children.iterator();
                while (i.hasNext()) {
                    EAVSAbstractModel child = (EAVSAbstractModel) i.next();
                    if (child != null) {
                        listener.onAddNode(child);
                        if (EAVSFinalNode.class.isAssignableFrom(child.getClass())) {
                            monTag.append("<" + (child).getTargetNameSpace() + ":" + child.getNomClasse() + ">"
                                    + ((EAVSFinalNode) child).getValue() + "</" + (child).getTargetNameSpace() + ":"
                                    + child.getNomClasse() + ">" + "\n");
                        } else {
                            EAVSAbstractModel abstractModelChild = child;
                            abstractModelChild.setNameSpaceListener(listener);
                            monTag.append(abstractModelChild.asXml() + "\n");
                        }
                    }
                }
                // Pour les messages 102, on construit le content avec les
                // messages 101
                if (AbstractContent.class.isAssignableFrom(this.getClass())) {
                    AbstractContent content = (AbstractContent) this;
                    Vector tabMessages101 = content.getTabMessages101();
                    for (int j = 0; j < tabMessages101.size(); j++) {
                        Message message101 = (Message) tabMessages101.get(j);
                        message101.deactivateRoot();
                        message101.setNameSpaceListener(listener);
                        monTag.append(message101.asXml());
                    }
                }
            }
        }
        if (generateNameSpace()) {
            nameSpacesForHeader = new StringBuffer();
            nameSpacesForHeader.append(listener.getNameSpaces() + " " + EAVSAbstractModel.SCHEMAINSTANCE + " "
                    + EAVSAbstractModel.MINORVERSION);
            result.append(nameSpacesForHeader);
        } else {
            result.append(overrideMinorVersion());
        }
        result.append(">\n");
        result.append(monTag);
        result.append("</" + getTargetNameSpace() + ":" + getNomClasse() + ">");
        validate();
        return result;
    }

    public void fillMetaData(Map metadata) {
        // overload if needed
    }

    public boolean generateNameSpace() {
        return false;
    }

    /* abstract public List getChildren(); */

    public StringBuffer getNameSpacesForHeader() {
        return nameSpacesForHeader;
    }

    protected String getNomClasse() {
        return EAVSUtils.getNomClasse(this.getClass());
    }

    public abstract String getTargetNameSpace();

    public abstract String getTargetURL();

    protected String overrideMinorVersion() {
        return "";
    }

    public void setNameSpaceListener(EAVSNameSpaceListener aListener) {
        listener = aListener;
    }

    public abstract void validate() throws EAVSInvalidXmlFormatException;
}
