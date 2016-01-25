/*
 * This class was automatically generated with <a href="http://www.castor.org">Castor 0.9.6</a>, using an XML Schema.
 * $Id: DemUrgType.java,v 1.1 2006/06/01 11:02:31 vch Exp $
 */

package globaz.phenix.mapping.demande.castor.types;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import java.util.Hashtable;

/**
 * Class DemUrgType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/06/01 11:02:31 $
 */
public class DemUrgType implements java.io.Serializable {

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
    private static java.util.Hashtable _memberTable = DemUrgType.init();

    /**
     * The instance of the NON type
     */
    public static final DemUrgType NON = new DemUrgType(DemUrgType.NON_TYPE, "NON");

    /**
     * The NON type
     */
    public static final int NON_TYPE = 1;

    /**
     * The instance of the OUI type
     */
    public static final DemUrgType OUI = new DemUrgType(DemUrgType.OUI_TYPE, "OUI");

    /**
     * The OUI type
     */
    public static final int OUI_TYPE = 0;

    /**
     * Method enumerate
     * 
     * Returns an enumeration of all possible instances of DemUrgType
     * 
     * @return Enumeration
     */
    public static java.util.Enumeration enumerate() {
        return DemUrgType._memberTable.elements();
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
        members.put("OUI", DemUrgType.OUI);
        members.put("NON", DemUrgType.NON);
        return members;
    } // -- java.util.Hashtable init()

    // ----------------/
    // - Constructors -/
    // ----------------/

    /**
     * Method valueOf
     * 
     * Returns a new DemUrgType based on the given String value.
     * 
     * @param string
     * @return DemUrgType
     */
    public static globaz.phenix.mapping.demande.castor.types.DemUrgType valueOf(java.lang.String string) {
        java.lang.Object obj = null;
        if (string != null) {
            obj = DemUrgType._memberTable.get(string);
        }
        if (obj == null) {
            String err = "'" + string + "' is not a valid DemUrgType";
            throw new IllegalArgumentException(err);
        }
        return (DemUrgType) obj;
    } // -- globaz.phenix.mapping.demande.castor.types.DemUrgType

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

    private DemUrgType(int type, java.lang.String value) {
        super();
        this.type = type;
        stringValue = value;
    } // -- globaz.phenix.mapping.demande.castor.types.DemUrgType(int,

    /**
     * Method getType
     * 
     * Returns the type of this DemUrgType
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
        return DemUrgType.valueOf(stringValue);
    } // -- java.lang.Object readResolve()

    /**
     * Method toString
     * 
     * Returns the String representation of this DemUrgType
     * 
     * @return String
     */
    @Override
    public java.lang.String toString() {
        return stringValue;
    } // -- java.lang.String toString()

}
