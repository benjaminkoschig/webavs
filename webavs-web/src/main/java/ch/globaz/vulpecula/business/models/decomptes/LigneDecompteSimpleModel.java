package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSimpleModel;

public class LigneDecompteSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = 5974286606277066186L;

    private String id;
    private String idDecompte;
    private String idPosteTravail;
    private String nombreHeures;
    private String remarque;
    private String salaireHoraire;
    private String salaireTotal;
    private String periodeDebut;
    private String periodeFin;
    private String tauxContribuable;
    private String sequence;
    private String dateAnnonce;
    private String dateAnnonceLPP;
    private String montantFranchise;
    private String genreCotisations;
    private boolean toTreat;
    // Décompte complémentaire pour e-business
    private String mntVacancesFeries;
    private String mntGratifications;
    private String mntAbsencesJustifiees;
    private String mntAPGComplementaireSM;
    private String correlationId;
    private String anneeCotisations;
    private String lineCorrelationId;
    private String posteCorrelationId;
    private String tauxSaisieEbu;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getIdDecompte() {
        return idDecompte;
    }

    public void setIdDecompte(String idDecompte) {
        this.idDecompte = idDecompte;
    }

    public String getIdPosteTravail() {
        return idPosteTravail;
    }

    public void setIdPosteTravail(String idPosteTravail) {
        this.idPosteTravail = idPosteTravail;
    }

    public String getNombreHeures() {
        return nombreHeures;
    }

    public void setNombreHeures(String nombreHeures) {
        this.nombreHeures = nombreHeures;
    }

    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getSalaireHoraire() {
        return salaireHoraire;
    }

    public void setSalaireHoraire(String salaireHoraire) {
        this.salaireHoraire = salaireHoraire;
    }

    public String getSalaireTotal() {
        return salaireTotal;
    }

    public void setSalaireTotal(String salaireTotal) {
        this.salaireTotal = salaireTotal;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public void setPeriodeDebut(String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    public void setPeriodeFin(String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public String getTauxContribuable() {
        return tauxContribuable;
    }

    public void setTauxContribuable(String tauxContribuable) {
        this.tauxContribuable = tauxContribuable;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getDateAnnonce() {
        return dateAnnonce;
    }

    public void setDateAnnonce(String dateAnnonce) {
        this.dateAnnonce = dateAnnonce;
    }

    public final String getDateAnnonceLPP() {
        return dateAnnonceLPP;
    }

    public final void setDateAnnonceLPP(String dateAnnonceLPP) {
        this.dateAnnonceLPP = dateAnnonceLPP;
    }

    public String getMontantFranchise() {
        return montantFranchise;
    }

    public void setMontantFranchise(String montantFranchise) {
        this.montantFranchise = montantFranchise;
    }

    public String getGenreCotisations() {
        return genreCotisations;
    }

    public void setGenreCotisations(String genreCotisations) {
        this.genreCotisations = genreCotisations;
    }

    /**
     * @param toTreat the toTreat to set
     */
    public void setToTreat(boolean toTreat) {
        this.toTreat = toTreat;
    }

    public boolean getToTreat() {
        return toTreat;
    }

    /**
     * @return the mntVacancesFeries
     */
    public String getMntVacancesFeries() {
        return mntVacancesFeries;
    }

    /**
     * @param mntVacancesFeries the mntVacancesFeries to set
     */
    public void setMntVacancesFeries(String mntVacancesFeries) {
        this.mntVacancesFeries = mntVacancesFeries;
    }

    /**
     * @return the mntGratifications
     */
    public String getMntGratifications() {
        return mntGratifications;
    }

    /**
     * @param mntGratifications the mntGratifications to set
     */
    public void setMntGratifications(String mntGratifications) {
        this.mntGratifications = mntGratifications;
    }

    /**
     * @return the mntAbsencesJustifiees
     */
    public String getMntAbsencesJustifiees() {
        return mntAbsencesJustifiees;
    }

    /**
     * @param mntAbsencesJustifiees the mntAbsencesJustifiees to set
     */
    public void setMntAbsencesJustifiees(String mntAbsencesJustifiees) {
        this.mntAbsencesJustifiees = mntAbsencesJustifiees;
    }

    /**
     * @return the mntAPGComplementaireSM
     */
    public String getMntAPGComplementaireSM() {
        return mntAPGComplementaireSM;
    }

    /**
     * @param mntAPGComplementaireSM the mntAPGComplementaireSM to set
     */
    public void setMntAPGComplementaireSM(String mntAPGComplementaireSM) {
        this.mntAPGComplementaireSM = mntAPGComplementaireSM;
    }

    /**
     * @return the correlationId
     */
    public String getCorrelationId() {
        return correlationId;
    }

    /**
     * @param correlationId the correlationId to set
     */
    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getAnneeCotisations() {
        return anneeCotisations;
    }

    public void setAnneeCotisations(String anneeCotisations) {
        this.anneeCotisations = anneeCotisations;
    }

    public String getLineCorrelationId() {
        return lineCorrelationId;
    }

    public void setLineCorrelationId(String lineCorrelationId) {
        this.lineCorrelationId = lineCorrelationId;
    }

    public String getPosteCorrelationId() {
        return posteCorrelationId;
    }

    public void setPosteCorrelationId(String posteCorrelationId) {
        this.posteCorrelationId = posteCorrelationId;
    }

    public String getTauxSaisieEbu() {
        return tauxSaisieEbu;
    }

    public void setTauxSaisieEbu(String tauxSaisieEbu) {
        this.tauxSaisieEbu = tauxSaisieEbu;
    }

}
