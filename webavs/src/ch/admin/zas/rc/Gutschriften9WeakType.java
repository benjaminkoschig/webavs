//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in
// JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.06.25 at 01:29:35 PM CEST
//

package ch.admin.zas.rc;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Gutschriften9_WeakType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Gutschriften9_WeakType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DJEohneErziehungsgutschrift" type="{http://www.zas.admin.ch/RC}BetragFR_Type" minOccurs="0"/>
 *         &lt;element name="AngerechneteErziehungsgutschrift" type="{http://www.zas.admin.ch/RC}BetragFR_Type" minOccurs="0"/>
 *         &lt;element name="AnzahlErziehungsgutschrift" type="{http://www.zas.admin.ch/RC}AnzahlErziehungsgutschrift9_Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Gutschriften9_WeakType", propOrder = { "djEohneErziehungsgutschrift",
        "angerechneteErziehungsgutschrift", "anzahlErziehungsgutschrift" })
public class Gutschriften9WeakType {

    @XmlElement(name = "DJEohneErziehungsgutschrift")
    protected BigDecimal djEohneErziehungsgutschrift;
    @XmlElement(name = "AngerechneteErziehungsgutschrift")
    protected BigDecimal angerechneteErziehungsgutschrift;
    @XmlElement(name = "AnzahlErziehungsgutschrift")
    protected Short anzahlErziehungsgutschrift;

    /**
     * Gets the value of the djEohneErziehungsgutschrift property.
     * 
     * @return
     *         possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getDJEohneErziehungsgutschrift() {
        return djEohneErziehungsgutschrift;
    }

    /**
     * Sets the value of the djEohneErziehungsgutschrift property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setDJEohneErziehungsgutschrift(BigDecimal value) {
        djEohneErziehungsgutschrift = value;
    }

    /**
     * Gets the value of the angerechneteErziehungsgutschrift property.
     * 
     * @return
     *         possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getAngerechneteErziehungsgutschrift() {
        return angerechneteErziehungsgutschrift;
    }

    /**
     * Sets the value of the angerechneteErziehungsgutschrift property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setAngerechneteErziehungsgutschrift(BigDecimal value) {
        angerechneteErziehungsgutschrift = value;
    }

    /**
     * Gets the value of the anzahlErziehungsgutschrift property.
     * 
     * @return
     *         possible object is {@link Short }
     * 
     */
    public Short getAnzahlErziehungsgutschrift() {
        return anzahlErziehungsgutschrift;
    }

    /**
     * Sets the value of the anzahlErziehungsgutschrift property.
     * 
     * @param value
     *            allowed object is {@link Short }
     * 
     */
    public void setAnzahlErziehungsgutschrift(Short value) {
        anzahlErziehungsgutschrift = value;
    }

}
