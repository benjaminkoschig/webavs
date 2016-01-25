
package ch.globaz.orion.ws.affiliation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for findCategorieAffiliation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="findCategorieAffiliation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NumeroAffilie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DateDebutPeriode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DateFinPeriode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findCategorieAffiliation", propOrder = {
    "numeroAffilie",
    "dateDebutPeriode",
    "dateFinPeriode"
})
public class FindCategorieAffiliation {

    @XmlElement(name = "NumeroAffilie")
    protected String numeroAffilie;
    @XmlElement(name = "DateDebutPeriode")
    protected String dateDebutPeriode;
    @XmlElement(name = "DateFinPeriode")
    protected String dateFinPeriode;

    /**
     * Gets the value of the numeroAffilie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * Sets the value of the numeroAffilie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroAffilie(String value) {
        this.numeroAffilie = value;
    }

    /**
     * Gets the value of the dateDebutPeriode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    /**
     * Sets the value of the dateDebutPeriode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateDebutPeriode(String value) {
        this.dateDebutPeriode = value;
    }

    /**
     * Gets the value of the dateFinPeriode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    /**
     * Sets the value of the dateFinPeriode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateFinPeriode(String value) {
        this.dateFinPeriode = value;
    }

}
