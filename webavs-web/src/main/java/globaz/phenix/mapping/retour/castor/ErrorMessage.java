/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.9M1</a>, using an XML Schema.
 * $Id: ErrorMessage.java,v 1.1 2006/06/01 11:02:17 vch Exp $
 */

package globaz.phenix.mapping.retour.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import java.util.Vector;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Contient toues les messages de génération en cas d'erreur
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:17 $
 */
public class ErrorMessage implements java.io.Serializable {

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
     * @return ErrorMessage
     */
    public static globaz.phenix.mapping.retour.castor.ErrorMessage unmarshal(java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (globaz.phenix.mapping.retour.castor.ErrorMessage) Unmarshaller.unmarshal(
                globaz.phenix.mapping.retour.castor.ErrorMessage.class, reader);
    } // -- globaz.phenix.mapping.retour.castor.ErrorMessage

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Identifiant du message
     */
    private java.util.Vector _errorItemList;

    // -----------/
    // - Methods -/
    // -----------/

    public ErrorMessage() {
        super();
        _errorItemList = new Vector();
    } // -- globaz.phenix.mapping.retour.castor.ErrorMessage()

    /**
     * Method addErrorItem
     * 
     * 
     * 
     * @param vErrorItem
     */
    public void addErrorItem(globaz.phenix.mapping.retour.castor.ErrorItem vErrorItem)
            throws java.lang.IndexOutOfBoundsException {
        _errorItemList.addElement(vErrorItem);
    } // -- void addErrorItem(globaz.phenix.mapping.retour.castor.ErrorItem)

    // globaz.phenix.mapping.retour.castor.ErrorItem)

    /**
     * Method addErrorItem
     * 
     * 
     * 
     * @param index
     * @param vErrorItem
     */
    public void addErrorItem(int index, globaz.phenix.mapping.retour.castor.ErrorItem vErrorItem)
            throws java.lang.IndexOutOfBoundsException {
        _errorItemList.insertElementAt(vErrorItem, index);
    } // -- void addErrorItem(int,

    /**
     * Method enumerateErrorItem
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateErrorItem() {
        return _errorItemList.elements();
    } // -- java.util.Enumeration enumerateErrorItem()

    /**
     * Method getErrorItem
     * 
     * 
     * 
     * @return ErrorItem
     */
    public globaz.phenix.mapping.retour.castor.ErrorItem[] getErrorItem() {
        int size = _errorItemList.size();
        globaz.phenix.mapping.retour.castor.ErrorItem[] mArray = new globaz.phenix.mapping.retour.castor.ErrorItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (globaz.phenix.mapping.retour.castor.ErrorItem) _errorItemList.elementAt(index);
        }
        return mArray;
    } // -- globaz.phenix.mapping.retour.castor.ErrorItem[] getErrorItem()

    /**
     * Method getErrorItem
     * 
     * 
     * 
     * @param index
     * @return ErrorItem
     */
    public globaz.phenix.mapping.retour.castor.ErrorItem getErrorItem(int index)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _errorItemList.size())) {
            throw new IndexOutOfBoundsException("getErrorItem: Index value '" + index + "' not in range [0.."
                    + _errorItemList.size() + "]");
        }

        return (globaz.phenix.mapping.retour.castor.ErrorItem) _errorItemList.elementAt(index);
    } // -- globaz.phenix.mapping.retour.castor.ErrorItem getErrorItem(int)

    /**
     * Method getErrorItemCount
     * 
     * 
     * 
     * @return int
     */
    public int getErrorItemCount() {
        return _errorItemList.size();
    } // -- int getErrorItemCount()

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
     * Method removeAllErrorItem
     * 
     */
    public void removeAllErrorItem() {
        _errorItemList.removeAllElements();
    } // -- void removeAllErrorItem()

    /**
     * Method removeErrorItem
     * 
     * 
     * 
     * @param index
     * @return ErrorItem
     */
    public globaz.phenix.mapping.retour.castor.ErrorItem removeErrorItem(int index) {
        java.lang.Object obj = _errorItemList.elementAt(index);
        _errorItemList.removeElementAt(index);
        return (globaz.phenix.mapping.retour.castor.ErrorItem) obj;
    } // -- globaz.phenix.mapping.retour.castor.ErrorItem removeErrorItem(int)

    // globaz.phenix.mapping.retour.castor.ErrorItem)

    /**
     * Method setErrorItem
     * 
     * 
     * 
     * @param errorItemArray
     */
    public void setErrorItem(globaz.phenix.mapping.retour.castor.ErrorItem[] errorItemArray) {
        // -- copy array
        _errorItemList.removeAllElements();
        for (int i = 0; i < errorItemArray.length; i++) {
            _errorItemList.addElement(errorItemArray[i]);
        }
    } // -- void setErrorItem(globaz.phenix.mapping.retour.castor.ErrorItem)

    /**
     * Method setErrorItem
     * 
     * 
     * 
     * @param index
     * @param vErrorItem
     */
    public void setErrorItem(int index, globaz.phenix.mapping.retour.castor.ErrorItem vErrorItem)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _errorItemList.size())) {
            throw new IndexOutOfBoundsException("setErrorItem: Index value '" + index + "' not in range [0.."
                    + _errorItemList.size() + "]");
        }
        _errorItemList.setElementAt(vErrorItem, index);
    } // -- void setErrorItem(int,

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
