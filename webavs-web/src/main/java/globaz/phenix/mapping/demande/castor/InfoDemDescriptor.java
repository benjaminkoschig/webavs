/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: InfoDemDescriptor.java,v 1.1 2006/06/01 11:02:22 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.validators.IntegerValidator;

/**
 * Class InfoDemDescriptor.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:22 $
 */
public class InfoDemDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {

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

    public InfoDemDescriptor() {
        super();
        xmlName = "infoDem";

        // -- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl desc = null;
        org.exolab.castor.xml.XMLFieldHandler handler = null;
        org.exolab.castor.xml.FieldValidator fieldValidator = null;
        // -- initialize attribute descriptors

        // -- initialize element descriptors

        // -- _prdFis
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.Integer.TYPE, "_prdFis", "prdFis",
                org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                InfoDem target = (InfoDem) object;
                if (!target.hasPrdFis()) {
                    return null;
                }
                return new java.lang.Integer(target.getPrdFis());
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    InfoDem target = (InfoDem) object;
                    // ignore null values for non optional primitives
                    if (value == null) {
                        return;
                    }

                    target.setPrdFis(((java.lang.Integer) value).intValue());
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(handler);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _prdFis
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
            IntegerValidator typeValidator = new IntegerValidator();
            typeValidator.setMinInclusive(1995);
            typeValidator.setMaxInclusive(2100);
            typeValidator.setTotalDigits(4);
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        // -- _demUrg
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                globaz.phenix.mapping.demande.castor.types.DemUrgType.class, "_demUrg", "demUrg",
                org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                InfoDem target = (InfoDem) object;
                return target.getDemUrg();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    InfoDem target = (InfoDem) object;
                    target.setDemUrg((globaz.phenix.mapping.demande.castor.types.DemUrgType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(new org.exolab.castor.xml.handlers.EnumFieldHandler(
                globaz.phenix.mapping.demande.castor.types.DemUrgType.class, handler));
        desc.setImmutable(true);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _demUrg
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
        }
        desc.setValidator(fieldValidator);
        // -- _modRps
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(
                globaz.phenix.mapping.demande.castor.types.ModRpsType.class, "_modRps", "modRps",
                org.exolab.castor.xml.NodeType.Element);
        handler = (new org.exolab.castor.xml.XMLFieldHandler() {
            @Override
            public java.lang.Object getValue(java.lang.Object object) throws IllegalStateException {
                InfoDem target = (InfoDem) object;
                return target.getModRps();
            }

            @Override
            public java.lang.Object newInstance(java.lang.Object parent) {
                return null;
            }

            @Override
            public void setValue(java.lang.Object object, java.lang.Object value) throws IllegalStateException,
                    IllegalArgumentException {
                try {
                    InfoDem target = (InfoDem) object;
                    target.setModRps((globaz.phenix.mapping.demande.castor.types.ModRpsType) value);
                } catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
        });
        desc.setHandler(new org.exolab.castor.xml.handlers.EnumFieldHandler(
                globaz.phenix.mapping.demande.castor.types.ModRpsType.class, handler));
        desc.setImmutable(true);
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);

        // -- validation code for: _modRps
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { // -- local scope
        }
        desc.setValidator(fieldValidator);
    } // -- globaz.phenix.mapping.demande.castor.InfoDemDescriptor()

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
        return globaz.phenix.mapping.demande.castor.InfoDem.class;
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
