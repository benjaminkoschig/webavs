//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in
// JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.06.25 at 01:29:35 PM CEST
//

package ch.admin.zas.rc;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 
 * Code application 11: copie ou extrait de CI
 * 
 * 
 * <p>
 * Java class for VAIKMeldungIKKopieAuszug_Type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VAIKMeldungIKKopieAuszug_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.zas.admin.ch/RC}TBasisMeldungMitId">
 *       &lt;sequence>
 *         &lt;element name="temporaer" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="LeistungsberechtigtePerson" type="{http://www.zas.admin.ch/RC}LeistungsberechtigtePerson_Type"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="CHWohnsitzCode" type="{http://www.zas.admin.ch/RC}CHWohnsitzCode_Type" minOccurs="0"/>
 *         &lt;element name="WohnsitzDauer" type="{http://www.zas.admin.ch/RC}AHVPeriodeJM_Type" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VAIKMeldungIKKopieAuszug_Type", propOrder = { "temporaer", "chWohnsitzCode", "wohnsitzDauer" })
public class VAIKMeldungIKKopieAuszugType extends TBasisMeldungMitId {

    protected VAIKMeldungIKKopieAuszugType.Temporaer temporaer;
    @XmlElement(name = "CHWohnsitzCode")
    protected Short chWohnsitzCode;
    @XmlElement(name = "WohnsitzDauer")
    protected List<AHVPeriodeJMType> wohnsitzDauer;

    /**
     * Gets the value of the temporaer property.
     * 
     * @return
     *         possible object is {@link VAIKMeldungIKKopieAuszugType.Temporaer }
     * 
     */
    public VAIKMeldungIKKopieAuszugType.Temporaer getTemporaer() {
        return temporaer;
    }

    /**
     * Sets the value of the temporaer property.
     * 
     * @param value
     *            allowed object is {@link VAIKMeldungIKKopieAuszugType.Temporaer }
     * 
     */
    public void setTemporaer(VAIKMeldungIKKopieAuszugType.Temporaer value) {
        temporaer = value;
    }

    /**
     * Gets the value of the chWohnsitzCode property.
     * 
     * @return
     *         possible object is {@link Short }
     * 
     */
    public Short getCHWohnsitzCode() {
        return chWohnsitzCode;
    }

    /**
     * Sets the value of the chWohnsitzCode property.
     * 
     * @param value
     *            allowed object is {@link Short }
     * 
     */
    public void setCHWohnsitzCode(Short value) {
        chWohnsitzCode = value;
    }

    /**
     * Gets the value of the wohnsitzDauer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the wohnsitzDauer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getWohnsitzDauer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link AHVPeriodeJMType }
     * 
     * 
     */
    public List<AHVPeriodeJMType> getWohnsitzDauer() {
        if (wohnsitzDauer == null) {
            wohnsitzDauer = new ArrayList<AHVPeriodeJMType>();
        }
        return wohnsitzDauer;
    }

    /**
     * <p>
     * Java class for anonymous complex type.
     * 
     * <p>
     * The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="LeistungsberechtigtePerson" type="{http://www.zas.admin.ch/RC}LeistungsberechtigtePerson_Type"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "leistungsberechtigtePerson" })
    public static class Temporaer {

        @XmlElement(name = "LeistungsberechtigtePerson", required = true)
        protected LeistungsberechtigtePersonType leistungsberechtigtePerson;

        /**
         * Gets the value of the leistungsberechtigtePerson property.
         * 
         * @return
         *         possible object is {@link LeistungsberechtigtePersonType }
         * 
         */
        public LeistungsberechtigtePersonType getLeistungsberechtigtePerson() {
            return leistungsberechtigtePerson;
        }

        /**
         * Sets the value of the leistungsberechtigtePerson property.
         * 
         * @param value
         *            allowed object is {@link LeistungsberechtigtePersonType }
         * 
         */
        public void setLeistungsberechtigtePerson(LeistungsberechtigtePersonType value) {
            leistungsberechtigtePerson = value;
        }

    }

}
