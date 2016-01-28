/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.9M1</a>, using an XML Schema.
 * $Id: EnteteCommunicationDescriptor.java,v 1.1 2006/06/01 11:02:17 vch Exp $
 */

package globaz.phenix.mapping.retour.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.validators.StringValidator;

/**
 * Class EnteteCommunicationDescriptor.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:17 $
 */
public class EnteteCommunicationDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

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

    public EnteteCommunicationDescriptor() {
        super();
        nsURI = "http://globaz/xml/jaxb";
        xmlName = "EnteteCommunication";

        // -- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.mapping.FieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        // -- initialize attribute descriptors

        // -- initialize element descriptors

        // -- _numReceptionCom
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_numReceptionCom",
                "NumReceptionCom", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                EnteteCommunication target = (EnteteCommunication) object;
                return target.getNumReceptionCom();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    EnteteCommunication target = (EnteteCommunication) object;
                    target.setNumReceptionCom((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://globaz/xml/jaxb");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _numReceptionCom
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _nbreReceptionCom
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_nbreReceptionCom",
                "NbreReceptionCom", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                EnteteCommunication target = (EnteteCommunication) object;
                return target.getNbreReceptionCom();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    EnteteCommunication target = (EnteteCommunication) object;
                    target.setNbreReceptionCom((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://globaz/xml/jaxb");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _nbreReceptionCom
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _dateReceptionCom
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_dateReceptionCom",
                "DateReceptionCom", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                EnteteCommunication target = (EnteteCommunication) object;
                return target.getDateReceptionCom();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    EnteteCommunication target = (EnteteCommunication) object;
                    target.setDateReceptionCom((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://globaz/xml/jaxb");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _dateReceptionCom
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _dateTransformationCom
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_dateTransformationCom",
                "DateTransformationCom", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                EnteteCommunication target = (EnteteCommunication) object;
                return target.getDateTransformationCom();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    EnteteCommunication target = (EnteteCommunication) object;
                    target.setDateTransformationCom((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://globaz/xml/jaxb");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _dateTransformationCom
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _nomCtcCom
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_nomCtcCom", "NomCtcCom",
                org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                EnteteCommunication target = (EnteteCommunication) object;
                return target.getNomCtcCom();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    EnteteCommunication target = (EnteteCommunication) object;
                    target.setNomCtcCom((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://globaz/xml/jaxb");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _nomCtcCom
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _EMailCtcCom
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_EMailCtcCom",
                "EMailCtcCom", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                EnteteCommunication target = (EnteteCommunication) object;
                return target.getEMailCtcCom();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    EnteteCommunication target = (EnteteCommunication) object;
                    target.setEMailCtcCom((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://globaz/xml/jaxb");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _EMailCtcCom
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _telCtcCom
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_telCtcCom", "TelCtcCom",
                org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                EnteteCommunication target = (EnteteCommunication) object;
                return target.getTelCtcCom();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    EnteteCommunication target = (EnteteCommunication) object;
                    target.setTelCtcCom((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://globaz/xml/jaxb");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _telCtcCom
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _userId
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_userId", "UserId",
                org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                EnteteCommunication target = (EnteteCommunication) object;
                return target.getUserId();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    EnteteCommunication target = (EnteteCommunication) object;
                    target.setUserId((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://globaz/xml/jaxb");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _userId
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
    } // -- globaz.phenix.mapping.retour.castor.EnteteCommunicationDescriptor()

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
        return globaz.phenix.mapping.retour.castor.EnteteCommunication.class;
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
