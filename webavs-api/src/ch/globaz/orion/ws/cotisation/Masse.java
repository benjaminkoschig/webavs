
package ch.globaz.orion.ws.cotisation;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for masse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="masse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="valeur" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="libelle_fr" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="libelle_de" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="libelle_it" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idCotisation" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="typeCotisation" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="codeCanton" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "masse", propOrder = {
    "valeur",
    "libelleFr",
    "libelleDe",
    "libelleIt",
    "idCotisation",
    "typeCotisation",
    "codeCanton"
})
public class Masse {

    protected BigDecimal valeur;
    @XmlElement(name = "libelle_fr")
    protected String libelleFr;
    @XmlElement(name = "libelle_de")
    protected String libelleDe;
    @XmlElement(name = "libelle_it")
    protected String libelleIt;
    protected int idCotisation;
    protected int typeCotisation;
    protected int codeCanton;

    /**
     * Gets the value of the valeur property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getValeur() {
        return valeur;
    }

    /**
     * Sets the value of the valeur property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setValeur(BigDecimal value) {
        this.valeur = value;
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
     * Gets the value of the idCotisation property.
     * 
     */
    public int getIdCotisation() {
        return idCotisation;
    }

    /**
     * Sets the value of the idCotisation property.
     * 
     */
    public void setIdCotisation(int value) {
        this.idCotisation = value;
    }

    /**
     * Gets the value of the typeCotisation property.
     * 
     */
    public int getTypeCotisation() {
        return typeCotisation;
    }

    /**
     * Sets the value of the typeCotisation property.
     * 
     */
    public void setTypeCotisation(int value) {
        this.typeCotisation = value;
    }

    /**
     * Gets the value of the codeCanton property.
     * 
     */
    public int getCodeCanton() {
        return codeCanton;
    }

    /**
     * Sets the value of the codeCanton property.
     * 
     */
    public void setCodeCanton(int value) {
        this.codeCanton = value;
    }

}
