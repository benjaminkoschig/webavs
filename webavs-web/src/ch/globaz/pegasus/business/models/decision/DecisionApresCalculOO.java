/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.droit.VersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

/**
 * @author SCE
 * 
 *         26 juil. 2010
 */
public class DecisionApresCalculOO extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private DecisionHeader decisionHeader = null;
    private SimpleDemande demande = null;
    private SimpleDroit droit = null;

    private PCAccordee pcAccordee = null;
    private SimplePlanDeCalcul planCalcul = null;
    private SimpleDecisionApresCalcul simpleDecisionApresCalcul = null;
    private SimplePrestationsAccordees simplePrestation = null;
    private SimpleValidationDecision simpleValidationDecision = null;

    private VersionDroit versionDroit = null;

    public DecisionApresCalculOO() {
        super();
        versionDroit = new VersionDroit();
        decisionHeader = new DecisionHeader();
        simpleDecisionApresCalcul = new SimpleDecisionApresCalcul();
        pcAccordee = new PCAccordee();
        simpleValidationDecision = new SimpleValidationDecision();
        droit = new SimpleDroit();
        demande = new SimpleDemande();
        planCalcul = new SimplePlanDeCalcul();
        simplePrestation = new SimplePrestationsAccordees();

    }

    public PersonneEtendueComplexModel getPersonneForDossier() {
        return getVersionDroit().getDemande().getDossier().getDemandePrestation().getPersonneEtendue();
    }

    public String getNoDecision() {
        return decisionHeader.getSimpleDecisionHeader().getNoDecision();
    }

    public boolean isDecisionValidee() {
        return IPCDecision.CS_ETAT_DECISION_VALIDE.equals(getDecisionHeader().getSimpleDecisionHeader()
                .getCsEtatDecision());

    }

    /**
     * @return the simpleDecisionHeader
     */
    public DecisionHeader getDecisionHeader() {
        return decisionHeader;
    }

    public SimpleDemande getDemande() {
        return demande;
    }

    public SimpleDroit getDroit() {
        return droit;
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

    public SimplePrestationsAccordees getSimplePrestation() {
        return simplePrestation;
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

    /**
     * @param simpleDecisionHeader
     *            the simpleDecisionHeader to set
     */
    public void setDecisionHeader(DecisionHeader decisionHeader) {
        simpleDecisionApresCalcul.setIdDecisionHeader(decisionHeader.getSimpleDecisionHeader().getId());
    }

    public void setDemande(SimpleDemande demande) {
        this.demande = demande;
    }

    public void setDroit(SimpleDroit droit) {
        this.droit = droit;
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

    public void setSimplePrestation(SimplePrestationsAccordees simplePrestation) {
        this.simplePrestation = simplePrestation;
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

    public String getIdTiersCourrier() {
        return decisionHeader.getSimpleDecisionHeader().getIdTiersCourrier();
    }

    /**
     * @param simpleVersionDroit
     *            the simpleVersionDroit to set
     */
    public void setVersionDroit(VersionDroit versionDroit) {
        this.versionDroit = versionDroit;
    }

}
