package ch.globaz.al.business.models.tarif;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * classe de recherche (simple) sur modèle LegislationTarif
 * 
 * @author PTA
 * 
 */
public class LegislationTarifSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Recherche sur l'id de la législation
     */
    private String forIdLegislationTarif = null;
    /**
     * Recherche sur le type de législation
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_LEGISLATION
     */
    private String forTypeLegislation = null;

    /**
     * @return the forIdLegislationTarif
     */
    public String getForIdLegislationTarif() {
        return forIdLegislationTarif;
    }

    /**
     * @return the forTypeLegislation
     */
    public String getForTypeLegislation() {
        return forTypeLegislation;
    }

    /**
     * @param forIdLegislationTarif
     *            the forIdLegislationTarif to set
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_LEGISLATION
     */
    public void setForIdLegislationTarif(String forIdLegislationTarif) {
        this.forIdLegislationTarif = forIdLegislationTarif;
    }

    /**
     * @param forTypeLegislation
     *            the forTypeLegislation to set
     * 
     * @see ch.globaz.al.business.constantes.ALCSTarif#GROUP_LEGISLATION
     */
    public void setForTypeLegislation(String forTypeLegislation) {
        this.forTypeLegislation = forTypeLegislation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<LegislationTarifModel> whichModelClass() {
        return LegislationTarifModel.class;
    }

}
