
package ch.globaz.orion.ws.cotisation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for massesForAffilie complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="massesForAffilie">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="noAffilieFormatte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idAffiliation" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="raisonSociale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="masses" type="{http://cotisation.ws.orion.globaz.ch/}masse" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "massesForAffilie", propOrder = {
    "noAffilieFormatte",
    "idAffiliation",
    "raisonSociale",
    "masses"
})
public class MassesForAffilie {

    protected String noAffilieFormatte;
    protected int idAffiliation;
    protected String raisonSociale;
    @XmlElement(nillable = true)
    protected List<Masse> masses;

    /**
     * Gets the value of the noAffilieFormatte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoAffilieFormatte() {
        return noAffilieFormatte;
    }

    /**
     * Sets the value of the noAffilieFormatte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoAffilieFormatte(String value) {
        this.noAffilieFormatte = value;
    }

    /**
     * Gets the value of the idAffiliation property.
     * 
     */
    public int getIdAffiliation() {
        return idAffiliation;
    }

    /**
     * Sets the value of the idAffiliation property.
     * 
     */
    public void setIdAffiliation(int value) {
        this.idAffiliation = value;
    }

    /**
     * Gets the value of the raisonSociale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRaisonSociale() {
        return raisonSociale;
    }

    /**
     * Sets the value of the raisonSociale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRaisonSociale(String value) {
        this.raisonSociale = value;
    }

    /**
     * Gets the value of the masses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the masses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMasses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Masse }
     * 
     * 
     */
    public List<Masse> getMasses() {
        if (masses == null) {
            masses = new ArrayList<Masse>();
        }
        return this.masses;
    }

}
