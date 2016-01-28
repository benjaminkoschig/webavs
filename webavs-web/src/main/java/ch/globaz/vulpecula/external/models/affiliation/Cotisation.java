package ch.globaz.vulpecula.external.models.affiliation;

import java.util.Locale;
import ch.globaz.vulpecula.domain.models.common.Date;
import ch.globaz.vulpecula.domain.models.common.Taux;
import ch.globaz.vulpecula.domain.models.decompte.TypeAssurance;

/**
 * Cotisation au sens du module NAOS (à définir)
 * 
 * @author Arnaud Geiser (AGE) | Créé le 17 janv. 2014
 * 
 */
public class Cotisation {
    private String id;
    private Date dateDebut;
    private Date dateFin;
    private String motifFin;
    private String periodicite;
    private String masseAnnuelle;
    private String montantTrimestriel;
    private String montantSemestriel;
    private String montantAnnuel;
    private String montantMensuel;
    private String anneeDecision;
    private String maisonMere;
    private String planAffiliationId;
    private PlanCaisse planCaisse;
    private String adhesionId;
    private String exceptionPeriodicite;
    private String traitementMoisAnnee;
    private String tauxAssuranceId;
    private String categorieTauxId;
    private Assurance assurance;
    private Taux taux;

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(final Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(final Date dateFin) {
        this.dateFin = dateFin;
    }

    public String getMotifFin() {
        return motifFin;
    }

    public void setMotifFin(final String motifFin) {
        this.motifFin = motifFin;
    }

    public String getPeriodicite() {
        return periodicite;
    }

    public void setPeriodicite(final String periodicite) {
        this.periodicite = periodicite;
    }

    public String getMasseAnnuelle() {
        return masseAnnuelle;
    }

    public void setMasseAnnuelle(final String masseAnnuelle) {
        this.masseAnnuelle = masseAnnuelle;
    }

    public String getMontantTrimestriel() {
        return montantTrimestriel;
    }

    public void setMontantTrimestriel(final String montantTrimestriel) {
        this.montantTrimestriel = montantTrimestriel;
    }

    public String getMontantSemestriel() {
        return montantSemestriel;
    }

    public void setMontantSemestriel(final String montantSemestriel) {
        this.montantSemestriel = montantSemestriel;
    }

    public String getMontantAnnuel() {
        return montantAnnuel;
    }

    public void setMontantAnnuel(final String montantAnnuel) {
        this.montantAnnuel = montantAnnuel;
    }

    public String getMontantMensuel() {
        return montantMensuel;
    }

    public void setMontantMensuel(final String montantMensuel) {
        this.montantMensuel = montantMensuel;
    }

    public String getAnneeDecision() {
        return anneeDecision;
    }

    public void setAnneeDecision(final String anneeDecision) {
        this.anneeDecision = anneeDecision;
    }

    public String getMaisonMere() {
        return maisonMere;
    }

    public void setMaisonMere(final String maisonMere) {
        this.maisonMere = maisonMere;
    }

    public String getPlanAffiliationId() {
        return planAffiliationId;
    }

    public void setPlanAffiliationId(final String planAffiliationId) {
        this.planAffiliationId = planAffiliationId;
    }

    public String getAdhesionId() {
        return adhesionId;
    }

    public void setAdhesionId(final String adhesionId) {
        this.adhesionId = adhesionId;
    }

    public String getExceptionPeriodicite() {
        return exceptionPeriodicite;
    }

    public void setExceptionPeriodicite(final String exceptionPeriodicite) {
        this.exceptionPeriodicite = exceptionPeriodicite;
    }

    public String getTraitementMoisAnnee() {
        return traitementMoisAnnee;
    }

    public void setTraitementMoisAnnee(final String traitementMoisAnnee) {
        this.traitementMoisAnnee = traitementMoisAnnee;
    }

    public String getTauxAssuranceId() {
        return tauxAssuranceId;
    }

    public void setTauxAssuranceId(final String tauxAssuranceId) {
        this.tauxAssuranceId = tauxAssuranceId;
    }

    public String getCategorieTauxId() {
        return categorieTauxId;
    }

    public void setCategorieTauxId(final String categorieTauxId) {
        this.categorieTauxId = categorieTauxId;
    }

    public Assurance getAssurance() {
        return assurance;
    }

    public void setAssurance(final Assurance assurance) {
        this.assurance = assurance;
    }

    public PlanCaisse getPlanCaisse() {
        return planCaisse;
    }

    public void setPlanCaisse(final PlanCaisse planCaisse) {
        this.planCaisse = planCaisse;
    }

    public String getDesignation() {
        return assurance.getLibelleFr();
    }

    public Taux getTaux() {
        return taux;
    }

    public void setTaux(final Taux taux) {
        this.taux = taux;
    }

    public TypeAssurance getTypeAssurance() {
        if (assurance == null) {
            return null;
        }
        return assurance.getTypeAssurance();
    }

    /**
     * Retourne l'id du plan caisse associé à la cotisation
     * 
     * @return String représentant l'id du plan caisse
     */
    public String getPlanCaisseId() {
        if (planCaisse == null) {
            return null;
        }
        return planCaisse.getId();
    }

    /**
     * Retourne le libelle du plan caisse
     * 
     * @return String représentant le libelle du plan caisse
     */
    public String getPlanCaisseLibelle() {
        if (planCaisse == null) {
            return null;
        }
        return planCaisse.getLibelle();
    }

    /**
     * Retourne la langue de l'assurance selon la locale passée en paramètre
     * 
     * @param locale Locale (langue)
     * @return String représentant la traduction de l'assurance
     */
    public String getAssuranceLibelle(final Locale locale) {
        if (assurance == null) {
            return null;
        }
        return assurance.getLibelle(locale);
    }

    /**
     * Retourne l'id de l'assurance associée
     * 
     * @return String représentant l'id de l'assurance
     */
    public String getAssuranceId() {
        if (assurance == null) {
            return null;
        }
        return assurance.getId();
    }

    /**
     * Retourne si l'assurance est de type AVS.
     * 
     * @return true si l'assurance est de type AVS, false dans le cas où l'assurance est null ou si autre type
     */
    public boolean isAssuranceAVS() {
        if (assurance == null) {
            return false;
        }
        return assurance.isTypeAVS();
    }

    /**
     * Retourne si l'assurance est de type AC.
     * 
     * @return true si l'assurance est de type AC, false dans les cas où l'assurance est null ou si autre type
     */
    public boolean isAssuranceAC() {
        if (assurance == null) {
            return false;
        }
        return assurance.isTypeAC();
    }

    /**
     * Retourne si l'assurance est de type AC2.
     * 
     * @return true si l'assurance est de type AC2, false dans le cas où l'assurance est null ou si autre type
     */
    public boolean isAssuranceAC2() {
        if (assurance == null) {
            return false;
        }
        return assurance.isTypeAC2();
    }

    /**
     * Retourne si l'assurance est de type AF.
     * 
     * @return true si l'assurance est de type AF, false dans les cas où l'assurance est null ou si autre type
     */
    public boolean isAssuranceAF() {
        if (assurance == null) {
            return false;
        }
        return assurance.isTypeAF();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Cotisation) {
            Cotisation cotisation = (Cotisation) obj;
            if (cotisation.getId() != null && getId() != null) {
                return cotisation.getId().equals(getId());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((adhesionId == null) ? 0 : adhesionId.hashCode());
        result = prime * result + ((anneeDecision == null) ? 0 : anneeDecision.hashCode());
        result = prime * result + ((assurance == null) ? 0 : assurance.hashCode());
        result = prime * result + ((categorieTauxId == null) ? 0 : categorieTauxId.hashCode());
        result = prime * result + ((dateDebut == null) ? 0 : dateDebut.hashCode());
        result = prime * result + ((dateFin == null) ? 0 : dateFin.hashCode());
        result = prime * result + ((exceptionPeriodicite == null) ? 0 : exceptionPeriodicite.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((maisonMere == null) ? 0 : maisonMere.hashCode());
        result = prime * result + ((masseAnnuelle == null) ? 0 : masseAnnuelle.hashCode());
        result = prime * result + ((montantAnnuel == null) ? 0 : montantAnnuel.hashCode());
        result = prime * result + ((montantMensuel == null) ? 0 : montantMensuel.hashCode());
        result = prime * result + ((montantSemestriel == null) ? 0 : montantSemestriel.hashCode());
        result = prime * result + ((montantTrimestriel == null) ? 0 : montantTrimestriel.hashCode());
        result = prime * result + ((motifFin == null) ? 0 : motifFin.hashCode());
        result = prime * result + ((periodicite == null) ? 0 : periodicite.hashCode());
        result = prime * result + ((planAffiliationId == null) ? 0 : planAffiliationId.hashCode());
        result = prime * result + ((planCaisse == null) ? 0 : planCaisse.hashCode());
        result = prime * result + ((tauxAssuranceId == null) ? 0 : tauxAssuranceId.hashCode());
        result = prime * result + ((traitementMoisAnnee == null) ? 0 : traitementMoisAnnee.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Cotisation [id=" + id + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + ", motifFin=" + motifFin
                + ", periodicite=" + periodicite + ", masseAnnuelle=" + masseAnnuelle + ", montantTrimestriel="
                + montantTrimestriel + ", montantSemestriel=" + montantSemestriel + ", montantAnnuel=" + montantAnnuel
                + ", montantMensuel=" + montantMensuel + ", anneeDecision=" + anneeDecision + ", maisonMere="
                + maisonMere + ", planAffiliationId=" + planAffiliationId + ", planCaisse=" + planCaisse
                + ", adhesionId=" + adhesionId + ", exceptionPeriodicite=" + exceptionPeriodicite
                + ", traitementMoisAnnee=" + traitementMoisAnnee + ", tauxAssuranceId=" + tauxAssuranceId
                + ", categorieTauxId=" + categorieTauxId + ", assurance=" + assurance + ", taux=" + taux + "]";
    }
}
