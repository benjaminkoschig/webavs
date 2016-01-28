/**
 * 
 */
package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimpleRequerant extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idMembreFamille = null;
    private String idRequerant = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idRequerant;
    }

    /**
     * @return the idMembreFamille
     */
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /**
     * @return the idRequerant
     */
    public String getIdRequerant() {
        return idRequerant;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idRequerant = id;
    }

    /**
     * @param idMembreFamille
     *            the idMembreFamille to set
     */
    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    /**
     * @param idRequerant
     *            the idRequerant to set
     */
    public void setIdRequerant(String idRequerant) {
        this.idRequerant = idRequerant;
    }

}
