package ch.globaz.orion.ws.affiliation.jaxws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "findCategorieAffiliation", namespace = "http://affiliation.ws.orion.globaz.ch/")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findCategorieAffiliation", namespace = "http://affiliation.ws.orion.globaz.ch/", propOrder = {
        "numeroAffilie", "dateDebutPeriode", "dateFinPeriode" })
public class FindCategorieAffiliation {

    @XmlElement(name = "NumeroAffilie", namespace = "")
    private String numeroAffilie;
    @XmlElement(name = "DateDebutPeriode", namespace = "")
    private String dateDebutPeriode;
    @XmlElement(name = "DateFinPeriode", namespace = "")
    private String dateFinPeriode;

    /**
     * 
     * @return
     *         returns String
     */
    public String getNumeroAffilie() {
        return numeroAffilie;
    }

    /**
     * 
     * @param numeroAffilie
     *            the value for the numeroAffilie property
     */
    public void setNumeroAffilie(String numeroAffilie) {
        this.numeroAffilie = numeroAffilie;
    }

    /**
     * 
     * @return
     *         returns String
     */
    public String getDateDebutPeriode() {
        return dateDebutPeriode;
    }

    /**
     * 
     * @param dateDebutPeriode
     *            the value for the dateDebutPeriode property
     */
    public void setDateDebutPeriode(String dateDebutPeriode) {
        this.dateDebutPeriode = dateDebutPeriode;
    }

    /**
     * 
     * @return
     *         returns String
     */
    public String getDateFinPeriode() {
        return dateFinPeriode;
    }

    /**
     * 
     * @param dateFinPeriode
     *            the value for the dateFinPeriode property
     */
    public void setDateFinPeriode(String dateFinPeriode) {
        this.dateFinPeriode = dateFinPeriode;
    }

}
