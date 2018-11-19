/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeSearchComplexModel;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author SCE
 * 
 *         21 juil. 2010
 */
public class DecisionRefusSearch extends JadeSearchComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String forIdDecisionHeader = null;
    private String forIdDecisionRefus = null;
    private String forIdDemande = null;
    private String forDateDecisionMin;
    private String forDateDecisionMax;
    private Collection<String> forIdsDecisionHeader = new HashSet<String>();
    private String forCsEtatDemande = null;
    private Collection<String> forNss = null;

    public DecisionRefusSearch whereKeyForRpc() {
        setWhereKey("RPC");
        return this;
    }

    public DecisionRefusSearch orderByIdDemande() {
        setOrderKey("ID_DEMANDE");
        return this;
    }

    public String getForDateDecisionMin() {
        return forDateDecisionMin;
    }

    public void setForDateDecisionMin(String forDateDecisionMin) {
        this.forDateDecisionMin = forDateDecisionMin;
    }

    public String getForDateDecisionMax() {
        return forDateDecisionMax;
    }

    public void setForDateDecisionMax(String forDateDecisionMax) {
        this.forDateDecisionMax = forDateDecisionMax;
    }

    public Collection<String> getForIdsDecisionHeader() {
        return forIdsDecisionHeader;
    }

    public void setForIdsDecisionHeader(Collection<String> forIdsDecisionHeader) {
        this.forIdsDecisionHeader = forIdsDecisionHeader;
    }

    public String getForCsEtatDemande() {
        return forCsEtatDemande;
    }

    public void setForCsEtatDemande(String forCsEtatDemande) {
        this.forCsEtatDemande = forCsEtatDemande;
    }

    /**
     * @return the forIdDemande
     */
    public String getForIdDecisionHeader() {
        return forIdDecisionHeader;
    }

    /**
     * @return the forIdDecisionRefus
     */
    public String getForIdDecisionRefus() {
        return forIdDecisionRefus;
    }

    /**
     * @return the forIdDemande
     */
    public String getForIdDemande() {
        return forIdDemande;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDecisionHeader(String forIdDecisionHeader) {
        this.forIdDecisionHeader = forIdDecisionHeader;
    }

    /**
     * @param forIdDecisionRefus
     *            the forIdDecisionRefus to set
     */
    public void setForIdDecisionRefus(String forIdDecisionRefus) {
        this.forIdDecisionRefus = forIdDecisionRefus;
    }

    /**
     * @param forIdDemande
     *            the forIdDemande to set
     */
    public void setForIdDemande(String forIdDemande) {
        this.forIdDemande = forIdDemande;
    }

    @Override
    public Class<DecisionRefus> whichModelClass() {
        return DecisionRefus.class;
    }

    public Collection<String> getForNss() {
        return forNss;
    }

    public void setForNss(Collection<String> forNss) {
        this.forNss = forNss;
    }
    
}
