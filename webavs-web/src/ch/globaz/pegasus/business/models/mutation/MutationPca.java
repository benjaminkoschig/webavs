package ch.globaz.pegasus.business.models.mutation;

public class MutationPca extends MutationAbstract {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String csTypePreparationDecision = null;
    private String dateFinPca = null;
    private String idDemande = null;
    private Boolean isDateFinForce = false;

    public String getCsTypePreparationDecision() {
        return csTypePreparationDecision;
    }

    public String getDateFinPca() {
        return dateFinPca;
    }

    public String getIdDemande() {
        return idDemande;
    }

    public void setCsTypePreparationDecision(String csTypePreparationDecision) {
        this.csTypePreparationDecision = csTypePreparationDecision;
    }

    public void setDateFinPca(String dateFinPca) {
        this.dateFinPca = dateFinPca;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    @Override
    public String toString() {
        return super.toString() + " " + dateFinPca;
    }

    public Boolean getIsDateFinForce() {
        return isDateFinForce;
    }

    public void setIsDateFinForce(Boolean isDateFinForce) {
        this.isDateFinForce = isDateFinForce;
    }

}
