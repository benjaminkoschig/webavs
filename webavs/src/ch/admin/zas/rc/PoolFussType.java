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
 * Anwendungsgebiet 99
 * 
 * <p>
 * Java class for PoolFuss_Type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PoolFuss_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Eintragungengesamtzahl" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PoolFuss_Type", propOrder = { "eintragungengesamtzahl" })
public class PoolFussType {

    @XmlElement(name = "Eintragungengesamtzahl")
    protected int eintragungengesamtzahl;

    /**
     * Gets the value of the eintragungengesamtzahl property.
     * 
     */
    public int getEintragungengesamtzahl() {
        return eintragungengesamtzahl;
    }

    /**
     * Sets the value of the eintragungengesamtzahl property.
     * 
     */
    public void setEintragungengesamtzahl(int value) {
        eintragungengesamtzahl = value;
    }

}
