package ch.globaz.naos.business.model;

import globaz.jade.persistence.model.JadeSimpleModel;

public class CotisationSimpleModel extends JadeSimpleModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String adhesionId;
    private String anneeDecision;
    private String assuranceId;
    private String categorieTauxId;
    private String cotisationId;
    private String dateDebut;
    private String dateFin;
    private String exceptionPeriodicite;
    private String maisonMere;
    private String masseAnnuelle;
    private String montantAnnuel;
    private String montantMensuel;
    private String montantSemestriel;
    private String montantTrimestriel;
    private String motifFin;
    private String periodicite;
    private String planAffiliationId;
    private String planCaisseId;
    private String tauxAssuranceId;
    private String traitementMoisAnnee;

    public String getAdhesionId() {
        return adhesionId;
    }

    public String getAnneeDecision() {
        return anneeDecision;
    }

    public String getAssuranceId() {
        return assuranceId;
    }

    public String getCategorieTauxId() {
        return categorieTauxId;
    }

    public String getCotisationId() {
        return cotisationId;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public String getExceptionPeriodicite() {
        return exceptionPeriodicite;
    }

    @Override
    public String getId() {
        return getCotisationId();
    }

    public String getMaisonMere() {
        return maisonMere;
    }

    public String getMasseAnnuelle() {
        return masseAnnuelle;
    }

    public String getMontantAnnuel() {
        return montantAnnuel;
    }

    public String getMontantMensuel() {
        return montantMensuel;
    }

    public String getMontantSemestriel() {
        return montantSemestriel;
    }

    public String getMontantTrimestriel() {
        return montantTrimestriel;
    }

    public String getMotifFin() {
        return motifFin;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public String getPlanAffiliationId() {
        return planAffiliationId;
    }

    public String getPlanCaisseId() {
        return planCaisseId;
    }

    public String getTauxAssuranceId() {
        return tauxAssuranceId;
    }

    public String getTraitementMoisAnnee() {
        return traitementMoisAnnee;
    }

    public void setAdhesionId(String adhesionId) {
        this.adhesionId = adhesionId;
    }

    public void setAnneeDecision(String anneeDecision) {
        this.anneeDecision = anneeDecision;
    }

    public void setAssuranceId(String assuranceId) {
        this.assuranceId = assuranceId;
    }

    public void setCategorieTauxId(String categorieTauxId) {
        this.categorieTauxId = categorieTauxId;
    }

    public void setCotisationId(String cotisationId) {
        this.cotisationId = cotisationId;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public void setExceptionPeriodicite(String exceptionPeriodicite) {
        this.exceptionPeriodicite = exceptionPeriodicite;
    }

    @Override
    public void setId(String id) {
        setCotisationId(id);
    }

    public void setMaisonMere(String maisonMere) {
        this.maisonMere = maisonMere;
    }

    public void setMasseAnnuelle(String masseAnnuelle) {
        this.masseAnnuelle = masseAnnuelle;
    }

    public void setMontantAnnuel(String montantAnnuel) {
        this.montantAnnuel = montantAnnuel;
    }

    public void setMontantMensuel(String montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public void setMontantSemestriel(String montantSemestriel) {
        this.montantSemestriel = montantSemestriel;
    }

    public void setMontantTrimestriel(String montantTrimestriel) {
        this.montantTrimestriel = montantTrimestriel;
    }

    public void setMotifFin(String motifFin) {
        this.motifFin = motifFin;
    }

    public void setPeriodicite(String periodicite) {
        this.periodicite = periodicite;
    }

    public void setPlanAffiliationId(String planAffiliationId) {
        this.planAffiliationId = planAffiliationId;
    }

    public void setPlanCaisseId(String planCaisseId) {
        this.planCaisseId = planCaisseId;
    }

    public void setTauxAssuranceId(String tauxAssuranceId) {
        this.tauxAssuranceId = tauxAssuranceId;
    }

    public void setTraitementMoisAnnee(String traitementMoisAnnee) {
        this.traitementMoisAnnee = traitementMoisAnnee;
    }
}
