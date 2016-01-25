package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * class repréentant le critère de tarif
 * 
 * @author PTA
 * 
 */
public class CritereTarifFkModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Id du critère de sélection de tarif
     */
    private String idCritereTarif = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {

        return idCritereTarif;
    }

    /**
     * @return the idCritereTarif
     */
    public String getIdCritereTarif() {
        return idCritereTarif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idCritereTarif = id;

    }

    /**
     * @param idCritereTarif
     *            the idCritereTarif to set
     */
    public void setIdCritereTarif(String idCritereTarif) {
        this.idCritereTarif = idCritereTarif;
    }
}