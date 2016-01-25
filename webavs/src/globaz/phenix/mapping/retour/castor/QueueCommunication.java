/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.9M1</a>, using an XML Schema.
 * $Id: QueueCommunication.java,v 1.1 2006/06/01 11:02:15 vch Exp $
 */

package globaz.phenix.mapping.retour.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Queue de la communication de réception du fisc
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:15 $
 */
public class QueueCommunication implements java.io.Serializable {

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
     * @return QueueCommunication
     */
    public static globaz.phenix.mapping.retour.castor.QueueCommunication unmarshal(java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (globaz.phenix.mapping.retour.castor.QueueCommunication) Unmarshaller.unmarshal(
                globaz.phenix.mapping.retour.castor.QueueCommunication.class, reader);
    } // -- globaz.phenix.mapping.retour.castor.QueueCommunication

    /**
     * keeps track of state for field: _nbreCommNOK
     */
    private boolean _has_nbreCommNOK;

    /**
     * keeps track of state for field: _nbreCommOK
     */
    private boolean _has_nbreCommOK;

    /**
     * Nombre de communication en erreur
     */
    private int _nbreCommNOK;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Nombre de commnucations ok
     */
    private int _nbreCommOK;

    // -----------/
    // - Methods -/
    // -----------/

    public QueueCommunication() {
        super();
    } // -- globaz.phenix.mapping.retour.castor.QueueCommunication()

    /**
     * Method deleteNbreCommNOK
     * 
     */
    public void deleteNbreCommNOK() {
        _has_nbreCommNOK = false;
    } // -- void deleteNbreCommNOK()

    /**
     * Method deleteNbreCommOK
     * 
     */
    public void deleteNbreCommOK() {
        _has_nbreCommOK = false;
    } // -- void deleteNbreCommOK()

    /**
     * Returns the value of field 'nbreCommNOK'. The field 'nbreCommNOK' has the following description: Nombre de
     * communication en erreur
     * 
     * @return int
     * @return the value of field 'nbreCommNOK'.
     */
    public int getNbreCommNOK() {
        return _nbreCommNOK;
    } // -- int getNbreCommNOK()

    /**
     * Returns the value of field 'nbreCommOK'. The field 'nbreCommOK' has the following description: Nombre de
     * commnucations ok
     * 
     * @return int
     * @return the value of field 'nbreCommOK'.
     */
    public int getNbreCommOK() {
        return _nbreCommOK;
    } // -- int getNbreCommOK()

    /**
     * Method hasNbreCommNOK
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasNbreCommNOK() {
        return _has_nbreCommNOK;
    } // -- boolean hasNbreCommNOK()

    /**
     * Method hasNbreCommOK
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasNbreCommOK() {
        return _has_nbreCommOK;
    } // -- boolean hasNbreCommOK()

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
     * Sets the value of field 'nbreCommNOK'. The field 'nbreCommNOK' has the following description: Nombre de
     * communication en erreur
     * 
     * @param nbreCommNOK
     *            the value of field 'nbreCommNOK'.
     */
    public void setNbreCommNOK(int nbreCommNOK) {
        _nbreCommNOK = nbreCommNOK;
        _has_nbreCommNOK = true;
    } // -- void setNbreCommNOK(int)

    /**
     * Sets the value of field 'nbreCommOK'. The field 'nbreCommOK' has the following description: Nombre de
     * commnucations ok
     * 
     * @param nbreCommOK
     *            the value of field 'nbreCommOK'.
     */
    public void setNbreCommOK(int nbreCommOK) {
        _nbreCommOK = nbreCommOK;
        _has_nbreCommOK = true;
    } // -- void setNbreCommOK(int)

    // unmarshal(java.io.Reader)

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()

}
