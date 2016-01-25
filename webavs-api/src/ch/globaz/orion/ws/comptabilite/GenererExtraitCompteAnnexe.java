
package ch.globaz.orion.ws.comptabilite;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for genererExtraitCompteAnnexe complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="genererExtraitCompteAnnexe">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idCompteAnnexe" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateDebut" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dateFin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "genererExtraitCompteAnnexe", propOrder = {
    "idCompteAnnexe",
    "dateDebut",
    "dateFin",
    "langue"
})
public class GenererExtraitCompteAnnexe {

    protected String idCompteAnnexe;
    protected String dateDebut;
    protected String dateFin;
    protected String langue;

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
     * Gets the value of the dateDebut property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateDebut() {
        return dateDebut;
    }

    /**
     * Sets the value of the dateDebut property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateDebut(String value) {
        this.dateDebut = value;
    }

    /**
     * Gets the value of the dateFin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateFin() {
        return dateFin;
    }

    /**
     * Sets the value of the dateFin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateFin(String value) {
        this.dateFin = value;
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
