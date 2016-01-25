package ch.globaz.pegasus.business.models.lot;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.lots.SimpleLot;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordee;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleValidationDecision;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;

public class ComptabiliserLot extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private PCAccordee pcAccordee = null;
    private SimpleCreanceAccordee simpleCreanceAccordee = null;
    private SimpleDecisionHeader simpleDecisionHeader = null;
    private SimpleLot simpleLot = null;
    private SimpleOrdreVersement simpleOrdreVersement = null;
    private SimplePrestation simplePrestation = null;
    private SimpleValidationDecision simpleValidationDecision = null;

    public ComptabiliserLot() {
        pcAccordee = new PCAccordee();
        simpleCreanceAccordee = new SimpleCreanceAccordee();
        simpleDecisionHeader = new SimpleDecisionHeader();
        simpleLot = new SimpleLot();
        simpleOrdreVersement = new SimpleOrdreVersement();
        simplePrestation = new SimplePrestation();
        simpleValidationDecision = new SimpleValidationDecision();
    }

    @Override
    public String getId() {
        return simpleLot.getId();
    }

    public PCAccordee getPcAccordee() {
        return pcAccordee;
    }

    public SimpleCreanceAccordee getSimpleCreanceAccordee() {
        return simpleCreanceAccordee;
    }

    public SimpleDecisionHeader getSimpleDecisionHeader() {
        return simpleDecisionHeader;
    }

    public SimpleLot getSimpleLot() {
        return simpleLot;
    }

    public SimpleOrdreVersement getSimpleOrdreVersement() {
        return simpleOrdreVersement;
    }

    public SimplePrestation getSimplePrestation() {
        return simplePrestation;
    }

    public SimpleValidationDecision getSimpleValidationDecision() {
        return simpleValidationDecision;
    }

    @Override
    public String getSpy() {
        return simpleLot.getSpy();
    }

    @Override
    public void setId(String id) {
        simpleLot.setId(id);
    }

    public void setPcAccordee(PCAccordee pcAccordee) {
        this.pcAccordee = pcAccordee;
    }

    public void setSimpleCreanceAccordee(SimpleCreanceAccordee simpleCreanceAccordee) {
        this.simpleCreanceAccordee = simpleCreanceAccordee;
    }

    public void setSimpleDecisionHeader(SimpleDecisionHeader simpleDecisionHeader) {
        this.simpleDecisionHeader = simpleDecisionHeader;
    }

    public void setSimpleLot(SimpleLot simpleLot) {
        this.simpleLot = simpleLot;
    }

    public void setSimpleOrdreVersement(SimpleOrdreVersement simpleOrdreVersement) {
        this.simpleOrdreVersement = simpleOrdreVersement;
    }

    public void setSimplePrestation(SimplePrestation simplePrestation) {
        this.simplePrestation = simplePrestation;
    }

    public void setSimpleValidationDecision(SimpleValidationDecision simpleValidationDecision) {
        this.simpleValidationDecision = simpleValidationDecision;
    }

    @Override
    public void setSpy(String spy) {
        simpleLot.setSpy(spy);
    }

}
