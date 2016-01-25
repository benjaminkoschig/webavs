package ch.globaz.pegasus.business.models.mutation;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.models.decision.SimpleValidationDecision;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class MutationMontantPCA extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SimpleVersionDroit droitCourant;
    private SimpleVersionDroit droitPrcedant;
    private SimplePCAccordee pcaCourant;
    private SimplePCAccordee pcaPrecedante;
    private PersonneEtendueComplexModel personneEtendueComplexModel;
    private SimplePlanDeCalcul planClaculeCourant;
    private SimplePlanDeCalcul planClaculePrecedant;
    private SimpleDecisionHeader simpleDecisionHeader;
    private SimpleValidationDecision simpleValidationDecision;

    public MutationMontantPCA() {
        droitCourant = new SimpleVersionDroit();
        droitPrcedant = new SimpleVersionDroit();
        pcaCourant = new SimplePCAccordee();
        pcaPrecedante = new SimplePCAccordee();
        planClaculeCourant = new SimplePlanDeCalcul();
        planClaculePrecedant = new SimplePlanDeCalcul();
        simpleDecisionHeader = new SimpleDecisionHeader();
        simpleValidationDecision = new SimpleValidationDecision();
        personneEtendueComplexModel = new PersonneEtendueComplexModel();
    }

    public SimpleVersionDroit getDroitCourant() {
        return droitCourant;
    }

    public SimpleVersionDroit getDroitPrcedant() {
        return droitPrcedant;
    }

    @Override
    public String getId() {
        return simpleDecisionHeader.getId();
    }

    public SimplePCAccordee getPcaCourant() {
        return pcaCourant;
    }

    public SimplePCAccordee getPcaPrecedante() {
        return pcaPrecedante;
    }

    public PersonneEtendueComplexModel getPersonneEtendueComplexModel() {
        return personneEtendueComplexModel;
    }

    public SimplePlanDeCalcul getPlanClaculeCourant() {
        return planClaculeCourant;
    }

    public SimplePlanDeCalcul getPlanClaculePrecedant() {
        return planClaculePrecedant;
    }

    public SimpleDecisionHeader getSimpleDecisionHeader() {
        return simpleDecisionHeader;
    }

    public SimpleValidationDecision getSimpleValidationDecision() {
        return simpleValidationDecision;
    }

    @Override
    public String getSpy() {
        return simpleDecisionHeader.getSpy();
    }

    public void setDroitCourant(SimpleVersionDroit droitCourant) {
        this.droitCourant = droitCourant;
    }

    public void setDroitPrcedant(SimpleVersionDroit droitPrcedant) {
        this.droitPrcedant = droitPrcedant;
    }

    @Override
    public void setId(String id) {
        simpleDecisionHeader.setId(id);

    }

    public void setPcaCourant(SimplePCAccordee pcaCourant) {
        this.pcaCourant = pcaCourant;
    }

    public void setPcaPrecedante(SimplePCAccordee pcaPrecedante) {
        this.pcaPrecedante = pcaPrecedante;
    }

    public void setPersonneEtendueComplexModel(PersonneEtendueComplexModel personneEtendueComplexModel) {
        this.personneEtendueComplexModel = personneEtendueComplexModel;
    }

    public void setPlanClaculeCourant(SimplePlanDeCalcul planClaculeCourant) {
        this.planClaculeCourant = planClaculeCourant;
    }

    public void setPlanClaculePrecedant(SimplePlanDeCalcul planClaculePrecedant) {
        this.planClaculePrecedant = planClaculePrecedant;
    }

    public void setSimpleDecisionHeader(SimpleDecisionHeader simpleDecisionHeader) {
        this.simpleDecisionHeader = simpleDecisionHeader;
    }

    public void setSimpleValidationDecision(SimpleValidationDecision simpleValidationDecision) {
        this.simpleValidationDecision = simpleValidationDecision;
    }

    @Override
    public void setSpy(String spy) {
        simpleDecisionHeader.setSpy(spy);
    }

}
