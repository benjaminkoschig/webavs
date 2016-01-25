/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: IdnDem.java,v 1.1 2006/06/01 11:02:21 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Identification de la demande
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:21 $
 */
public class IdnDem implements java.io.Serializable {

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
        return Unmarshaller.unmarshal(globaz.phenix.mapping.demande.castor.IdnDem.class, reader);
    } // -- java.lang.Object unmarshal(java.io.Reader)

    /**
     * Date de la demande
     */
    private java.lang.String _datDem;

    /**
     * Adresse Email de la personne de contact pour la demande
     */
    private java.lang.String _emailContact;

    /**
     * Nom de la personne de contact pour la demande
     */
    private java.lang.String _nomContact;

    /**
     * Numéro de la demande
     */
    private java.lang.String _numDem;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Numéro de téléphone de la personne de contact pour la demande
     */
    private java.lang.String _telContact;

    // -----------/
    // - Methods -/
    // -----------/

    public IdnDem() {
        super();
    } // -- globaz.phenix.mapping.demande.castor.IdnDem()

    /**
     * Returns the value of field 'datDem'. The field 'datDem' has the following description: Date de la demande
     * 
     * @return String
     * @return the value of field 'datDem'.
     */
    public java.lang.String getDatDem() {
        return _datDem;
    } // -- java.lang.String getDatDem()

    /**
     * Returns the value of field 'emailContact'. The field 'emailContact' has the following description: Adresse Email
     * de la personne de contact pour la demande
     * 
     * @return String
     * @return the value of field 'emailContact'.
     */
    public java.lang.String getEmailContact() {
        return _emailContact;
    } // -- java.lang.String getEmailContact()

    /**
     * Returns the value of field 'nomContact'. The field 'nomContact' has the following description: Nom de la personne
     * de contact pour la demande
     * 
     * @return String
     * @return the value of field 'nomContact'.
     */
    public java.lang.String getNomContact() {
        return _nomContact;
    } // -- java.lang.String getNomContact()

    /**
     * Returns the value of field 'numDem'. The field 'numDem' has the following description: Numéro de la demande
     * 
     * @return String
     * @return the value of field 'numDem'.
     */
    public java.lang.String getNumDem() {
        return _numDem;
    } // -- java.lang.String getNumDem()

    /**
     * Returns the value of field 'telContact'. The field 'telContact' has the following description: Numéro de
     * téléphone de la personne de contact pour la demande
     * 
     * @return String
     * @return the value of field 'telContact'.
     */
    public java.lang.String getTelContact() {
        return _telContact;
    } // -- java.lang.String getTelContact()

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
     * Sets the value of field 'datDem'. The field 'datDem' has the following description: Date de la demande
     * 
     * @param datDem
     *            the value of field 'datDem'.
     */
    public void setDatDem(java.lang.String datDem) {
        _datDem = datDem;
    } // -- void setDatDem(java.lang.String)

    /**
     * Sets the value of field 'emailContact'. The field 'emailContact' has the following description: Adresse Email de
     * la personne de contact pour la demande
     * 
     * @param emailContact
     *            the value of field 'emailContact'.
     */
    public void setEmailContact(java.lang.String emailContact) {
        _emailContact = emailContact;
    } // -- void setEmailContact(java.lang.String)

    /**
     * Sets the value of field 'nomContact'. The field 'nomContact' has the following description: Nom de la personne de
     * contact pour la demande
     * 
     * @param nomContact
     *            the value of field 'nomContact'.
     */
    public void setNomContact(java.lang.String nomContact) {
        _nomContact = nomContact;
    } // -- void setNomContact(java.lang.String)

    /**
     * Sets the value of field 'numDem'. The field 'numDem' has the following description: Numéro de la demande
     * 
     * @param numDem
     *            the value of field 'numDem'.
     */
    public void setNumDem(java.lang.String numDem) {
        _numDem = numDem;
    } // -- void setNumDem(java.lang.String)

    /**
     * Sets the value of field 'telContact'. The field 'telContact' has the following description: Numéro de
     * téléphone de la personne de contact pour la demande
     * 
     * @param telContact
     *            the value of field 'telContact'.
     */
    public void setTelContact(java.lang.String telContact) {
        _telContact = telContact;
    } // -- void setTelContact(java.lang.String)

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()

}
