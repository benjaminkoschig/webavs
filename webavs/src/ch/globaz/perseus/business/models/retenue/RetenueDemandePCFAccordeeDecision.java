package ch.globaz.perseus.business.models.retenue;

import globaz.jade.persistence.model.JadeComplexModel;

public class RetenueDemandePCFAccordeeDecision extends JadeComplexModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String dateDebutDemande = null;

    private String dateValidationDecision = null;
    private String idDemande = null;
    private String idDossier = null;
    private String idPCFAccordee = null;
    private String numeroDecisionDecision = null;
    private SimpleRetenue simpleRetenue = null;

    /**
	 * 
	 */
    public RetenueDemandePCFAccordeeDecision() {
        super();
        setSimpleRetenue(new SimpleRetenue());
    }

    /**
     * @return the dateDebutDemande
     */
    public String getDateDebutDemande() {
        return dateDebutDemande;
    }

    /**
     * @return the dateValidationDecision
     */
    public String getDateValidationDecision() {
        return dateValidationDecision;
    }

    @Override
    public String getId() {
        return getSimpleRetenue().getId();
    }

    /**
     * @return the idDemande
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    /**
     * @return the idPCFAccordee
     */
    public String getIdPCFAccordee() {
        return idPCFAccordee;
    }

    /**
     * @return the numeroDecisionDecision
     */
    public String getNumeroDecisionDecision() {
        return numeroDecisionDecision;
    }

    /**
     * @return the simpleRetenue
     */
    public SimpleRetenue getSimpleRetenue() {
        return simpleRetenue;
    }

    @Override
    public String getSpy() {
        return getSimpleRetenue().getSpy();
    }

    /**
     * @param dateDebutDemande
     *            the dateDebutDemande to set
     */
    public void setDateDebutDemande(String dateDebutDemande) {
        this.dateDebutDemande = dateDebutDemande;
    }

    /**
     * @param dateValidationDecision
     *            the dateValidationDecision to set
     */
    public void setDateValidationDecision(String dateValidationDecision) {
        this.dateValidationDecision = dateValidationDecision;
    }

    @Override
    public void setId(String id) {
        getSimpleRetenue().setId(id);
    }

    /**
     * @param idDemande
     *            the idDemande to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    /**
     * @param idPCFAccordee
     *            the idPCFAccordee to set
     */
    public void setIdPCFAccordee(String idPCFAccordee) {
        this.idPCFAccordee = idPCFAccordee;
    }

    /**
     * @param numeroDecisionDecision
     *            the numeroDecisionDecision to set
     */
    public void setNumeroDecisionDecision(String numeroDecisionDecision) {
        this.numeroDecisionDecision = numeroDecisionDecision;
    }

    /**
     * @param simpleRetenue
     *            the simpleRetenue to set
     */
    public void setSimpleRetenue(SimpleRetenue simpleRetenue) {
        this.simpleRetenue = simpleRetenue;
    }

    @Override
    public void setSpy(String spy) {
        getSimpleRetenue().setSpy(spy);
    }
}
