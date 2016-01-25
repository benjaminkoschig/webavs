/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: EnteteDemandeDescriptor.java,v 1.1 2006/06/01 11:02:23 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

/**
 * Class EnteteDemandeDescriptor.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:23 $
 */
public class EnteteDemandeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /**
     * Field identity
     */
    private org.exolab.castor.xml.XMLFieldDescriptor identity;

    /**
     * Field nsPrefix
     */
    private java.lang.String nsPrefix;

    /**
     * Field nsURI
     */
    private java.lang.String nsURI;

    /**
     * Field xmlName
     */
    private java.lang.String xmlName;

    // ----------------/
    // - Constructors -/
    // ----------------/

    public EnteteDemandeDescriptor() {
        super();
        xmlName = "enteteDemande";

        // -- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.xml.XMLFieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        // -- initialize attribute descriptors

        // -- initialize element descriptors

        // -- _idnCaiAvs
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                globaz.phenix.mapping.demande.castor.IdnCaiAvs.class, "_idnCaiAvs", "idnCaiAvs",
                org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                EnteteDemande target = (EnteteDemande) object;
                return target.getIdnCaiAvs();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new globaz.phenix.mapping.demande.castor.IdnCaiAvs();
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    EnteteDemande target = (EnteteDemande) object;
                    target.setIdnCaiAvs((globaz.phenix.mapping.demande.castor.IdnCaiAvs) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _idnCaiAvs
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
        }
        desc.setValidator(fieldValidator);
        // -- _idnDem
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(globaz.phenix.mapping.demande.castor.IdnDem.class,
                "_idnDem", "idnDem", org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                EnteteDemande target = (EnteteDemande) object;
                return target.getIdnDem();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new globaz.phenix.mapping.demande.castor.IdnDem();
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    EnteteDemande target = (EnteteDemande) object;
                    target.setIdnDem((globaz.phenix.mapping.demande.castor.IdnDem) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _idnDem
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
        }
        desc.setValidator(fieldValidator);
        // -- _infoDem
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                globaz.phenix.mapping.demande.castor.InfoDem.class, "_infoDem", "infoDem",
                org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                EnteteDemande target = (EnteteDemande) object;
                return target.getInfoDem();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new globaz.phenix.mapping.demande.castor.InfoDem();
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    EnteteDemande target = (EnteteDemande) object;
                    target.setInfoDem((globaz.phenix.mapping.demande.castor.InfoDem) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _infoDem
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
        }
        desc.setValidator(fieldValidator);
    } // -- globaz.phenix.mapping.demande.castor.EnteteDemandeDescriptor()

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method getAccessMode
     * 
     * 
     * 
     * @return AccessMode
     */
    @Override
    public org.exolab.castor.mapping.AccessMode getAccessMode() {
        return null;
    } // -- org.exolab.castor.mapping.AccessMode getAccessMode()

    /**
     * Method getExtends
     * 
     * 
     * 
     * @return ClassDescriptor
     */
    @Override
    public org.exolab.castor.mapping.ClassDescriptor getExtends() {
        return null;
    } // -- org.exolab.castor.mapping.ClassDescriptor getExtends()

    /**
     * Method getIdentity
     * 
     * 
     * 
     * @return FieldDescriptor
     */
    @Override
    public org.exolab.castor.mapping.FieldDescriptor getIdentity() {
        return identity;
    } // -- org.exolab.castor.mapping.FieldDescriptor getIdentity()

    /**
     * Method getJavaClass
     * 
     * 
     * 
     * @return Class
     */
    @Override
    public java.lang.Class getJavaClass() {
        return globaz.phenix.mapping.demande.castor.EnteteDemande.class;
    } // -- java.lang.Class getJavaClass()

    /**
     * Method getNameSpacePrefix
     * 
     * 
     * 
     * @return String
     */
    @Override
    public java.lang.String getNameSpacePrefix() {
        return nsPrefix;
    } // -- java.lang.String getNameSpacePrefix()

    /**
     * Method getNameSpaceURI
     * 
     * 
     * 
     * @return String
     */
    @Override
    public java.lang.String getNameSpaceURI() {
        return nsURI;
    } // -- java.lang.String getNameSpaceURI()

    /**
     * Method getValidator
     * 
     * 
     * 
     * @return TypeValidator
     */
    @Override
    public org.exolab.castor.xml.TypeValidator getValidator() {
        return this;
    } // -- org.exolab.castor.xml.TypeValidator getValidator()

    /**
     * Method getXMLName
     * 
     * 
     * 
     * @return String
     */
    @Override
    public java.lang.String getXMLName() {
        return xmlName;
    } // -- java.lang.String getXMLName()

}
