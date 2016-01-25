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
}
