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
 * Ayant droit
 * 
 * <p>
 * Java class for LeistungsberechtigtePerson_Type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LeistungsberechtigtePerson_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IstDerAnspruchgeber">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;maxInclusive value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Versichertennummer" type="{http://www.zas.admin.ch/RC}Versichertennummer11oder13Stellen_Type" minOccurs="0"/>
 *         &lt;element name="VersichertenAngaben" type="{http://www.zas.admin.ch/RC}TVersicherteAngabenFakultativ" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LeistungsberechtigtePerson_Type", propOrder = { "istDerAnspruchgeber", "versichertennummer",
        "versichertenAngaben" })
public class LeistungsberechtigtePersonType {

    @XmlElement(name = "IstDerAnspruchgeber")
    protected int istDerAnspruchgeber;
    @XmlElement(name = "Versichertennummer")
    protected String versichertennummer;
    @XmlElement(name = "VersichertenAngaben")
    protected TVersicherteAngabenFakultativ versichertenAngaben;

    /**
     * Gets the value of the istDerAnspruchgeber property.
     * 
     */
    public int getIstDerAnspruchgeber() {
        return istDerAnspruchgeber;
    }

    /**
     * Sets the value of the istDerAnspruchgeber property.
     * 
     */
    public void setIstDerAnspruchgeber(int value) {
        istDerAnspruchgeber = value;
    }

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

    /**
     * Gets the value of the versichertenAngaben property.
     * 
     * @return
     *         possible object is {@link TVersicherteAngabenFakultativ }
     * 
     */
    public TVersicherteAngabenFakultativ getVersichertenAngaben() {
        return versichertenAngaben;
    }

    /**
     * Sets the value of the versichertenAngaben property.
     * 
     * @param value
     *            allowed object is {@link TVersicherteAngabenFakultativ }
     * 
     */
    public void setVersichertenAngaben(TVersicherteAngabenFakultativ value) {
        versichertenAngaben = value;
    }

}
