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
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Annonce d'une nouvelle rente ordinaire (10e r�vision)
 * 
 * <p>
 * Java class for AenderungsmeldungO10_Type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AenderungsmeldungO10_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="KasseZweigstelle" type="{http://www.zas.admin.ch/RC}IrgendeineZweigstelle_weakType"/>
 *         &lt;element name="Meldungsnummer" type="{http://www.zas.admin.ch/RC}Meldungsnummer_Type"/>
 *         &lt;element name="KasseneigenerHinweis" type="{http://www.zas.admin.ch/RC}KasseneigenerHinweis_Type" minOccurs="0"/>
 *         &lt;element name="LeistungsberechtigtePerson" type="{http://www.zas.admin.ch/RC}RRLeistungsberechtigtePersonAusl_WeakType"/>
 *         &lt;element name="Leistungsbeschreibung">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Leistungsart" type="{http://www.zas.admin.ch/RC}OrdentlicheRente10_strongType"/>
 *                   &lt;element name="Anspruchsbeginn" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type" minOccurs="0"/>
 *                   &lt;element name="Anspruchsende" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type" minOccurs="0"/>
 *                   &lt;element name="Mutationscode" type="{http://www.zas.admin.ch/RC}Mutationscode_Type" minOccurs="0"/>
 *                   &lt;element name="Monatsbetrag" type="{http://www.zas.admin.ch/RC}BetragFR_Type" minOccurs="0"/>
 *                   &lt;element name="Berechnungsgrundlagen" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Niveaujahr" type="{http://www.zas.admin.ch/RC}AHVDatumJ_Type" minOccurs="0"/>
 *                             &lt;element name="SkalaBerechnung" type="{http://www.zas.admin.ch/RC}SkalaBerechnung_WeakType" minOccurs="0"/>
 *                             &lt;element name="DJEBeschreibung" type="{http://www.zas.admin.ch/RC}DJE10Beschreibung_WeakType" minOccurs="0"/>
 *                             &lt;element name="Gutschriften" type="{http://www.zas.admin.ch/RC}Gutschriften10_Type" minOccurs="0"/>
 *                             &lt;element name="IVDaten" type="{http://www.zas.admin.ch/RC}IVDaten10_WeakType" minOccurs="0"/>
 *                             &lt;element name="FlexiblesRentenAlter" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;choice>
 *                                       &lt;element name="Rentenvorbezug" type="{http://www.zas.admin.ch/RC}Rentenvorbezug_WeakType" minOccurs="0"/>
 *                                       &lt;element name="Rentenaufschub" type="{http://www.zas.admin.ch/RC}Rentenaufschub_WeakType" minOccurs="0"/>
 *                                     &lt;/choice>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="SonderfallcodeRente" type="{http://www.zas.admin.ch/RC}SonderfallcodeRente_Type" maxOccurs="5" minOccurs="0"/>
 *                   &lt;element name="KuerzungSelbstverschulden" type="{http://www.zas.admin.ch/RC}KuerzungSelbstverschulden_Type" minOccurs="0"/>
 *                   &lt;element name="IstInvaliderHinterlassener" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="Berichtsmonat" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AenderungsmeldungO10_Type", propOrder = { "kasseZweigstelle", "meldungsnummer",
        "kasseneigenerHinweis", "leistungsberechtigtePerson", "leistungsbeschreibung", "berichtsmonat" })
public class AenderungsmeldungO10Type {

    @XmlElement(name = "KasseZweigstelle", required = true)
    protected String kasseZweigstelle;
    @XmlElement(name = "Meldungsnummer")
    protected long meldungsnummer;
    @XmlElement(name = "KasseneigenerHinweis")
    protected String kasseneigenerHinweis;
    @XmlElement(name = "LeistungsberechtigtePerson", required = true)
    protected RRLeistungsberechtigtePersonAuslWeakType leistungsberechtigtePerson;
    @XmlElement(name = "Leistungsbeschreibung", required = true)
    protected AenderungsmeldungO10Type.Leistungsbeschreibung leistungsbeschreibung;
    @XmlElement(name = "Berichtsmonat", required = true)
    protected XMLGregorianCalendar berichtsmonat;

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
     *         possible object is {@link RRLeistungsberechtigtePersonAuslWeakType }
     * 
     */
    public RRLeistungsberechtigtePersonAuslWeakType getLeistungsberechtigtePerson() {
        return leistungsberechtigtePerson;
    }

    /**
     * Sets the value of the leistungsberechtigtePerson property.
     * 
     * @param value
     *            allowed object is {@link RRLeistungsberechtigtePersonAuslWeakType }
     * 
     */
    public void setLeistungsberechtigtePerson(RRLeistungsberechtigtePersonAuslWeakType value) {
        leistungsberechtigtePerson = value;
    }

