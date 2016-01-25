package ch.globaz.vulpecula.comptabilite.importationcg.xmlobject;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Description de la classe
 * 
 * @since WebBMS 0.5
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "mandat")
public class Mandat {
    @XmlAttribute(name = "value", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String value;

    public Mandat() {
        super();
    }

    public Mandat(String string) {
        value = string;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *         possible object is {@link String }
     * 
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setValue(String value) {
        this.value = value;
    }
}
