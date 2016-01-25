/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.VersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;

/**
 * @author SCE
 * 
 *         26 juil. 2010
 */
public class DecisionApresCalcul extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DecisionHeader decisionHeader = null;
    // private SimpleDroit droit = null;
    private PCAccordee pcAccordee = null;
    private SimplePlanDeCalcul planCalcul = null;
    private SimpleDecisionApresCalcul simpleDecisionApresCalcul = null;
    private SimpleValidationDecision simpleValidationDecision = null;
    private VersionDroit versionDroit = null;

    public DecisionApresCalcul() {
        super();
        versionDroit = new VersionDroit();
        decisionHeader = new DecisionHeader();
        simpleDecisionApresCalcul = new SimpleDecisionApresCalcul();
        pcAccordee = new PCAccordee();
        simpleValidationDecision = new SimpleValidationDecision();
        // this.droit = new SimpleDroit();
        planCalcul = new SimplePlanDeCalcul();
    }

    /**
     * @return the simpleDecisionHeader
     */
    public DecisionHeader getDecisionHeader() {
        return decisionHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleDecisionApresCalcul.getId();
    }

    public PCAccordee getPcAccordee() {
        return pcAccordee;
    }

    public SimplePlanDeCalcul getPlanCalcul() {
        return planCalcul;
    }

    /**
     * @return the simpleDecisionApresCalcul
     */
    public SimpleDecisionApresCalcul getSimpleDecisionApresCalcul() {
        return simpleDecisionApresCalcul;
    }

    public SimpleValidationDecision getSimpleValidationDecision() {
        return simpleValidationDecision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return decisionHeader.getSimpleDecisionHeader().getSpy();
    }

    /**
     * @return the Droit
     */
    public VersionDroit getVersionDroit() {
        return versionDroit;
    }

    // /**
    // * @param simpleDecisionHeader
    // * the simpleDecisionHeader to set
    // */
    // public void setDecisionHeader(DecisionHeader decisionHeader) {
    // this.simpleDecisionApresCalcul.setIdDecisionHeader(decisionHeader.getSimpleDecisionHeader().getId());
    // }

    public void setDecisionHeader(DecisionHeader decisionHeader) {
        this.decisionHeader = decisionHeader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleDecisionApresCalcul.setId(id);

    }

    public void setPcAccordee(PCAccordee pcAccordee) {
        this.pcAccordee = pcAccordee;
    }

    public void setPlanCalcul(SimplePlanDeCalcul planCalcul) {
        this.planCalcul = planCalcul;
    }

    /**
     * @param simpleDecisionApresCalcul
     *            the simpleDecisionApresCalcul to set
     */
    public void setSimpleDecisionApresCalcul(SimpleDecisionApresCalcul simpleDecisionApresCalcul) {
        this.simpleDecisionApresCalcul = simpleDecisionApresCalcul;
    }

    public void setSimpleValidationDecision(SimpleValidationDecision simpleValidationDecision) {
        this.simpleValidationDecision = simpleValidationDecision;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        decisionHeader.getSimpleDecisionHeader().setSpy(spy);

    }

    /**
     * @param simpleVersionDroit
     *            the simpleVersionDroit to set
     */
    public void setVersionDroit(VersionDroit versionDroit) {
        this.versionDroit = versionDroit;
    }

}
