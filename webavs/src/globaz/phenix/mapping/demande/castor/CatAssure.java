/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: CatAssure.java,v 1.1 2006/06/01 11:02:23 vch Exp $
 */

package globaz.phenix.mapping.demande.castor;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Catégorie de l'assuré. L'une des 4 valeurs : "PCI" = Indépendants ou exploitants "PSA" = Sans activité lucrative
 * "SAL" = Salariés affiliés indépendants "AGR" = Collaborateurs agricoles Canton ZH et SG: BeitrArt
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:23 $
 */
public class CatAssure implements java.io.Serializable {

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
        return Unmarshaller.unmarshal(globaz.phenix.mapping.demande.castor.CatAssure.class, reader);
    } // -- java.lang.Object unmarshal(java.io.Reader)

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    // -----------/
    // - Methods -/
    // -----------/

    public CatAssure() {
        super();
        setContent("");
    } // -- globaz.phenix.mapping.demande.castor.CatAssure()

    /**
     * Returns the value of field 'content'. The field 'content' has the following description: internal content storage
     * 
     * @return String
     * @return the value of field 'content'.
     */
    public java.lang.String getContent() {
        return _content;
    } // -- java.lang.String getContent()

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
     * Sets the value of field 'content'. The field 'content' has the following description: internal content storage
     * 
     * @param content
     *            the value of field 'content'.
     */
    public void setContent(java.lang.String content) {
        _content = content;
    } // -- void setContent(java.lang.String)

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } // -- void validate()

}
