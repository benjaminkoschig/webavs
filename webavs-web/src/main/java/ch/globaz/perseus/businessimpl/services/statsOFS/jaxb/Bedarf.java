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
 *                 &lt;attribute name="bedarfsart_id" type="{}shsCode" />
 *                 &lt;attribute name="betrag" type="{}shsBetrag" />
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
@XmlRootElement(name = "bedarf")
public class Bedarf {

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
     *       &lt;attribute name="bedarfsart_id" type="{}shsCode" />
     *       &lt;attribute name="betrag" type="{}shsBetrag" />
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

        @XmlAttribute(name = "bedarfsart_id")
        protected List<String> bedarfsartId;
        @XmlAttribute
        protected String betrag;

        /**
         * Gets the value of the bedarfsartId property.
         * 
         * <p>
         * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you
         * make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE>
         * method for the bedarfsartId property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * 
         * <pre>
         * getBedarfsartId().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list {@link String }
         * 
         * 
         */
        public List<String> getBedarfsartId() {
            if (bedarfsartId == null) {
                bedarfsartId = new ArrayList<String>();
            }
            return bedarfsartId;
        }

        /**
         * Gets the value of the betrag property.
         * 
         * @return possible object is {@link String }
         * 
         */
        public String getBetrag() {
            return betrag;
        }

        /**
         * Sets the value of the betrag property.
         * 
         * @param value
         *            allowed object is {@link String }
         * 
         */
        public void setBetrag(String value) {
            betrag = value;
        }

    }

    protected List<Bedarf.Row> row;

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
     * Objects of the following type(s) are allowed in the list {@link Bedarf.Row }
     * 
     * 
     */
    public List<Bedarf.Row> getRow() {
        if (row == null) {
            row = new ArrayList<Bedarf.Row>();
        }
        return row;
    }

}
