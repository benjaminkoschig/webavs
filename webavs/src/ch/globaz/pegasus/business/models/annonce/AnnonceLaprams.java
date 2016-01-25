/**
 * 
 */
package ch.globaz.pegasus.business.models.annonce;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionSuppression;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;

/**
 * @author eco
 */
public class AnnonceLaprams extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PCAccordee pcAccordee = null;
    private SimpleAnnonceLaprams simpleAnnonceLaprams = null;
    private SimpleDecisionHeader simpleDecisionHeader = null;
    private SimpleDecisionSuppression simpleDecisionSuppression = null;
    private SimpleVersionDroit simpleVersionDroitForSuppression = null;

    public AnnonceLaprams() {
        super();
        simpleAnnonceLaprams = new SimpleAnnonceLaprams();
        simpleDecisionHeader = new SimpleDecisionHeader();
        pcAccordee = new PCAccordee();
        simpleDecisionSuppression = new SimpleDecisionSuppression();
        simpleVersionDroitForSuppression = new SimpleVersionDroit();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        return simpleAnnonceLaprams.getId();
    }

    public PCAccordee getPcAccordee() {
        return pcAccordee;
    }

    public SimpleAnnonceLaprams getSimpleAnnonceLaprams() {
        return simpleAnnonceLaprams;
    }

    public SimpleDecisionHeader getSimpleDecisionHeader() {
        return simpleDecisionHeader;
    }

    public SimpleDecisionSuppression getSimpleDecisionSuppression() {
        return simpleDecisionSuppression;
    }

    public SimpleVersionDroit getSimpleVersionDroitForSuppression() {
        return simpleVersionDroitForSuppression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleAnnonceLaprams.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleAnnonceLaprams.setId(id);
    }

    public void setPcAccordee(PCAccordee pcAccordee) {
        this.pcAccordee = pcAccordee;
    }

    public void setSimpleAnnonceLaprams(SimpleAnnonceLaprams simpleAnnonceLaprams) {
        this.simpleAnnonceLaprams = simpleAnnonceLaprams;
    }

    public void setSimpleDecisionHeader(SimpleDecisionHeader simpleDecisionHeader) {
        this.simpleDecisionHeader = simpleDecisionHeader;
    }

    public void setSimpleDecisionSuppression(SimpleDecisionSuppression simpleDecisionSuppression) {
        this.simpleDecisionSuppression = simpleDecisionSuppression;
    }

    public void setSimpleVersionDroitForSuppression(SimpleVersionDroit simpleVersionDroitForSuppression) {
        this.simpleVersionDroitForSuppression = simpleVersionDroitForSuppression;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleAnnonceLaprams.setSpy(spy);
    }

}
