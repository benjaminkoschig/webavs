/**
 * 
 */
package ch.globaz.perseus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.perseus.business.models.demande.SimpleDemande;

/**
 * @author MBO
 * 
 */
public class DecisionForTypeDocument extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleDecision simpleDecision = null;
    private SimpleDemande simpleDemande = null;

    public DecisionForTypeDocument() {
        super();
        simpleDecision = new SimpleDecision();
        simpleDemande = new SimpleDemande();
    }

    // public DecisionForTypeDocument(SimpleDecision simpleDecision, SimpleDemande simpleDemande) {
    // super();
    // this.simpleDecision = new SimpleDecision();
    // this.simpleDemande = new SimpleDemande();
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */
    @Override
    public String getId() {
        // TODO Auto-generated method stub
        return simpleDecision.getIdDecision();
    }

    public SimpleDecision getSimpleDecision() {
        return simpleDecision;
    }

    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return simpleDecision.getSpy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        simpleDecision.setId(id);

    }

    public void setSimpleDecision(SimpleDecision simpleDecision) {
        this.simpleDecision = simpleDecision;
    }

    public void setSimpleDemande(SimpleDemande simpleDemande) {
        this.simpleDemande = simpleDemande;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */
    @Override
    public void setSpy(String spy) {
        simpleDecision.setSpy(spy);

    }

}
