/**
 * 
 */
package ch.globaz.pegasus.business.models.decision;

import globaz.jade.persistence.model.JadeComplexModel;

/**
 * @author SCE
 * 
 *         22 sept. 2010
 */
public class Decision extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

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
    private String noVersionDroitApc = null;

    private String noVersionDroitSup = null;

    public Decision() {
        super();
        decisionHeader = new DecisionHeader();

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

    /*
     * (non-Javadoc)
     * 
     * @see globaz.jade.persistence.model.JadeAbstractModel#getSpy()
     */
    @Override
    public String getSpy() {
        return decisionHeader.getSpy();
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
