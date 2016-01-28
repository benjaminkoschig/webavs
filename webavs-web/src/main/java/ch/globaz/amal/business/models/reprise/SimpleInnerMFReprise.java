/**
 * 
 */
package ch.globaz.amal.business.models.reprise;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DHI
 * 
 */
public class SimpleInnerMFReprise extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idInnerMF = null;
    private String idTiers = null;

    /**
	 * 
	 */
    public SimpleInnerMFReprise() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idInnerMF;
    }

    /**
     * @return the idInnerMF
     */
    public String getIdInnerMF() {
        return idInnerMF;
    }

    /**
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idInnerMF = id;
    }

    /**
     * @param idInnerMF
     *            the idInnerMF to set
     */
    public void setIdInnerMF(String idInnerMF) {
        this.idInnerMF = idInnerMF;
    }

    /**
     * @param idTiers
     *            the idTiers to set
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

}
