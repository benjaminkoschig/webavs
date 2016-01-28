/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.9M1</a>, using an XML Schema.
 * $Id: ErrorItem.java,v 1.1 2006/06/01 11:02:17 vch Exp $
 */

package globaz.phenix.mapping.retour.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Identifiant du message
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:17 $
 */
public class ErrorItem implements java.io.Serializable {

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
     * @return ErrorItem
     */
    public static globaz.phenix.mapping.retour.castor.ErrorItem unmarshal(java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (globaz.phenix.mapping.retour.castor.ErrorItem) Unmarshaller.unmarshal(
                globaz.phenix.mapping.retour.castor.ErrorItem.class, reader);
    } // -- globaz.phenix.mapping.retour.castor.ErrorItem

    /**
     * Code de l'erreur
     */
    private int _errorNiveau;

    /**
     * Texte de l'erreur
     */
    private java.lang.String _errorText;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * keeps track of state for field: _errorNiveau
     */
    private boolean _has_errorNiveau;

    // -----------/
    // - Methods -/
    // -----------/

    public ErrorItem() {
        super();
    } // -- globaz.phenix.mapping.retour.castor.ErrorItem()

    /**
     * Method deleteErrorNiveau
     * 
     */
    public void deleteErrorNiveau() {
        _has_errorNiveau = false;
    } // -- void deleteErrorNiveau()

    /**
     * Returns the value of field 'errorNiveau'. The field 'errorNiveau' has the following description: Code de l'erreur
     * 
     * @return int
     * @return the value of field 'errorNiveau'.
     */
    public int getErrorNiveau() {
        return _errorNiveau;
    } // -- int getErrorNiveau()

    /**
     * Returns the value of field 'errorText'. The field 'errorText' has the following description: Texte de l'erreur
     * 
     * @return String
     * @return the value of field 'errorText'.
     */
    public java.lang.String getErrorText() {
        return _errorText;
    } // -- java.lang.String getErrorText()

    /**
     * Method hasErrorNiveau
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasErrorNiveau() {
        return _has_errorNiveau;
    } // -- boolean hasErrorNiveau()

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
     * Sets the value of field 'errorNiveau'. The field 'errorNiveau' has the following description: Code de l'erreur
     * 
     * @param errorNiveau
     *            the value of field 'errorNiveau'.
     */
    public void setErrorNiveau(int errorNiveau) {
        _errorNiveau = errorNiveau;
        _has_errorNiveau = true;
    } // -- void setErrorNiveau(int)

    /**
     * Sets the value of field 'errorText'. The field 'errorText' has the following description: Texte de l'erreur
     * 
     * @param errorText
     *            the value of field 'errorText'.
     */
    public void setErrorText(java.lang.String errorText) {
        _errorText = errorText;
    } // -- void setErrorText(java.lang.String)

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
