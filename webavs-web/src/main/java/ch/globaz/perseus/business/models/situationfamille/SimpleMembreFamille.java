package ch.globaz.perseus.business.models.situationfamille;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleMembreFamille extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idMembreFamille = null;
    private String idTiers = null;
    private Boolean isAI = false;

    public SimpleMembreFamille() {
        super();

    }

    @Override
    public String getId() {
        return idMembreFamille;
    }

    /**
     * @return the idMembreFamille
     */
    public String getIdMembreFamille() {
        return idMembreFamille;
    }

    /**
     * @return the idTiers
     */
    public String getIdTiers() {
        return idTiers;
    }

    public Boolean getIsAI() {
        return isAI;
    }

    @Override
    public void setId(String id) {
        idMembreFamille = id;
    }

    /**
     * @param idMembreFamille
     *            the idMembreFamille to set
     */
    public void setIdMembreFamille(String idMembreFamille) {
        this.idMembreFamille = idMembreFamille;
    }

    /**
     * @param idTiers
     *            the idTiers to set
     */
    public void setIdTiers(String idTiers) {
        this.idTiers = idTiers;
    }

    public void setIsAI(Boolean isAI) {
        this.isAI = isAI;
    }

}
