/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: ModRpsType.java,v 1.1 2006/06/01 11:02:31 vch Exp $
 */

package globaz.phenix.mapping.demande.castor.types;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import java.util.Hashtable;

/**
 * Class ModRpsType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:31 $
 */
public class ModRpsType implements java.io.Serializable {

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * Field _memberTable
     */
    private static java.util.Hashtable _memberTable = ModRpsType.init();

    /**
     * The instance of the PAP type
     */
    public static final ModRpsType PAP = new ModRpsType(ModRpsType.PAP_TYPE, "PAP");

    /**
     * The PAP type
     */
    public static final int PAP_TYPE = 0;

    /**
     * The instance of the XML type
     */
    public static final ModRpsType XML = new ModRpsType(ModRpsType.XML_TYPE, "XML");

    /**
     * The XML type
     */
    public static final int XML_TYPE = 1;

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of ModRpsType
     * 
     * @return Enumeration
     */
    public static java.util.Enumeration enumerate() {
        return ModRpsType._memberTable.elements();
    } // -- java.util.Enumeration enumerate()

    /**
     * Method init
     * 
     * 
     * 
     * @return Hashtable
     */
    private static java.util.Hashtable init() {
        Hashtable members = new Hashtable();
        members.put("PAP", ModRpsType.PAP);
        members.put("XML", ModRpsType.XML);
        return members;
    } // -- java.util.Hashtable init()

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Method valueOf
     * 
     * Returns a new ModRpsType based on the given String value.
     * 
     * @param string
     * @return ModRpsType
     */
    public static globaz.phenix.mapping.demande.castor.types.ModRpsType valueOf(java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = ModRpsType._memberTable.get(string);
        }
        if (obj == null) {
            String err = "'" + string + "' is not a valid ModRpsType";
            throw new IllegalArgumentException(err);
        }
        return (ModRpsType) obj;
    } // -- globaz.phenix.mapping.demande.castor.types.ModRpsType

    // valueOf(java.lang.String)

    // java.lang.String)

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;

    /**
     * Field type
     */
    private int type = -1;

    private ModRpsType(int type, java.lang.String value) {
        super();
        this.type = type;
        stringValue = value;
    } // -- globaz.phenix.mapping.demande.castor.types.ModRpsType(int,

    /**
     * Method getType
     * 
     * Returns the type of this ModRpsType
     * 
     * @return int
     */
    public int getType() {
        return type;
    } // -- int getType()

    /**
     * Method readResolve
     * 
     * will be called during deserialization to replace the deserialized object with the correct constant instance. <br/>
     * 
     * @return Object
     */
    private java.lang.Object readResolve() {
        return ModRpsType.valueOf(stringValue);
    } // -- java.lang.Object readResolve()

    /**
     * Method toString
     * 
     * Returns the String representation of this ModRpsType
     * 
     * @return String
     */
    @Override
    public java.lang.String toString() {
        return stringValue;
    } // -- java.lang.String toString()

}
