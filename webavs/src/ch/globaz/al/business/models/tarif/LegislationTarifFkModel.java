package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeSimpleModel;

/**
 * Modèle simple de législation de tarif. Ne contient que les clés primaires/étrangère de la table
 * 
 * @author jts
 * @see ch.globaz.al.business.models.tarif.LegislationTarifModel
 */
public class LegislationTarifFkModel extends JadeSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Id de la législation
     */
    private String idLegislationTarif = null;

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return idLegislationTarif;
    }

    /**
     * @return the idLegislationTarif
     */
    public String getIdLegislationTarif() {
        return idLegislationTarif;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        idLegislationTarif = id;

    }

    /**
     * @param idLegislationTarif
     *            the idLegislationTarif to set
     */
    public void setIdLegislationTarif(String idLegislationTarif) {
        this.idLegislationTarif = idLegislationTarif;
    }
}
