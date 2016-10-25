
package ch.globaz.orion.ws.cotisation;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for decompteMensuel complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="decompteMensuel">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="anneeDecompte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dejaEtabli" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="idAffilie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="linesDecompte" type="{http://cotisation.ws.orion.globaz.ch/}decompteMensuelLine" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="moisDecompte" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="numeroAffilie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "decompteMensuel", propOrder = {
    "anneeDecompte",
    "dejaEtabli",
    "idAffilie",
    "linesDecompte",
    "moisDecompte",
    "numeroAffilie"
})
public class DecompteMensuel {

    protected String anneeDecompte;
    protected boolean dejaEtabli;
    protected String idAffilie;
    @XmlElement(nillable = true)
    protected List<DecompteMensuelLine> linesDecompte;
    protected String moisDecompte;
    protected String numeroAffilie;

    /**
     * Gets the value of the anneeDecompte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnneeDecompte() {
        return anneeDecompte;
    }

    /**
     * Sets the value of the anneeDecompte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnneeDecompte(String value) {
        this.anneeDecompte = value;
    }

    /**
     * Gets the value of the dejaEtabli property.
     * 
     */
    public boolean isDejaEtabli() {
        return dejaEtabli;
    }

    /**
     * Sets the value of the dejaEtabli property.
     * 
     */
    public void setDejaEtabli(boolean value) {
        this.dejaEtabli = value;
    }

    /**
     * Gets the value of the idAffilie property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAffilie() {
        return idAffilie;
    }

    /**
     * Sets the value of the idAffilie property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAffilie(String value) {
        this.idAffilie = value;
    }

    /**
     * Gets the value of the linesDecompte property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the linesDecompte property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLinesDecompte().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DecompteMensuelLine }
     * 
     * 
     */
    public List<DecompteMensuelLine> getLinesDecompte() {
        if (linesDecompte == null) {
            linesDecompte = new ArrayList<DecompteMensuelLine>();
        }
        return this.linesDecompte;
    }

    /**
     * Gets the value of the moisDecompte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMoisDecompte() {
        return moisDecompte;
    }

    /**
     * Sets the value of the moisDecompte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMoisDecompte(String value) {
        this.moisDecompte = value;
    }

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

}
