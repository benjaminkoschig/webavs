package ch.globaz.amal.business.models.contribuable;

import globaz.jade.persistence.model.JadeSearchSimpleModel;

/**
 * @author CBU
 * 
 */
public class SimpleContribuableSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private Boolean forContribuableActif = null;
    private String forIdContribuable = null;
    private String forIdTier = null;
    private String forNoContribuable = null;

    public Boolean getForContribuableActif() {
        return forContribuableActif;
    }

    /**
     * @return
     */
    public String getForIdContribuable() {
        return forIdContribuable;
    }

    public String getForIdTier() {
        return forIdTier;
    }

    public String getForNoContribuable() {
        return forNoContribuable;
    }

    public void setForContribuableActif(Boolean forContribuableActif) {
        this.forContribuableActif = forContribuableActif;
    }

    /**
     * @param forIdContribuable
     */
    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    public void setForIdTier(String forIdTier) {
        this.forIdTier = forIdTier;
    }

    public void setForNoContribuable(String forNoContribuable) {
        this.forNoContribuable = forNoContribuable;
    }

    @Override
    public Class whichModelClass() {
        return SimpleContribuable.class;
    }

}
