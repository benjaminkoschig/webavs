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
 * Java class for Gutschriften10_Type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Gutschriften10_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AnzahlErziehungsgutschrift" type="{http://www.zas.admin.ch/RC}AnzahlErziehungsgutschrift10_Type" minOccurs="0"/>
 *         &lt;element name="AnzahlBetreuungsgutschrift" type="{http://www.zas.admin.ch/RC}AnzahlBetreuungsgutschrift10_Type" minOccurs="0"/>
 *         &lt;element name="AnzahlUebergangsgutschrift" type="{http://www.zas.admin.ch/RC}AnzahlUebergangsgutschrift10_Type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Gutschriften10_Type", propOrder = { "anzahlErziehungsgutschrift", "anzahlBetreuungsgutschrift",
        "anzahlUebergangsgutschrift" })
public class Gutschriften10Type {

    @XmlElement(name = "AnzahlErziehungsgutschrift")
    protected BigDecimal anzahlErziehungsgutschrift;
    @XmlElement(name = "AnzahlBetreuungsgutschrift")
    protected BigDecimal anzahlBetreuungsgutschrift;
    @XmlElement(name = "AnzahlUebergangsgutschrift")
    protected BigDecimal anzahlUebergangsgutschrift;

    /**
     * Gets the value of the anzahlErziehungsgutschrift property.
     * 
     * @return
     *         possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getAnzahlErziehungsgutschrift() {
        return anzahlErziehungsgutschrift;
    }

    /**
     * Sets the value of the anzahlErziehungsgutschrift property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setAnzahlErziehungsgutschrift(BigDecimal value) {
        anzahlErziehungsgutschrift = value;
    }

    /**
     * Gets the value of the anzahlBetreuungsgutschrift property.
     * 
     * @return
     *         possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getAnzahlBetreuungsgutschrift() {
        return anzahlBetreuungsgutschrift;
    }

    /**
     * Sets the value of the anzahlBetreuungsgutschrift property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setAnzahlBetreuungsgutschrift(BigDecimal value) {
        anzahlBetreuungsgutschrift = value;
    }

    /**
     * Gets the value of the anzahlUebergangsgutschrift property.
     * 
     * @return
     *         possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getAnzahlUebergangsgutschrift() {
        return anzahlUebergangsgutschrift;
    }

    /**
     * Sets the value of the anzahlUebergangsgutschrift property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setAnzahlUebergangsgutschrift(BigDecimal value) {
        anzahlUebergangsgutschrift = value;
    }

}
