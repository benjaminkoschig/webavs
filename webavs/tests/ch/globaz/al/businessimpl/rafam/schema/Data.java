//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in
// JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.04.25 at 02:41:04 PM CEST
//

package ch.globaz.al.businessimpl.rafam.schema;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}etatDroit" minOccurs="0"/>
 *         &lt;element ref="{}statutFamilial" minOccurs="0"/>
 *         &lt;element ref="{}debutDroit" minOccurs="0"/>
 *         &lt;element ref="{}finDroitForcee" minOccurs="0"/>
 *         &lt;element ref="{}montantForce" minOccurs="0"/>
 *         &lt;element ref="{}force" minOccurs="0"/>
 *         &lt;element ref="{}supplementFnb" minOccurs="0"/>
 *         &lt;element ref="{}capableExercer" minOccurs="0"/>
 *         &lt;element ref="{}allocationNaissanceVersee" minOccurs="0"/>
 *         &lt;element ref="{}typeAllocationNaissance" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "data")
public class Data {

    protected String etatDroit;
    protected String statutFamilial;
    protected String debutDroit;
    protected String finDroitForcee;
    protected BigDecimal montantForce;
    protected Boolean force;
    protected Boolean supplementFnb;
    protected Boolean capableExercer;
    protected Boolean allocationNaissanceVersee;
    protected String typeAllocationNaissance;

    /**
     * Gets the value of the etatDroit property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getEtatDroit() {
        return etatDroit;
    }

    /**
     * Sets the value of the etatDroit property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setEtatDroit(String value) {
        etatDroit = value;
    }

    /**
     * Gets the value of the statutFamilial property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getStatutFamilial() {
        return statutFamilial;
    }

    /**
     * Sets the value of the statutFamilial property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setStatutFamilial(String value) {
        statutFamilial = value;
    }

    /**
     * Gets the value of the debutDroit property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getDebutDroit() {
        return debutDroit;
    }

    /**
     * Sets the value of the debutDroit property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDebutDroit(String value) {
        debutDroit = value;
    }

    /**
     * Gets the value of the finDroitForcee property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getFinDroitForcee() {
        return finDroitForcee;
    }

    /**
     * Sets the value of the finDroitForcee property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setFinDroitForcee(String value) {
        finDroitForcee = value;
    }

    /**
     * Gets the value of the montantForce property.
     * 
     * @return
     *         possible object is {@link BigDecimal }
     * 
     */
    public BigDecimal getMontantForce() {
        return montantForce;
    }

    /**
     * Sets the value of the montantForce property.
     * 
     * @param value
     *            allowed object is {@link BigDecimal }
     * 
     */
    public void setMontantForce(BigDecimal value) {
        montantForce = value;
    }

    /**
     * Gets the value of the force property.
     * 
     * @return
     *         possible object is {@link Boolean }
     * 
     */
    public Boolean isForce() {
        return force;
    }

    /**
     * Sets the value of the force property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setForce(Boolean value) {
        force = value;
    }

    /**
     * Gets the value of the supplementFnb property.
     * 
     * @return
     *         possible object is {@link Boolean }
     * 
     */
    public Boolean isSupplementFnb() {
        return supplementFnb;
    }

    /**
     * Sets the value of the supplementFnb property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setSupplementFnb(Boolean value) {
        supplementFnb = value;
    }

    /**
     * Gets the value of the capableExercer property.
     * 
     * @return
     *         possible object is {@link Boolean }
     * 
     */
    public Boolean isCapableExercer() {
        return capableExercer;
    }

    /**
     * Sets the value of the capableExercer property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setCapableExercer(Boolean value) {
        capableExercer = value;
    }

    /**
     * Gets the value of the allocationNaissanceVersee property.
     * 
     * @return
     *         possible object is {@link Boolean }
     * 
     */
    public Boolean isAllocationNaissanceVersee() {
        return allocationNaissanceVersee;
    }

    /**
     * Sets the value of the allocationNaissanceVersee property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setAllocationNaissanceVersee(Boolean value) {
        allocationNaissanceVersee = value;
    }

    /**
     * Gets the value of the typeAllocationNaissance property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getTypeAllocationNaissance() {
        return typeAllocationNaissance;
    }

    /**
     * Sets the value of the typeAllocationNaissance property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTypeAllocationNaissance(String value) {
        typeAllocationNaissance = value;
    }

}
