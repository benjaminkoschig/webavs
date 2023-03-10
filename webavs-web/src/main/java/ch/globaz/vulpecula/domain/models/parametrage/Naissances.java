//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.05.26 at 04:47:59 PM CEST 
//


package ch.globaz.vulpecula.domain.models.parametrage;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for naissances complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="naissances">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="naissance" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="predicats" type="{}predicats" minOccurs="0"/>
 *                   &lt;element name="nbjours" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *                 &lt;/sequence>
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
@XmlType(name = "naissances", propOrder = {
    "naissance"
})
public class Naissances {

    @XmlElement(required = true)
    protected List<Naissances.Naissance> naissance;

    /**
     * Gets the value of the naissance property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the naissance property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNaissance().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Naissances.Naissance }
     * 
     * 
     */
    public List<Naissances.Naissance> getNaissance() {
        if (naissance == null) {
            naissance = new ArrayList<Naissances.Naissance>();
        }
        return this.naissance;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="predicats" type="{}predicats" minOccurs="0"/>
     *         &lt;element name="nbjours" type="{http://www.w3.org/2001/XMLSchema}integer"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "predicats",
        "nbjours"
    })
    public static class Naissance {

        protected Predicats predicats;
        @XmlElement(required = true)
        protected BigInteger nbjours;

        /**
         * Gets the value of the predicats property.
         * 
         * @return
         *     possible object is
         *     {@link Predicats }
         *     
         */
        public Predicats getPredicats() {
            return predicats;
        }

        /**
         * Sets the value of the predicats property.
         * 
         * @param value
         *     allowed object is
         *     {@link Predicats }
         *     
         */
        public void setPredicats(Predicats value) {
            this.predicats = value;
        }

        /**
         * Gets the value of the nbjours property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getNbjours() {
            return nbjours;
        }

        /**
         * Sets the value of the nbjours property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setNbjours(BigInteger value) {
            this.nbjours = value;
        }

    }

}
