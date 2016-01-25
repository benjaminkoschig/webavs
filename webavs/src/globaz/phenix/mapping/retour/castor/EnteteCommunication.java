/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.9M1</a>, using an XML Schema.
 * $Id: EnteteCommunication.java,v 1.1 2006/06/01 11:02:17 vch Exp $
 */

package globaz.phenix.mapping.retour.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Entete de la communication de retour du fisc
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:17 $
 */
public class EnteteCommunication implements java.io.Serializable {

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
     * @return EnteteCommunication
     */
    public static globaz.phenix.mapping.retour.castor.EnteteCommunication unmarshal(java.io.Reader reader)
            throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (globaz.phenix.mapping.retour.castor.EnteteCommunication) Unmarshaller.unmarshal(
                globaz.phenix.mapping.retour.castor.EnteteCommunication.class, reader);
    } // -- globaz.phenix.mapping.retour.castor.EnteteCommunication

    /**
     * Date de réception de la communication, c.-à-d. date à laquelle le fichier original a été créé.
     */
    private java.lang.String _dateReceptionCom;

    /**
     * Date de création du fichier XML de données selon schéma Globaz
     */
    private java.lang.String _dateTransformationCom;

    /**
     * Adresse Email de la personne de contact
     */
    private java.lang.String _EMailCtcCom;

    /**
     * Nombre de commnucations contenu dans ce fichier de réception
     */
    private java.lang.String _nbreReceptionCom;

    /**
     * Nom de la personne de contact
     */
    private java.lang.String _nomCtcCom;

    /**
     * Numéro de la communication de retour du fisc
     */
    private java.lang.String _numReceptionCom;

    /**
     * Numéro de téléphone de la personne de contact
     */
    private java.lang.String _telCtcCom;

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Id de l'utilisateur de l'application WebCotisations
     */
    private java.lang.String _userId;

    // -----------/
    // - Methods -/
    // -----------/

    public EnteteCommunication() {
        super();
    } // -- globaz.phenix.mapping.retour.castor.EnteteCommunication()

    /**
     * Returns the value of field 'dateReceptionCom'. The field 'dateReceptionCom' has the following description: Date
     * de réception de la communication, c.-à-d. date à laquelle le fichier original a été créé.
     * 
     * @return String
     * @return the value of field 'dateReceptionCom'.
     */
    public java.lang.String getDateReceptionCom() {
        return _dateReceptionCom;
    } // -- java.lang.String getDateReceptionCom()

    /**
     * Returns the value of field 'dateTransformationCom'. The field 'dateTransformationCom' has the following
     * description: Date de création du fichier XML de données selon schéma Globaz
     * 
     * @return String
     * @return the value of field 'dateTransformationCom'.
     */
    public java.lang.String getDateTransformationCom() {
        return _dateTransformationCom;
    } // -- java.lang.String getDateTransformationCom()

    /**
     * Returns the value of field 'EMailCtcCom'. The field 'EMailCtcCom' has the following description: Adresse Email de
     * la personne de contact
     * 
     * @return String
     * @return the value of field 'EMailCtcCom'.
     */
    public java.lang.String getEMailCtcCom() {
        return _EMailCtcCom;
    } // -- java.lang.String getEMailCtcCom()

    /**
     * Returns the value of field 'nbreReceptionCom'. The field 'nbreReceptionCom' has the following description: Nombre
     * de commnucations contenu dans ce fichier de réception
     * 
     * @return String
     * @return the value of field 'nbreReceptionCom'.
     */
    public java.lang.String getNbreReceptionCom() {
        return _nbreReceptionCom;
    } // -- java.lang.String getNbreReceptionCom()

    /**
     * Returns the value of field 'nomCtcCom'. The field 'nomCtcCom' has the following description: Nom de la personne
     * de contact
     * 
     * @return String
     * @return the value of field 'nomCtcCom'.
     */
    public java.lang.String getNomCtcCom() {
        return _nomCtcCom;
    } // -- java.lang.String getNomCtcCom()

    /**
     * Returns the value of field 'numReceptionCom'. The field 'numReceptionCom' has the following description: Numéro
     * de la communication de retour du fisc
     * 
     * @return String
     * @return the value of field 'numReceptionCom'.
     */
    public java.lang.String getNumReceptionCom() {
        return _numReceptionCom;
    } // -- java.lang.String getNumReceptionCom()

    /**
     * Returns the value of field 'telCtcCom'. The field 'telCtcCom' has the following description: Numéro de
     * téléphone de la personne de contact
     * 
     * @return String
     * @return the value of field 'telCtcCom'.
     */
    public java.lang.String getTelCtcCom() {
        return _telCtcCom;
    } // -- java.lang.String getTelCtcCom()

    /**
     * Returns the value of field 'userId'. The field 'userId' has the following description: Id de l'utilisateur de
     * l'application WebCotisations
     * 
     * @return String
     * @return the value of field 'userId'.
     */
    public java.lang.String getUserId() {
        return _userId;
    } // -- java.lang.String getUserId()

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
     * Sets the value of field 'dateReceptionCom'. The field 'dateReceptionCom' has the following description: Date de
     * réception de la communication, c.-à-d. date à laquelle le fichier original a été créé.
     * 
     * @param dateReceptionCom
     *            the value of field 'dateReceptionCom'
     */
    public void setDateReceptionCom(java.lang.String dateReceptionCom) {
        _dateReceptionCom = dateReceptionCom;
    } // -- void setDateReceptionCom(java.lang.String)

    /**
     * Sets the value of field 'dateTransformationCom'. The field 'dateTransformationCom' has the following description:
     * Date de création du fichier XML de données selon schéma Globaz
     * 
     * @param dateTransformationCom
     *            the value of field 'dateTransformationCom'.
     */
    public void setDateTransformationCom(java.lang.String dateTransformationCom) {
        _dateTransformationCom = dateTransformationCom;
    } // -- void setDateTransformationCom(java.lang.String)

    /**
     * Sets the value of field 'EMailCtcCom'. The field 'EMailCtcCom' has the following description: Adresse Email de la
     * personne de contact
     * 
     * @param EMailCtcCom
     *            the value of field 'EMailCtcCom'.
     */
    public void setEMailCtcCom(java.lang.String EMailCtcCom) {
        _EMailCtcCom = EMailCtcCom;
    } // -- void setEMailCtcCom(java.lang.String)

    /**
     * Sets the value of field 'nbreReceptionCom'. The field 'nbreReceptionCom' has the following description: Nombre de
     * commnucations contenu dans ce fichier de réception
     * 
     * @param nbreReceptionCom
     *            the value of field 'nbreReceptionCom'
     */
    public void setNbreReceptionCom(java.lang.String nbreReceptionCom) {
        _nbreReceptionCom = nbreReceptionCom;
    } // -- void setNbreReceptionCom(java.lang.String)

    /**
     * Sets the value of field 'nomCtcCom'. The field 'nomCtcCom' has the following description: Nom de la personne de
     * contact
     * 
     * @param nomCtcCom
     *            the value of field 'nomCtcCom'.
     */
    public void setNomCtcCom(java.lang.String nomCtcCom) {
        _nomCtcCom = nomCtcCom;
    } // -- void setNomCtcCom(java.lang.String)

    /**
     * Sets the value of field 'numReceptionCom'. The field 'numReceptionCom' has the following description: Numéro de
     * la communication de retour du fisc
     * 
     * @param numReceptionCom
     *            the value of field 'numReceptionCom'.
     */
    public void setNumReceptionCom(java.lang.String numReceptionCom) {
        _numReceptionCom = numReceptionCom;
    } // -- void setNumReceptionCom(java.lang.String)

    /**
     * Sets the value of field 'telCtcCom'. The field 'telCtcCom' has the following description: Numéro de téléphone
     * de la personne de contact
     * 
     * @param telCtcCom
     *            the value of field 'telCtcCom'.
     */
    public void setTelCtcCom(java.lang.String telCtcCom) {
        _telCtcCom = telCtcCom;
    } // -- void setTelCtcCom(java.lang.String)

    /**
     * Sets the value of field 'userId'. The field 'userId' has the following description: Id de l'utilisateur de
     * l'application WebCotisations
     * 
     * @param userId
     *            the value of field 'userId'.
     */
    public void setUserId(java.lang.String userId) {
        _userId = userId;
    } // -- void setUserId(java.lang.String)

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
