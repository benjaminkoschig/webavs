//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in
// JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.10.04 at 12:29:02 PM CEST
//

package ch.globaz.perseus.businessimpl.services.statsOFS.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

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
 *         &lt;element name="row" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="massnahme_id" type="{}shsCode" />
 *                 &lt;attribute name="wert" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="kommentar" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "row" })
@XmlRootElement(name = "massnahme")
public class Massnahme {

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
     *       &lt;attribute name="massnahme_id" type="{}shsCode" />
     *       &lt;attribute name="wert" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute name="kommentar" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Row {

        @XmlAttribute
        protected String kommentar;
        @XmlAttribute(name = "massnahme_id")
        protected List<String> massnahmeId;
        @XmlAttribute
        protected String wert;

        /**
         * Gets the value of the kommentar property.
         * 
         * @return possible object is {@link String }
         * 
         */
        public String getKommentar() {
            return kommentar;
        }

        /**
         * Gets the value of the massnahmeId property.
         * 
         * <p>
         * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you
         * make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE>
         * method for the massnahmeId property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * 
         * <pre>
         * getMassnahmeId().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list {@link String }
         * 
         * 
         */
        public List<String> getMassnahmeId() {
            if (massnahmeId == null) {
                massnahmeId = new ArrayList<String>();
            }
            return massnahmeId;
        }

        /**
         * Gets the value of the wert property.
         * 
         * @return possible object is {@link String }
         * 
         */
        public String getWert() {
            return wert;
        }

        /**
         * Sets the value of the kommentar property.
         * 
         * @param value
         *            allowed object is {@link String }
         * 
         */
        public void setKommentar(String value) {
            kommentar = value;
        }

        /**
         * Sets the value of the wert property.
         * 
         * @param value
         *            allowed object is {@link String }
         * 
         */
        public void setWert(String value) {
            wert = value;
        }

    }

    protected List<Massnahme.Row> row;

    /**
     * Gets the value of the row property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the row property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getRow().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Massnahme.Row }
     * 
     * 
     */
    public List<Massnahme.Row> getRow() {
        if (row == null) {
            row = new ArrayList<Massnahme.Row>();
        }
        return row;
    }

}
