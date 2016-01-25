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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * Code application 20: accus� de r�ception d'une annonce au registre central des assur�s
 * 
 * <p>
 * Java class for VAIKEmpfangsbestaetigung_Type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VAIKEmpfangsbestaetigung_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="KasseZweigstelle" type="{http://www.zas.admin.ch/RC}IrgendeineZweigstelle_weakType"/>
 *         &lt;element name="KasseneigenerHinweis" type="{http://www.zas.admin.ch/RC}KasseneigenerHinweis_Type"/>
 *         &lt;element name="Meldungsnummer" type="{http://www.zas.admin.ch/RC}Meldungsnummer_Type"/>
 *         &lt;element name="Versichertennummer" type="{http://www.zas.admin.ch/RC}Versichertennummer11oder13Stellen_Type" minOccurs="0"/>
 *         &lt;element name="Versichertennummer13Stellen" type="{http://www.zas.admin.ch/RC}Versichertennummer13Stellen_Type" minOccurs="0"/>
 *         &lt;element name="VersichertenAngaben" type="{http://www.zas.admin.ch/RC}TVersicherteAngabenFakultativ" minOccurs="0"/>
 *         &lt;element name="VersichertenNrBisher" type="{http://www.zas.admin.ch/RC}Versichertennummer8bis13Stellen_Type" minOccurs="0"/>
 *         &lt;element name="MZRSchluesselzahl" type="{http://www.zas.admin.ch/RC}MZRSchluesselzahl_weakType"/>
 *         &lt;element name="Verarbeitungscode">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.zas.admin.ch/RC>TVerarbeitungscode">
 *                 &lt;attribute name="nils" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Bemerkung1">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Bemerkung2">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Bemerkung3">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="1"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="FehlerhaftesFeld" maxOccurs="3" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence minOccurs="0">
 *                   &lt;element name="Feldnummer">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
 *                         &lt;maxInclusive value="99"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Tagname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="nils" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
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
@XmlType(name = "VAIKEmpfangsbestaetigung_Type", propOrder = { "kasseZweigstelle", "kasseneigenerHinweis",
        "meldungsnummer", "versichertennummer", "versichertennummer13Stellen", "versichertenAngaben",
        "versichertenNrBisher", "mzrSchluesselzahl", "verarbeitungscode", "bemerkung1", "bemerkung2", "bemerkung3",
        "fehlerhaftesFeld" })
public class VAIKEmpfangsbestaetigungType {

    @XmlElement(name = "KasseZweigstelle", required = true, nillable = true)
    protected String kasseZweigstelle;
    @XmlElement(name = "KasseneigenerHinweis", required = true)
    protected String kasseneigenerHinweis;
    @XmlElement(name = "Meldungsnummer")
    protected long meldungsnummer;
    @XmlElement(name = "Versichertennummer")
    protected String versichertennummer;
    @XmlElement(name = "Versichertennummer13Stellen")
    protected Long versichertennummer13Stellen;
    @XmlElement(name = "VersichertenAngaben")
    protected TVersicherteAngabenFakultativ versichertenAngaben;
    @XmlList
    @XmlElement(name = "VersichertenNrBisher")
    protected List<String> versichertenNrBisher;
    @XmlElement(name = "MZRSchluesselzahl")
    protected short mzrSchluesselzahl;
    @XmlElement(name = "Verarbeitungscode", required = true)
    protected VAIKEmpfangsbestaetigungType.Verarbeitungscode verarbeitungscode;
    @XmlElement(name = "Bemerkung1")
    protected short bemerkung1;
    @XmlElement(name = "Bemerkung2")
    protected short bemerkung2;
    @XmlElement(name = "Bemerkung3")
    protected short bemerkung3;
    @XmlElement(name = "FehlerhaftesFeld")
    protected List<VAIKEmpfangsbestaetigungType.FehlerhaftesFeld> fehlerhaftesFeld;

    /**
     * Gets the value of the kasseZweigstelle property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getKasseZweigstelle() {
        return kasseZweigstelle;
    }

    /**
     * Sets the value of the kasseZweigstelle property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setKasseZweigstelle(String value) {
        kasseZweigstelle = value;
    }

    /**
     * Gets the value of the kasseneigenerHinweis property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getKasseneigenerHinweis() {
        return kasseneigenerHinweis;
    }

    /**
     * Sets the value of the kasseneigenerHinweis property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setKasseneigenerHinweis(String value) {
        kasseneigenerHinweis = value;
    }

    /**
     * Gets the value of the meldungsnummer property.
     * 
     */
    public long getMeldungsnummer() {
        return meldungsnummer;
    }

