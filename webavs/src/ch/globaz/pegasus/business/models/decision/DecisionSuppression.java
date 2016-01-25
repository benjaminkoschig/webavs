/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.droit.VersionDroit;

/**
 * @author SCE
 * 
 *         29 juil. 2010
 */
public class DecisionSuppression extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DecisionHeader decisionHeader = null;

    private SimpleDecisionSuppression simpleDecisionSuppression = null;

    private VersionDroit versionDroit = null;

    public DecisionSuppression() {
        super();

        decisionHeader = new DecisionHeader();
        simpleDecisionSuppression = new SimpleDecisionSuppression();
        versionDroit = new VersionDroit();

    }

    /**
     * @return the decisionHeader
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
        return simpleDecisionSuppression.getId();
    }

    /**
     * @return the simpleDecisionSuppression
     */
    public SimpleDecisionSuppression getSimpleDecisionSuppression() {
        return simpleDecisionSuppression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleDecisionSuppression.getSpy();
    }

    /**
     * @return the versiondroit
     */
    public VersionDroit getVersionDroit() {
        return versionDroit;
    }

    /**
     * @param decisionHeader
     *            the decisionHeader to set
     */
    public void setDecisionHeader(DecisionHeader decisionHeader) {
        simpleDecisionSuppression.setIdDecisionHeader(decisionHeader.getSimpleDecisionHeader().getId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleDecisionSuppression.setId(id);

    }

    /**
     * @param simpleDecisionSuppression
     *            the simpleDecisionSuppression to set
     */
    public void setSimpleDecisionSuppression(SimpleDecisionSuppression simpleDecisionSuppression) {
        this.simpleDecisionSuppression = simpleDecisionSuppression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        getSimpleDecisionSuppression().setSpy(spy);
    }

    /**
     * @param droit
     *            the droit to set
     */
    public void setVersionDroit(VersionDroit versionDroit) {
        this.versionDroit = versionDroit;
    }

}
