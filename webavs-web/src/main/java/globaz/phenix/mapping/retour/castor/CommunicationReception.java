/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.9M1</a>, using an XML Schema.
 * $Id: CommunicationReception.java,v 1.1 2006/06/01 11:02:17 vch Exp $
 */

package globaz.phenix.mapping.retour.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import java.util.Vector;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Description du fichier XML des communications fiscales de réception. Schéma XSD V1.0
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:17 $
 */
public class CommunicationReception implements java.io.Serializable {

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
     * @return CommunicationReception
     */
    public static globaz.phenix.mapping.retour.castor.CommunicationReception unmarshal(java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (globaz.phenix.mapping.retour.castor.CommunicationReception) Unmarshaller.unmarshal(
                globaz.phenix.mapping.retour.castor.CommunicationReception.class, reader);
    } // -- globaz.phenix.mapping.retour.castor.CommunicationReception

    /**
     * La communication de retour du fisc
     */
    private java.util.Vector _communicationList;

    /**
     * Entete de la communication de retour du fisc
     */
    private globaz.phenix.mapping.retour.castor.EnteteCommunication _enteteCommunication;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Queue de la communication de réception du fisc
     */
    private globaz.phenix.mapping.retour.castor.QueueCommunication _queueCommunication;

    // -----------/
    // - Methods -/
    // -----------/

    public CommunicationReception() {
        super();
        _communicationList = new Vector();
    } // -- globaz.phenix.mapping.retour.castor.CommunicationReception()

    // addCommunication(globaz.phenix.mapping.retour.castor.Communication)

    /**
     * Method addCommunication
     * 
     * 
     * 
     * @param vCommunication
     */
    public void addCommunication(globaz.phenix.mapping.retour.castor.Communication vCommunication)
            throws java.lang.IndexOutOfBoundsException {
        _communicationList.addElement(vCommunication);
    } // -- void

    // globaz.phenix.mapping.retour.castor.Communication)

    /**
     * Method addCommunication
     * 
     * 
     * 
     * @param index
     * @param vCommunication
     */
    public void addCommunication(int index, globaz.phenix.mapping.retour.castor.Communication vCommunication)
            throws java.lang.IndexOutOfBoundsException {
        _communicationList.insertElementAt(vCommunication, index);
    } // -- void addCommunication(int,

    /**
     * Method enumerateCommunication
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateCommunication() {
        return _communicationList.elements();
    } // -- java.util.Enumeration enumerateCommunication()

    // getCommunication(int)

    /**
     * Method getCommunication
     * 
     * 
     * 
     * @return Communication
     */
    public globaz.phenix.mapping.retour.castor.Communication[] getCommunication() {
        int size = _communicationList.size();
        globaz.phenix.mapping.retour.castor.Communication[] mArray = new globaz.phenix.mapping.retour.castor.Communication[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (globaz.phenix.mapping.retour.castor.Communication) _communicationList.elementAt(index);
        }
        return mArray;
    } // -- globaz.phenix.mapping.retour.castor.Communication[]

    // getCommunication()

    /**
     * Method getCommunication
     * 
     * 
     * 
     * @param index
     * @return Communication
     */
    public globaz.phenix.mapping.retour.castor.Communication getCommunication(int index)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _communicationList.size())) {
            throw new IndexOutOfBoundsException("getCommunication: Index value '" + index + "' not in range [0.."
                    + _communicationList.size() + "]");
        }

        return (globaz.phenix.mapping.retour.castor.Communication) _communicationList.elementAt(index);
    } // -- globaz.phenix.mapping.retour.castor.Communication

    /**
     * Method getCommunicationCount
     * 
     * 
     * 
     * @return int
     */
    public int getCommunicationCount() {
        return _communicationList.size();
    } // -- int getCommunicationCount()

    // getEnteteCommunication()

    /**
     * Returns the value of field 'enteteCommunication'. The field 'enteteCommunication' has the following description:
     * Entete de la communication de retour du fisc
     * 
     * @return EnteteCommunication
     * @return the value of field 'enteteCommunication'.
     */
    public globaz.phenix.mapping.retour.castor.EnteteCommunication getEnteteCommunication() {
        return _enteteCommunication;
    } // -- globaz.phenix.mapping.retour.castor.EnteteCommunication

    // getQueueCommunication()

    /**
     * Returns the value of field 'queueCommunication'. The field 'queueCommunication' has the following description:
     * Queue de la communication de réception du fisc
     * 
     * @return QueueCommunication
     * @return the value of field 'queueCommunication'.
     */
    public globaz.phenix.mapping.retour.castor.QueueCommunication getQueueCommunication() {
        return _queueCommunication;
    } // -- globaz.phenix.mapping.retour.castor.QueueCommunication

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
     * Method removeAllCommunication
     * 
     */
    public void removeAllCommunication() {
        _communicationList.removeAllElements();
    } // -- void removeAllCommunication()

    // removeCommunication(int)

    /**
     * Method removeCommunication
     * 
     * 
     * 
     * @param index
     * @return Communication
     */
    public globaz.phenix.mapping.retour.castor.Communication removeCommunication(int index) {
        java.lang.Object obj = _communicationList.elementAt(index);
        _communicationList.removeElementAt(index);
        return (globaz.phenix.mapping.retour.castor.Communication) obj;
    } // -- globaz.phenix.mapping.retour.castor.Communication

    // globaz.phenix.mapping.retour.castor.Communication)

    /**
     * Method setCommunication
     * 
     * 
     * 
     * @param communicationArray
     */
    public void setCommunication(globaz.phenix.mapping.retour.castor.Communication[] communicationArray) {
        // -- copy array
        _communicationList.removeAllElements();
        for (int i = 0; i < communicationArray.length; i++) {
            _communicationList.addElement(communicationArray[i]);
        }
    } // -- void

    // setCommunication(globaz.phenix.mapping.retour.castor.Communication)

    /**
     * Method setCommunication
     * 
     * 
     * 
     * @param index
     * @param vCommunication
     */
    public void setCommunication(int index, globaz.phenix.mapping.retour.castor.Communication vCommunication)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _communicationList.size())) {
            throw new IndexOutOfBoundsException("setCommunication: Index value '" + index + "' not in range [0.."
                    + _communicationList.size() + "]");
        }
        _communicationList.setElementAt(vCommunication, index);
    } // -- void setCommunication(int,

    // setEnteteCommunication(globaz.phenix.mapping.retour.castor.EnteteCommunication)

    /**
     * Sets the value of field 'enteteCommunication'. The field 'enteteCommunication' has the following description:
     * Entete de la communication de retour du fisc
     * 
     * @param enteteCommunication
     *            the value of field 'enteteCommunication'.
     */
    public void setEnteteCommunication(globaz.phenix.mapping.retour.castor.EnteteCommunication enteteCommunication) {
        _enteteCommunication = enteteCommunication;
    } // -- void

    // setQueueCommunication(globaz.phenix.mapping.retour.castor.QueueCommunication)

    /**
     * Sets the value of field 'queueCommunication'. The field 'queueCommunication' has the following description: Queue
     * de la communication de réception du fisc
     * 
     * @param queueCommunication
     *            the value of field 'queueCommunication'.
     */
    public void setQueueCommunication(globaz.phenix.mapping.retour.castor.QueueCommunication queueCommunication) {
        _queueCommunication = queueCommunication;
    } // -- void

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
