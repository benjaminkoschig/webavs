
package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for decompteMensuelLine complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="decompteMensuelLine">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="idCotisation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idRubrique" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="libelleDe" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="libelleFr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="libelleIt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="masse" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="typeCotisation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "decompteMensuelLine", propOrder = {
    "idCotisation",
    "idRubrique",
    "libelleDe",
    "libelleFr",
    "libelleIt",
    "masse",
    "typeCotisation"
})
public class DecompteMensuelLine {

    protected String idCotisation;
    protected int idRubrique;
    protected String libelleDe;
    protected String libelleFr;
    protected String libelleIt;
    protected BigDecimal masse;
    protected String typeCotisation;

    /**
     * Gets the value of the idCotisation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCotisation() {
        return idCotisation;
    }

    /**
     * Sets the value of the idCotisation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCotisation(String value) {
        this.idCotisation = value;
    }

    /**
     * Gets the value of the idRubrique property.
     * 
     */
    public int getIdRubrique() {
        return idRubrique;
    }

    /**
     * Sets the value of the idRubrique property.
     * 
     */
    public void setIdRubrique(int value) {
        this.idRubrique = value;
    }

    /**
     * Gets the value of the libelleDe property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleDe() {
        return libelleDe;
    }

    /**
     * Sets the value of the libelleDe property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleDe(String value) {
        this.libelleDe = value;
    }

    /**
     * Gets the value of the libelleFr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleFr() {
        return libelleFr;
    }

    /**
     * Sets the value of the libelleFr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleFr(String value) {
        this.libelleFr = value;
    }

    /**
     * Gets the value of the libelleIt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLibelleIt() {
        return libelleIt;
    }

    /**
     * Sets the value of the libelleIt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLibelleIt(String value) {
        this.libelleIt = value;
    }

    /**
     * Gets the value of the masse property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMasse() {
        return masse;
    }

    /**
     * Sets the value of the masse property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMasse(BigDecimal value) {
        this.masse = value;
    }

    /**
     * Gets the value of the typeCotisation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeCotisation() {
        return typeCotisation;
    }

    /**
     * Sets the value of the typeCotisation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeCotisation(String value) {
        this.typeCotisation = value;
    }

}
