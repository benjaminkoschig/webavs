package ch.globaz.vulpecula.business.models.decomptes;

import globaz.jade.persistence.model.JadeSimpleModel;
import ch.globaz.vulpecula.domain.models.common.PeriodeMensuelle;

/**
 * Simple Model qui fait le lien entre la DB et Java, via Jade Nouvelle
 * Persistence
 * 
 */
public class DecompteSimpleModel extends JadeSimpleModel {
    private static final long serialVersionUID = -94988013473909732L;

    private String id;
    private String idEmployeur;
    private String idPassageFacturation;
    private String idRapportControle;
    private String numeroDecompte;
    private String dateEtablissement;
    private String dateReception;
    private String dateRappel;
    private String montantControle;
    private String periodeDebut;
    private String periodeFin;
    private String type;
    private String etat;
    private String interetsMoratoires;
    private String remarqueRectification;
    private Boolean manuel;
    private String motifProlongation;
    private Boolean rectifie;
    private Boolean controleAC2;
    private String typeProvenance;

    public Boolean getRectifie() {
        return rectifie;
    }

    public void setRectifie(Boolean rectifie) {
        this.rectifie = rectifie;
    }

    public DecompteSimpleModel() {
        super();
    }

    public String getDateEtablissement() {
        return dateEtablissement;
    }

    public String getDateReception() {
        return dateReception;
    }

    public String getEtat() {
        return etat;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getIdEmployeur() {
        return idEmployeur;
    }

    public String getIdPassageFacturation() {
        return idPassageFacturation;
    }

    public String getIdRapportControle() {
        return idRapportControle;
    }

    public String getInteretsMoratoires() {
        return interetsMoratoires;
    }

    public String getMontantControle() {
        return montantControle;
    }

    /**
     * @return the numeroDecompte
     */
    public String getNumeroDecompte() {
        return numeroDecompte;
    }

    public String getPeriodeDebut() {
        return periodeDebut;
    }

    public String getPeriodeFin() {
        return periodeFin;
    }

    /**
     * Retourne la période du décompte au format 03.2014 - 05.2014
     * 
     * @return
     */
    public String getPeriodeFormatte() {
        PeriodeMensuelle periodeMensuelle = new PeriodeMensuelle(periodeDebut, periodeFin);
        return periodeMensuelle.toString();
    }

    public String getRemarqueRectification() {
        return remarqueRectification;
    }

    public String getType() {
        return type;
    }

    public String getMotifProlongation() {
        return motifProlongation;
    }

    public void setDateEtablissement(final String dateEtablissement) {
        this.dateEtablissement = dateEtablissement;
    }

    public void setDateReception(final String dateReception) {
        this.dateReception = dateReception;
    }

    public void setEtat(final String etat) {
        this.etat = etat;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    public void setIdEmployeur(final String idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public void setIdPassageFacturation(final String idPassageFacturation) {
        this.idPassageFacturation = idPassageFacturation;
    }

    public void setIdRapportControle(final String idRapportControle) {
        this.idRapportControle = idRapportControle;
    }

    public void setInteretsMoratoires(final String interetsMoratoires) {
        this.interetsMoratoires = interetsMoratoires;
    }

    public void setMontantControle(final String montantControle) {
        this.montantControle = montantControle;
    }

    /**
     * @param numeroDecompte
     *            the numeroDecompte to set
     */
    public void setNumeroDecompte(final String numeroDecompte) {
        this.numeroDecompte = numeroDecompte;
    }

    public void setPeriodeDebut(final String periodeDebut) {
        this.periodeDebut = periodeDebut;
    }

    public void setPeriodeFin(final String periodeFin) {
        this.periodeFin = periodeFin;
    }

    public void setRemarqueRectification(final String remarqueRectification) {
        this.remarqueRectification = remarqueRectification;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setDateRappel(final String dateRappel) {
        this.dateRappel = dateRappel;
    }

    public String getDateRappel() {
        return dateRappel;
    }

    public Boolean getManuel() {
        return manuel;
    }

    public void setManuel(final Boolean manuel) {
        this.manuel = manuel;
    }

    public void setMotifProlongation(String motifProlongation) {
        this.motifProlongation = motifProlongation;
    }

    public Boolean getControleAC2() {
        return controleAC2;
    }

    public void setControleAC2(Boolean controleAC2) {
        this.controleAC2 = controleAC2;
    }

	/**
	 * @return the typeProvenance
	 */
	public String getTypeProvenance() {
		return typeProvenance;
	}

	/**
	 * @param typeProvenance the typeProvenance to set
	 */
	public void setTypeProvenance(String typeProvenance) {
		this.typeProvenance = typeProvenance;
	}
}
