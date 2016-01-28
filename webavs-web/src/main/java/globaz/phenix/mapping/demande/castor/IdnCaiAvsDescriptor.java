/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: IdnCaiAvsDescriptor.java,v 1.1 2006/06/01 11:02:22 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.validators.StringValidator;

/**
 * Class IdnCaiAvsDescriptor.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:22 $
 */
public class IdnCaiAvsDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

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

    public IdnCaiAvsDescriptor() {
        super();
        xmlName = "idnCaiAvs";

        // -- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.xml.XMLFieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        // -- initialize attribute descriptors

        // -- initialize element descriptors

        // -- _nomCaiAvs
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_nomCaiAvs", "nomCaiAvs",
                org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                IdnCaiAvs target = (IdnCaiAvs) object;
                return target.getNomCaiAvs();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    IdnCaiAvs target = (IdnCaiAvs) object;
                    target.setNomCaiAvs((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _nomCaiAvs
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setMaxLength(50);
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _numCaiAvs
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_numCaiAvs", "numCaiAvs",
                org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                IdnCaiAvs target = (IdnCaiAvs) object;
                return target.getNumCaiAvs();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    IdnCaiAvs target = (IdnCaiAvs) object;
                    target.setNumCaiAvs((java.lang.String) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _numCaiAvs
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setMaxLength(6);
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _adrCaiAvs
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                globaz.phenix.mapping.demande.castor.AdrCaiAvs.class, "_adrCaiAvs", "adrCaiAvs",
                org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                IdnCaiAvs target = (IdnCaiAvs) object;
                return target.getAdrCaiAvs();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return new globaz.phenix.mapping.demande.castor.AdrCaiAvs();
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    IdnCaiAvs target = (IdnCaiAvs) object;
                    target.setAdrCaiAvs((globaz.phenix.mapping.demande.castor.AdrCaiAvs) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _adrCaiAvs
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
        }
        desc.setValidator(fieldValidator);
    } // -- globaz.phenix.mapping.demande.castor.IdnCaiAvsDescriptor()

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
        return globaz.phenix.mapping.demande.castor.IdnCaiAvs.class;
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
