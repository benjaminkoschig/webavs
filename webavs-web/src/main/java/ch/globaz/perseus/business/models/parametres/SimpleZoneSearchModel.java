/**
 * 
 */
package ch.globaz.perseus.business.models.parametres;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimpleZoneSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String likeDesignation = null;

    /**
	 * 
	 */
    public SimpleZoneSearchModel() {
        super();
    }

    /**
     * @return the likeDesignation
     */
    public String getLikeDesignation() {
        return likeDesignation;
    }

    /**
     * @param likeDesignation
     *            the likeDesignation to set
     */
    public void setLikeDesignation(String likeDesignation) {
        this.likeDesignation = likeDesignation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return SimpleZone.class;
    }

}
