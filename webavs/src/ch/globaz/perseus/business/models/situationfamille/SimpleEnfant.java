/**
 * 
 */
package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * @author DDE
 * 
 */
public class SimpleEnfant extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idEnfant = null;
    private String idMembreFamille = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idEnfant;
    }

    /**
     * @return the idEnfant
     */
    public String getIdEnfant() {
        return idEnfant;
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
        idEnfant = id;
    }

    /**
     * @param idEnfant
     *            the idEnfant to set
     */
    public void setIdEnfant(String idEnfant) {
        this.idEnfant = idEnfant;
    }

    /**
     * @param idMembreFamille
     *            the idMembreFamille to set
     */
    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

}
