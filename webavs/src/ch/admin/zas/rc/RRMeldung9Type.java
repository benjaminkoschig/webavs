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
 * Meldung 9.Revision: Anwendungsgebiet IN (41,42,43)
 * 
 * <p>
 * Java class for RRMeldung9_Type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RRMeldung9_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="OrdentlicheRente">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="Zuwachsmeldung" type="{http://www.zas.admin.ch/RC}ZuwachsmeldungO9_Type"/>
 *                   &lt;element name="Abgangsmeldung" type="{http://www.zas.admin.ch/RC}Abgangsmeldung_Type"/>
 *                   &lt;element name="Aenderungsmeldung" type="{http://www.zas.admin.ch/RC}AenderungsmeldungO9_Type"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="AusserordentlicheRente">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="Zuwachsmeldung" type="{http://www.zas.admin.ch/RC}ZuwachsmeldungAO9_Type"/>
 *                   &lt;element name="Abgangsmeldung" type="{http://www.zas.admin.ch/RC}Abgangsmeldung_Type"/>
 *                   &lt;element name="Aenderungsmeldung" type="{http://www.zas.admin.ch/RC}AenderungsmeldungAO9_Type"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Hilflosenentschaedigung">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="Zuwachsmeldung" type="{http://www.zas.admin.ch/RC}ZuwachsmeldungHE9_Type"/>
 *                   &lt;element name="Abgangsmeldung" type="{http://www.zas.admin.ch/RC}Abgangsmeldung_Type"/>
 *                   &lt;element name="Aenderungsmeldung" type="{http://www.zas.admin.ch/RC}AenderungsmeldungHE9_Type"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RRMeldung9_Type", propOrder = { "ordentlicheRente", "ausserordentlicheRente",
        "hilflosenentschaedigung" })
public class RRMeldung9Type {

    @XmlElement(name = "OrdentlicheRente")
    protected RRMeldung9Type.OrdentlicheRente ordentlicheRente;
    @XmlElement(name = "AusserordentlicheRente")
    protected RRMeldung9Type.AusserordentlicheRente ausserordentlicheRente;
    @XmlElement(name = "Hilflosenentschaedigung")
    protected RRMeldung9Type.Hilflosenentschaedigung hilflosenentschaedigung;

    /**
     * Gets the value of the ordentlicheRente property.
     * 
     * @return
     *         possible object is {@link RRMeldung9Type.OrdentlicheRente }
     * 
     */
    public RRMeldung9Type.OrdentlicheRente getOrdentlicheRente() {
        return ordentlicheRente;
    }

    /**
     * Sets the value of the ordentlicheRente property.
     * 
     * @param value
     *            allowed object is {@link RRMeldung9Type.OrdentlicheRente }
     * 
     */
    public void setOrdentlicheRente(RRMeldung9Type.OrdentlicheRente value) {
        ordentlicheRente = value;
    }

    /**
     * Gets the value of the ausserordentlicheRente property.
     * 
     * @return
     *         possible object is {@link RRMeldung9Type.AusserordentlicheRente }
     * 
     */
    public RRMeldung9Type.AusserordentlicheRente getAusserordentlicheRente() {
        return ausserordentlicheRente;
    }

    /**
     * Sets the value of the ausserordentlicheRente property.
     * 
     * @param value
     *            allowed object is {@link RRMeldung9Type.AusserordentlicheRente }
     * 
     */
    public void setAusserordentlicheRente(RRMeldung9Type.AusserordentlicheRente value) {
        ausserordentlicheRente = value;
    }

    /**
     * Gets the value of the hilflosenentschaedigung property.
     * 
     * @return
     *         possible object is {@link RRMeldung9Type.Hilflosenentschaedigung }
     * 
     */
    public RRMeldung9Type.Hilflosenentschaedigung getHilflosenentschaedigung() {
        return hilflosenentschaedigung;
    }

