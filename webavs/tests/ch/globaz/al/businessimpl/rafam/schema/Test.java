//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in
// JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.04.25 at 02:41:04 PM CEST
//

package ch.globaz.al.businessimpl.rafam.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *       &lt;sequence>
 *         &lt;element ref="{}description"/>
 *         &lt;element ref="{}idDroit"/>
 *         &lt;element ref="{}actions"/>
 *         &lt;element ref="{}validations"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}unsignedInt" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "description", "idDroit", "actions", "validations" })
@XmlRootElement(name = "test")
public class Test {

    @XmlElement(required = true)
    protected String description;
    @XmlSchemaType(name = "unsignedInt")
    protected long idDroit;
    @XmlElement(required = true)
    protected Actions actions;
    @XmlElement(required = true)
    protected Validations validations;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "unsignedInt")
    protected long id;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDescription(String value) {
        description = value;
    }

    /**
     * Gets the value of the idDroit property.
     * 
     */
    public long getIdDroit() {
        return idDroit;
    }

    /**
     * Sets the value of the idDroit property.
     * 
     */
    public void setIdDroit(long value) {
        idDroit = value;
    }

    /**
     * Gets the value of the actions property.
     * 
     * @return
     *         possible object is {@link Actions }
     * 
     */
    public Actions getActions() {
        return actions;
    }

    /**
     * Sets the value of the actions property.
     * 
     * @param value
     *            allowed object is {@link Actions }
     * 
     */
    public void setActions(Actions value) {
        actions = value;
    }

    /**
     * Gets the value of the validations property.
     * 
     * @return
     *         possible object is {@link Validations }
     * 
     */
    public Validations getValidations() {
        return validations;
    }

    /**
     * Sets the value of the validations property.
     * 
     * @param value
     *            allowed object is {@link Validations }
     * 
     */
    public void setValidations(Validations value) {
        validations = value;
    }

    /**
     * Gets the value of the id property.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(long value) {
        id = value;
    }

}
