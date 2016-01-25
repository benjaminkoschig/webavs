/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.demande.Demande;

/**
 * @author SCE
 * 
 *         19 juil. 2010
 */
public class DecisionRefus extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DecisionHeader decisionHeader = null;
    private Demande demande = null;
    private SimpleDecisionRefus simpleDecisionRefus = null;

    public DecisionRefus() {
        super();
        demande = new Demande();
        decisionHeader = new DecisionHeader();
        simpleDecisionRefus = new SimpleDecisionRefus();
    }

    /**
     * @return the simpleDecisionHeader
     */
    public DecisionHeader getDecisionHeader() {
        return decisionHeader;
    }

    /**
     * @return the simpleDemande
     */
    public Demande getDemande() {
        return demande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleDecisionRefus.getId();
    }

    /**
     * @return the simpleDecisionRefus
     */
    public SimpleDecisionRefus getSimpleDecisionRefus() {
        return simpleDecisionRefus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleDecisionRefus.getSpy();
    }

    /**
     * @param simpleDecisionHeader
     *            the simpleDecisionHeader to set
     */
    public void setDecisionHeader(DecisionHeader decisionHeader) {
        simpleDecisionRefus.setIdDecisionHeader(decisionHeader.getSimpleDecisionHeader().getId());
    }

    /**
     * @param simpleDemande
     *            the simpleDemande to set
     */
    public void setDemande(Demande demande) {
        this.demande = demande;

    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleDecisionRefus.setId(id);

    }

    /**
     * @param simpleDecisionRefus
     *            the simpleDecisionRefus to set
     */
    public void setSimpleDecisionRefus(SimpleDecisionRefus simpleDecisionRefus) {
        this.simpleDecisionRefus = simpleDecisionRefus;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleDecisionRefus.setSpy(spy);

    }

}
