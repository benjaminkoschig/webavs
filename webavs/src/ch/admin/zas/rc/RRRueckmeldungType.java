//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in
// JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.06.25 at 01:29:35 PM CEST
//

package ch.admin.zas.rc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * 
 * Rückmeldung der ZAS an die AK nach Verarbeitung der Meldung (9. und 10.Revision, Anwendungsgebiet = 50)
 * 
 * 
 * <p>
 * Java class for RRRueckmeldung_Type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RRRueckmeldung_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="KasseZweigstelle" type="{http://www.zas.admin.ch/RC}IrgendeineZweigstelle_weakType"/>
 *         &lt;element name="Meldungsnummer" type="{http://www.zas.admin.ch/RC}Meldungsnummer_Type"/>
 *         &lt;element name="KasseneigenerHinweis" type="{http://www.zas.admin.ch/RC}KasseneigenerHinweis_Type" minOccurs="0"/>
 *         &lt;element name="LeistungsberechtigtePerson">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Versichertennummer" type="{http://www.zas.admin.ch/RC}Versichertennummer11oder13Stellen_Type"/>
 *                   &lt;element name="NeueVersichertennummer" type="{http://www.zas.admin.ch/RC}Versichertennummer11oder13Stellen_Type" minOccurs="0"/>
 *                   &lt;element name="FamilienAngehoerige" type="{http://www.zas.admin.ch/RC}FamilienAngehoerige_Type"/>
 *                   &lt;element name="Zivilstand" type="{http://www.zas.admin.ch/RC}ZivilstandNr_Type" minOccurs="0"/>
 *                   &lt;element name="WohnkantonStaat" type="{http://www.zas.admin.ch/RC}WohnkantonStaatBSV_weakType" minOccurs="0"/>
 *                   &lt;element name="istFluechtling" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Leistungsbeschreibung">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Leistungsart" type="{http://www.zas.admin.ch/RC}Leistungsart_alle_Revisionen_strongType"/>
 *                   &lt;element name="Anspruchsbeginn" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type" minOccurs="0"/>
 *                   &lt;element name="Monatsbetrag" type="{http://www.zas.admin.ch/RC}BetragFR_Type" minOccurs="0"/>
 *                   &lt;element name="MonatsbetragErsetzteOrdentlicheRente" type="{http://www.zas.admin.ch/RC}BetragFR_Type" minOccurs="0"/>
 *                   &lt;element name="Anspruchsende" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type" minOccurs="0"/>
 *                   &lt;element name="Mutationscode" type="{http://www.zas.admin.ch/RC}Mutationscode_Type" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Berichtsmonat" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type"/>
 *         &lt;element name="AntwortZAS">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Verarbeitungscode">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="2"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="Meldungsart">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
 *                         &lt;minInclusive value="0"/>
 *                         &lt;maxInclusive value="4"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
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
@XmlType(name = "RRRueckmeldung_Type", propOrder = { "kasseZweigstelle", "meldungsnummer", "kasseneigenerHinweis",
        "leistungsberechtigtePerson", "leistungsbeschreibung", "berichtsmonat", "antwortZAS" })
public class RRRueckmeldungType {

    @XmlElement(name = "KasseZweigstelle", required = true)
    protected String kasseZweigstelle;
    @XmlElement(name = "Meldungsnummer")
    protected long meldungsnummer;
    @XmlElement(name = "KasseneigenerHinweis")
    protected String kasseneigenerHinweis;
    @XmlElement(name = "LeistungsberechtigtePerson", required = true)
    protected RRRueckmeldungType.LeistungsberechtigtePerson leistungsberechtigtePerson;
    @XmlElement(name = "Leistungsbeschreibung", required = true)
    protected RRRueckmeldungType.Leistungsbeschreibung leistungsbeschreibung;
    @XmlElement(name = "Berichtsmonat", required = true)
    protected XMLGregorianCalendar berichtsmonat;
    @XmlElement(name = "AntwortZAS", required = true)
    protected RRRueckmeldungType.AntwortZAS antwortZAS;

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
     * Gets the value of the leistungsberechtigtePerson property.
     * 
     * @return
     *         possible object is {@link RRRueckmeldungType.LeistungsberechtigtePerson }
     * 
     */
    public RRRueckmeldungType.LeistungsberechtigtePerson getLeistungsberechtigtePerson() {
        return leistungsberechtigtePerson;
    }

    /**
     * Sets the value of the leistungsberechtigtePerson property.
     * 
     * @param value
     *            allowed object is {@link RRRueckmeldungType.LeistungsberechtigtePerson }
     * 
     */
    public void setLeistungsberechtigtePerson(RRRueckmeldungType.LeistungsberechtigtePerson value) {
        leistungsberechtigtePerson = value;
    }

    /**
     * Gets the value of the leistungsbeschreibung property.
     * 
     * @return
     *         possible object is {@link RRRueckmeldungType.Leistungsbeschreibung }
     * 
     */
    public RRRueckmeldungType.Leistungsbeschreibung getLeistungsbeschreibung() {
        return leistungsbeschreibung;
    }

    /**
     * Sets the value of the leistungsbeschreibung property.
     * 
     * @param value
     *            allowed object is {@link RRRueckmeldungType.Leistungsbeschreibung }
     * 
     */
    public void setLeistungsbeschreibung(RRRueckmeldungType.Leistungsbeschreibung value) {
        leistungsbeschreibung = value;
    }

    /**
     * Gets the value of the berichtsmonat property.
     * 
     * @return
     *         possible object is {@link XMLGregorianCalendar }
     * 
     */
    public XMLGregorianCalendar getBerichtsmonat() {
        return berichtsmonat;
    }

    /**
     * Sets the value of the berichtsmonat property.
     * 
     * @param value
     *            allowed object is {@link XMLGregorianCalendar }
     * 
     */
    public void setBerichtsmonat(XMLGregorianCalendar value) {
        berichtsmonat = value;
    }

    /**
     * Gets the value of the antwortZAS property.
     * 
     * @return
     *         possible object is {@link RRRueckmeldungType.AntwortZAS }
     * 
     */
    public RRRueckmeldungType.AntwortZAS getAntwortZAS() {
        return antwortZAS;
    }

    /**
     * Sets the value of the antwortZAS property.
     * 
     * @param value
     *            allowed object is {@link RRRueckmeldungType.AntwortZAS }
     * 
     */
    public void setAntwortZAS(RRRueckmeldungType.AntwortZAS value) {
        antwortZAS = value;
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
     *         &lt;element name="Verarbeitungscode">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="2"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="Meldungsart">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}unsignedByte">
     *               &lt;minInclusive value="0"/>
     *               &lt;maxInclusive value="4"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
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
    @XmlType(name = "", propOrder = { "verarbeitungscode", "meldungsart" })
    public static class AntwortZAS {

        @XmlElement(name = "Verarbeitungscode")
        protected short verarbeitungscode;
        @XmlElement(name = "Meldungsart")
        protected short meldungsart;

        /**
         * Gets the value of the verarbeitungscode property.
         * 
         */
        public short getVerarbeitungscode() {
            return verarbeitungscode;
        }

        /**
         * Sets the value of the verarbeitungscode property.
         * 
         */
        public void setVerarbeitungscode(short value) {
            verarbeitungscode = value;
        }

        /**
         * Gets the value of the meldungsart property.
         * 
         */
        public short getMeldungsart() {
            return meldungsart;
        }

        /**
         * Sets the value of the meldungsart property.
         * 
         */
        public void setMeldungsart(short value) {
            meldungsart = value;
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
     *       &lt;sequence>
     *         &lt;element name="Versichertennummer" type="{http://www.zas.admin.ch/RC}Versichertennummer11oder13Stellen_Type"/>
     *         &lt;element name="NeueVersichertennummer" type="{http://www.zas.admin.ch/RC}Versichertennummer11oder13Stellen_Type" minOccurs="0"/>
     *         &lt;element name="FamilienAngehoerige" type="{http://www.zas.admin.ch/RC}FamilienAngehoerige_Type"/>
     *         &lt;element name="Zivilstand" type="{http://www.zas.admin.ch/RC}ZivilstandNr_Type" minOccurs="0"/>
     *         &lt;element name="WohnkantonStaat" type="{http://www.zas.admin.ch/RC}WohnkantonStaatBSV_weakType" minOccurs="0"/>
     *         &lt;element name="istFluechtling" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "versichertennummer", "neueVersichertennummer", "familienAngehoerige",
            "zivilstand", "wohnkantonStaat", "istFluechtling" })
    public static class LeistungsberechtigtePerson {

        @XmlElement(name = "Versichertennummer", required = true)
        protected String versichertennummer;
        @XmlElement(name = "NeueVersichertennummer")
        protected String neueVersichertennummer;
        @XmlElement(name = "FamilienAngehoerige", required = true)
        protected FamilienAngehoerigeType familienAngehoerige;
        @XmlElement(name = "Zivilstand")
        protected Short zivilstand;
        @XmlElement(name = "WohnkantonStaat")
        protected String wohnkantonStaat;
        protected Boolean istFluechtling;

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
         * Gets the value of the neueVersichertennummer property.
         * 
         * @return
         *         possible object is {@link String }
         * 
         */
        public String getNeueVersichertennummer() {
            return neueVersichertennummer;
        }

        /**
         * Sets the value of the neueVersichertennummer property.
         * 
         * @param value
         *            allowed object is {@link String }
         * 
         */
        public void setNeueVersichertennummer(String value) {
            neueVersichertennummer = value;
        }

        /**
         * Gets the value of the familienAngehoerige property.
         * 
         * @return
         *         possible object is {@link FamilienAngehoerigeType }
         * 
         */
        public FamilienAngehoerigeType getFamilienAngehoerige() {
            return familienAngehoerige;
        }

        /**
         * Sets the value of the familienAngehoerige property.
         * 
         * @param value
         *            allowed object is {@link FamilienAngehoerigeType }
         * 
         */
        public void setFamilienAngehoerige(FamilienAngehoerigeType value) {
            familienAngehoerige = value;
        }

        /**
         * Gets the value of the zivilstand property.
         * 
         * @return
         *         possible object is {@link Short }
         * 
         */
        public Short getZivilstand() {
            return zivilstand;
        }

        /**
         * Sets the value of the zivilstand property.
         * 
         * @param value
         *            allowed object is {@link Short }
         * 
         */
        public void setZivilstand(Short value) {
            zivilstand = value;
        }

        /**
         * Gets the value of the wohnkantonStaat property.
         * 
         * @return
         *         possible object is {@link String }
         * 
         */
        public String getWohnkantonStaat() {
            return wohnkantonStaat;
        }

        /**
         * Sets the value of the wohnkantonStaat property.
         * 
         * @param value
         *            allowed object is {@link String }
         * 
         */
        public void setWohnkantonStaat(String value) {
            wohnkantonStaat = value;
        }

        /**
         * Gets the value of the istFluechtling property.
         * 
         * @return
         *         possible object is {@link Boolean }
         * 
         */
        public Boolean isIstFluechtling() {
            return istFluechtling;
        }

        /**
         * Sets the value of the istFluechtling property.
         * 
         * @param value
         *            allowed object is {@link Boolean }
         * 
         */
        public void setIstFluechtling(Boolean value) {
            istFluechtling = value;
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
     *       &lt;sequence>
     *         &lt;element name="Leistungsart" type="{http://www.zas.admin.ch/RC}Leistungsart_alle_Revisionen_strongType"/>
     *         &lt;element name="Anspruchsbeginn" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type" minOccurs="0"/>
     *         &lt;element name="Monatsbetrag" type="{http://www.zas.admin.ch/RC}BetragFR_Type" minOccurs="0"/>
     *         &lt;element name="MonatsbetragErsetzteOrdentlicheRente" type="{http://www.zas.admin.ch/RC}BetragFR_Type" minOccurs="0"/>
     *         &lt;element name="Anspruchsende" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type" minOccurs="0"/>
     *         &lt;element name="Mutationscode" type="{http://www.zas.admin.ch/RC}Mutationscode_Type" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "leistungsart", "anspruchsbeginn", "monatsbetrag",
            "monatsbetragErsetzteOrdentlicheRente", "anspruchsende", "mutationscode" })
    public static class Leistungsbeschreibung {

        @XmlList
        @XmlElement(name = "Leistungsart", required = true)
        protected List<String> leistungsart;
        @XmlElement(name = "Anspruchsbeginn")
        protected XMLGregorianCalendar anspruchsbeginn;
        @XmlElement(name = "Monatsbetrag")
        protected BigDecimal monatsbetrag;
        @XmlElement(name = "MonatsbetragErsetzteOrdentlicheRente")
        protected BigDecimal monatsbetragErsetzteOrdentlicheRente;
        @XmlElement(name = "Anspruchsende")
        protected XMLGregorianCalendar anspruchsende;
        @XmlElement(name = "Mutationscode")
        protected Short mutationscode;

        /**
         * Gets the value of the leistungsart property.
         * 
         * <p>
         * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you
         * make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE>
         * method for the leistungsart property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * 
         * <pre>
         * getLeistungsart().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list {@link String }
         * 
         * 
         */
        public List<String> getLeistungsart() {
            if (leistungsart == null) {
                leistungsart = new ArrayList<String>();
            }
            return leistungsart;
        }

        /**
         * Gets the value of the anspruchsbeginn property.
         * 
         * @return
         *         possible object is {@link XMLGregorianCalendar }
         * 
         */
        public XMLGregorianCalendar getAnspruchsbeginn() {
            return anspruchsbeginn;
        }

        /**
         * Sets the value of the anspruchsbeginn property.
         * 
         * @param value
         *            allowed object is {@link XMLGregorianCalendar }
         * 
         */
        public void setAnspruchsbeginn(XMLGregorianCalendar value) {
            anspruchsbeginn = value;
        }

        /**
         * Gets the value of the monatsbetrag property.
         * 
         * @return
         *         possible object is {@link BigDecimal }
         * 
         */
        public BigDecimal getMonatsbetrag() {
            return monatsbetrag;
        }

        /**
         * Sets the value of the monatsbetrag property.
         * 
         * @param value
         *            allowed object is {@link BigDecimal }
         * 
         */
        public void setMonatsbetrag(BigDecimal value) {
            monatsbetrag = value;
        }

        /**
         * Gets the value of the monatsbetragErsetzteOrdentlicheRente property.
         * 
         * @return
         *         possible object is {@link BigDecimal }
         * 
         */
        public BigDecimal getMonatsbetragErsetzteOrdentlicheRente() {
            return monatsbetragErsetzteOrdentlicheRente;
        }

        /**
         * Sets the value of the monatsbetragErsetzteOrdentlicheRente property.
         * 
         * @param value
         *            allowed object is {@link BigDecimal }
         * 
         */
        public void setMonatsbetragErsetzteOrdentlicheRente(BigDecimal value) {
            monatsbetragErsetzteOrdentlicheRente = value;
        }

        /**
         * Gets the value of the anspruchsende property.
         * 
         * @return
         *         possible object is {@link XMLGregorianCalendar }
         * 
         */
        public XMLGregorianCalendar getAnspruchsende() {
            return anspruchsende;
        }

        /**
         * Sets the value of the anspruchsende property.
         * 
         * @param value
         *            allowed object is {@link XMLGregorianCalendar }
         * 
         */
        public void setAnspruchsende(XMLGregorianCalendar value) {
            anspruchsende = value;
        }

        /**
         * Gets the value of the mutationscode property.
         * 
         * @return
         *         possible object is {@link Short }
         * 
         */
        public Short getMutationscode() {
            return mutationscode;
        }

        /**
         * Sets the value of the mutationscode property.
         * 
         * @param value
         *            allowed object is {@link Short }
         * 
         */
        public void setMutationscode(Short value) {
            mutationscode = value;
        }

    }

}
