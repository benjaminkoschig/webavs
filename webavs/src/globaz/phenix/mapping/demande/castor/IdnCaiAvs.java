/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: IdnCaiAvs.java,v 1.1 2006/06/01 11:02:20 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Identification de la Caisse
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:20 $
 */
public class IdnCaiAvs implements java.io.Serializable {

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
        return Unmarshaller.unmarshal(globaz.phenix.mapping.demande.castor.IdnCaiAvs.class, reader);
    } // -- java.lang.Object unmarshal(java.io.Reader)

    /**
     * Adresse de la Caisse Avs
     */
    private globaz.phenix.mapping.demande.castor.AdrCaiAvs _adrCaiAvs;

    /**
     * Nom de la Caisse Avs
     */
    private java.lang.String _nomCaiAvs;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Numéro de la Caisse Avs
     */
    private java.lang.String _numCaiAvs;

    // -----------/
    // - Methods -/
    // -----------/

    public IdnCaiAvs() {
        super();
    } // -- globaz.phenix.mapping.demande.castor.IdnCaiAvs()

    /**
     * Returns the value of field 'adrCaiAvs'. The field 'adrCaiAvs' has the following description: Adresse de la Caisse
     * Avs
     * 
     * @return AdrCaiAvs
     * @return the value of field 'adrCaiAvs'.
     */
    public globaz.phenix.mapping.demande.castor.AdrCaiAvs getAdrCaiAvs() {
        return _adrCaiAvs;
    } // -- globaz.phenix.mapping.demande.castor.AdrCaiAvs getAdrCaiAvs()

    /**
     * Returns the value of field 'nomCaiAvs'. The field 'nomCaiAvs' has the following description: Nom de la Caisse Avs
     * 
     * @return String
     * @return the value of field 'nomCaiAvs'.
     */
    public java.lang.String getNomCaiAvs() {
        return _nomCaiAvs;
    } // -- java.lang.String getNomCaiAvs()

    /**
     * Returns the value of field 'numCaiAvs'. The field 'numCaiAvs' has the following description: Numéro de la Caisse
     * Avs
     * 
     * @return String
     * @return the value of field 'numCaiAvs'.
     */
    public java.lang.String getNumCaiAvs() {
        return _numCaiAvs;
    } // -- java.lang.String getNumCaiAvs()

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
     * Sets the value of field 'adrCaiAvs'. The field 'adrCaiAvs' has the following description: Adresse de la Caisse
     * Avs
     * 
     * @param adrCaiAvs
     *            the value of field 'adrCaiAvs'.
     */
    public void setAdrCaiAvs(globaz.phenix.mapping.demande.castor.AdrCaiAvs adrCaiAvs) {
        _adrCaiAvs = adrCaiAvs;
    } // -- void setAdrCaiAvs(globaz.phenix.mapping.demande.castor.AdrCaiAvs)

    /**
     * Sets the value of field 'nomCaiAvs'. The field 'nomCaiAvs' has the following description: Nom de la Caisse Avs
     * 
     * @param nomCaiAvs
     *            the value of field 'nomCaiAvs'.
     */
    public void setNomCaiAvs(java.lang.String nomCaiAvs) {
        _nomCaiAvs = nomCaiAvs;
    } // -- void setNomCaiAvs(java.lang.String)

    /**
     * Sets the value of field 'numCaiAvs'. The field 'numCaiAvs' has the following description: Numéro de la Caisse
     * Avs
     * 
     * @param numCaiAvs
     *            the value of field 'numCaiAvs'.
     */
    public void setNumCaiAvs(java.lang.String numCaiAvs) {
        _numCaiAvs = numCaiAvs;
    } // -- void setNumCaiAvs(java.lang.String)

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()

}
