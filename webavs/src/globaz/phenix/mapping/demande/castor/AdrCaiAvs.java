/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: AdrCaiAvs.java,v 1.1 2006/06/01 11:02:22 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Adresse de la Caisse Avs
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:22 $
 */
public class AdrCaiAvs implements java.io.Serializable {

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Object
     */
    public static java.lang.Object unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException,
            org.exolab.castor.xml.ValidationException {
        return Unmarshaller.unmarshal(globaz.phenix.mapping.demande.castor.AdrCaiAvs.class, reader);
    } // -- java.lang.Object unmarshal(java.io.Reader)

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Field _adrGrp
     */
    private globaz.phenix.mapping.demande.castor.AdrGrp _adrGrp;

    // -----------/
    // - Methods -/
    // -----------/

    public AdrCaiAvs() {
        super();
    } // -- globaz.phenix.mapping.demande.castor.AdrCaiAvs()

    /**
     * Returns the value of field 'adrGrp'.
     * 
     * @return AdrGrp
     * @return the value of field 'adrGrp'.
     */
    public globaz.phenix.mapping.demande.castor.AdrGrp getAdrGrp() {
        return _adrGrp;
    } // -- globaz.phenix.mapping.demande.castor.AdrGrp getAdrGrp()

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } // -- boolean isValid()

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out) throws org.exolab.castor.xml.MarshalException,
            org.exolab.castor.xml.ValidationException {

        Marshaller.marshal(this, out);
    } // -- void marshal(java.io.Writer)

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler) throws java.io.IOException,
            org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {

        Marshaller.marshal(this, handler);
    } // -- void marshal(org.xml.sax.ContentHandler)

    /**
     * Sets the value of field 'adrGrp'.
     * 
     * @param adrGrp
     *            the value of field 'adrGrp'.
     */
    public void setAdrGrp(globaz.phenix.mapping.demande.castor.AdrGrp adrGrp) {
        _adrGrp = adrGrp;
    } // -- void setAdrGrp(globaz.phenix.mapping.demande.castor.AdrGrp)

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()

}
