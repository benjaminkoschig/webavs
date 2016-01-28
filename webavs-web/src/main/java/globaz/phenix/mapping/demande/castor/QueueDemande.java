/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: QueueDemande.java,v 1.1 2006/06/01 11:02:21 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Fin de la demande Canton de VD: cet élément doit être renseignée pour une demande à ce canton Canton de ZH et
 * SG: inutile
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:21 $
 */
public class QueueDemande implements java.io.Serializable {

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
        return Unmarshaller.unmarshal(globaz.phenix.mapping.demande.castor.QueueDemande.class, reader);
    } // -- java.lang.Object unmarshal(java.io.Reader)

    /**
     * keeps track of state for field: _nbrAssure
     */
    private boolean _has_nbrAssure;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Nombre d'assurés contenus dans la demande
     */
    private int _nbrAssure;

    // -----------/
    // - Methods -/
    // -----------/

    public QueueDemande() {
        super();
    } // -- globaz.phenix.mapping.demande.castor.QueueDemande()

    /**
     * Method deleteNbrAssure
     * 
     */
    public void deleteNbrAssure() {
        _has_nbrAssure = false;
    } // -- void deleteNbrAssure()

    /**
     * Returns the value of field 'nbrAssure'. The field 'nbrAssure' has the following description: Nombre d'assurés
     * contenus dans la demande
     * 
     * @return int
     * @return the value of field 'nbrAssure'.
     */
    public int getNbrAssure() {
        return _nbrAssure;
    } // -- int getNbrAssure()

    /**
     * Method hasNbrAssure
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasNbrAssure() {
        return _has_nbrAssure;
    } // -- boolean hasNbrAssure()

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
     * Sets the value of field 'nbrAssure'. The field 'nbrAssure' has the following description: Nombre d'assurés
     * contenus dans la demande
     * 
     * @param nbrAssure
     *            the value of field 'nbrAssure'.
     */
    public void setNbrAssure(int nbrAssure) {
        _nbrAssure = nbrAssure;
        _has_nbrAssure = true;
    } // -- void setNbrAssure(int)

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()

}
