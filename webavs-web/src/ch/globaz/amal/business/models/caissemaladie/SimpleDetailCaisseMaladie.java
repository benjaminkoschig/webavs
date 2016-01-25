/**
 * 
 */
package ch.globaz.amal.business.models.caissemaladie;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author cbu
 * 
 */
public class SimpleDetailCaisseMaladie extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDetailCaisseMaladie = null;
    private String idTiers = null;
    private String sedexYear = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idDetailCaisseMaladie;
    }

    public String getIdDetailCaisseMaladie() {
        return idDetailCaisseMaladie;
    }

    public String getIdTiers() {
        return idTiers;
    }

    public String getSedexYear() {
        return sedexYear;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idDetailCaisseMaladie = id;

    }

    public void setIdDetailCaisseMaladie(String idDetailCaisseMaladie) {
        this.idDetailCaisseMaladie = idDetailCaisseMaladie;
    }

    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setSedexYear(String sedexYear) {
        this.sedexYear = sedexYear;
    }

}
