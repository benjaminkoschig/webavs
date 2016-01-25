/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: InfoDem.java,v 1.1 2006/06/01 11:02:22 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Information sur la demande
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:22 $
 */
public class InfoDem implements java.io.Serializable {

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
        return Unmarshaller.unmarshal(globaz.phenix.mapping.demande.castor.InfoDem.class, reader);
    } // -- java.lang.Object unmarshal(java.io.Reader)

    /**
     * Demande urgente :OUI ou NON
     */
    private globaz.phenix.mapping.demande.castor.types.DemUrgType _demUrg;

    /**
     * keeps track of state for field: _prdFis
     */
    private boolean _has_prdFis;

    /**
     * Mode de réponse
     */
    private globaz.phenix.mapping.demande.castor.types.ModRpsType _modRps;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * ID301 Période fiscale
     */
    private int _prdFis;

    // -----------/
    // - Methods -/
    // -----------/

    public InfoDem() {
        super();
    } // -- globaz.phenix.mapping.demande.castor.InfoDem()

    /**
     * Method deletePrdFis
     * 
     */
    public void deletePrdFis() {
        _has_prdFis = false;
    } // -- void deletePrdFis()

    /**
     * Returns the value of field 'demUrg'. The field 'demUrg' has the following description: Demande urgente :OUI ou
     * NON
     * 
     * @return DemUrgType
     * @return the value of field 'demUrg'.
     */
    public globaz.phenix.mapping.demande.castor.types.DemUrgType getDemUrg() {
        return _demUrg;
    } // -- globaz.phenix.mapping.demande.castor.types.DemUrgType getDemUrg()

    /**
     * Returns the value of field 'modRps'. The field 'modRps' has the following description: Mode de réponse
     * 
     * @return ModRpsType
     * @return the value of field 'modRps'.
     */
    public globaz.phenix.mapping.demande.castor.types.ModRpsType getModRps() {
        return _modRps;
    } // -- globaz.phenix.mapping.demande.castor.types.ModRpsType getModRps()

    /**
     * Returns the value of field 'prdFis'. The field 'prdFis' has the following description: ID301 Période fiscale
     * 
     * @return int
     * @return the value of field 'prdFis'.
     */
    public int getPrdFis() {
        return _prdFis;
    } // -- int getPrdFis()

    /**
     * Method hasPrdFis
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasPrdFis() {
        return _has_prdFis;
    } // -- boolean hasPrdFis()

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

    // setDemUrg(globaz.phenix.mapping.demande.castor.types.DemUrgType)

    /**
     * Sets the value of field 'demUrg'. The field 'demUrg' has the following description: Demande urgente :OUI ou NON
     * 
     * @param demUrg
     *            the value of field 'demUrg'.
     */
    public void setDemUrg(globaz.phenix.mapping.demande.castor.types.DemUrgType demUrg) {
        _demUrg = demUrg;
    } // -- void

    // setModRps(globaz.phenix.mapping.demande.castor.types.ModRpsType)

    /**
     * Sets the value of field 'modRps'. The field 'modRps' has the following description: Mode de réponse
     * 
     * @param modRps
     *            the value of field 'modRps'.
     */
    public void setModRps(globaz.phenix.mapping.demande.castor.types.ModRpsType modRps) {
        _modRps = modRps;
    } // -- void

    /**
     * Sets the value of field 'prdFis'. The field 'prdFis' has the following description: ID301 Période fiscale
     * 
     * @param prdFis
     *            the value of field 'prdFis'.
     */
    public void setPrdFis(int prdFis) {
        _prdFis = prdFis;
        _has_prdFis = true;
    } // -- void setPrdFis(int)

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()

}
