/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: AdrGrp.java,v 1.1 2006/06/01 11:02:21 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Groupe adresse. Six lignes d'adresse, dont une ligne obligatoire
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:21 $
 */
public class AdrGrp implements java.io.Serializable {

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
        return Unmarshaller.unmarshal(globaz.phenix.mapping.demande.castor.AdrGrp.class, reader);
    } // -- java.lang.Object unmarshal(java.io.Reader)

    /**
     * Ligne d'adresse 1
     */
    private java.lang.String _adrLigne1;

    /**
     * Ligne d'adresse 2
     */
    private java.lang.String _adrLigne2;

    /**
     * Ligne d'adresse 3
     */
    private java.lang.String _adrLigne3;

    /**
     * Ligne d'adresse 4
     */
    private java.lang.String _adrLigne4;

    /**
     * Ligne d'adresse 5
     */
    private java.lang.String _adrLigne5;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Ligne d'adresse 6
     */
    private java.lang.String _adrLigne6;

    // -----------/
    // - Methods -/
    // -----------/

    public AdrGrp() {
        super();
    } // -- globaz.phenix.mapping.demande.castor.AdrGrp()

    /**
     * Returns the value of field 'adrLigne1'. The field 'adrLigne1' has the following description: Ligne d'adresse 1
     * 
     * @return String
     * @return the value of field 'adrLigne1'.
     */
    public java.lang.String getAdrLigne1() {
        return _adrLigne1;
    } // -- java.lang.String getAdrLigne1()

    /**
     * Returns the value of field 'adrLigne2'. The field 'adrLigne2' has the following description: Ligne d'adresse 2
     * 
     * @return String
     * @return the value of field 'adrLigne2'.
     */
    public java.lang.String getAdrLigne2() {
        return _adrLigne2;
    } // -- java.lang.String getAdrLigne2()

    /**
     * Returns the value of field 'adrLigne3'. The field 'adrLigne3' has the following description: Ligne d'adresse 3
     * 
     * @return String
     * @return the value of field 'adrLigne3'.
     */
    public java.lang.String getAdrLigne3() {
        return _adrLigne3;
    } // -- java.lang.String getAdrLigne3()

    /**
     * Returns the value of field 'adrLigne4'. The field 'adrLigne4' has the following description: Ligne d'adresse 4
     * 
     * @return String
     * @return the value of field 'adrLigne4'.
     */
    public java.lang.String getAdrLigne4() {
        return _adrLigne4;
    } // -- java.lang.String getAdrLigne4()

    /**
     * Returns the value of field 'adrLigne5'. The field 'adrLigne5' has the following description: Ligne d'adresse 5
     * 
     * @return String
     * @return the value of field 'adrLigne5'.
     */
    public java.lang.String getAdrLigne5() {
        return _adrLigne5;
    } // -- java.lang.String getAdrLigne5()

    /**
     * Returns the value of field 'adrLigne6'. The field 'adrLigne6' has the following description: Ligne d'adresse 6
     * 
     * @return String
     * @return the value of field 'adrLigne6'.
     */
    public java.lang.String getAdrLigne6() {
        return _adrLigne6;
    } // -- java.lang.String getAdrLigne6()

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
     * Sets the value of field 'adrLigne1'. The field 'adrLigne1' has the following description: Ligne d'adresse 1
     * 
     * @param adrLigne1
     *            the value of field 'adrLigne1'.
     */
    public void setAdrLigne1(java.lang.String adrLigne1) {
        _adrLigne1 = adrLigne1;
    } // -- void setAdrLigne1(java.lang.String)

    /**
     * Sets the value of field 'adrLigne2'. The field 'adrLigne2' has the following description: Ligne d'adresse 2
     * 
     * @param adrLigne2
     *            the value of field 'adrLigne2'.
     */
    public void setAdrLigne2(java.lang.String adrLigne2) {
        _adrLigne2 = adrLigne2;
    } // -- void setAdrLigne2(java.lang.String)

    /**
     * Sets the value of field 'adrLigne3'. The field 'adrLigne3' has the following description: Ligne d'adresse 3
     * 
     * @param adrLigne3
     *            the value of field 'adrLigne3'.
     */
    public void setAdrLigne3(java.lang.String adrLigne3) {
        _adrLigne3 = adrLigne3;
    } // -- void setAdrLigne3(java.lang.String)

    /**
     * Sets the value of field 'adrLigne4'. The field 'adrLigne4' has the following description: Ligne d'adresse 4
     * 
     * @param adrLigne4
     *            the value of field 'adrLigne4'.
     */
    public void setAdrLigne4(java.lang.String adrLigne4) {
        _adrLigne4 = adrLigne4;
    } // -- void setAdrLigne4(java.lang.String)

    /**
     * Sets the value of field 'adrLigne5'. The field 'adrLigne5' has the following description: Ligne d'adresse 5
     * 
     * @param adrLigne5
     *            the value of field 'adrLigne5'.
     */
    public void setAdrLigne5(java.lang.String adrLigne5) {
        _adrLigne5 = adrLigne5;
    } // -- void setAdrLigne5(java.lang.String)

    /**
     * Sets the value of field 'adrLigne6'. The field 'adrLigne6' has the following description: Ligne d'adresse 6
     * 
     * @param adrLigne6
     *            the value of field 'adrLigne6'.
     */
    public void setAdrLigne6(java.lang.String adrLigne6) {
        _adrLigne6 = adrLigne6;
    } // -- void setAdrLigne6(java.lang.String)

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()

}
