//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in
// JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.06.25 at 01:29:35 PM CEST
//

package ch.admin.zas.rc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * Code application 11: entr�e dans l'AVS/AI d'un assur�
 * d�j� dans le registre central des assur�s
 * 
 * 
 * <p>
 * Java class for VAIKMeldungEintrittAHVIV_Type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VAIKMeldungEintrittAHVIV_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.zas.admin.ch/RC}TKopfMeldung">
 *       &lt;sequence>
 *         &lt;element name="Versichertennummer" type="{http://www.zas.admin.ch/RC}Versichertennummer11oder13Stellen_Type"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VAIKMeldungEintrittAHVIV_Type", propOrder = { "versichertennummer" })
public class VAIKMeldungEintrittAHVIVType extends TKopfMeldung {

    @XmlElement(name = "Versichertennummer", required = true)
    protected String versichertennummer;

    /**
     * Gets the value of the versichertennummer property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getVersichertennummer() {
        return versichertennummer;
    }

    /**
     * Sets the value of the versichertennummer property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setVersichertennummer(String value) {
        versichertennummer = value;
    }

}
