/**
 * 
 */
package ch.globaz.amal.business.models.famille;

import globaz.jade.persistence.model.JadeSearchComplexModel;

/**
 * @author DHI
 * 
 */
public class FamilleContribuableViewSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forAnneeHistorique = null;
    private Boolean forCodeActif = null;
    private Boolean forContribuableActif = null;
    private String forIdContribuable = null;

    public FamilleContribuableViewSearch() {
        forIdContribuable = new String();
    }

    public String getForAnneeHistorique() {
        return forAnneeHistorique;
    }

    public Boolean getForCodeActif() {
        return forCodeActif;
    }

    public Boolean getForContribuableActif() {
        return forContribuableActif;
    }

    public String getForIdContribuable() {
        return forIdContribuable;
    }

    public void setForAnneeHistorique(String forAnneeHistorique) {
        this.forAnneeHistorique = forAnneeHistorique;
    }

    public void setForCodeActif(Boolean forCodeActif) {
        this.forCodeActif = forCodeActif;
    }

    public void setForContribuableActif(Boolean forContribuableActif) {
        this.forContribuableActif = forContribuableActif;
    }

    public void setForIdContribuable(String forIdContribuable) {
        this.forIdContribuable = forIdContribuable;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class whichModelClass() {
        return FamilleContribuableView.class;
    }
}
