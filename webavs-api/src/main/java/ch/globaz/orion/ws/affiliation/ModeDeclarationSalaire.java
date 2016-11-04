
package ch.globaz.orion.ws.affiliation;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for modeDeclarationSalaire.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="modeDeclarationSalaire">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PUCS"/>
 *     &lt;enumeration value="DAN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "modeDeclarationSalaire")
@XmlEnum
public enum ModeDeclarationSalaire {

    PUCS,
    DAN;

    public String value() {
        return name();
    }

    public static ModeDeclarationSalaire fromValue(String v) {
        return valueOf(v);
    }

}
