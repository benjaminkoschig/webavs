/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: DemandeCommunicationAvs.java,v 1.1 2006/06/01 11:02:23 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import java.util.Vector;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Description du schéma pour la "Demande de Communication Avs", version 1.2 - 19.07.2004
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:23 $
 */
public class DemandeCommunicationAvs implements java.io.Serializable {

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
        return Unmarshaller.unmarshal(globaz.phenix.mapping.demande.castor.DemandeCommunicationAvs.class, reader);
    } // -- java.lang.Object unmarshal(java.io.Reader)

    /**
     * Assurés pour lesquels la demande est établie
     */
    private java.util.Vector _assureList;

    /**
     * Entête de la demande Canton de VD: cet élément doit être renseignée pour une demande à ce canton Canton de
     * ZH et SG: inutile
     */
    private globaz.phenix.mapping.demande.castor.EnteteDemande _enteteDemande;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Fin de la demande Canton de VD: cet élément doit être renseignée pour une demande à ce canton Canton de ZH
     * et SG: inutile
     */
    private globaz.phenix.mapping.demande.castor.QueueDemande _queueDemande;

    // -----------/
    // - Methods -/
    // -----------/

    public DemandeCommunicationAvs() {
        super();
        _assureList = new Vector();
    } // -- globaz.phenix.mapping.demande.castor.DemandeCommunicationAvs()

    /**
     * Method addAssure
     * 
     * 
     * 
     * @param vAssure
     */
    public void addAssure(globaz.phenix.mapping.demande.castor.Assure vAssure)
            throws java.lang.IndexOutOfBoundsException {
        _assureList.addElement(vAssure);
    } // -- void addAssure(globaz.phenix.mapping.demande.castor.Assure)

    /**
     * Method addAssure
     * 
     * 
     * 
     * @param index
     * @param vAssure
     */
    public void addAssure(int index, globaz.phenix.mapping.demande.castor.Assure vAssure)
            throws java.lang.IndexOutOfBoundsException {
        _assureList.insertElementAt(vAssure, index);
    } // -- void addAssure(int, globaz.phenix.mapping.demande.castor.Assure)

    /**
     * Method enumerateAssure
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateAssure() {
        return _assureList.elements();
    } // -- java.util.Enumeration enumerateAssure()

    /**
     * Method getAssure
     * 
     * 
     * 
     * @return Assure
     */
    public globaz.phenix.mapping.demande.castor.Assure[] getAssure() {
        int size = _assureList.size();
        globaz.phenix.mapping.demande.castor.Assure[] mArray = new globaz.phenix.mapping.demande.castor.Assure[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (globaz.phenix.mapping.demande.castor.Assure) _assureList.elementAt(index);
        }
        return mArray;
    } // -- globaz.phenix.mapping.demande.castor.Assure[] getAssure()

    /**
     * Method getAssure
     * 
     * 
     * 
     * @param index
     * @return Assure
     */
    public globaz.phenix.mapping.demande.castor.Assure getAssure(int index) throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _assureList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (globaz.phenix.mapping.demande.castor.Assure) _assureList.elementAt(index);
    } // -- globaz.phenix.mapping.demande.castor.Assure getAssure(int)

    /**
     * Method getAssureCount
     * 
     * 
     * 
     * @return int
     */
    public int getAssureCount() {
        return _assureList.size();
    } // -- int getAssureCount()

    // getEnteteDemande()

    /**
     * Returns the value of field 'enteteDemande'. The field 'enteteDemande' has the following description: Entête de
     * la demande Canton de VD: cet élément doit être renseignée pour une demande à ce canton Canton de ZH et SG:
     * inutile
     * 
     * @return EnteteDemande
     * @return the value of field 'enteteDemande'.
     */
    public globaz.phenix.mapping.demande.castor.EnteteDemande getEnteteDemande() {
        return _enteteDemande;
    } // -- globaz.phenix.mapping.demande.castor.EnteteDemande

    /**
     * Returns the value of field 'queueDemande'. The field 'queueDemande' has the following description: Fin de la
     * demande Canton de VD: cet élément doit être renseignée pour une demande à ce canton Canton de ZH et SG:
     * inutile
     * 
     * @return QueueDemande
     * @return the value of field 'queueDemande'.
     */
    public globaz.phenix.mapping.demande.castor.QueueDemande getQueueDemande() {
        return _queueDemande;
    } // -- globaz.phenix.mapping.demande.castor.QueueDemande getQueueDemande()

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
     * Method removeAllAssure
     * 
     */
    public void removeAllAssure() {
        _assureList.removeAllElements();
    } // -- void removeAllAssure()

    /**
     * Method removeAssure
     * 
     * 
     * 
     * @param index
     * @return Assure
     */
    public globaz.phenix.mapping.demande.castor.Assure removeAssure(int index) {
        java.lang.Object obj = _assureList.elementAt(index);
        _assureList.removeElementAt(index);
        return (globaz.phenix.mapping.demande.castor.Assure) obj;
    } // -- globaz.phenix.mapping.demande.castor.Assure removeAssure(int)

    /**
     * Method setAssure
     * 
     * 
     * 
     * @param assureArray
     */
    public void setAssure(globaz.phenix.mapping.demande.castor.Assure[] assureArray) {
        // -- copy array
        _assureList.removeAllElements();
        for (int i = 0; i < assureArray.length; i++) {
            _assureList.addElement(assureArray[i]);
        }
    } // -- void setAssure(globaz.phenix.mapping.demande.castor.Assure)

    /**
     * Method setAssure
     * 
     * 
     * 
     * @param index
     * @param vAssure
     */
    public void setAssure(int index, globaz.phenix.mapping.demande.castor.Assure vAssure)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _assureList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _assureList.setElementAt(vAssure, index);
    } // -- void setAssure(int, globaz.phenix.mapping.demande.castor.Assure)

    // setEnteteDemande(globaz.phenix.mapping.demande.castor.EnteteDemande)

    /**
     * Sets the value of field 'enteteDemande'. The field 'enteteDemande' has the following description: Entête de la
     * demande Canton de VD: cet élément doit être renseignée pour une demande à ce canton Canton de ZH et SG:
     * inutile
     * 
     * @param enteteDemande
     *            the value of field 'enteteDemande'.
     */
    public void setEnteteDemande(globaz.phenix.mapping.demande.castor.EnteteDemande enteteDemande) {
        _enteteDemande = enteteDemande;
    } // -- void

    // setQueueDemande(globaz.phenix.mapping.demande.castor.QueueDemande)

    /**
     * Sets the value of field 'queueDemande'. The field 'queueDemande' has the following description: Fin de la demande
     * Canton de VD: cet élément doit être renseignée pour une demande à ce canton Canton de ZH et SG: inutile
     * 
     * @param queueDemande
     *            the value of field 'queueDemande'.
     */
    public void setQueueDemande(globaz.phenix.mapping.demande.castor.QueueDemande queueDemande) {
        _queueDemande = queueDemande;
    } // -- void

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()

}
