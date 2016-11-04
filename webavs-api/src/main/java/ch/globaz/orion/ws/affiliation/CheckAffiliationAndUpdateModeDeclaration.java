
package ch.globaz.orion.ws.affiliation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for checkAffiliationAndUpdateModeDeclaration complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="checkAffiliationAndUpdateModeDeclaration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="numeroAffilie" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modeDeclarationSalaire" type="{http://affiliation.ws.orion.globaz.ch/}modeDeclarationSalaire" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "checkAffiliationAndUpdateModeDeclaration", propOrder = {
    "numeroAffilie",
    "modeDeclarationSalaire"
})
public class CheckAffiliationAndUpdateModeDeclaration {

    protected String numeroAffilie;
    protected ModeDeclarationSalaire modeDeclarationSalaire;

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
     * Gets the value of the modeDeclarationSalaire property.
     * 
     * @return
     *     possible object is
     *     {@link ModeDeclarationSalaire }
     *     
     */
    public ModeDeclarationSalaire getModeDeclarationSalaire() {
        return modeDeclarationSalaire;
    }

    /**
     * Sets the value of the modeDeclarationSalaire property.
     * 
     * @param value
     *     allowed object is
     *     {@link ModeDeclarationSalaire }
     *     
     */
    public void setModeDeclarationSalaire(ModeDeclarationSalaire value) {
        this.modeDeclarationSalaire = value;
    }

}
