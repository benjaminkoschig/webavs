package ch.globaz.pegasus.business.models.lot;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class Prestation extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // private SimpleDecisionHeader simpleDecisionHeader = null;
    // private SimplePCAccordee simplePCAccordee = null;
    private SimplePrestation simplePrestation = null;
    // private SimpleValidationDecision simpleValidationDecision = null;
    private PersonneEtendueComplexModel tiersBeneficiaire = null;

    public Prestation() {
        super();
        simplePrestation = new SimplePrestation();
        // this.simpleDecisionHeader = new SimpleDecisionHeader();
        tiersBeneficiaire = new PersonneEtendueComplexModel();
        // this.simpleValidationDecision = new SimpleValidationDecision();
        // this.simplePCAccordee = new SimplePCAccordee();
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getId()
     */

    @Override
    public String getId() {
        return simplePrestation.getIdPrestation();
    }

    public String getPeriod() {
        return simplePrestation.getDateDebut() + " - " + simplePrestation.getDateFin();
        // if (this.simpleDecisionHeader.getCsTypeDecision().equals(IPCDecision.CS_TYPE_SUPPRESSION_SC)) {
        // return JadeDateUtil.convertDateMonthYear(JadeDateUtil.addMonths(
        // "01." + this.simpleDecisionHeader.getDateFinDecision(), 1))
        // + " - ";
        // } else {
        // return this.simpleDecisionHeader.getDateDebutDecision() + " - "
        // + this.simpleDecisionHeader.getDateFinDecision();
        // }
    }

    // public SimpleDecisionHeader getSimpleDecisionHeader() {
    // return this.simpleDecisionHeader;
    // }

    // public SimplePCAccordee getSimplePCAccordee() {
    // return this.simplePCAccordee;
    // }

    /**
     * @return the home
     */
    public SimplePrestation getSimplePrestation() {
        return simplePrestation;
    }

    // public SimpleValidationDecision getSimpleValidationDecision() {
    // return this.simpleValidationDecision;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */

    @Override
    public String getSpy() {
        return simplePrestation.getSpy();
    }

    public PersonneEtendueComplexModel getTiersBeneficiaire() {
        return tiersBeneficiaire;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setId(java.lang.String)
     */

    @Override
    public void setId(String id) {
        simplePrestation.setIdPrestation(id);

    }

    // public void setSimpleDecisionHeader(SimpleDecisionHeader simpleDecisionHeader) {
    // this.simpleDecisionHeader = simpleDecisionHeader;
    // }

    // public void setSimplePCAccordee(SimplePCAccordee simplePCAccordee) {
    // this.simplePCAccordee = simplePCAccordee;
    // }

    /**
     * @param simplePrestation
     *            the simplePrestation to set
     */
    public void setSimplePrestation(SimplePrestation simplePrestation) {
        this.simplePrestation = simplePrestation;
    }

    // public void setSimpleValidationDecision(SimpleValidationDecision simpleValidationDecision) {
    // this.simpleValidationDecision = simpleValidationDecision;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#setSpy(java.lang.String)
     */

    @Override
    public void setSpy(String spy) {
        simplePrestation.setSpy(spy);
    }

    public void setTiersBeneficiaire(PersonneEtendueComplexModel tiersBeneficiaire) {
        this.tiersBeneficiaire = tiersBeneficiaire;
    }

}
