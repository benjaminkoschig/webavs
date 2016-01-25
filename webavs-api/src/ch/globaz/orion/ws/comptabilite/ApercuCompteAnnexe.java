
package ch.globaz.orion.ws.comptabilite;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for apercuCompteAnnexe complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="apercuCompteAnnexe">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idCompteAnnexe" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="libelle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="solde" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "apercuCompteAnnexe", propOrder = {
    "idCompteAnnexe",
    "libelle",
    "solde"
})
public class ApercuCompteAnnexe {

    protected String idCompteAnnexe;
    protected String libelle;
    protected String solde;

    /**
     * Gets the value of the idCompteAnnexe property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCompteAnnexe() {
        return idCompteAnnexe;
    }

    /**
     * Sets the value of the idCompteAnnexe property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCompteAnnexe(String value) {
        this.idCompteAnnexe = value;
    }

    /**
     * Gets the value of the libelle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelle() {
        return libelle;
    }

    /**
     * Sets the value of the libelle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelle(String value) {
        this.libelle = value;
    }

    /**
     * Gets the value of the solde property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSolde() {
        return solde;
    }

    /**
     * Sets the value of the solde property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSolde(String value) {
        this.solde = value;
    }

}