    /**
     * Sets the value of the meldungsnummer property.
     * 
     */
    public void setMeldungsnummer(long value) {
        meldungsnummer = value;
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
     * Gets the value of the versichertennummer13Stellen property.
     * 
     * @return
     *         possible object is {@link Long }
     * 
     */
    public Long getVersichertennummer13Stellen() {
        return versichertennummer13Stellen;
    }

    /**
     * Sets the value of the versichertennummer13Stellen property.
     * 
     * @param value
     *            allowed object is {@link Long }
     * 
     */
    public void setVersichertennummer13Stellen(Long value) {
        versichertennummer13Stellen = value;
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

    /**
     * Gets the value of the versichertenNrBisher property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the versichertenNrBisher property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getVersichertenNrBisher().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link String }
     * 
     * 
     */
    public List<String> getVersichertenNrBisher() {
        if (versichertenNrBisher == null) {
            versichertenNrBisher = new ArrayList<String>();
        }
        return versichertenNrBisher;
    }

    /**
     * Gets the value of the mzrSchluesselzahl property.
     * 
     */
    public short getMZRSchluesselzahl() {
        return mzrSchluesselzahl;
    }

    /**
     * Sets the value of the mzrSchluesselzahl property.
     * 
     */
    public void setMZRSchluesselzahl(short value) {
        mzrSchluesselzahl = value;
    }

    /**
     * Gets the value of the verarbeitungscode property.
     * 
     * @return
     *         possible object is {@link VAIKEmpfangsbestaetigungType.Verarbeitungscode }
     * 
     */
    public VAIKEmpfangsbestaetigungType.Verarbeitungscode getVerarbeitungscode() {
        return verarbeitungscode;
    }

    /**
     * Sets the value of the verarbeitungscode property.
     * 
     * @param value
     *            allowed object is {@link VAIKEmpfangsbestaetigungType.Verarbeitungscode }
     * 
     */
    public void setVerarbeitungscode(VAIKEmpfangsbestaetigungType.Verarbeitungscode value) {
        verarbeitungscode = value;
    }

    /**
     * Gets the value of the bemerkung1 property.
     * 
     */
    public short getBemerkung1() {
        return bemerkung1;
    }

    /**
     * Sets the value of the bemerkung1 property.
     * 
     */
    public void setBemerkung1(short value) {
        bemerkung1 = value;
    }

    /**
     * Gets the value of the bemerkung2 property.
     * 
     */
    public short getBemerkung2() {
        return bemerkung2;
    }

    /**
     * Sets the value of the bemerkung2 property.
     * 
     */
    public void setBemerkung2(short value) {
        bemerkung2 = value;
    }

    /**
     * Gets the value of the bemerkung3 property.
     * 
     */
    public short getBemerkung3() {
        return bemerkung3;
    }

    /**
     * Sets the value of the bemerkung3 property.
     * 
     */
    public void setBemerkung3(short value) {
        bemerkung3 = value;
    }

    /**
     * Gets the value of the fehlerhaftesFeld property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for
     * the fehlerhaftesFeld property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getFehlerhaftesFeld().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link VAIKEmpfangsbestaetigungType.FehlerhaftesFeld }
     * 
     * 
     */
    public List<VAIKEmpfangsbestaetigungType.FehlerhaftesFeld> getFehlerhaftesFeld() {
        if (fehlerhaftesFeld == null) {
            fehlerhaftesFeld = new ArrayList<VAIKEmpfangsbestaetigungType.FehlerhaftesFeld>();
        }
        return fehlerhaftesFeld;
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
     *       &lt;sequence minOccurs="0">
     *         &lt;element name="Feldnummer">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
     *               &lt;maxInclusive value="99"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Tagname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="nils" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "feldnummer", "tagname" })
    public static class FehlerhaftesFeld {

        @XmlElement(name = "Feldnummer")
        protected Short feldnummer;
        @XmlElement(name = "Tagname")
        protected String tagname;
        @XmlAttribute
        protected Boolean nils;

        /**
         * Gets the value of the feldnummer property.
         * 
         * @return
         *         possible object is {@link Short }
         * 
         */
        public Short getFeldnummer() {
            return feldnummer;
        }

        /**
         * Sets the value of the feldnummer property.
         * 
         * @param value
         *            allowed object is {@link Short }
         * 
         */
        public void setFeldnummer(Short value) {
            feldnummer = value;
        }

        /**
         * Gets the value of the tagname property.
         * 
         * @return
         *         possible object is {@link String }
         * 
         */
        public String getTagname() {
            return tagname;
        }

        /**
         * Sets the value of the tagname property.
         * 
         * @param value
         *            allowed object is {@link String }
         * 
         */
        public void setTagname(String value) {
            tagname = value;
        }

        /**
         * Gets the value of the nils property.
         * 
         * @return
         *         possible object is {@link Boolean }
         * 
         */
        public boolean isNils() {
            if (nils == null) {
                return false;
            } else {
                return nils;
            }
        }

        /**
         * Sets the value of the nils property.
         * 
         * @param value
         *            allowed object is {@link Boolean }
         * 
         */
        public void setNils(Boolean value) {
            nils = value;
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
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.zas.admin.ch/RC>TVerarbeitungscode">
     *       &lt;attribute name="nils" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "value" })
    public static class Verarbeitungscode {

        @XmlValue
        protected long value;
        @XmlAttribute
        protected Boolean nils;

        /**
         * Gets the value of the value property.
         * 
         */
        public long getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         */
        public void setValue(long value) {
            this.value = value;
        }

        /**
         * Gets the value of the nils property.
         * 
         * @return
         *         possible object is {@link Boolean }
         * 
         */
        public boolean isNils() {
            if (nils == null) {
                return false;
            } else {
                return nils;
            }
        }

        /**
         * Sets the value of the nils property.
         * 
         * @param value
         *            allowed object is {@link Boolean }
         * 
         */
        public void setNils(Boolean value) {
            nils = value;
        }

    }

}
