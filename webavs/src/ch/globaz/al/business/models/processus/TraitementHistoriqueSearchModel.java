package ch.globaz.al.business.models.processus;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * Mod�le de recherche d'un historique de traitement
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
     * Crit�re id entit�
     */
    private String forIdEntite = null;
    /**
     * Crit�re id traitement p�riodique
     */
    private String forIdTraitementPeriodique = null;

    /**
     * 
     * @return id de l'entit�
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
     *            id de l'entit�
     */
    public void setForIdEntite(String forIdEntite) {
        this.forIdEntite = forIdEntite;
    }

    /**
     * 
     * @param forIdTraitementPeriodique
     *            id du traitement p�riodique
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