    /**
     * Sets the value of the hilflosenentschaedigung property.
     * 
     * @param value
     *            allowed object is {@link RRMeldung9Type.Hilflosenentschaedigung }
     * 
     */
    public void setHilflosenentschaedigung(RRMeldung9Type.Hilflosenentschaedigung value) {
        hilflosenentschaedigung = value;
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
     *       &lt;choice>
     *         &lt;element name="Zuwachsmeldung" type="{http://www.zas.admin.ch/RC}ZuwachsmeldungAO9_Type"/>
     *         &lt;element name="Abgangsmeldung" type="{http://www.zas.admin.ch/RC}Abgangsmeldung_Type"/>
     *         &lt;element name="Aenderungsmeldung" type="{http://www.zas.admin.ch/RC}AenderungsmeldungAO9_Type"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "zuwachsmeldung", "abgangsmeldung", "aenderungsmeldung" })
    public static class AusserordentlicheRente {

        @XmlElement(name = "Zuwachsmeldung")
        protected ZuwachsmeldungAO9Type zuwachsmeldung;
        @XmlElement(name = "Abgangsmeldung")
        protected AbgangsmeldungType abgangsmeldung;
        @XmlElement(name = "Aenderungsmeldung")
        protected AenderungsmeldungAO9Type aenderungsmeldung;

        /**
         * Gets the value of the zuwachsmeldung property.
         * 
         * @return
         *         possible object is {@link ZuwachsmeldungAO9Type }
         * 
         */
        public ZuwachsmeldungAO9Type getZuwachsmeldung() {
            return zuwachsmeldung;
        }

        /**
         * Sets the value of the zuwachsmeldung property.
         * 
         * @param value
         *            allowed object is {@link ZuwachsmeldungAO9Type }
         * 
         */
        public void setZuwachsmeldung(ZuwachsmeldungAO9Type value) {
            zuwachsmeldung = value;
        }

        /**
         * Gets the value of the abgangsmeldung property.
         * 
         * @return
         *         possible object is {@link AbgangsmeldungType }
         * 
         */
        public AbgangsmeldungType getAbgangsmeldung() {
            return abgangsmeldung;
        }

        /**
         * Sets the value of the abgangsmeldung property.
         * 
         * @param value
         *            allowed object is {@link AbgangsmeldungType }
         * 
         */
        public void setAbgangsmeldung(AbgangsmeldungType value) {
            abgangsmeldung = value;
        }

        /**
         * Gets the value of the aenderungsmeldung property.
         * 
         * @return
         *         possible object is {@link AenderungsmeldungAO9Type }
         * 
         */
        public AenderungsmeldungAO9Type getAenderungsmeldung() {
            return aenderungsmeldung;
        }

        /**
         * Sets the value of the aenderungsmeldung property.
         * 
         * @param value
         *            allowed object is {@link AenderungsmeldungAO9Type }
         * 
         */
        public void setAenderungsmeldung(AenderungsmeldungAO9Type value) {
            aenderungsmeldung = value;
        }

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
     *       &lt;choice>
     *         &lt;element name="Zuwachsmeldung" type="{http://www.zas.admin.ch/RC}ZuwachsmeldungHE9_Type"/>
     *         &lt;element name="Abgangsmeldung" type="{http://www.zas.admin.ch/RC}Abgangsmeldung_Type"/>
     *         &lt;element name="Aenderungsmeldung" type="{http://www.zas.admin.ch/RC}AenderungsmeldungHE9_Type"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "zuwachsmeldung", "abgangsmeldung", "aenderungsmeldung" })
    public static class Hilflosenentschaedigung {

        @XmlElement(name = "Zuwachsmeldung")
        protected ZuwachsmeldungHE9Type zuwachsmeldung;
        @XmlElement(name = "Abgangsmeldung")
        protected AbgangsmeldungType abgangsmeldung;
        @XmlElement(name = "Aenderungsmeldung")
        protected AenderungsmeldungHE9Type aenderungsmeldung;

        /**
         * Gets the value of the zuwachsmeldung property.
         * 
         * @return
         *         possible object is {@link ZuwachsmeldungHE9Type }
         * 
         */
        public ZuwachsmeldungHE9Type getZuwachsmeldung() {
            return zuwachsmeldung;
        }

        /**
         * Sets the value of the zuwachsmeldung property.
         * 
         * @param value
         *            allowed object is {@link ZuwachsmeldungHE9Type }
         * 
         */
        public void setZuwachsmeldung(ZuwachsmeldungHE9Type value) {
            zuwachsmeldung = value;
        }

        /**
         * Gets the value of the abgangsmeldung property.
         * 
         * @return
         *         possible object is {@link AbgangsmeldungType }
         * 
         */
        public AbgangsmeldungType getAbgangsmeldung() {
            return abgangsmeldung;
        }

        /**
         * Sets the value of the abgangsmeldung property.
         * 
         * @param value
         *            allowed object is {@link AbgangsmeldungType }
         * 
         */
        public void setAbgangsmeldung(AbgangsmeldungType value) {
            abgangsmeldung = value;
        }

        /**
         * Gets the value of the aenderungsmeldung property.
         * 
         * @return
         *         possible object is {@link AenderungsmeldungHE9Type }
         * 
         */
        public AenderungsmeldungHE9Type getAenderungsmeldung() {
            return aenderungsmeldung;
        }

        /**
         * Sets the value of the aenderungsmeldung property.
         * 
         * @param value
         *            allowed object is {@link AenderungsmeldungHE9Type }
         * 
         */
        public void setAenderungsmeldung(AenderungsmeldungHE9Type value) {
            aenderungsmeldung = value;
        }

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
     *       &lt;choice>
     *         &lt;element name="Zuwachsmeldung" type="{http://www.zas.admin.ch/RC}ZuwachsmeldungO9_Type"/>
     *         &lt;element name="Abgangsmeldung" type="{http://www.zas.admin.ch/RC}Abgangsmeldung_Type"/>
     *         &lt;element name="Aenderungsmeldung" type="{http://www.zas.admin.ch/RC}AenderungsmeldungO9_Type"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "zuwachsmeldung", "abgangsmeldung", "aenderungsmeldung" })
    public static class OrdentlicheRente {

        @XmlElement(name = "Zuwachsmeldung")
        protected ZuwachsmeldungO9Type zuwachsmeldung;
        @XmlElement(name = "Abgangsmeldung")
        protected AbgangsmeldungType abgangsmeldung;
        @XmlElement(name = "Aenderungsmeldung")
        protected AenderungsmeldungO9Type aenderungsmeldung;

        /**
         * Gets the value of the zuwachsmeldung property.
         * 
         * @return
         *         possible object is {@link ZuwachsmeldungO9Type }
         * 
         */
        public ZuwachsmeldungO9Type getZuwachsmeldung() {
            return zuwachsmeldung;
        }

        /**
         * Sets the value of the zuwachsmeldung property.
         * 
         * @param value
         *            allowed object is {@link ZuwachsmeldungO9Type }
         * 
         */
        public void setZuwachsmeldung(ZuwachsmeldungO9Type value) {
            zuwachsmeldung = value;
        }

        /**
         * Gets the value of the abgangsmeldung property.
         * 
         * @return
         *         possible object is {@link AbgangsmeldungType }
         * 
         */
        public AbgangsmeldungType getAbgangsmeldung() {
            return abgangsmeldung;
        }

        /**
         * Sets the value of the abgangsmeldung property.
         * 
         * @param value
         *            allowed object is {@link AbgangsmeldungType }
         * 
         */
        public void setAbgangsmeldung(AbgangsmeldungType value) {
            abgangsmeldung = value;
        }

        /**
         * Gets the value of the aenderungsmeldung property.
         * 
         * @return
         *         possible object is {@link AenderungsmeldungO9Type }
         * 
         */
        public AenderungsmeldungO9Type getAenderungsmeldung() {
            return aenderungsmeldung;
        }

        /**
         * Sets the value of the aenderungsmeldung property.
         * 
         * @param value
         *            allowed object is {@link AenderungsmeldungO9Type }
         * 
         */
        public void setAenderungsmeldung(AenderungsmeldungO9Type value) {
            aenderungsmeldung = value;
        }

    }

}
