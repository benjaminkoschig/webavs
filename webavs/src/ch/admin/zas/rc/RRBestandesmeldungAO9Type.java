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
 * Annonce de l'�tat des rentes
 * 
 * <p>
 * Java class for RRBestandesmeldungAO9_Type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RRBestandesmeldungAO9_Type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="KasseZweigstelle" type="{http://www.zas.admin.ch/RC}IrgendeineZweigstelle_weakType"/>
 *         &lt;element name="Meldungsnummer" type="{http://www.zas.admin.ch/RC}Meldungsnummer_Type" minOccurs="0"/>
 *         &lt;element name="KasseneigenerHinweis" type="{http://www.zas.admin.ch/RC}KasseneigenerHinweis_Type" minOccurs="0"/>
 *         &lt;element name="LeistungsberechtigtePerson" type="{http://www.zas.admin.ch/RC}RRLeistungsberechtigtePersonAusl_Type"/>
 *         &lt;element name="Leistungsbeschreibung">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Leistungsart" type="{http://www.zas.admin.ch/RC}AusserordentlicheRente9_strongType"/>
 *                   &lt;element name="Anspruchsbeginn" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type"/>
 *                   &lt;element name="Anspruchsende" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type" minOccurs="0"/>
 *                   &lt;element name="Mutationscode" type="{http://www.zas.admin.ch/RC}Mutationscode_Type" minOccurs="0"/>
 *                   &lt;element name="Monatsbetrag" type="{http://www.zas.admin.ch/RC}BetragFR_Type"/>
 *                   &lt;element name="MonatsbetragErsetzteOrdentlicheRente" type="{http://www.zas.admin.ch/RC}BetragFR_Type" minOccurs="0"/>
 *                   &lt;element name="Berechnungsgrundlagen">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="Niveaujahr" type="{http://www.zas.admin.ch/RC}AHVDatumJ_Type" minOccurs="0"/>
 *                             &lt;element name="EinkommensgrenzenCode" type="{http://www.zas.admin.ch/RC}EinkommensgrenzenCode_Type"/>
 *                             &lt;element name="MinimalgarantieCode" type="{http://www.zas.admin.ch/RC}MinimalgarantieCode_Type"/>
 *                             &lt;element name="SkalaBerechnung" type="{http://www.zas.admin.ch/RC}SkalaBerechnung_Type" minOccurs="0"/>
 *                             &lt;element name="DJEBeschreibung" type="{http://www.zas.admin.ch/RC}DJE9Beschreibung_Type" minOccurs="0"/>
 *                             &lt;element name="Gutschriften" type="{http://www.zas.admin.ch/RC}Gutschriften9_Type" minOccurs="0"/>
 *                             &lt;element name="IVDaten" type="{http://www.zas.admin.ch/RC}IVDaten9_Type" minOccurs="0"/>
 *                             &lt;element name="IVDatenEhefrau" type="{http://www.zas.admin.ch/RC}IVDaten9_Type" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="SonderfallcodeRente" type="{http://www.zas.admin.ch/RC}SonderfallcodeRente_Type" maxOccurs="5" minOccurs="0"/>
 *                   &lt;element name="KuerzungSelbstverschulden" type="{http://www.zas.admin.ch/RC}KuerzungSelbstverschulden_Type" minOccurs="0"/>
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
@XmlType(name = "RRBestandesmeldungAO9_Type", propOrder = { "kasseZweigstelle", "meldungsnummer",
        "kasseneigenerHinweis", "leistungsberechtigtePerson", "leistungsbeschreibung", "berichtsmonat" })
public class RRBestandesmeldungAO9Type {

    @XmlElement(name = "KasseZweigstelle", required = true)
    protected String kasseZweigstelle;
    @XmlElement(name = "Meldungsnummer")
    protected Long meldungsnummer;
    @XmlElement(name = "KasseneigenerHinweis")
    protected String kasseneigenerHinweis;
    @XmlElement(name = "LeistungsberechtigtePerson", required = true)
    protected RRLeistungsberechtigtePersonAuslType leistungsberechtigtePerson;
    @XmlElement(name = "Leistungsbeschreibung", required = true)
    protected RRBestandesmeldungAO9Type.Leistungsbeschreibung leistungsbeschreibung;
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
     * @return
     *         possible object is {@link Long }
     * 
     */
    public Long getMeldungsnummer() {
        return meldungsnummer;
    }

    /**
     * Sets the value of the meldungsnummer property.
     * 
     * @param value
     *            allowed object is {@link Long }
     * 
     */
    public void setMeldungsnummer(Long value) {
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
     *         possible object is {@link RRLeistungsberechtigtePersonAuslType }
     * 
     */
    public RRLeistungsberechtigtePersonAuslType getLeistungsberechtigtePerson() {
        return leistungsberechtigtePerson;
    }

    /**
     * Sets the value of the leistungsberechtigtePerson property.
     * 
     * @param value
     *            allowed object is {@link RRLeistungsberechtigtePersonAuslType }
     * 
     */
    public void setLeistungsberechtigtePerson(RRLeistungsberechtigtePersonAuslType value) {
        leistungsberechtigtePerson = value;
    }

    /**
     * Gets the value of the leistungsbeschreibung property.
     * 
     * @return
     *         possible object is {@link RRBestandesmeldungAO9Type.Leistungsbeschreibung }
     * 
     */
    public RRBestandesmeldungAO9Type.Leistungsbeschreibung getLeistungsbeschreibung() {
        return leistungsbeschreibung;
    }

    /**
     * Sets the value of the leistungsbeschreibung property.
     * 
     * @param value
     *            allowed object is {@link RRBestandesmeldungAO9Type.Leistungsbeschreibung }
     * 
     */
    public void setLeistungsbeschreibung(RRBestandesmeldungAO9Type.Leistungsbeschreibung value) {
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
     *         &lt;element name="Leistungsart" type="{http://www.zas.admin.ch/RC}AusserordentlicheRente9_strongType"/>
     *         &lt;element name="Anspruchsbeginn" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type"/>
     *         &lt;element name="Anspruchsende" type="{http://www.zas.admin.ch/RC}AHVDatumJM_Type" minOccurs="0"/>
     *         &lt;element name="Mutationscode" type="{http://www.zas.admin.ch/RC}Mutationscode_Type" minOccurs="0"/>
     *         &lt;element name="Monatsbetrag" type="{http://www.zas.admin.ch/RC}BetragFR_Type"/>
     *         &lt;element name="MonatsbetragErsetzteOrdentlicheRente" type="{http://www.zas.admin.ch/RC}BetragFR_Type" minOccurs="0"/>
     *         &lt;element name="Berechnungsgrundlagen">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="Niveaujahr" type="{http://www.zas.admin.ch/RC}AHVDatumJ_Type" minOccurs="0"/>
     *                   &lt;element name="EinkommensgrenzenCode" type="{http://www.zas.admin.ch/RC}EinkommensgrenzenCode_Type"/>
     *                   &lt;element name="MinimalgarantieCode" type="{http://www.zas.admin.ch/RC}MinimalgarantieCode_Type"/>
     *                   &lt;element name="SkalaBerechnung" type="{http://www.zas.admin.ch/RC}SkalaBerechnung_Type" minOccurs="0"/>
     *                   &lt;element name="DJEBeschreibung" type="{http://www.zas.admin.ch/RC}DJE9Beschreibung_Type" minOccurs="0"/>
     *                   &lt;element name="Gutschriften" type="{http://www.zas.admin.ch/RC}Gutschriften9_Type" minOccurs="0"/>
     *                   &lt;element name="IVDaten" type="{http://www.zas.admin.ch/RC}IVDaten9_Type" minOccurs="0"/>
     *                   &lt;element name="IVDatenEhefrau" type="{http://www.zas.admin.ch/RC}IVDaten9_Type" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="SonderfallcodeRente" type="{http://www.zas.admin.ch/RC}SonderfallcodeRente_Type" maxOccurs="5" minOccurs="0"/>
     *         &lt;element name="KuerzungSelbstverschulden" type="{http://www.zas.admin.ch/RC}KuerzungSelbstverschulden_Type" minOccurs="0"/>
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
            "monatsbetrag", "monatsbetragErsetzteOrdentlicheRente", "berechnungsgrundlagen", "sonderfallcodeRente",
            "kuerzungSelbstverschulden" })
    public static class Leistungsbeschreibung {

        @XmlElement(name = "Leistungsart", required = true)
        protected String leistungsart;
        @XmlElement(name = "Anspruchsbeginn", required = true)
        protected XMLGregorianCalendar anspruchsbeginn;
        @XmlElement(name = "Anspruchsende")
        protected XMLGregorianCalendar anspruchsende;
        @XmlElement(name = "Mutationscode")
        protected Short mutationscode;
        @XmlElement(name = "Monatsbetrag", required = true)
        protected BigDecimal monatsbetrag;
        @XmlElement(name = "MonatsbetragErsetzteOrdentlicheRente")
        protected BigDecimal monatsbetragErsetzteOrdentlicheRente;
        @XmlElement(name = "Berechnungsgrundlagen", required = true)
        protected RRBestandesmeldungAO9Type.Leistungsbeschreibung.Berechnungsgrundlagen berechnungsgrundlagen;
        @XmlElement(name = "SonderfallcodeRente", type = Short.class)
        protected List<Short> sonderfallcodeRente;
        @XmlElement(name = "KuerzungSelbstverschulden")
        protected Short kuerzungSelbstverschulden;

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
         * Gets the value of the berechnungsgrundlagen property.
         * 
         * @return
         *         possible object is {@link RRBestandesmeldungAO9Type.Leistungsbeschreibung.Berechnungsgrundlagen }
         * 
         */
        public RRBestandesmeldungAO9Type.Leistungsbeschreibung.Berechnungsgrundlagen getBerechnungsgrundlagen() {
            return berechnungsgrundlagen;
        }

        /**
         * Sets the value of the berechnungsgrundlagen property.
         * 
         * @param value
         *            allowed object is {@link RRBestandesmeldungAO9Type.Leistungsbeschreibung.Berechnungsgrundlagen }
         * 
         */
        public void setBerechnungsgrundlagen(RRBestandesmeldungAO9Type.Leistungsbeschreibung.Berechnungsgrundlagen value) {
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
         *         &lt;element name="EinkommensgrenzenCode" type="{http://www.zas.admin.ch/RC}EinkommensgrenzenCode_Type"/>
         *         &lt;element name="MinimalgarantieCode" type="{http://www.zas.admin.ch/RC}MinimalgarantieCode_Type"/>
         *         &lt;element name="SkalaBerechnung" type="{http://www.zas.admin.ch/RC}SkalaBerechnung_Type" minOccurs="0"/>
         *         &lt;element name="DJEBeschreibung" type="{http://www.zas.admin.ch/RC}DJE9Beschreibung_Type" minOccurs="0"/>
         *         &lt;element name="Gutschriften" type="{http://www.zas.admin.ch/RC}Gutschriften9_Type" minOccurs="0"/>
         *         &lt;element name="IVDaten" type="{http://www.zas.admin.ch/RC}IVDaten9_Type" minOccurs="0"/>
         *         &lt;element name="IVDatenEhefrau" type="{http://www.zas.admin.ch/RC}IVDaten9_Type" minOccurs="0"/>
         *       &lt;/sequence>
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = { "niveaujahr", "einkommensgrenzenCode", "minimalgarantieCode",
                "skalaBerechnung", "djeBeschreibung", "gutschriften", "ivDaten", "ivDatenEhefrau" })
        public static class Berechnungsgrundlagen {

            @XmlElement(name = "Niveaujahr")
            protected XMLGregorianCalendar niveaujahr;
            @XmlElement(name = "EinkommensgrenzenCode")
            protected boolean einkommensgrenzenCode;
            @XmlElement(name = "MinimalgarantieCode")
            protected boolean minimalgarantieCode;
            @XmlElement(name = "SkalaBerechnung")
            protected SkalaBerechnungType skalaBerechnung;
            @XmlElement(name = "DJEBeschreibung")
            protected DJE9BeschreibungType djeBeschreibung;
            @XmlElement(name = "Gutschriften")
            protected Gutschriften9Type gutschriften;
            @XmlElement(name = "IVDaten")
            protected IVDaten9Type ivDaten;
            @XmlElement(name = "IVDatenEhefrau")
            protected IVDaten9Type ivDatenEhefrau;

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
             * Gets the value of the einkommensgrenzenCode property.
             * 
             */
            public boolean isEinkommensgrenzenCode() {
                return einkommensgrenzenCode;
            }

            /**
             * Sets the value of the einkommensgrenzenCode property.
             * 
             */
            public void setEinkommensgrenzenCode(boolean value) {
                einkommensgrenzenCode = value;
            }

            /**
             * Gets the value of the minimalgarantieCode property.
             * 
             */
            public boolean isMinimalgarantieCode() {
                return minimalgarantieCode;
            }

            /**
             * Sets the value of the minimalgarantieCode property.
             * 
             */
            public void setMinimalgarantieCode(boolean value) {
                minimalgarantieCode = value;
            }

            /**
             * Gets the value of the skalaBerechnung property.
             * 
             * @return
             *         possible object is {@link SkalaBerechnungType }
             * 
             */
            public SkalaBerechnungType getSkalaBerechnung() {
                return skalaBerechnung;
            }

            /**
             * Sets the value of the skalaBerechnung property.
             * 
             * @param value
             *            allowed object is {@link SkalaBerechnungType }
             * 
             */
            public void setSkalaBerechnung(SkalaBerechnungType value) {
                skalaBerechnung = value;
            }

            /**
             * Gets the value of the djeBeschreibung property.
             * 
             * @return
             *         possible object is {@link DJE9BeschreibungType }
             * 
             */
            public DJE9BeschreibungType getDJEBeschreibung() {
                return djeBeschreibung;
            }

            /**
             * Sets the value of the djeBeschreibung property.
             * 
             * @param value
             *            allowed object is {@link DJE9BeschreibungType }
             * 
             */
            public void setDJEBeschreibung(DJE9BeschreibungType value) {
                djeBeschreibung = value;
            }

            /**
             * Gets the value of the gutschriften property.
             * 
             * @return
             *         possible object is {@link Gutschriften9Type }
             * 
             */
            public Gutschriften9Type getGutschriften() {
                return gutschriften;
            }

            /**
             * Sets the value of the gutschriften property.
             * 
             * @param value
             *            allowed object is {@link Gutschriften9Type }
             * 
             */
            public void setGutschriften(Gutschriften9Type value) {
                gutschriften = value;
            }

            /**
             * Gets the value of the ivDaten property.
             * 
             * @return
             *         possible object is {@link IVDaten9Type }
             * 
             */
            public IVDaten9Type getIVDaten() {
                return ivDaten;
            }

            /**
             * Sets the value of the ivDaten property.
             * 
             * @param value
             *            allowed object is {@link IVDaten9Type }
             * 
             */
            public void setIVDaten(IVDaten9Type value) {
                ivDaten = value;
            }

            /**
             * Gets the value of the ivDatenEhefrau property.
             * 
             * @return
             *         possible object is {@link IVDaten9Type }
             * 
             */
            public IVDaten9Type getIVDatenEhefrau() {
                return ivDatenEhefrau;
            }

            /**
             * Sets the value of the ivDatenEhefrau property.
             * 
             * @param value
             *            allowed object is {@link IVDaten9Type }
             * 
             */
            public void setIVDatenEhefrau(IVDaten9Type value) {
                ivDatenEhefrau = value;
            }

        }

    }

}
