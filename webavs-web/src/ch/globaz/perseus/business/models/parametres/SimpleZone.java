/**
 * 
 */
package ch.globaz.perseus.business.models.parametres;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimpleZone extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String designation = null;
    private String idZone = null;

    /**
	 * 
	 */
    public SimpleZone() {
        super();
    }

    /**
     * @return the designation
     */
    public String getDesignation() {
        return designation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idZone;
    }

    /**
     * @return the idZone
     */
    public String getIdZone() {
        return idZone;
    }

    /**
     * @param designation
     *            the designation to set
     */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idZone = id;
    }

    /**
     * @param idZone
     *            the idZone to set
     */
    public void setIdZone(String idZone) {
        this.idZone = idZone;
    }

}
