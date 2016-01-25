
package ch.globaz.orion.ws.comptabilite;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for listerApercuCompteAnnexe complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="listerApercuCompteAnnexe">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numeroAffilie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="langue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "listerApercuCompteAnnexe", propOrder = {
    "numeroAffilie",
    "langue"
})
public class ListerApercuCompteAnnexe {

    protected String numeroAffilie;
    protected String langue;

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
     * Gets the value of the langue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLangue() {
        return langue;
    }

    /**
     * Sets the value of the langue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLangue(String value) {
        this.langue = value;
    }

}
