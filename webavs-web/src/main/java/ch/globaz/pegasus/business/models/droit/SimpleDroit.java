package ch.globaz.pegasus.business.models.droit;

import globaz.jade.persistence.model.JadeSimpleModel;

public class SimpleDroit extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String idDemandePC = null;
    private String idDroit = null;

    @Override
    public String getId() {
        return idDroit;
    }

    /**
     * @return the idDemandePC
     */
    public String getIdDemandePC() {
        return idDemandePC;
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return idDroit;
    }

    @Override
    public void setId(String id) {
        idDroit = id;
    }

    /**
     * @param idDemandePC
     *            the idDemandePC to set
     */
    public void setIdDemandePC(String idDemandePC) {
        this.idDemandePC = idDemandePC;
    }

    /**
     * @param idDroit
     *            the idDroit to set
     */
    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

}
