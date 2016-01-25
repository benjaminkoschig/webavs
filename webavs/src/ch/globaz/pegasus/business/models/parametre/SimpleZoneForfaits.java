package ch.globaz.pegasus.business.models.parametre;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleZoneForfaits extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csCanton = null;
    private String designation = null;
    private String idZoneForfait = null;

    /**
     * @return the csCanton
     */
    public String getCsCanton() {
        return csCanton;
    }

    /**
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    @Override
    public String getId() {
        return idZoneForfait;
    }

    /**
     * @return the idZoneForfait
     */
    public String getIdZoneForfait() {
        return idZoneForfait;
    }

    /**
     * @param csCanton
     *            the csCanton to set
     */
    public void setCsCanton(String csCanton) {
        this.csCanton = csCanton;
    }

    /**
     * @param designation
     *            the designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @Override
    public void setId(String id) {
        idZoneForfait = id;

    }

    /**
     * @param idZoneForfait
     *            the idZoneForfait to set
     */
    public void setIdZoneForfait(String idZoneForfait) {
        this.idZoneForfait = idZoneForfait;
    }
}
