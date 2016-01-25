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
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for IVDaten10_WeakType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IVDaten10_WeakType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IVStelle" type="{http://www.zas.admin.ch/RC}IVStelle_weakType" minOccurs="0"/>
 *         &lt;element name="Invaliditaetsgrad" type="{http://www.zas.admin.ch/RC}Invaliditaetsgrad_numType" minOccurs="0"/>
 *         &lt;element name="Gebrechensschluessel" type="{http://www.zas.admin.ch/RC}Gebrechensschluessel_numWeakType" minOccurs="0"/>
 *         &lt;element name="Funktionsausfallcode" type="{http://www.zas.admin.ch/RC}IVFunktionsausfall_numWeakType" minOccurs="0"/>
 *         &lt;element name="DatumVersicherungsfall" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type" minOccurs="0"/>
 *         &lt;element name="IstFruehInvalid" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IVDaten10_WeakType", propOrder = { "ivStelle", "invaliditaetsgrad", "gebrechensschluessel",
        "funktionsausfallcode", "datumVersicherungsfall", "istFruehInvalid" })
public class IVDaten10WeakType {

    @XmlElement(name = "IVStelle")
    protected Integer ivStelle;
    @XmlElement(name = "Invaliditaetsgrad")
    protected Short invaliditaetsgrad;
    @XmlElement(name = "Gebrechensschluessel")
    protected Integer gebrechensschluessel;
    @XmlElement(name = "Funktionsausfallcode")
    protected Short funktionsausfallcode;
    @XmlElement(name = "DatumVersicherungsfall")
    protected XMLGregorianCalendar datumVersicherungsfall;
    @XmlElement(name = "IstFruehInvalid")
    protected Boolean istFruehInvalid;

    /**
     * Gets the value of the ivStelle property.
     * 
     * @return
     *         possible object is {@link Integer }
     * 
     */
    public Integer getIVStelle() {
        return ivStelle;
    }

    /**
     * Sets the value of the ivStelle property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setIVStelle(Integer value) {
        ivStelle = value;
    }

    /**
     * Gets the value of the invaliditaetsgrad property.
     * 
     * @return
     *         possible object is {@link Short }
     * 
     */
    public Short getInvaliditaetsgrad() {
        return invaliditaetsgrad;
    }

    /**
     * Sets the value of the invaliditaetsgrad property.
     * 
     * @param value
     *            allowed object is {@link Short }
     * 
     */
    public void setInvaliditaetsgrad(Short value) {
        invaliditaetsgrad = value;
    }

    /**
     * Gets the value of the gebrechensschluessel property.
     * 
     * @return
     *         possible object is {@link Integer }
     * 
     */
    public Integer getGebrechensschluessel() {
        return gebrechensschluessel;
    }

    /**
     * Sets the value of the gebrechensschluessel property.
     * 
     * @param value
     *            allowed object is {@link Integer }
     * 
     */
    public void setGebrechensschluessel(Integer value) {
        gebrechensschluessel = value;
    }

    /**
     * Gets the value of the funktionsausfallcode property.
     * 
     * @return
     *         possible object is {@link Short }
     * 
     */
    public Short getFunktionsausfallcode() {
        return funktionsausfallcode;
    }

    /**
     * Sets the value of the funktionsausfallcode property.
     * 
     * @param value
     *            allowed object is {@link Short }
     * 
     */
    public void setFunktionsausfallcode(Short value) {
        funktionsausfallcode = value;
    }

    /**
     * Gets the value of the datumVersicherungsfall property.
     * 
     * @return
     *         possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getDatumVersicherungsfall() {
        return datumVersicherungsfall;
    }

    /**
     * Sets the value of the datumVersicherungsfall property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setDatumVersicherungsfall(XMLGregorianCalendar value) {
        datumVersicherungsfall = value;
    }

    /**
     * Gets the value of the istFruehInvalid property.
     * 
     * @return
     *         possible object is {@link Boolean }
     * 
     */
    public Boolean isIstFruehInvalid() {
        return istFruehInvalid;
    }

    /**
     * Sets the value of the istFruehInvalid property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setIstFruehInvalid(Boolean value) {
        istFruehInvalid = value;
    }

}
