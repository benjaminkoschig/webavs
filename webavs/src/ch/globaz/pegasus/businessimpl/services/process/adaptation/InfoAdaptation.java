package ch.globaz.pegasus.businessimpl.services.process.adaptation;

public class InfoAdaptation {
    private String idPlanCalcul;
    private String idVersionDroit;
    private String idPca;
    private String montantAnciennePca;

    public InfoAdaptation() {
    }

    public String getIdPlanCalcul() {
        return idPlanCalcul;
    }

    public void setIdPlanCalcul(String idPlanCalcul) {
        this.idPlanCalcul = idPlanCalcul;
    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public String getIdPca() {
        return idPca;
    }

    public void setIdPca(String idPca) {
        this.idPca = idPca;
    }

    public String getMontantAnciennePca() {
        return montantAnciennePca;
    }

    public void setMontantAnciennePca(String montantAnciennePca) {
        this.montantAnciennePca = montantAnciennePca;
    }
}
