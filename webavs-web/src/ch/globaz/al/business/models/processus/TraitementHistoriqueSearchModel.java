package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Modèle de recherche d'un historique de traitement
 * 
 * @author GMO
 * 
 */
public class TraitementHistoriqueSearchModel extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /**
     * Critère id entité
     */
    private String forIdEntite = null;
    /**
     * Critère id traitement périodique
     */
    private String forIdTraitementPeriodique = null;

    /**
     * 
     * @return id de l'entité
     */
    public String getForIdEntite() {
        return forIdEntite;
    }

    /**
     * 
     * @return forIdTraitementPeriodique
     */
    public String getForIdTraitementPeriodique() {
        return forIdTraitementPeriodique;
    }

    /**
     * 
     * @param forIdEntite
     *            id de l'entité
     */
    public void setForIdEntite(String forIdEntite) {
        this.forIdEntite = forIdEntite;
    }

    /**
     * 
     * @param forIdTraitementPeriodique
     *            id du traitement périodique
     */
    public void setForIdTraitementPeriodique(String forIdTraitementPeriodique) {
        this.forIdTraitementPeriodique = forIdTraitementPeriodique;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return TraitementHistoriqueModel.class;
    }

}