    /**
     * Gets the value of the leistungsbeschreibung property.
     * 
     * @return
     *         possible object is {@link AenderungsmeldungO10Type.Leistungsbeschreibung }
     * 
     */
    public AenderungsmeldungO10Type.Leistungsbeschreibung getLeistungsbeschreibung() {
        return leistungsbeschreibung;
    }

    /**
     * Sets the value of the leistungsbeschreibung property.
     * 
     * @param value
     *            allowed object is {@link AenderungsmeldungO10Type.Leistungsbeschreibung }
     * 
     */
    public void setLeistungsbeschreibung(AenderungsmeldungO10Type.Leistungsbeschreibung value) {
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
     *         &lt;element name="Leistungsart" type="{http://www.zas.admin.ch/RC}OrdentlicheRente10_strongType"/>
     *         &lt;element name="Anspruchsbeginn" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type" minOccurs="0"/>
     *         &lt;element name="Anspruchsende" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type" minOccurs="0"/>
     *         &lt;element name="Mutationscode" type="{http://www.zas.admin.ch/RC}Mutationscode_Type" minOccurs="0"/>
     *         &lt;element name="Monatsbetrag" type="{http://www.zas.admin.ch/RC}BetragFR_Type" minOccurs="0"/>
     *         &lt;element name="Berechnungsgrundlagen" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Niveaujahr" type="{http://www.zas.admin.ch/RC}AHVDatumJ_Type" minOccurs="0"/>
     *                   &lt;element name="SkalaBerechnung" type="{http://www.zas.admin.ch/RC}SkalaBerechnung_WeakType" minOccurs="0"/>
     *                   &lt;element name="DJEBeschreibung" type="{http://www.zas.admin.ch/RC}DJE10Beschreibung_WeakType" minOccurs="0"/>
     *                   &lt;element name="Gutschriften" type="{http://www.zas.admin.ch/RC}Gutschriften10_Type" minOccurs="0"/>
     *                   &lt;element name="IVDaten" type="{http://www.zas.admin.ch/RC}IVDaten10_WeakType" minOccurs="0"/>
     *                   &lt;element name="FlexiblesRentenAlter" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;choice>
     *                             &lt;element name="Rentenvorbezug" type="{http://www.zas.admin.ch/RC}Rentenvorbezug_WeakType" minOccurs="0"/>
     *                             &lt;element name="Rentenaufschub" type="{http://www.zas.admin.ch/RC}Rentenaufschub_WeakType" minOccurs="0"/>
     *                           &lt;/choice>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="SonderfallcodeRente" type="{http://www.zas.admin.ch/RC}SonderfallcodeRente_Type" maxOccurs="5" minOccurs="0"/>
     *         &lt;element name="KuerzungSelbstverschulden" type="{http://www.zas.admin.ch/RC}KuerzungSelbstverschulden_Type" minOccurs="0"/>
     *         &lt;element name="IstInvaliderHinterlassener" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = { "leistungsart", "anspruchsbeginn", "anspruchsende", "mutationscode",
            "monatsbetrag", "berechnungsgrundlagen", "sonderfallcodeRente", "kuerzungSelbstverschulden",
            "istInvaliderHinterlassener" })
    public static class Leistungsbeschreibung {

        @XmlElement(name = "Leistungsart", required = true)
        protected String leistungsart;
        @XmlElement(name = "Anspruchsbeginn")
        protected XMLGregorianCalendar anspruchsbeginn;
        @XmlElement(name = "Anspruchsende")
        protected XMLGregorianCalendar anspruchsende;
        @XmlElement(name = "Mutationscode")
        protected Short mutationscode;
        @XmlElement(name = "Monatsbetrag")
        protected BigDecimal monatsbetrag;
        @XmlElement(name = "Berechnungsgrundlagen")
        protected AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen berechnungsgrundlagen;
        @XmlElement(name = "SonderfallcodeRente", type = Short.class)
        protected List<Short> sonderfallcodeRente;
        @XmlElement(name = "KuerzungSelbstverschulden")
        protected Short kuerzungSelbstverschulden;
        @XmlElement(name = "IstInvaliderHinterlassener")
        protected Boolean istInvaliderHinterlassener;

        /**
         * Gets the value of the leistungsart property.
         * 
         * @return
         *         possible object is {@link String }
         * 
         */
        public String getLeistungsart() {
            return leistungsart;
        }

        /**
         * Sets the value of the leistungsart property.
         * 
         * @param value
         *            allowed object is {@link String }
         * 
         */
        public void setLeistungsart(String value) {
            leistungsart = value;
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
         * Gets the value of the berechnungsgrundlagen property.
         * 
         * @return
         *         possible object is {@link AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen }
         * 
         */
        public AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen getBerechnungsgrundlagen() {
            return berechnungsgrundlagen;
        }

        /**
         * Sets the value of the berechnungsgrundlagen property.
         * 
         * @param value
         *            allowed object is {@link AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen }
         * 
         */
        public void setBerechnungsgrundlagen(AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen value) {
            berechnungsgrundlagen = value;
        }

        /**
         * Gets the value of the sonderfallcodeRente property.
         * 
         * <p>
         * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you
         * make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE>
         * method for the sonderfallcodeRente property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * 
         * <pre>
         * getSonderfallcodeRente().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list {@link Short }
         * 
         * 
         */
        public List<Short> getSonderfallcodeRente() {
            if (sonderfallcodeRente == null) {
                sonderfallcodeRente = new ArrayList<Short>();
            }
            return sonderfallcodeRente;
        }

        /**
         * Gets the value of the kuerzungSelbstverschulden property.
         * 
         * @return
         *         possible object is {@link Short }
         * 
         */
        public Short getKuerzungSelbstverschulden() {
            return kuerzungSelbstverschulden;
        }

        /**
         * Sets the value of the kuerzungSelbstverschulden property.
         * 
         * @param value
         *            allowed object is {@link Short }
         * 
         */
        public void setKuerzungSelbstverschulden(Short value) {
            kuerzungSelbstverschulden = value;
        }

        /**
         * Gets the value of the istInvaliderHinterlassener property.
         * 
         * @return
         *         possible object is {@link Boolean }
         * 
         */
        public Boolean isIstInvaliderHinterlassener() {
            return istInvaliderHinterlassener;
        }

        /**
         * Sets the value of the istInvaliderHinterlassener property.
         * 
         * @param value
         *            allowed object is {@link Boolean }
         * 
         */
        public void setIstInvaliderHinterlassener(Boolean value) {
            istInvaliderHinterlassener = value;
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
         *         &lt;element name="Niveaujahr" type="{http://www.zas.admin.ch/RC}AHVDatumJ_Type" minOccurs="0"/>
         *         &lt;element name="SkalaBerechnung" type="{http://www.zas.admin.ch/RC}SkalaBerechnung_WeakType" minOccurs="0"/>
         *         &lt;element name="DJEBeschreibung" type="{http://www.zas.admin.ch/RC}DJE10Beschreibung_WeakType" minOccurs="0"/>
         *         &lt;element name="Gutschriften" type="{http://www.zas.admin.ch/RC}Gutschriften10_Type" minOccurs="0"/>
         *         &lt;element name="IVDaten" type="{http://www.zas.admin.ch/RC}IVDaten10_WeakType" minOccurs="0"/>
         *         &lt;element name="FlexiblesRentenAlter" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;choice>
         *                   &lt;element name="Rentenvorbezug" type="{http://www.zas.admin.ch/RC}Rentenvorbezug_WeakType" minOccurs="0"/>
         *                   &lt;element name="Rentenaufschub" type="{http://www.zas.admin.ch/RC}Rentenaufschub_WeakType" minOccurs="0"/>
         *                 &lt;/choice>
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
        @XmlType(name = "", propOrder = { "niveaujahr", "skalaBerechnung", "djeBeschreibung", "gutschriften",
                "ivDaten", "flexiblesRentenAlter" })
        public static class Berechnungsgrundlagen {

            @XmlElement(name = "Niveaujahr")
            protected XMLGregorianCalendar niveaujahr;
            @XmlElement(name = "SkalaBerechnung")
            protected SkalaBerechnungWeakType skalaBerechnung;
            @XmlElement(name = "DJEBeschreibung")
            protected DJE10BeschreibungWeakType djeBeschreibung;
            @XmlElement(name = "Gutschriften")
            protected Gutschriften10Type gutschriften;
            @XmlElement(name = "IVDaten")
            protected IVDaten10WeakType ivDaten;
            @XmlElement(name = "FlexiblesRentenAlter")
            protected AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter flexiblesRentenAlter;

            /**
             * Gets the value of the niveaujahr property.
             * 
             * @return
             *         possible object is {@link XMLGregorianCalendar }
             * 
             */
            public XMLGregorianCalendar getNiveaujahr() {
                return niveaujahr;
            }

            /**
             * Sets the value of the niveaujahr property.
             * 
             * @param value
             *            allowed object is {@link XMLGregorianCalendar }
             * 
             */
            public void setNiveaujahr(XMLGregorianCalendar value) {
                niveaujahr = value;
            }

            /**
             * Gets the value of the skalaBerechnung property.
             * 
             * @return
             *         possible object is {@link SkalaBerechnungWeakType }
             * 
             */
            public SkalaBerechnungWeakType getSkalaBerechnung() {
                return skalaBerechnung;
            }

            /**
             * Sets the value of the skalaBerechnung property.
             * 
             * @param value
             *            allowed object is {@link SkalaBerechnungWeakType }
             * 
             */
            public void setSkalaBerechnung(SkalaBerechnungWeakType value) {
                skalaBerechnung = value;
            }

            /**
             * Gets the value of the djeBeschreibung property.
             * 
             * @return
             *         possible object is {@link DJE10BeschreibungWeakType }
             * 
             */
            public DJE10BeschreibungWeakType getDJEBeschreibung() {
                return djeBeschreibung;
            }

            /**
             * Sets the value of the djeBeschreibung property.
             * 
             * @param value
             *            allowed object is {@link DJE10BeschreibungWeakType }
             * 
             */
            public void setDJEBeschreibung(DJE10BeschreibungWeakType value) {
                djeBeschreibung = value;
            }

            /**
             * Gets the value of the gutschriften property.
             * 
             * @return
             *         possible object is {@link Gutschriften10Type }
             * 
             */
            public Gutschriften10Type getGutschriften() {
                return gutschriften;
            }

            /**
             * Sets the value of the gutschriften property.
             * 
             * @param value
             *            allowed object is {@link Gutschriften10Type }
             * 
             */
            public void setGutschriften(Gutschriften10Type value) {
                gutschriften = value;
            }

            /**
             * Gets the value of the ivDaten property.
             * 
             * @return
             *         possible object is {@link IVDaten10WeakType }
             * 
             */
            public IVDaten10WeakType getIVDaten() {
                return ivDaten;
            }

            /**
             * Sets the value of the ivDaten property.
             * 
             * @param value
             *            allowed object is {@link IVDaten10WeakType }
             * 
             */
            public void setIVDaten(IVDaten10WeakType value) {
                ivDaten = value;
            }

            /**
             * Gets the value of the flexiblesRentenAlter property.
             * 
             * @return
             *         possible object is
             *         {@link AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter }
             * 
             */
            public AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter getFlexiblesRentenAlter() {
                return flexiblesRentenAlter;
            }

            /**
             * Sets the value of the flexiblesRentenAlter property.
             * 
             * @param value
             *            allowed object is
             *            {@link AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter }
             * 
             */
            public void setFlexiblesRentenAlter(
                    AenderungsmeldungO10Type.Leistungsbeschreibung.Berechnungsgrundlagen.FlexiblesRentenAlter value) {
                flexiblesRentenAlter = value;
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
             *         &lt;element name="Rentenvorbezug" type="{http://www.zas.admin.ch/RC}Rentenvorbezug_WeakType" minOccurs="0"/>
             *         &lt;element name="Rentenaufschub" type="{http://www.zas.admin.ch/RC}Rentenaufschub_WeakType" minOccurs="0"/>
             *       &lt;/choice>
             *     &lt;/restriction>
             *   &lt;/complexContent>
             * &lt;/complexType>
             * </pre>
             * 
             * 
             */
            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = { "rentenvorbezug", "rentenaufschub" })
            public static class FlexiblesRentenAlter {

                @XmlElement(name = "Rentenvorbezug")
                protected RentenvorbezugWeakType rentenvorbezug;
                @XmlElement(name = "Rentenaufschub")
                protected RentenaufschubWeakType rentenaufschub;

                /**
                 * Gets the value of the rentenvorbezug property.
                 * 
                 * @return
                 *         possible object is {@link RentenvorbezugWeakType }
                 * 
                 */
                public RentenvorbezugWeakType getRentenvorbezug() {
                    return rentenvorbezug;
                }

                /**
                 * Sets the value of the rentenvorbezug property.
                 * 
                 * @param value
                 *            allowed object is {@link RentenvorbezugWeakType }
                 * 
                 */
                public void setRentenvorbezug(RentenvorbezugWeakType value) {
                    rentenvorbezug = value;
                }

                /**
                 * Gets the value of the rentenaufschub property.
                 * 
                 * @return
                 *         possible object is {@link RentenaufschubWeakType }
                 * 
                 */
                public RentenaufschubWeakType getRentenaufschub() {
                    return rentenaufschub;
                }

                /**
                 * Sets the value of the rentenaufschub property.
                 * 
                 * @param value
                 *            allowed object is {@link RentenaufschubWeakType }
                 * 
                 */
                public void setRentenaufschub(RentenaufschubWeakType value) {
                    rentenaufschub = value;
                }

            }

        }

    }

}
