/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: EnteteDemande.java,v 1.1 2006/06/01 11:02:23 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Entête de la demande Canton de VD: cet élément doit être renseignée pour une demande à ce canton Canton de ZH
 * et SG: inutile
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:23 $
 */
public class EnteteDemande implements java.io.Serializable {

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
        return Unmarshaller.unmarshal(globaz.phenix.mapping.demande.castor.EnteteDemande.class, reader);
    } // -- java.lang.Object unmarshal(java.io.Reader)

    /**
     * Identification de la Caisse
     */
    private globaz.phenix.mapping.demande.castor.IdnCaiAvs _idnCaiAvs;

    /**
     * Identification de la demande
     */
    private globaz.phenix.mapping.demande.castor.IdnDem _idnDem;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Information sur la demande
     */
    private globaz.phenix.mapping.demande.castor.InfoDem _infoDem;

    // -----------/
    // - Methods -/
    // -----------/

    public EnteteDemande() {
        super();
    } // -- globaz.phenix.mapping.demande.castor.EnteteDemande()

    /**
     * Returns the value of field 'idnCaiAvs'. The field 'idnCaiAvs' has the following description: Identification de la
     * Caisse
     * 
     * @return IdnCaiAvs
     * @return the value of field 'idnCaiAvs'.
     */
    public globaz.phenix.mapping.demande.castor.IdnCaiAvs getIdnCaiAvs() {
        return _idnCaiAvs;
    } // -- globaz.phenix.mapping.demande.castor.IdnCaiAvs getIdnCaiAvs()

    /**
     * Returns the value of field 'idnDem'. The field 'idnDem' has the following description: Identification de la
     * demande
     * 
     * @return IdnDem
     * @return the value of field 'idnDem'.
     */
    public globaz.phenix.mapping.demande.castor.IdnDem getIdnDem() {
        return _idnDem;
    } // -- globaz.phenix.mapping.demande.castor.IdnDem getIdnDem()

    /**
     * Returns the value of field 'infoDem'. The field 'infoDem' has the following description: Information sur la
     * demande
     * 
     * @return InfoDem
     * @return the value of field 'infoDem'.
     */
    public globaz.phenix.mapping.demande.castor.InfoDem getInfoDem() {
        return _infoDem;
    } // -- globaz.phenix.mapping.demande.castor.InfoDem getInfoDem()

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
     * Sets the value of field 'idnCaiAvs'. The field 'idnCaiAvs' has the following description: Identification de la
     * Caisse
     * 
     * @param idnCaiAvs
     *            the value of field 'idnCaiAvs'.
     */
    public void setIdnCaiAvs(globaz.phenix.mapping.demande.castor.IdnCaiAvs idnCaiAvs) {
        _idnCaiAvs = idnCaiAvs;
    } // -- void setIdnCaiAvs(globaz.phenix.mapping.demande.castor.IdnCaiAvs)

    /**
     * Sets the value of field 'idnDem'. The field 'idnDem' has the following description: Identification de la demande
     * 
     * @param idnDem
     *            the value of field 'idnDem'.
     */
    public void setIdnDem(globaz.phenix.mapping.demande.castor.IdnDem idnDem) {
        _idnDem = idnDem;
    } // -- void setIdnDem(globaz.phenix.mapping.demande.castor.IdnDem)

    /**
     * Sets the value of field 'infoDem'. The field 'infoDem' has the following description: Information sur la demande
     * 
     * @param infoDem
     *            the value of field 'infoDem'.
     */
    public void setInfoDem(globaz.phenix.mapping.demande.castor.InfoDem infoDem) {
        _infoDem = infoDem;
    } // -- void setInfoDem(globaz.phenix.mapping.demande.castor.InfoDem)

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()

}
