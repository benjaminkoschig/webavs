/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchSimpleModel;
import java.util.Collection;

/**
 * @author SCE Modele de recherche simple pour les décisions apres calcul 14 juil. 2010
 */
public class SimpleDecisionApresCalculSearch extends JadeSearchSimpleModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecisionApresCalcul = null;
    private String forIdDecisionHeader = null;
    private String forIdVersionDroit = null;
    private Collection<String> forInIdDecisionHeader = null;

    /**
     * @return the forIdDecisionApresCalcul
     */
    public String getForIdDecisionApresCalcul() {
        return forIdDecisionApresCalcul;
    }

    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    public String getForIdVersionDroit() {
        return forIdVersionDroit;
    }

    public Collection<String> getForInIdDecisionHeader() {
        return forInIdDecisionHeader;
    }

    /**
     * @param forIdDecisionApresCalcul
     *            the forIdDecisionApresCalcul to set
     */
    public void setForIdDecisionApresCalcul(String forIdDecisionApresCalcul) {
        this.forIdDecisionApresCalcul = forIdDecisionApresCalcul;
    }

    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
    }

    public void setForIdVersionDroit(String forIdVersionDroit) {
        this.forIdVersionDroit = forIdVersionDroit;
    }

    public void setForInIdDecisionHeader(Collection<String> forInIdDecisionHeader) {
        this.forInIdDecisionHeader = forInIdDecisionHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractSearchModel#whichModelClass()
     */
    @Override
    public Class<SimpleDecisionApresCalcul> whichModelClass() {
        return SimpleDecisionApresCalcul.class;
    }

}
