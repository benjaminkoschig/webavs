/**
 * 
 */
package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimpleConjoint extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idConjoint = null;
    private String idMembreFamille = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idConjoint;
    }

    /**
     * @return the idConjoint
     */
    public String getIdConjoint() {
        return idConjoint;
    }

    /**
     * @return the idMembreFamille
     */
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idConjoint = id;
    }

    /**
     * @param idConjoint
     *            the idConjoint to set
     */
    public void setIdConjoint(String idConjoint) {
        this.idConjoint = idConjoint;
    }

    /**
     * @param idMembreFamille
     *            the idMembreFamille to set
     */
    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

}
