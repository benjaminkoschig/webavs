package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * classe modèle de la catégorie tarif
 * 
 * @author jts
 * 
 */
public class CategorieTarifFkModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Id de la catégorie de tarif
     */
    private String idCategorieTarif = null;
    /**
     * id de la législation
     * 
     * @see LegislationTarifModel
     */
    private String idLegislation = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idCategorieTarif;
    }

    /**
     * @return the idCategorieTarif
     */
    public String getIdCategorieTarif() {
        return idCategorieTarif;
    }

    /**
     * @return the idLegislation
     */
    public String getIdLegislation() {
        return idLegislation;
    }

    /*
     * e(non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idCategorieTarif = id;

    }

    /**
     * @param idCategorieTarif
     *            the idCategorieTarif to set
     */
    public void setIdCategorieTarif(String idCategorieTarif) {
        this.idCategorieTarif = idCategorieTarif;
    }

    /**
     * @param idLegislation
     *            the idLegislation to set
     */
    public void setIdLegislation(String idLegislation) {
        this.idLegislation = idLegislation;
    }

}