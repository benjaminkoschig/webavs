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
 * 
 * Donn�es concernant l'ayant droit valables au d�but du mois de rapport ou du mois de fin de droit � la
 * prestation. Personne habitant en suisse ou dans le monde.
 * 
 * 
 * <p>
 * Java class for RRLeistungsberechtigtePersonCH_Type complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RRLeistungsberechtigtePersonCH_Type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.zas.admin.ch/RC}RRLeistungsberechtigtePerson_Type">
 *       &lt;sequence>
 *         &lt;element name="WohnkantonStaat" type="{http://www.zas.admin.ch/RC}KantonNrBSV_StrongType"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RRLeistungsberechtigtePersonCH_Type", propOrder = { "wohnkantonStaat" })
public class RRLeistungsberechtigtePersonCHType extends RRLeistungsberechtigtePersonType {

    @XmlElement(name = "WohnkantonStaat")
    protected int wohnkantonStaat;

    /**
     * Gets the value of the wohnkantonStaat property.
     * 
     */
    public int getWohnkantonStaat() {
        return wohnkantonStaat;
    }

    /**
     * Sets the value of the wohnkantonStaat property.
     * 
     */
    public void setWohnkantonStaat(int value) {
        wohnkantonStaat = value;
    }

}
