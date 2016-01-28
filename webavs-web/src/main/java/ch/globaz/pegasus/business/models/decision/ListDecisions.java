package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.pegasus.business.models.demande.SimpleDemande;
import ch.globaz.pegasus.business.models.droit.SimpleDroit;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePCAccordee;

public class ListDecisions extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csMotifDecSup = null;
    private String csSousMotifDecSup = null;
    private String csTypePreparation = null;
    private String dateSuppression = null;
    private DecisionHeader decisionHeader = null;
    // id des décision
    private String idDecisionApresCalcul = null;
    private String idDecisionRefus = null;
    private String idDecisionSuppression = null;
    // Id demande pour decref
    private String idDemandePc = null;

    // Id droit et version droit pour APC
    private String idDroitApc = null;

    // Id droit et version droit pour SUPP
    private String idDroitSup = null;

    private String idVersionDroitApc = null;

    private String idVersionDroitSup = null;

    private String montantPC = null;

    private String noVersionDroitApc = null;

    private String noVersionDroitSup = null;
    private SimpleDemande simpleDemande = null;
    private SimpleDroit simpleDroit = null;
    private SimplePCAccordee simplePcAccordee = null;
    private SimplePrestationsAccordees simplePrestation = null;
    private SimpleValidationDecision simpleValidationDecision = null;

    public ListDecisions() {
        super();
        decisionHeader = new DecisionHeader();
        simplePcAccordee = new SimplePCAccordee();
        simpleValidationDecision = new SimpleValidationDecision();
        simpleDroit = new SimpleDroit();
        simpleDemande = new SimpleDemande();
        simplePrestation = new SimplePrestationsAccordees();

    }

    public String getCsMotifDecSup() {
        return csMotifDecSup;
    }

    public String getCsSousMotifDecSup() {
        return csSousMotifDecSup;
    }

    public String getCsTypePreparation() {
        return csTypePreparation;
    }

    public String getDateSuppression() {
        return dateSuppression;
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
        return decisionHeader.getId();
    }

    /**
     * @return the idDecisionApresCalcul
     */
    public String getIdDecisionApresCalcul() {
        return idDecisionApresCalcul;
    }

    /**
     * @return the idDecisionRefus
     */
    public String getIdDecisionRefus() {
        return idDecisionRefus;
    }

    /**
     * @return the idDecisionSuppression
     */
    public String getIdDecisionSuppression() {
        return idDecisionSuppression;
    }

    /**
     * @return the idDemandePc
     */
    public String getIdDemandePc() {
        return idDemandePc;
    }

    /**
     * @return the idDroitApc
     */
    public String getIdDroitApc() {
        return idDroitApc;
    }

    /**
     * @return the idDroitSup
     */
    public String getIdDroitSup() {
        return idDroitSup;
    }

    /**
     * @return the idVersionDroitApc
     */
    public String getIdVersionDroitApc() {
        return idVersionDroitApc;
    }

    /**
     * @return the idVersionDroitSup
     */
    public String getIdVersionDroitSup() {
        return idVersionDroitSup;
    }

    public String getMontantPC() {
        return montantPC;
    }

    /**
     * @return the noVersionDroitApc
     */
    public String getNoVersionDroitApc() {
        return noVersionDroitApc;
    }

    /**
     * @return the noVersionDroitSup
     */
    public String getNoVersionDroitSup() {
        return noVersionDroitSup;
    }

    public SimpleDemande getSimpleDemande() {
        return simpleDemande;
    }

    public SimpleDroit getSimpleDroit() {
        return simpleDroit;
    }

    public SimplePCAccordee getSimplePcAccordee() {
        return simplePcAccordee;
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
        return decisionHeader.getSpy();
    }

    public void setCsMotifDecSup(String csMotifDecSup) {
        this.csMotifDecSup = csMotifDecSup;
    }

    public void setCsSousMotifDecSup(String csSousMotifDecSup) {
        this.csSousMotifDecSup = csSousMotifDecSup;
    }

    public void setCsTypePreparation(String csTypePreparation) {
        this.csTypePreparation = csTypePreparation;
    }

    public void setDateSuppression(String dateSuppression) {
        this.dateSuppression = dateSuppression;
    }

    /**
     * @param decisionHeader
     *            the decisionHeader to set
     */
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
        decisionHeader.setId(id);

    }

    /**
     * @param idDecisionApresCalcul
     *            the idDecisionApresCalcul to set
     */
    public void setIdDecisionApresCalcul(String idDecisionApresCalcul) {
        this.idDecisionApresCalcul = idDecisionApresCalcul;
    }

    /**
     * @param idDecisionRefus
     *            the idDecisionRefus to set
     */
    public void setIdDecisionRefus(String idDecisionRefus) {
        this.idDecisionRefus = idDecisionRefus;
    }

    /**
     * @param idDecisionSuppression
     *            the idDecisionSuppression to set
     */
    public void setIdDecisionSuppression(String idDecisionSuppression) {
        this.idDecisionSuppression = idDecisionSuppression;
    }

    /**
     * @param idDemandePc
     *            the idDemandePc to set
     */
    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    /**
     * @param idDroitApc
     *            the idDroitApc to set
     */
    public void setIdDroitApc(String idDroitApc) {
        this.idDroitApc = idDroitApc;
    }

    /**
     * @param idDroitSup
     *            the idDroitSup to set
     */
    public void setIdDroitSup(String idDroitSup) {
        this.idDroitSup = idDroitSup;
    }

    /**
     * @param idVersionDroitApc
     *            the idVersionDroitApc to set
     */
    public void setIdVersionDroitApc(String idVersionDroitApc) {
        this.idVersionDroitApc = idVersionDroitApc;
    }

    /**
     * @param idVersionDroitSup
     *            the idVersionDroitSup to set
     */
    public void setIdVersionDroitSup(String idVersionDroitSup) {
        this.idVersionDroitSup = idVersionDroitSup;
    }

    public void setMontantPC(String montantPC) {
        this.montantPC = montantPC;
    }

    /**
     * @param noVersionDroitApc
     *            the noVersionDroitApc to set
     */
    public void setNoVersionDroitApc(String noVersionDroitApc) {
        this.noVersionDroitApc = noVersionDroitApc;
    }

    /**
     * @param noVersionDroitSup
     *            the noVersionDroitSup to set
     */
    public void setNoVersionDroitSup(String noVersionDroitSup) {
        this.noVersionDroitSup = noVersionDroitSup;
    }

    public void setSimpleDemande(SimpleDemande simpleDemande) {
        this.simpleDemande = simpleDemande;
    }

    public void setSimpleDroit(SimpleDroit simpleDroit) {
        this.simpleDroit = simpleDroit;
    }

    public void setSimplePcAccordee(SimplePCAccordee simplePcAccordee) {
        this.simplePcAccordee = simplePcAccordee;
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
        decisionHeader.setSpy(spy);

    }

}
