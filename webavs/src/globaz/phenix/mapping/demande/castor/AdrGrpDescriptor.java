/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: AdrGrpDescriptor.java,v 1.1 2006/06/01 11:02:22 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.validators.StringValidator;

/**
 * Class AdrGrpDescriptor.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:22 $
 */
public class AdrGrpDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

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

    public AdrGrpDescriptor() {
        super();
        xmlName = "AdrGrp";

        // -- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.xml.XMLFieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        // -- initialize attribute descriptors

        // -- initialize element descriptors

        // -- _adrLigne1
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_adrLigne1", "adrLigne1",
                org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AdrGrp target = (AdrGrp) object;
                return target.getAdrLigne1();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    AdrGrp target = (AdrGrp) object;
                    target.setAdrLigne1((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _adrLigne1
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setMaxLength(50);
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _adrLigne2
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_adrLigne2", "adrLigne2",
                org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AdrGrp target = (AdrGrp) object;
                return target.getAdrLigne2();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    AdrGrp target = (AdrGrp) object;
                    target.setAdrLigne2((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _adrLigne2
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setMaxLength(50);
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _adrLigne3
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_adrLigne3", "adrLigne3",
                org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AdrGrp target = (AdrGrp) object;
                return target.getAdrLigne3();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    AdrGrp target = (AdrGrp) object;
                    target.setAdrLigne3((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _adrLigne3
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setMaxLength(50);
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _adrLigne4
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_adrLigne4", "adrLigne4",
                org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AdrGrp target = (AdrGrp) object;
                return target.getAdrLigne4();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    AdrGrp target = (AdrGrp) object;
                    target.setAdrLigne4((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _adrLigne4
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setMaxLength(50);
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _adrLigne5
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_adrLigne5", "adrLigne5",
                org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AdrGrp target = (AdrGrp) object;
                return target.getAdrLigne5();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    AdrGrp target = (AdrGrp) object;
                    target.setAdrLigne5((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _adrLigne5
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setMaxLength(50);
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _adrLigne6
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_adrLigne6", "adrLigne6",
                org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                AdrGrp target = (AdrGrp) object;
                return target.getAdrLigne6();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    AdrGrp target = (AdrGrp) object;
                    target.setAdrLigne6((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _adrLigne6
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setMaxLength(50);
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
    } // -- globaz.phenix.mapping.demande.castor.AdrGrpDescriptor()

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
        return globaz.phenix.mapping.demande.castor.AdrGrp.class;
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
