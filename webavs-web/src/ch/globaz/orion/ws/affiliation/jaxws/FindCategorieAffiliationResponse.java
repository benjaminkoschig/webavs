package ch.globaz.orion.ws.affiliation.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "findCategorieAffiliationResponse", namespace = "http://affiliation.ws.orion.globaz.ch/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findCategorieAffiliationResponse", namespace = "http://affiliation.ws.orion.globaz.ch/")
public class FindCategorieAffiliationResponse {

    @XmlElement(name = "return", namespace = "")
    private Integer _return;

    /**
     * 
     * @return
     *         returns Integer
     */
    public Integer getReturn() {
        return _return;
    }

    /**
     * 
     * @param _return
     *            the value for the _return property
     */
    public void setReturn(Integer _return) {
        this._return = _return;
    }

}
